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
		populateTable(this.database, NO_RECORDS);
		System.out.println(PRIMARY_TABLE + " has been created of type: " + dbConfig.getType());
		return true;
	}	

	static void populateTable(Database my_table, int nrecs ) {
		int range;
		DatabaseEntry kdbt, ddbt;
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

				//NEW CODE - checks if key already in the database
				OperationStatus result;
				result = my_table.exists(null, kdbt);
				if (!result.toString().equals("OperationStatus.NOTFOUND"))
					throw new RuntimeException("Key is already in the database!");

				/* to insert the key/data pair into the database */
		    my_table.putNoOverwrite(null, kdbt, ddbt);
			}
		}
		catch (DatabaseException dbe) {
			System.err.println("Populate the table: "+dbe.toString());
		  System.exit(1);
		}
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
