import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

/*
 * requires refactoring
 * much cleaning required
*/

public class DataBase{
	private static final int NO_RECORDS = 100000;
	private static final String DATABASE_DIR = "./tmp/user_db";
	private static final String PRIMARY_TABLE = "./tmp/user_db/primary_table_file1";
	private static final String SECONDARY_TABLE = "./tmp/user_db/secondary_table_file2";

		
	private static DataBase db = null;	
	private Database database = null;	
	private SecondaryDatabase secdatabase = null;

	
	private Random random;
	private int duplicateKeys;

	private TestData testData;

	// not sure if all these method calls should be in constructor
	protected DataBase(){
		this.testData = TestData.getInstance();
		random = new Random(1000000);
		duplicateKeys = 0;
		if(!createDirectory(DATABASE_DIR)){
			System.err.println("Unable to create file for database");
			System.exit(-1);
		}
		if(!createBase()){
			System.err.println("Database was not created properly");
			System.exit(-1);
		}
		populateTable();
		System.out.println(duplicateKeys + " duplicate keys created (none were inserted don't worry)");
		System.out.println("test search data string = " + testData.getDataString() + " it is the " + testData.getDataRecNo() + " record inserted at " +
								 testData.getDataDate());
		System.out.println("test search key string = " + testData.getKeyString() + " it is the " + testData.getKeyRecNo() + " record inserted at " +
								 testData.getKeyDate());

		// get rid of this part later just for testing and in hurry
		System.out.println("printing secondary database keys");
		DatabaseEntry data = new DatabaseEntry();
		DatabaseEntry dbKey = new DatabaseEntry();
		database2 = db.getSecondaryDb();
		Cursor c = database2.openSecondaryCursor(null, null);
		OperationStatus oprStatus = c.getFirst(dbKey, data, LockMode.DEFAULT);
		while (oprStatus == OperationStatus.SUCCESS) {
			String s = new String(dbKey.getData());
			System.out.println(s);
			oprStatus = c.getNext(dbKey, data, LockMode.DEFAULT);
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

	public SecondaryDatabase getSecondaryDb(){
		return this.secdatabase;
	}

	private final boolean createDirectory(String file){
		File dbDirect = new File(file);
	  dbDirect.mkdirs();
		return dbDirect.exists();
	}

	private final boolean createBase(){
		// Create the database object.
		// There is no environment for this simple example.
		DatabaseConfig dbConfig = new DatabaseConfig();

		switch(Pref.getDbType()){
			case 1:
							dbConfig.setType(DatabaseType.BTREE);
							break;
			case 2:
							dbConfig.setType(DatabaseType.HASH);
							break;
			case 3:
							configureIndexFileDb();
							return true;
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
		return true;
	}	

	private final boolean configureIndexFileDb(){
		DatabaseConfig primaryConfig = new DatabaseConfig();
		SecondaryConfig secConfig = new SecondaryConfig();

				
		primaryConfig.setAllowCreate(true);
		primaryConfig.setType(DatabaseType.HASH);
		primaryConfig.setSortedDuplicates(false);
		// duplicate code clean up
		try{
			this.database = new Database(PRIMARY_TABLE, null, primaryConfig);
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

		System.out.println(PRIMARY_TABLE + " has been created of type: " + primaryConfig.getType());
		
			
		
		secConfig.setKeyCreator(new FirstCharKeyCreator());
		secConfig.setAllowCreate(true);
		secConfig.setType(DatabaseType.HASH);
		secConfig.setSortedDuplicates(false);
		secConfig.setAllowPopulate(true);

		try{
			this.secdatabase = new SecondaryDatabase(SECONDARY_TABLE, null, this.database, secConfig);
		}catch(DatabaseException dbe){
			System.err.println("Error while instantiating secondary database: " + dbe.toString());
			this.close();
			System.exit(-1);
		}catch(FileNotFoundException fnfe){
			System.err.println("Secondary database file not found: " + fnfe.toString());
		}
		
		System.out.println(SECONDARY_TABLE + " has been created of type: " + secConfig.getType());
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

		range = 64 + random.nextInt( 64 );
		keyString = "";
		for ( int j = 0; j < range; j++ ) 
			keyString+=(new Character((char)(97+random.nextInt(26)))).toString();
		
		kdbt = new DatabaseEntry(keyString.getBytes());
		kdbt.setSize(keyString.length()); 

		range = 64 + random.nextInt( 64 );
		dataString = "";
		for ( int j = 0; j < range; j++ ) 
			dataString+=(new Character((char)(97+random.nextInt(26)))).toString();
		              
		ddbt = new DatabaseEntry(dataString.getBytes());
		ddbt.setSize(dataString.length()); 
		
		OperationStatus result = null;

				

		try{
			result = this.database.exists(null, kdbt);
		}catch(DatabaseException dbe){
			System.err.println("Unable to check if key exists");
			dbe.printStackTrace();
		}
		if(!result.toString().equals(OperationStatus.NOTFOUND)){
			try{
				this.database.put(null, kdbt, ddbt);
				if(count == 1){
					this.testData.setTestData(dataString, count);
					this.testData.setTestKey(keyString, count);
				}
			}catch(DatabaseException dbe){
				System.err.println("Unable to put key/data pair in database");
				dbe.printStackTrace();
			}
			return 1;
		}
		duplicateKeys++;
		return 0;
	}


	public void close(){
		try{
			if(this.secdatabase != null){
				this.secdatabase.close();
			}
			this.database.close();
			this.database.remove(PRIMARY_TABLE,null,null);
		}catch(DatabaseException dbe){
			System.err.println("unable to close database");
			dbe.printStackTrace();
		}catch (FileNotFoundException fnfe){
			System.err.println("can not find file to remove Database");
			fnfe.printStackTrace();
		}
	}

/*
 * secondary keys are the first char in the primary key string 
*/
	private class FirstCharKeyCreator implements SecondaryKeyCreator {
			public boolean createSecondaryKey(SecondaryDatabase secondary,
                                      DatabaseEntry key,
                                      DatabaseEntry data,
                                      DatabaseEntry result)
            throws DatabaseException {
        byte[] firstByte = new byte[1];
        firstByte[0] = data.getData()[0];
        result.setData(firstByte);
        return true;
    }
	}
}
