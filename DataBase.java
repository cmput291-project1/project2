import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class DataBase{
	private static final int NO_RECORDS = 100000;
	private static final String SAMPLE_TABLE = "/tmp/user_db/table";
	private static DataBase db = null;	
	
	protected DataBase(){
		if(!this.createTable()){
			System.err.println("Unable to create table");
			System.exit(-1);
		}
		createBase();
	}

	public static DataBase getInstance(){
		if(db == null){
			db = new DataBase();
		}

		return db;
	}

	public final boolean createTable(){
		File dbDirect = new File("/tmp/user_db");
	   boolean success = dbDirect.mkdirs();
		
		return success;
	}

	public final boolean createBase(){
		 // Create the database object.
	    // There is no environment for this simple example.
	    DatabaseConfig dbConfig = new DatabaseConfig();
		if(Pref.getDbType() == 1){
			dbConfig.setType(DatabaseType.BTREE);
		}else if(Pref.getDbType() == 2){
			dbConfig.setType(DatabaseType.DB_HASH);
		}else if(Pref.getDbType() == 3){
			System.out.println("not implemented yet");
		}else{
			System.err.println("invalid type selected");
			System.exit(-1);
		}
	    
	    dbConfig.setAllowCreate(true);
	    Database my_table = new Database(SAMPLE_TABLE, null, dbConfig);
	    System.out.println(SAMPLE_TABLE + " has been created");

	    /* populate the new database with NO_RECORDS records */
	    populateTable(my_table,NO_RECORDS);
	    System.out.println(NO_RECORDS + " records inserted into" + SAMPLE_TABLE);
	}	

	 /*
     *  To pouplate the given table with nrecs records
     */
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

                // to print out the key/data pair
                //System.out.println("KEY: " + s + " " + s.length());	

		/* to generate a data string */
		range = 64 + random.nextInt( 64 );
		s = "";
		for ( int j = 0; j < range; j++ ) 
		  s+=(new Character((char)(97+random.nextInt(26)))).toString();
                // to print out the key/data pair
                //System.out.println("DATA: " + s + " " + s.length());	
                //System.out.println("");
		
		/* to create a DBT for data */
		ddbt = new DatabaseEntry(s.getBytes());
		ddbt.setSize(s.length()); 

		//NEW CODE - checks if key already in the database
		OperationStatus result;
		result = my_table.exists(null, kdbt);
		if (!result.toString().equals("OperationStatus.NOTFOUND"))
			System.out.println("Key is already in the database!");

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

		/*
			change this to reference the database created
		*/
		/* cloase the database and the db enviornment */
	    my_table.close();

	    /* to remove the table */
	    my_table.remove(SAMPLE_TABLE,null,null);
	}
}
