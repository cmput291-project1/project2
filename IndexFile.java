import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class IndexFile{
	private static final String DATA_SECONDARY_TABLE = "/tmp/slmyers_db/data_index";
	public static final String TREE_TABLE = "/tmp/slmyers_db/search_file";
	public static final String HASH_TABLE = "/tmp/slmyers_db/data_support_file";
	private DataBase db;
	private static IndexFile indexFile = null;
	private SecondaryDatabase dataSecondary = null;
	private Database databaseHash = null;
	private Database databaseTree = null;

	protected IndexFile(){
		db = DataBase.getInstance();
	}
	
	public static IndexFile getInstance(){
		if(indexFile == null){
			indexFile = new IndexFile();
		}
		return indexFile;
	}

	public void initIndexFile(){
		if(db.createDirectory(DataBase.DATABASE_DIR)){
			this.databaseHash = db.createDb(HASH_TABLE, DatabaseType.HASH);
			System.out.println(HASH_TABLE + " has been created of type: " + DatabaseType.HASH);
			int count = db.populateTable(this.databaseHash, DataBase.NO_RECORDS);
			System.out.println(HASH_TABLE + " has been entered with " + count + " records.");
			
			this.databaseTree = db.createDb(TREE_TABLE, DatabaseType.HASH);
			System.out.println(TREE_TABLE + " has been created of type: " + DatabaseType.BTREE);
			count = db.populateTable(this.databaseTree, DataBase.NO_RECORDS);
			System.out.println(TREE_TABLE + " has been entered with " + count + " records.");

			configureDataSecondary();
		}
		else{
			throw new RuntimeException("unable to create database. Please run: rm -rf /tmp/slmyers_db\n and then start program again");
		}
		
	}
	
	public Database getTreePrimary(){
		return databaseTree;
	}

	public Database getHashPrimary(){
		return databaseHash;
	}

	public SecondaryDatabase getDataSecondary(){
		return this.dataSecondary;
	}

	public final boolean checkDirectory(){
		return new File(DataBase.DATABASE_DIR).exists();
	}

	

	public void configureDataSecondary(){
		SecondaryConfig secConfig = new SecondaryConfig();
		secConfig.setKeyCreator(new DataKeyCreator());
		secConfig.setAllowCreate(true);
		secConfig.setType(DatabaseType.HASH);
		secConfig.setSortedDuplicates(true);
		secConfig.setAllowPopulate(true);

		try{
			this.dataSecondary = new SecondaryDatabase(DATA_SECONDARY_TABLE, null, this.databaseHash, secConfig);
		}catch(DatabaseException dbe){
			System.err.println("Error while instantiating secondary 'data' database: " + dbe.toString());
			this.close();
			System.exit(-1);
		}catch(FileNotFoundException fnfe){
			System.err.println("Secondary database file not found: " + fnfe.toString());
		}
		
		System.out.println(DATA_SECONDARY_TABLE + " has been created of type: " + secConfig.getType());
		
		if(Interval.testMode || Interval.testDupMode){
			try{
				printSecondary();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}
		}
	}
	
	public void printSecondary() throws DatabaseException{
		DatabaseEntry secKey = new DatabaseEntry();
		DatabaseEntry pKey = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		SecondaryCursor cursor = this.dataSecondary.openSecondaryCursor(null, null);

		String secondaryKey;		
		String primaryKey;
		String dataString;
		OperationStatus status;
		secKey.setReuseBuffer(false);
		pKey.setReuseBuffer(false);
		data.setReuseBuffer(false);
		
		
		
		while( (status = cursor.getNextNoDup(secKey, pKey, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS){
			System.out.println("secondary key: " + new String(secKey.getData()) + "\n");
			System.out.println("primary key: " + new String(pKey.getData()) + "\n");
			System.out.println("data: " + new String(data.getData()) + "\n");
			while((status = cursor.getNextDup(secKey, pKey, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS){
				System.out.println("\tsecondary key: " + new String(secKey.getData()) + "\n");
				System.out.println("\tprimary key: " + new String(pKey.getData()) + "\n");
				System.out.println("\tdata: " + new String(data.getData()) + "\n");
			}
			System.out.println("=======================================");
		}
		cursor.close();
	}
	


	public void close(){
		//closing secondary
		try{
			if(this.dataSecondary != null){
				this.dataSecondary.close();
			}
		}catch(DatabaseException dbe){
			System.err.println("unable to close database");
			dbe.printStackTrace();
		}
		dataSecondary = null;

		try{
			if(this.databaseHash != null){
				this.databaseHash.close();
				this.databaseHash.remove(HASH_TABLE, null, null);
				System.out.println(HASH_TABLE + "database is closed and removed");
			}
			if(this.databaseTree != null){
				this.databaseTree.close();
				this.databaseTree.remove(TREE_TABLE, null, null);
				System.out.println(TREE_TABLE + "database is closed and removed");
			}
		}
		catch(DatabaseException dbe){
			System.err.println("unable to close indexFile(s)");
			dbe.printStackTrace();
		}catch (FileNotFoundException fnfe){
			System.err.println("can not find file to remove indexFile(s)");
			fnfe.printStackTrace();
		}
	}
}
