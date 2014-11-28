import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

/*
 * requires refactoring
 * much cleaning required
*/

public class DataBase{
	private static final int NO_RECORDS = 100000;
	public static final String DATABASE_DIR = "./tmp/user_db";
	public static final String PRIMARY_TABLE = "./tmp/user_db/primary_table_file1";
	

		
	private static DataBase db = null;	
	private Database database = null;	

	private StringGenerator gen;

	// not sure if all these method calls should be in constructor
	protected DataBase(){

		this.gen = StringGenerator.getInstance();
		
		if(!createDirectory(DATABASE_DIR)){
			System.err.println("Unable to create file	 for database");
			System.exit(-1);
		}
		if(!createBase()){
			System.err.println("Database was not created properly");
			System.exit(-1);
		}
		
	}
	
	public static DataBase getInstance(){
		if(db == null){
			db = new DataBase();
		}

		return db;
	}

	public Database getPrimaryDb(){
		return this.database;
	}

	private final boolean createDirectory(String file){
		File dbDirect = new File(file);
	  dbDirect.mkdirs();
		return dbDirect.exists();
	}

	private final boolean createBase(){
		DatabaseConfig dbConfig = new DatabaseConfig();
		
		switch(Pref.getDbType()){
			case 1:
							dbConfig.setType(DatabaseType.BTREE);
							break;
			case 2:
							dbConfig.setType(DatabaseType.HASH);
							break;
			case 3:
							dbConfig.setType(DatabaseType.BTREE);
							break;
			default:
							System.out.println("Unrecognized database type.");
		}
		
		dbConfig.setAllowCreate(true);
		try{
			this.database = new Database(PRIMARY_TABLE, null, dbConfig);
		}catch (DatabaseException dbe){
			System.err.println("unable to create database");
			dbe.printStackTrace();
		}catch (FileNotFoundException fnfe){
			System.err.println("can not find file to create Database");
			fnfe.printStackTrace();
		}

		if(this.database == null){
			return false;
		}
		System.out.println(PRIMARY_TABLE + " has been created of type: " + dbConfig.getType());
		populateTable();
		
		return true;
	}	

	private void populateTable() {
		int count = 0;
		while(count < NO_RECORDS){
			count += addEntry(count);
		}
		System.out.println(NO_RECORDS + " records inserted into" + PRIMARY_TABLE);
	}
	
	private int addEntry(int count){
		int range;
		DatabaseEntry kdbt, ddbt;
		String keyString;
		String dataString;
		
		
		keyString = gen.generateString();
		kdbt = new DatabaseEntry(keyString.getBytes());
		kdbt.setSize(keyString.length()); 
		
		
		dataString = gen.generateString();
		ddbt = new DatabaseEntry(dataString.getBytes());
		ddbt.setSize(dataString.length()); 
		
		OperationStatus result = null;

				

		try{
			result = this.database.exists(null, kdbt);
		}catch(DatabaseException dbe){
			System.err.println("Unable to check if key exists");
			dbe.printStackTrace();
		}
		if(result == OperationStatus.NOTFOUND){
			try{
				this.database.putNoOverwrite(null, kdbt, ddbt);
			}catch(DatabaseException dbe){
				System.err.println("Unable to put key/data pair in database");
				dbe.printStackTrace();
			}
			return 1;
		}
		
		return 0;
	}


	public void close(){
		if(this.database != null){
			try{
				this.database.close();
				this.database.remove(PRIMARY_TABLE,null,null);
			}catch(DatabaseException dbe){
				System.err.println("unable to close database");
				dbe.printStackTrace();
			}catch (FileNotFoundException fnfe){
				System.err.println("can not find file to remove Database");
				fnfe.printStackTrace();
			}
			database = null;
		}
		
	}
}
