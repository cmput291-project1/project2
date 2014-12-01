import com.sleepycat.db.*;
import java.lang.String;
import java.util.concurrent.TimeUnit;
import java.io.UnsupportedEncodingException;
public class RangeSearch{
	//these should be private
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
		lowerLimit = new String();
		upperLimit = new String();
		resultSet = new ResultSet();
		
	}

	public void execute(boolean test, String lowerLimit, String upperLimit){
		if(!DataBase.INITIALIZED){
			db.initDataBase();
		}
		int dbtype = Pref.getDbType();
		if(!test){
			getLimits(); //sets this.upperLimit and this.lowerLimit if we're not running a test
		}
		else{
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
		if(dbtype == 1 || dbtype == 3){
			try{
				btreeSearch();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}
		}else{
			try{
				hashSearch();
			}catch(DatabaseException dbe){
				dbe.printStackTrace();
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}
		}
		System.out.println("there were " + resultSet.getCount() + " results found and it took " + duration + " microseconds.");		
		resultSet.clear();
	}

	public void getLimits(){
		System.out.println("Enter lower key for range search or 'menu' to return to menu: ");
		lowerLimit = getInput();
		
		System.out.println("Enter upper key for range search or 'menu' to return to menu: ");
		upperLimit = getInput();
		
	}	

	public String getInput(){
		String input = new String();
		input = scan.getString();
		
		if(input.equals("menu")){
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

		if(Pref.getDbType() == 1){
			dataBase = db.getPrimaryDb();
		}else{
			dataBase = db.getIndexTree();

		}
		Cursor cursor = dataBase.openCursor(null, null);
		

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		key.setReuseBuffer(false);		
		key.setData(lowerLimit.getBytes());
		
		data.setReuseBuffer(false);

		long startTime = System.nanoTime();
		
		oprStatus = cursor.getSearchKeyRange(key, data, LockMode.DEFAULT);
		if(oprStatus == OperationStatus.SUCCESS){
			retrievedKey = new String(key.getData(), "UTF-8");
		}
		while(oprStatus == OperationStatus.SUCCESS && !(retrievedKey.compareTo(upperLimit) > 0) ){
			retrievedData = new String(data.getData(), "UTF-8");
			resultSet.addResult(retrievedKey, retrievedData);
			oprStatus = cursor.getNext(key, data, LockMode.DEFAULT);
			retrievedKey = new String(key.getData(), "UTF-8");
		}
		long endTime = System.nanoTime();
		cursor.close();
		this.duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		resultSet.writeResults();
	}	

	public void hashSearch() throws DatabaseException, UnsupportedEncodingException{
		dataBase = db.getPrimaryDb();
		System.out.println("Search type is hashTable interval search.");
		System.out.println("lower limit is: " + lowerLimit);
		System.out.println("upper limit is: " + upperLimit);
	
		Cursor cursor = dataBase.openCursor(null, null);
		if(cursor == null){
			db.initDataBase();
			cursor = dataBase.openCursor(null, null);

		}
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		
		key.setReuseBuffer(false);
		data.setReuseBuffer(false);
		
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
		resultSet.writeResults();
	}
}
