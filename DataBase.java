import com.sleepycat.db.*;
import java.io.*;
import java.util.*;
import java.lang.Math;
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
	private StringGenerator gen;

	// not sure if all these method calls should be in constructor
	protected DataBase(){

		this.gen = StringGenerator.getInstance();
		this.testData = TestData.getInstance();
		random = new Random(1000000);
		duplicateKeys = 0;
		if(!createDirectory(DATABASE_DIR)){
			System.err.println("Unable to create file	 for database");
			System.exit(-1);
		}
		if(!createBase()){
			System.err.println("Database was not created properly");
			System.exit(-1);
		}
		System.out.println(duplicateKeys + " duplicate keys created (none were inserted don't worry)");
		System.out.println("test search data string = " + testData.getDataString() + '\n' + " it is the " + testData.getDataRecNo() + " record inserted at " +
								 testData.getDataDate());
		System.out.println("test search key string = " + testData.getKeyString() + '\n' + " it is the " + testData.getKeyRecNo() + " record inserted at " +
								 testData.getKeyDate());

		System.out.println("test value one record number : " + this.testData.getVal1RecNo() + " inserted on " + this.testData.getVal1Date());
		System.out.println("test value two record number : " + this.testData.getVal2RecNo() + " inserted on " + this.testData.getVal2Date());
		// comment out this block if you dont want key information w.r.t. secondary db
		/*
		if(Pref.getDbType() == 3){
			printKeys();
		}
		*/
	}
	
	@SuppressWarnings("unchecked")
	public void printKeys(){
		int count = 0;
		System.out.println("printing non-unique secondary database keys");
		long startTime = System.currentTimeMillis();
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry pdbKey = new DatabaseEntry();
			DatabaseEntry sdbkey = new DatabaseEntry();
			SecondaryCursor c = this.secdatabase.openSecondaryCursor(null, null);
			OperationStatus oprStatus = c.getFirst(sdbkey, pdbKey, data, LockMode.DEFAULT);
			while (oprStatus == OperationStatus.SUCCESS) {
				oprStatus = c.getNext(sdbkey,pdbKey, data, LockMode.DEFAULT);
				count++;
			}
		}catch(DatabaseException dbe){
			System.out.println("error printing secondary db keys: " + dbe.toString());
		}
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.println("there are " + count + " secondary keys it took " + duration + " milliseconds");

		count = 0;
		System.out.println("printin unique secondary database keys");
		startTime = System.currentTimeMillis();
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry pdbKey = new DatabaseEntry();
			DatabaseEntry sdbkey = new DatabaseEntry();
			SecondaryCursor c = this.secdatabase.openSecondaryCursor(null, null);
			OperationStatus oprStatus = c.getFirst(sdbkey, pdbKey, data, LockMode.DEFAULT);
			while( oprStatus == OperationStatus.SUCCESS ) {
				oprStatus = c.getNextNoDup(sdbkey,pdbKey, data, LockMode.DEFAULT);
				count++;
			}
		}catch(DatabaseException dbe){
			System.out.println("error printing secondary db keys: " + dbe.toString());
		}
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("there are " + count + " unique secondary keys it took " + duration + " milliseconds");
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
		DatabaseConfig dbConfig = new DatabaseConfig();

		switch(Pref.getDbType()){
			case 1:
							dbConfig.setType(DatabaseType.BTREE);
							break;
			case 2:
							dbConfig.setType(DatabaseType.HASH);
							break;
			case 3:
							return configureIndexFileDb();
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
		populateTable();
		System.out.println(PRIMARY_TABLE + " has been created of type: " + dbConfig.getType());
		return true;
	}	

	private final boolean configureIndexFileDb(){
		DatabaseConfig primaryConfig = new DatabaseConfig();
		SecondaryConfig secConfig = new SecondaryConfig();

				
		primaryConfig.setAllowCreate(true);
		primaryConfig.setType(DatabaseType.BTREE);
		
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
		populateTable();
		System.out.println(PRIMARY_TABLE + " has been created of type: " + primaryConfig.getType());
		
			
		
		secConfig.setMultiKeyCreator(new FirstCharKeyCreator());
		secConfig.setAllowCreate(true);
		secConfig.setType(DatabaseType.BTREE);
		secConfig.setSortedDuplicates(true);
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
		if(!result.toString().equals(OperationStatus.NOTFOUND)){
			try{
				this.database.putNoOverwrite(null, kdbt, ddbt);
				if(count == 1){
					this.testData.setTestData(dataString, count + 1);
					this.testData.setTestKey(keyString, count + 1);
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
	class FirstCharKeyCreator implements SecondaryMultiKeyCreator {

			
			public void createSecondaryKeys(SecondaryDatabase secondary,
                                      DatabaseEntry key,
                                      DatabaseEntry data,
                                      Set results){
        byte[] firstByte = new byte[1];
        firstByte[0] = data.getData()[0];
				DatabaseEntry result = new DatabaseEntry();
        result.setData(firstByte);
				results.add(result);
    }
	}
}
