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
	public static final String PRIMARY_TABLE2 = "/tmp/slmyers_db/primary_table_file2";
	

	
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
		DatabaseType type;
		int typeInt = Pref.getDbType();
		if(typeInt == 1 ){
			type = DatabaseType.BTREE;
		}else if (typeInt == 2){
			type = DatabaseType.HASH;
		}else{
			throw new RuntimeException("trying to create index file in DataBase.java");
		}
		
		this.database = createDb(PRIMARY_TABLE, type);
		System.out.println(PRIMARY_TABLE + " has been created of type: " + type);
		
		int count = populateTable(this.database, NO_RECORDS);
		System.out.println(PRIMARY_TABLE + " has been entered with " + count + " records.");
	}

	public Database getPrimaryDb(){
		return this.database;
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

	public int populateTable(Database my_table, int nrecs ) {
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
				System.out.println(PRIMARY_TABLE + "database is closed and removed");
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
}
