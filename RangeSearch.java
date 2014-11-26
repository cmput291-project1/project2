import com.sleepycat.db.*;


public class RangeSearch{
	DataBase db;	
	TestData testData;
	StringGenerator gen;
	public RangeSearch(){
		db = DataBase.getInstance();
		testData = TestData.getInstance();
		gen = StringGenerator.getInstance();
	}

	public void execute(){
		int dbtype = Pref.getDbType();
		
		if(dbtype == 1 || dbtype == 2){
			try{
					primaryRangeSearch();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}
		}
		else if (dbtype == 3){
			secondaryRangeSearch();
		}
		else{
			System.out.println("invalid database type");
		}
	}	

	public void primaryRangeSearch() throws DatabaseException{
		Cursor cursor = db.getPrimaryDb().openCursor(null, null);
		Cursor cursorTwo = cursor.dup(false);
		String key1 = gen.generateString();		
		String key2 = gen.generateString();

		
		
		OperationStatus status = null;		

	
		
	}

	public void secondaryRangeSearch(){
		System.out.println("not implemented yet");
	}
}
