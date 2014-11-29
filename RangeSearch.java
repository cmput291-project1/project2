import com.sleepycat.db.*;
import java.lang.String;
import java.util.concurrent.TimeUnit;
import java.io.UnsupportedEncodingException;
public class RangeSearch{
	DataBase db;	
	String lowerLimit;
	String upperLimit;
	ResultSet resultSet;
	Database dataBase;
	OperationStatus oprStatus;
	String retrievedKey;
	String retrievedData;
	Scan scan;
	long duration;

	public RangeSearch(){
		scan = Scan.getInstance();
		db = DataBase.getInstance();
		dataBase = DataBase.getInstance().getPrimaryDb();
		gen = StringGenerator.getInstance();
		lowerLimit = new String();
		upperLimit = new String();
		resultSet = new ResultSet();
		
	}

	public void execute(){
		int dbtype = Pref.getDbType();
		getLimits();
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
		System.out.println("there were " + resultSet.getCount() + " results found and it took " + duration + " microseconds.");		
		//TODO remove this line when assignment submitted
		//this.resultSet.print();
		//resultSet.duplicateKeys();

	}

	public void getLimits(){
		//lowerLimit = Interval.TEST_LOWER_LIMIT;
		//upperLimit = Interval.TEST_UPPER_LIMIT;
		//TODO edit code before submission
		
		System.out.println("Enter lower key for range search or 'm' to return to menu: ");
		lowerLimit = getInput();
		
		System.out.println("Enter lower key for range search or 'm' to return to menu: ");
		upperLimit = getInput();
		
	}	

	public String getInput(){
		String input = new String();
		input = scan.getString();

		if(input.equals("lower limit")){
			return Interval.LOWER_LIMIT;
		}
		else if(input.equals("upper limit")){
			return Interval.UPPER_LIMIT;
		}
		else if(input.equals("m")){
			Menu menu = new Menu();
			return null;
		}else{
			return input;
		}
	}

	public void btreeSearch() throws DatabaseException, UnsupportedEncodingException{
		System.out.println("Search type is BTREE interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);

		Cursor cursor = dataBase.openCursor(null, null);
		if(cursor == null){
			throw new RuntimeException("cursor opened in RangeSearch.btreeSearch() is null");
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		key.setData(lowerLimit.getBytes());
		key.setSize(lowerLimit.length());
		oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		
		if(oprStatus == OperationStatus.SUCCESS){
			System.out.println("lower limit found");
		}
		else{
			System.out.println("lower limit is not found: " + new String(key.getData()));
		}
		cursor.getFirst(key, data, LockMode.DEFAULT);
		
		if(oprStatus == OperationStatus.SUCCESS){
			key.setData(upperLimit.getBytes());
			key.setSize(upperLimit.length());
			oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		}
		if(oprStatus == OperationStatus.SUCCESS){
			System.out.println("upper limit found");
		}
		else{
			System.out.println("upper limit is not found: " + new String(key.getData()));
		}
		
		key.setData(lowerLimit.getBytes());
		key.setSize(lowerLimit.length());
		int count = 0;
		long startTime = System.nanoTime();
		
		oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		if(oprStatus == OperationStatus.SUCCESS){
			retrievedKey = new String(key.getData(), "UTF-8");
		}
		while(oprStatus == OperationStatus.SUCCESS && !(retrievedKey.compareTo(upperLimit) > 0) ){
			count++;
			if(!resultSet.containsKey(retrievedKey)){
				retrievedData = new String(data.getData(), "UTF-8");
				resultSet.addResult(retrievedKey, retrievedData);
			}
			oprStatus = cursor.getNext(key, data, LockMode.DEFAULT);
			retrievedKey = new String(key.getData(), "UTF-8");
			
		}
		long endTime = System.nanoTime();
		cursor.close();
		this.duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		
		resultSet.writeResults("btree.txt");
		System.out.println(count + " records have been examined");
		System.out.println("btree search has returned the lower limit: " + this.resultSet.containsKey(lowerLimit));
		System.out.println("btree search has returned the upper limit: " + this.resultSet.containsKey(upperLimit));
	}	

	public void hashSearch() throws DatabaseException, UnsupportedEncodingException{
		System.out.println("Search type is hashTable interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);
	
		Cursor cursor = dataBase.openCursor(null, null);
		if(cursor == null){
			throw new RuntimeException("cursor opened in RangeSearch.hashSearch() is null");
		}
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		key.setData(lowerLimit.getBytes());
		key.setSize(lowerLimit.length());
		oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		
		if(oprStatus == OperationStatus.SUCCESS){
			System.out.println("lower limit found");
		}
		else{
			System.out.println("lower limit is not found: " + new String(key.getData()));
		}
		cursor.getFirst(key, data, LockMode.DEFAULT);
		
		if(oprStatus == OperationStatus.SUCCESS){
			key.setData(upperLimit.getBytes());
			key.setSize(upperLimit.length());
			oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		}
		if(oprStatus == OperationStatus.SUCCESS){
			System.out.println("upper limit found");
		}
		else{
			System.out.println("upper limit is not found: " + new String(key.getData()));
		}
		
		
		int count = 0;
		long startTime = System.nanoTime();
		oprStatus = cursor.getFirst(key, data, LockMode.DEFAULT);
		while(oprStatus == OperationStatus.SUCCESS){
			retrievedKey = new String(key.getData(), "UTF-8");
			count++;
			if( (retrievedKey.compareTo(lowerLimit) >= 0) && (retrievedKey.compareTo(upperLimit) <= 0) ){
				retrievedData = new String(data.getData(), "UTF-8");
				resultSet.addResult(retrievedKey, retrievedData);
			}
			
			oprStatus = cursor.getNext(key, data, LockMode.DEFAULT);
		}
		long endTime = System.nanoTime();
		cursor.close();
		this.duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		resultSet.writeResults("hash.txt");
		System.out.println(count + " records have been examined");
		System.out.println("hash search has returned the lower limit: " + this.resultSet.containsKey(lowerLimit));
		System.out.println("hash search has returned the upper limit: " + this.resultSet.containsKey(upperLimit));
	}

	public final boolean verify(){
		System.out.println("verifying");
		return this.resultSet.verifyKeyRange(lowerLimit, upperLimit);
	}
}
