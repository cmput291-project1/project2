import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class IndexFile{
	private static final String DATA_SECONDARY_TABLE = "/tmp/slmyers_db/secondary_table_file2";


	private static IndexFile indexFile = null;
	private SecondaryDatabase dataSecondary = null;


	protected IndexFile(){
		
	}
	
	public static IndexFile getInstance(){
		if(indexFile == null){
			indexFile = new IndexFile();
		}
		return indexFile;
	}

	public SecondaryDatabase getDataSecondary(){
		return this.dataSecondary;
	}

	public final boolean checkDirectory(){
		return new File(DataBase.DATABASE_DIR).exists();
	}

	public void configureDataSecondary(){
		SecondaryConfig secConfig = new SecondaryConfig();
		secConfig.setKeyCreator(new DataKeyCreator());
		secConfig.setAllowCreate(true);
		secConfig.setType(DatabaseType.HASH);
		secConfig.setSortedDuplicates(true);
		secConfig.setAllowPopulate(true);

		try{
			this.dataSecondary = new SecondaryDatabase(DATA_SECONDARY_TABLE, null, DataBase.getInstance().getPrimaryDb(), secConfig);
		}catch(DatabaseException dbe){
			System.err.println("Error while instantiating secondary 'data' database: " + dbe.toString());
			this.close();
			System.exit(-1);
		}catch(FileNotFoundException fnfe){
			System.err.println("Secondary database file not found: " + fnfe.toString());
		}
		
		System.out.println(DATA_SECONDARY_TABLE + " has been created of type: " + secConfig.getType());
		
		if(Interval.testMode || Interval.testDupMode){
			try{
				printSecondary();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}
		}
	}
	//only verifies for secondary database where key != data for all keys
	public void printSecondary() throws DatabaseException{
		DatabaseEntry secKey = new DatabaseEntry();
		DatabaseEntry pKey = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		SecondaryCursor cursor = this.dataSecondary.openSecondaryCursor(null, null);

		String secondaryKey;		
		String primaryKey;
		String dataString;
		OperationStatus status;
		secKey.setReuseBuffer(false);
		pKey.setReuseBuffer(false);
		data.setReuseBuffer(false);
		
		
		
		while( (status = cursor.getNextNoDup(secKey, pKey, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS){
			System.out.println("nonDup secondary key: " + new String(secKey.getData()) + "\n");
			System.out.println("primary key: " + new String(pKey.getData()) + "\n");
			System.out.println("data: " + new String(data.getData()) + "\n");
			while((status = cursor.getNextDup(secKey, pKey, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS){
				System.out.println("\tDuplicate secondary key: " + new String(secKey.getData()) + "\n");
				System.out.println("\tprimary key: " + new String(pKey.getData()) + "\n");
				System.out.println("\tdata: " + new String(data.getData()) + "\n");
			}
		}
		cursor.close();
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
