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
		secConfig.setType(DatabaseType.BTREE);
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
		
		if(Interval.testMode){
			try{
				verifyDataSecondary();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}
		}
	}
	//only verifies for secondary database where key != data for all keys
	public void verifyDataSecondary() throws DatabaseException{
		DatabaseEntry secKey = new DatabaseEntry();
		DatabaseEntry pKey = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		SecondaryCursor cursor = this.dataSecondary.openSecondaryCursor(null, null);
		String[][] secondaryModel = Interval.getTestSecondary();
		String secondaryKey;		
		String primaryKey;
		String data;
		for(int i = 0; i < Interval.TEST_DATA.length; i++){
			if(cursor.getNext(secKey, pKey, data, LockMode.DEFAULT) != OperationStatus.SUCCESS){
				break;
			} 
			secondaryKey = new String(secKey.getData());
			primaryKey = new String(pKey.getData());
			data = new String(data.getData());
			if(!secondaryKey.equals(secondaryModel[i][0]))		
				throw new RuntimeException("secondary database is wrong. \nsec key (data): " + secondaryKey + "\nshould be: " +  secondaryModel[i][0]);
			if(!primaryKey.equals(secondaryModel[i][1]))
				throw new RuntimeException("secondary database is wrong. \nprimary key: " + primaryKey + "\nshould be: " +  secondaryModel[i][1]);
			secKey = new DatabaseEntry();
			pKey = new DatabaseEntry();
			data = new DatabaseEntry();
		}		
		
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
