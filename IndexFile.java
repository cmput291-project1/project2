import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class IndexFile{
	private static final String DATA_SECONDARY_TABLE = "./tmp/user_db/secondary_table_file2";
	private static final String LENGTH_SECONDARY_TABLE = "./tmp/user_db/secondary_table_file3";
	private static final String FIRST_CHAR_SECONDARY = "./tmp/user_db/secondary_table_file4";

	private static IndexFile indexFile = null;
	private SecondaryDatabase dataSecondary = null;
	private SecondaryDatabase lengthSecondary = null;
	private SecondaryDatabase firstCharSecondary = null;

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

	public SecondaryDatabase getLengthSecondary(){
		return this.lengthSecondary;
	}
	
	public SecondaryDatabase getLengthSecondary(){
		return this.firstCharSecondary;
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

	public void configureLengthSecondary(){
		SecondaryConfig secConfig = new SecondaryConfig();
		secConfig.setKeyCreator(new FirstCharLengthKeyCreator());
		secConfig.setAllowCreate(true);
		secConfig.setType(DatabaseType.BTREE);
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

	public void close(){
		try{
			if(this.dataSecondary != null){
				this.dataSecondary.close();
			}
			if(this.lengthSecondary != null){
				this.lengthSecondary.close();
			}
			if(this.firstCharSecondary != null){
				this.firstCharSecondary.close();
			}
		}catch(DatabaseException dbe){
			System.err.println("unable to close database");
			dbe.printStackTrace();
		}
		dataSecondary = null;
		lengthSecondary = null;
		firstCharSecondary = null;
	}

	public void configureFirstCharSecondary(){
		SecondaryConfig secConfig = new SecondaryConfig();
		secConfig.setKeyCreator(new FirstCharKeyCreator());
		secConfig.setAllowCreate(true);
		secConfig.setType(DatabaseType.HASH);
		secConfig.setSortedDuplicates(true);
		secConfig.setAllowPopulate(true);

		try{
			this.lengthSecondary = new SecondaryDatabase(FIRST_CHAR_SECONDARY, null, DataBase.getInstance().getPrimaryDb(), secConfig);
		}catch(DatabaseException dbe){
			System.err.println("Error while instantiating secondary 'first char' database: " + dbe.toString());
			this.close();
			System.exit(-1);
		}catch(FileNotFoundException fnfe){
			System.err.println("Secondary database file not found: " + fnfe.toString());
		}
		
		System.out.println(FIRST_CHAR_SECONDARY + " has been created of type: " + secConfig.getType());
	}
}
