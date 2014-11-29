import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class IndexFile2{
	private static final String DATA_SECONDARY_TABLE = "./tmp/user_db/secondary_table_file2";


	private static IndexFile indexFile = null;
	private Database dataSecondary = null;


	protected IndexFile2(){
		
	}
	
	public static IndexFile getInstance(){
		if(indexFile == null){
			indexFile = new IndexFile();
		}
		return indexFile;
	}

	public Database getDataSecondary(){
		return this.dataSecondary;
	}

	public final boolean checkDirectory(){
		return new File(DataBase.DATABASE_DIR).exists();
	}

	public void configureDataSecondary(){
		DataBase db = DataBase.getInstance();
		Database database = null;
		database = db.getPrimaryDb();
		DatabaseEntry dbKey = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setType(DatabaseType.BTREE);
	    	dbConfig.setAllowCreate(true);
		dbConfig.setSortedDuplicates(true);

		try{
			this.dataSecondary = new Database(DATA_SECONDARY_TABLE, null, dbConfig);
			// go through every record in primary database
			Cursor c = database.openCursor(null, null);
			OperationStatus oprStatus = c.getFirst(dbKey, data, LockMode.DEFAULT);
			while (oprStatus == OperationStatus.SUCCESS){
				this.dataSecondary.putNoOverwrite(null, data, dbKey);
				data = new DatabaseEntry();
				oprStatus = c.getNext(dbKey, data, LockMode.DEFAULT);
			}
			
			
		}catch(DatabaseException dbe){
			System.err.println("Error while instantiating secondary 'data' database: " + dbe.toString());
			this.close();
			System.exit(-1);
		}catch(FileNotFoundException fnfe){
			System.err.println("Secondary database file not found: " + fnfe.toString());
		}
		
		System.out.println(DATA_SECONDARY_TABLE + " has been created of type: " + dbConfig.getType());
	}

	


	public void close(){
		try{
			if(this.dataSecondary != null){
				this.dataSecondary.close();
			}
		}catch(DatabaseException dbe){
			System.err.println("unable to close database");
			dbe.printStackTrace();
		}
		dataSecondary = null;
	}

	
}
