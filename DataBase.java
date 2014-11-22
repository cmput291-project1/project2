import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class DataBase{
	private static final int NO_RECORDS = 100000;
	private static final String TABLE = "/tmp/user_db/table";
	private static DataBase db = null;	
	private Database database = null;	
	private Random random;

	protected DataBase(){
		random = new Random(1000000);
		if(!createFile()){
			System.err.println("Unable to create file for database");
			System.exit(-1);
		}
		if(!createBase()){
			System.err.println("Database was not created properly");
			System.exit(-1);
		}
		populateTable();

	}

	public static DataBase getInstance(){
		if(db == null){
			db = new DataBase();
		}

		return db;
	}

	private final boolean createFile(){
		File dbDirect = new File("/tmp/user_db");
	  dbDirect.mkdirs();
		return dbDirect.exists();
	}

	private final boolean createBase(){
		// Create the database object.
		// There is no environment for this simple example.
		DatabaseConfig dbConfig = new DatabaseConfig();

		if(Pref.getDbType() == 1){
			dbConfig.setType(DatabaseType.BTREE);
		}else if(Pref.getDbType() == 2){
			dbConfig.setType(DatabaseType.HASH);
		}else if(Pref.getDbType() == 3){
			System.out.println(" indexFile not implemented yet");
			System.exit(-1);
		}else{
			System.err.println("invalid type selected");
			System.exit(-1);
		}
	  
		dbConfig.setAllowCreate(true);
		try{
			this.database = new Database(TABLE, null, dbConfig);
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
		
		System.out.println(TABLE + " has been created of type: " + dbConfig.getType());

		
	  System.out.println(NO_RECORDS + " records inserted into" + TABLE);
	
		return true;
	}	

	 /*
     *  To pouplate the given table with nrecs records
     */
	private void populateTable() {
		int count = 0;
		while(count < NO_RECORDS){
			count += addEntry();
		}
	}
	
	private int addEntry(){
		int range;
    DatabaseEntry kdbt, ddbt;
		String s;

		range = 64 + random.nextInt( 64 );
		s = "";
		for ( int j = 0; j < range; j++ ) 
			s+=(new Character((char)(97+random.nextInt(26)))).toString();
		
		kdbt = new DatabaseEntry(s.getBytes());
		kdbt.setSize(s.length()); 

		range = 64 + random.nextInt( 64 );
		s = "";
		for ( int j = 0; j < range; j++ ) 
			s+=(new Character((char)(97+random.nextInt(26)))).toString();
		              
		ddbt = new DatabaseEntry(s.getBytes());
		ddbt.setSize(s.length()); 
		
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
			}catch(DatabaseException dbe){
				System.err.println("Unable to put key/data pair in database");
				dbe.printStackTrace();
			}
			return 1;
		}
		return 0;
	}


	public void close(){
		try{
			this.database.close();
			this.database.remove(TABLE,null,null);
		}catch(DatabaseException dbe){
			System.err.println("unable to close database");
			dbe.printStackTrace();
		}catch (FileNotFoundException fnfe){
			System.err.println("can not find file to remove Database");
			fnfe.printStackTrace();
		}
	}
}
