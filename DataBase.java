import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

/*
 * requires refactoring
 * much cleaning required
*/

public class DataBase{
	private static final int NO_RECORDS = 100000;
	private static final int NO_RECORDS_TEST = 11;
	public static final String DATABASE_DIR = "/tmp/slmyers_db";
	public static final String PRIMARY_TABLE = "/tmp/slmyers_db/primary_table_file1";
	

		
	private static DataBase db = null;	
	private Database database = null;	
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
		if(!createBase()){
			System.err.println("Database was not created properly");
			System.exit(-1);
		}
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
		int count = 0;
		
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

		if(!Interval.testMode){
			count = populateTable(this.database, NO_RECORDS);
		}
		else{
			count = populateTable(this.database, NO_RECORDS_TEST);
		}
		System.out.println(PRIMARY_TABLE + " has been inserted with: " + count + " records");
		return true;
	}	
	
	static int populateTestTable(Database my_table, int nrecs){
		DatabaseEntry kdbt, ddbt;
		int count = 0;
		//parallel arrays can be optimized		
		String[] keys = {"a","b","c","d","e","f","g","h","i","j","k"}; // 11 keys and values
		String[] values = {"k","l","a","e","b","u","v","z","q","y","k"};
		
		try {
			for(int i = 0; i < nrecs; i++){
				kdbt = new DatabaseEntry(keys[i].getBytes());
				kdbt.setSize(1);
				ddbt = new DatabaseEntry(values[i].getBytes());
				ddbt.setSize(1);
				OperationStatus result;
				result = my_table.exists(null, kdbt);
				if (!result.toString().equals("OperationStatus.NOTFOUND"))
					throw new RuntimeException("Key is already in the database!");

				/* to insert the key/data pair into the database */
		    	my_table.putNoOverwrite(null, kdbt, ddbt);
				count++;
			} 
		}catch (DatabaseException dbe) {
			System.err.println("Populate the table: "+dbe.toString());
		  	System.exit(1);
		}
		return count;
	}

	static int populateTable(Database my_table, int nrecs ) {
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
				System.out.println("key: " + s);				
		
				/* to create a DBT for key */
				kdbt = new DatabaseEntry(s.getBytes());
				kdbt.setSize(s.length()); 

				 	

				/* to generate a data string */
				range = 64 + random.nextInt( 64 );
				s = "";
				for ( int j = 0; j < range; j++ ) 
					s+=(new Character((char)(97+random.nextInt(26)))).toString();
				System.out.println("data: " + s);
		
		
				/* to create a DBT for data */
				ddbt = new DatabaseEntry(s.getBytes());
				ddbt.setSize(s.length()); 

				//NEW CODE - checks if key already in the database
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
				System.out.println("database is closed and removed");
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
}
