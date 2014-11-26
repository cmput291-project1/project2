import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class IndexFile{
	private static final String DATA_SECONDARY_TABLE = "./tmp/user_db/secondary_table_file2";
	private static final String LENGTH_SECONDARY_TABLE = "./tmp/user_db/secondary_table_file3";

	private static IndexFile indexFile = null;
	private SecondaryDatabase dataSecondary = null;
	private SecondaryDatabase lengthSecondary = null;

	protected IndexFile(){
		
	}
	
	public IndexFile getInstance(){
		if(indexFile == null){
			indexFile = new IndexFile();
		}
		return indexFile;
	}

	public SecondaryDatabase getDataSecondary(){
		return this.dataSecondary;
	}

	public SecondaryDatabase getLengthSecondary(){
		return this.lengthSecondary;
	}		

	private final boolean checkDirectory(){
		return new File(DataBase.DATABASE_DIR).exists();
	}

	private void configureDataSecondary(){
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

	private void configureLengthSecondary(){
		SecondaryConfig secConfig = new SecondaryConfig();
		secConfig.setKeyCreator(new FirstCharLengthKeyCreator());
		secConfig.setAllowCreate(true);
		secConfig.setType(DatabaseType.HASH);
		secConfig.setSortedDuplicates(true);
		secConfig.setAllowPopulate(true);

		try{
			this.lengthSecondary = new SecondaryDatabase(DATA_SECONDARY_TABLE, null, DataBase.getInstance().getPrimaryDb(), secConfig);
		}catch(DatabaseException dbe){
			System.err.println("Error while instantiating secondary 'data' database: " + dbe.toString());
			this.close();
			System.exit(-1);
		}catch(FileNotFoundException fnfe){
			System.err.println("Secondary database file not found: " + fnfe.toString());
		}
		
		System.out.println(DATA_SECONDARY_TABLE + " has been created of type: " + secConfig.getType());
	}

	private void close(){
		try{
			if(this.dataSecondary != null){
				this.dataSecondary.close();
				this.dataSecondary.remove(DATA_SECONDARY_TABLE, null, null);
			}
			if(this.lengthSecondary != null){
				this.lengthSecondary.close();
				this.lengthSecondary.remove(LENGTH_SECONDARY_TABLE, null, null);
			}
		}catch(DatabaseException dbe){
			System.err.println("unable to close database");
			dbe.printStackTrace();
		}catch (FileNotFoundException fnfe){
			System.err.println("can not find file to remove Database");
			fnfe.printStackTrace();
		}
		dataSecondary = null;
		lengthSecondary = null;
	}
}
