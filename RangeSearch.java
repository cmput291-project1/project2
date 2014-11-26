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
					primaryRangeSearch(dbtype);
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

	public void primaryRangeSearch(int type) throws DatabaseException{
		if(type == 1){
			btreeSearch();
		}else if(type == 2){
			hashSearch();
		}			
	}

	public void betreeSearch(){
		System.out.println("not implemented yet");
	}	

	public void hashSearch(){
		String lowerLimit = Interval.LOWER_LIMIT;
		String upperLimit = Interval.UPPER_LIMIT;

		Database db = DataBase.getInstance().getPrimaryDb();
		Cursor cursor = db.openCursor(null, null);

		OperationStatus oprStatus = OperationStatus.SUCCESS;
		
		while(oprStatus == OperationStatus.SUCCESS){

		}
		
	}

	public void secondaryRangeSearch(){
		System.out.println("not implemented yet");
	}
}
