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
		
		if(dbtype == 1 || dbtype == 2){
			primaryRangeSearch(dbtype);
		}
		else if (dbtype == 3){
			try{
				secondaryRangeSearch();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}
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
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}
		}			
	}

	public void btreeSearch(){
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
			String retrievedKey = new String(key.getData(), "UTF-8");
			String retrievedData = new String(data.getData(), "UTF-8");
			resultSet.addResult(retrievedKey, retrievedData);
		}
		while(oprStatus == OperationStatus.SUCCESS && (retrievedKey.compareTo(upperLimit) <= 0) ){
			String retrievedKey = new String(key.getData(), "UTF-8");
			String retrievedData = new String(data.getData(), "UTF-8");
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
			String retrievedKey = new String(key.getData(), "UTF-8");
			
			if( (retrievedKey.compareTo(lowerLimit) >= 0) && (retrievedKey.compareTo(upperLimit) <= 0) ){
				String retrievedData = new String(data.getData(), "UTF-8");
				resultSet.addResult(retrievedKey, retrievedData);
			}
			oprStatus = cursor.getNext(key, data, LockMode.DEFAULT);
		}
		long endTime = System.nanoTime();
		long duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		
		System.out.println("there were " + resultSet.getCount() + " results found and it took " + duration + " microseconds.");
	}

	public void secondaryRangeSearch() throws DatabaseException, UnsupportedEncodingException{
		System.out.println("Search type is indexFile interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);
			
		String firstChar = "a";
		DatabaseEntry data = new DatabaseEntry();
		DatabaseEntry pdbKey = new DatabaseEntry();
		DatabaseEntry sdbkey = new DatabaseEntry();

		sdbkey.setData(firstChar.getBytes());
		sdbkey.setSize(1);

		IndexFile indexFile = null;
		indexFile = indexFile.getInstance();

		String retrievedKey = null;
		String retrievedData = null;

		if(indexFile.getFirstCharSecondary() == null){
			indexFile.configureFirstCharSecondary();
		}
		
		SecondaryDatabase secdatabase = indexFile.getFirstCharSecondary();
		SecondaryCursor cursor = secdatabase.openSecondaryCursor(null, null);
		int count = 0;
		long startTime = System.nanoTime();
		oprStatus = cursor.getSearchKey(sdbkey, pdbKey, data, LockMode.DEFAULT);
		if(oprStatus == OperationStatus.SUCCESS){
			retrievedData = new String(data.getData(), "UTF-8");
			retrievedKey = new String(pdbKey.getData(), "UTF-8");
			resultSet.addResult(retrievedKey, retrievedData);
			count++;
		}
		while(oprStatus == OperationStatus.SUCCESS && count <= 150){
			oprStatus = cursor.getNextDup(sdbkey, pdbKey, data, LockMode.DEFAULT);
			if(oprStatus == OperationStatus.SUCCESS){
				retrievedData = new String(data.getData(), "UTF-8");
				retrievedKey = new String(pdbKey.getData(), "UTF-8");
				resultSet.addResult(retrievedKey, retrievedData);
				count++;
			}
		}
		
		long endTime = System.nanoTime();
		long duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		
		System.out.println("there were " + resultSet.getCount() + " results found and it took " + duration + " microseconds.");
	}
}
