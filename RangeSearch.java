import com.sleepycat.db.*;
import java.lang.String;
import java.util.concurrent.TimeUnit;
import java.io.UnsupportedEncodingException;
public class RangeSearch{
	DataBase db;	
	TestData testData;
	StringGenerator gen;
	String lowerLimit;
	String upperLimit;
	ResultSet resultSet;
	Database dataBase;
	OperationStatus oprStatus;
	String retrievedKey;
	String retrievedData;

	public RangeSearch(){
		db = DataBase.getInstance();
		dataBase = DataBase.getInstance().getPrimaryDb();
		testData = TestData.getInstance();
		gen = StringGenerator.getInstance();
		lowerLimit = Interval.LOWER_LIMIT;
		upperLimit = Interval.UPPER_LIMIT;
		resultSet = new ResultSet();
	}

	public void execute(){
		int dbtype = Pref.getDbType();
		if(dbtype == 1 || dbtype == 3){
			try{
				btreeSearch();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}
		}else if(dbtype == 2){
			try{
				hashSearch();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}
		}		
	}	

	public void btreeSearch() throws DatabaseException, UnsupportedEncodingException{
		System.out.println("Search type is BTREE interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);

		Cursor cursor = dataBase.openCursor(null, null);
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		key.setData(lowerLimit.getBytes());
		key.setSize(lowerLimit.length());

		long startTime = System.nanoTime();
		oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		if(oprStatus == OperationStatus.SUCCESS){
			retrievedKey = new String(key.getData(), "UTF-8");
			retrievedData = new String(data.getData(), "UTF-8");
			resultSet.addResult(retrievedKey, retrievedData);
		}
		while(oprStatus == OperationStatus.SUCCESS && (retrievedKey.compareTo(upperLimit) <= 0) && (retrievedKey.compareTo(lowerLimit) >= 0) ){
			retrievedKey = new String(key.getData(), "UTF-8");
			retrievedData = new String(data.getData(), "UTF-8");
			resultSet.addResult(retrievedKey, retrievedData);
			oprStatus = cursor.getNext(key, data, LockMode.DEFAULT);
		}
		long endTime = System.nanoTime();
		long duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		
		System.out.println("there were " + resultSet.getCount() + " results found and it took " + duration + " microseconds.");
	}	

	public void hashSearch() throws DatabaseException, UnsupportedEncodingException{
		System.out.println("Search type is hashTable interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);
	
		Cursor cursor = dataBase.openCursor(null, null);
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		long startTime = System.nanoTime();
		oprStatus = cursor.getFirst(key, data, LockMode.DEFAULT);
		while(oprStatus == OperationStatus.SUCCESS){
			retrievedKey = new String(key.getData(), "UTF-8");
			
			if( (retrievedKey.compareTo(lowerLimit) >= 0) && (retrievedKey.compareTo(upperLimit) <= 0) ){
				retrievedData = new String(data.getData(), "UTF-8");
				resultSet.addResult(retrievedKey, retrievedData);
			}
			oprStatus = cursor.getNext(key, data, LockMode.DEFAULT);
		}
		long endTime = System.nanoTime();
		long duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		
		System.out.println("there were " + resultSet.getCount() + " results found and it took " + duration + " microseconds.");
	}
}
