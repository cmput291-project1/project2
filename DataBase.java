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

		if(Interval.testMode){
			count = populateTable(this.database, NO_RECORDS_TEST);
			
		}
		else if(Interval.testDupMode){
			try{
				count = populateDupTestTable(this.database);
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}
		}
		else{
			count = populateTable(this.database, NO_RECORDS);
		}
		System.out.println(PRIMARY_TABLE + " has been inserted with: " + count + " records");
		return true;
	}	
	
	static int populateDupTestTable(Database my_table) throws DatabaseException{
		DatabaseEntry kdbt, ddbt;
		int count = 0;
		String[][] dupTable = Interval.DUP_TEST_MATRIX;
		try {
			for(int i = 0; i < 5; i++){
				
				kdbt = new DatabaseEntry(dupTable[i][0].getBytes());
				kdbt.setSize(dupTable[i][0].length());
				ddbt = new DatabaseEntry(dupTable[i][1].getBytes());
				ddbt.setSize(dupTable[i][1].length());
				OperationStatus result;
				result = my_table.exists(null, kdbt);
				if (!result.toString().equals("OperationStatus.NOTFOUND"))
					throw new RuntimeException("Key is already in the database!");

				/* to insert the key/data pair into the database */
		    	if(my_table.putNoOverwrite(null, kdbt, ddbt) != OperationStatus.SUCCESS){
						throw new RuntimeException("can not input test dup data!");
					}
				count++;
			} 
		}catch (DatabaseException dbe) {
			System.err.println("Populate the table: "+dbe.toString());
		  	System.exit(1);
		}
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		Cursor c = my_table.openCursor(null, null);

		while(c.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS){
			System.out.println("key: " + new String(key.getData()));
			System.out.println("data: " + new String(data.getData()));
		}
		
		
		return count;
	}

	static int populateTable(Database my_table, int nrecs ) {
		int range;
		DatabaseEntry kdbt, ddbt;
		int count = 0;
		String s;
		ArrayList<String> testKeys = null;
		ArrayList<String> testData = null;
		if(Interval.testMode){
			testKeys = new ArrayList<String>();
			testData = new ArrayList<String>();
		}
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
				if(Interval.testMode){
					testKeys.add(s);
				}	
		
				/* to create a DBT for key */
				kdbt = new DatabaseEntry(s.getBytes());
				kdbt.setSize(s.length()); 

				 	

				/* to generate a data string */
				range = 64 + random.nextInt( 64 );
				s = "";
				for ( int j = 0; j < range; j++ ) 
					s+=(new Character((char)(97+random.nextInt(26)))).toString();
				if(Interval.testMode){
					testData.add(s);
				}	
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
		if(Interval.testMode){
			//gross code
			String[] keys = Interval.TEST_KEYS_IN_ORDER;
			String[] data = Interval.TEST_DATA;
			if(keys.length != data.length){
				throw new RuntimeException("unequal test keys and test data");
			}			
		
			for(int i = 0; i < keys.length; i++){
				if(!testKeys.contains(keys[i])){
					throw new RuntimeException("test key was not created!");
				}
				if(!testData.contains(data[i])){
					throw new RuntimeException("test data was not created!");
				}
			}
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
