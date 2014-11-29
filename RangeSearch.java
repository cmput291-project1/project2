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
		dataBase = db.getInstance().getPrimaryDb();
		if(dataBase == null){
			System.out.println("Database needs to be populated first!");r
			return;
		}
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

	}

	public void getLimits(){
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
			System.out.println("Database needs to be populated first!");r
			return;
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		key.setReuseBuffer(false);		
		key.setData(lowerLimit.getBytes());
		
		data.setReuseBuffer(false);

		long startTime = System.nanoTime();
		
		oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		if(oprStatus == OperationStatus.SUCCESS){
			retrievedKey = new String(key.getData(), "UTF-8");
		}
		while(oprStatus == OperationStatus.SUCCESS && !(retrievedKey.compareTo(upperLimit) > 0) ){
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
		resultSet.writeResults(null);
		if(Interval.testMode){
			this.verify();
		}
	}	

	public void hashSearch() throws DatabaseException, UnsupportedEncodingException{
		System.out.println("Search type is hashTable interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);
	
		Cursor cursor = dataBase.openCursor(null, null);
		if(cursor == null){
			System.out.println("Database needs to be populated first!");r
			return;
		}
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		key.setReuseBuffer(false);
		data.setReuseBuffer(false);
		oprStatus = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		
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
		cursor.close();
		this.duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		resultSet.writeResults(null);
		if(Interval.testMode){
			this.verify();
		}
	}

	public final boolean verify(){
		System.out.println("verifying");
		return this.resultSet.verifyKeyRange(lowerLimit, upperLimit);
	}
}
