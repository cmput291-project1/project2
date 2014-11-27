import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class IndexFile{
	private static final String DATA_SECONDARY_TABLE = "./tmp/user_db/secondary_table_file2";


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
			this.dataSecondary = new SecondaryDatabase(LENGTH_SECONDARY_TABLE, null, DataBase.getInstance().getPrimaryDb(), secConfig);
		}catch(DatabaseException dbe){
			System.err.println("Error while instantiating secondary 'data' database: " + dbe.toString());
			this.close();
			System.exit(-1);
		}catch(FileNotFoundException fnfe){
			System.err.println("Secondary database file not found: " + fnfe.toString());
		}
		
		System.out.println(DATA_SECONDARY_TABLE + " has been created of type: " + secConfig.getType());
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
