import com.sleepycat.db.*;
import java.lang.String;
import java.util.concurrent.TimeUnit;

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
			primaryRangeSearch(dbtype);
		}
		else if (dbtype == 3){
			secondaryRangeSearch();
		}
		else{
			System.out.println("invalid database type");
		}
	}	

	public void primaryRangeSearch(int type){
		if(type == 1){
			btreeSearch();
		}else if(type == 2){
			try{
				hashSearch();
			}catch(DatabaseException dbe){
					dbe.printStackTrace();
			}
		}			
	}

	public void btreeSearch(){
		System.out.println("not implemented yet");
	}	

	public void hashSearch() throws DatabaseException{
		String lowerLimit = Interval.LOWER_LIMIT;
		String upperLimit = Interval.UPPER_LIMIT;
		ResultSet resultSet = new ResultSet();

		Database db = DataBase.getInstance().getPrimaryDb();
		Cursor cursor = db.openCursor(null, null);
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		long startTime = System.nanoTime();
		OperationStatus oprStatus = cursor.getFirst(key, data, LockMode.DEFAULT);
		while(oprStatus == OperationStatus.SUCCESS){
			String retrievedKey = new String(key.getData());
			if( (retrievedKey.compareTo(lowerLimit) >= 0) && (retrievedKey.compareTo(upperLimit) <= 0) ){
				String retrievedData = new String(data.getData());
				resultSet.addResult(retrievedKey, retrievedData);
			}
			oprStatus = cursor.getNext(key, data, LockMode.DEFAULT);
		}
		long endTime = System.nanoTime();
		long duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		System.out.println("Search type is hashTable interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);
		System.out.println("there were " + resultSet.getCount() + " results found and it took " + duration + " microseconds.");
	}

	public void secondaryRangeSearch(){
		System.out.println("not implemented yet");
	}
}
