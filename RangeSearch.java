import com.sleepycat.db.*;


public class RangeSearch{
	DataBase db;	
	TestData testData;

	public RangeSearch(){
		db = DataBase.getInstance();
		testData = TestData.getInstance();
	}

	public void execute(){
		int dbtype = Pref.getInstance().getDbtype();
		
		if(dbtype == 1 || dbtype == 2){
			primaryRangeSearch();
		}
		else if (dbtype == 3){
			secondaryRangeSearch();
		}
		else{
			System.out.println("invalid database type");
		}
	}	

	public void primaryRangeSearch() throws DatabaseException{
		Cursor cursor = db.getPrimaryDb().openCursor();
		DatabaseEntry data1 = new DatabaseEntry();
		DatabaseEntry key1 = new DatabaseEntry();
		DatabaseEntry data2 = new DatabaseEntry();
		DatabaseEntry key2 = new DatabaseEntry();
		DatabaseEntry returnedData = new DatabaseEntry();
		DatabaseEntry returnedKey = new DatabaseEntry();		
		
		
		OperationStatus status = null;		

	
		
	}

	public void secondaryRangeSearch(){
		System.out.println("not implemented yet");
	}
}
