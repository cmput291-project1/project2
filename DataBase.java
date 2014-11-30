import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

/*
 * requires refactoring
 * much cleaning required
*/

public class DataBase{
	public static final int NO_RECORDS = 100000;
	public static final int NO_RECORDS_TEST = 11;
	public static final String DATABASE_DIR = "/tmp/slmyers_db";
	public static final String PRIMARY_TABLE = "/tmp/slmyers_db/primary_table_file1";
	private static final String DATA_SECONDARY_TABLE = "/tmp/slmyers_db/data_index";
	public static final String TREE_TABLE = "/tmp/slmyers_db/search_file";
	public static final String HASH_TABLE = "/tmp/slmyers_db/data_support_file";

	
	private static DataBase db = null;	
	
	private Database database = null;
	private Database databaseHash = null;
	private Database databaseTree = null;	

	protected DataBase(){
	}
	
	public static DataBase getInstance(){
		if(db == null){
			db = new DataBase();
		}
		return db;
	}

	public void initDataBase(){
		if(!createDirectory(DATABASE_DIR)){
			System.err.println("Unable to create file	 for database");
			System.exit(-1);
		}
		DatabaseType type;
		int typeInt = Pref.getDbType();
		if(typeInt == 1 ){
			type = DatabaseType.BTREE;
		}else if (typeInt == 2){
			type = DatabaseType.HASH;
		}else if (typeInt == 3){
			initIndexFile(DatabaseType.BTREE);
			initIndexFile(DatabaseType.HASH);
			configureDataSecondary();
			return;
		}
		}else{
			throw new RuntimeException("unknown database attempting to be created in Database");
		}
		
		this.database = createDb(PRIMARY_TABLE, type);
		System.out.println(PRIMARY_TABLE + " has been created of type: " + type);
		
		int count = populateTable(this.database, NO_RECORDS);
		System.out.println(PRIMARY_TABLE + " has been entered with " + count + " records.");
	}

	public Database getPrimaryDb(){
		return this.database;
	}

	public Database getIndexTree(){
		return databaseTree;
	}

	public Database getIndexHash(){
		return databaseHash;
	}

	public SecondaryDatabase getIndexSecondary(){
		return this.dataSecondary;
	}

	public void initIndexFile(DatabaseType type){
		if(type.equals(DatabaseType.BTREE)){
			this.databaseTree = createDb(TREE_TABLE, type);
			System.out.println(TREE_TABLE + " has been created of type: " + type);
			int count = populateTable(this.databaseTree, NO_RECORDS);
			System.out.println(TREE_TABLE + " has been entered with " + count + " records.");
		}else if(type.equals(DatabaseType.HASH)){
			this.databaseHash = createDb(HASH_TABLE, type);
			System.out.println(HASH_TABLE + " has been created of type: " + type);
			int count = populateTable(this.databaseHash, NO_RECORDS);
			System.out.println(HASH_TABLE + " has been entered with " + count + " records.");
		}
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

	public final boolean createDirectory(String file){
		deleteFolder(new File(file));
		File dbDirect = new File(file);
	  	dbDirect.mkdirs();
		return dbDirect.exists();
	}

	public Database createDb(String file, DatabaseType type){
		Database db = null;
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setType(type);
		dbConfig.setAllowCreate(true);
		try{
			db = new Database(file, null, dbConfig);
		}catch (DatabaseException dbe){
			System.err.println("unable to create database");
			dbe.printStackTrace();
		}catch (FileNotFoundException fnfe){
			System.err.println("can not find file to create Database");
			fnfe.printStackTrace();
		}
		return db;
	}

	private static int populateTable(Database my_table, int nrecs ) {
		int range;
		DatabaseEntry kdbt, ddbt;
		int count = 0;
		String s;
		
		/*  
		 *  generate a random string with the length between 64 and 127,
		 *  inclusive.
		 *
		 *  Seed the random number once and once only.
		 */
		Random random = new Random(1000000);

		try {

    		for (int i = 0; i < nrecs; i++) {
				/* to generate a key string */
				range = 64 + random.nextInt( 64 );
				s = "";
				for ( int j = 0; j < range; j++ ) 
					s+=(new Character((char)(97+random.nextInt(26)))).toString();
				
		
				/* to create a DBT for key */
				kdbt = new DatabaseEntry(s.getBytes());
				kdbt.setSize(s.length()); 
				 	

				/* to generate a data string */
				range = 64 + random.nextInt( 64 );
				s = "";
				for ( int j = 0; j < range; j++ ) 
					s+=(new Character((char)(97+random.nextInt(26)))).toString();
				
				/* to create a DBT for data */
				ddbt = new DatabaseEntry(s.getBytes());
				ddbt.setSize(s.length()); 

				//TODO change so program recovers instead of exiting
				OperationStatus result;
				result = my_table.exists(null, kdbt);
				if (!result.toString().equals("OperationStatus.NOTFOUND"))
					throw new RuntimeException("Key is already in the database!");

					/* to insert the key/data pair into the database */
				my_table.putNoOverwrite(null, kdbt, ddbt);
				count++;
			}
		}
		catch (DatabaseException dbe) {
			System.err.println("Populate the table: "+dbe.toString());
		  	System.exit(1);
		}
		
		return count;
	}

	public void close(){
		if(this.database != null){
			try{
				this.database.close();
				this.database.remove(PRIMARY_TABLE,null,null);
				System.out.println(PRIMARY_TABLE + " database is closed and removed");
			}catch(DatabaseException dbe){
				System.err.println("unable to close database");
				dbe.printStackTrace();
			}catch (FileNotFoundException fnfe){
				System.err.println("can not find file to remove Database");
				fnfe.printStackTrace();
			}
			database = null;
			db = null;
		}
	}

	public static void deleteFolder(File folder) {
    File[] files = folder.listFiles();
    if(files!=null) { //some JVMs return null for empty dirs
        for(File f: files) {
            if(f.isDirectory()) {
                deleteFolder(f);
            } else {
                f.delete();
            }
        }
    }
    folder.delete();
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
}
