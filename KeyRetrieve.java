import com.sleepycat.db.*;
import java.util.concurrent.TimeUnit;
public class KeyRetrieve {
        //examples 
	// Random Key: thndjefjyfwgpbmhzbfsfkubphiyvqirxwggmuxhvqnfmczshjaldffddmqyylwfmvbttcpvdjjffawzrdmwzykaspfugguntavetgdszamyogibkekcrvjuf
	// Random Data: djjtchkroyzbyzqycjbjfvkxuwuywywkcvqltyagjavmhpewjuhfqsaawwzwvusrobrzmkbstekgkbawzkl

	// Last key: zzzzevhwlhknxjmuxosuaunkzvqaynhihjpryfhwzziekyjpwhtsffnvywrrcwpxpmavfbqzwbxwkwwodfxiwcxsvkfznsgymytctmjleuxohjezlyeodccqletvf
	// Last Data: woqlahmzrqlyhhjzdklmjqolkbhhiasczpgukpyobcwztbsffleukvbfdnqnubmorshieukeclbbtie

	// DATA BELOW I'M 100% SURE IS RIGHT AS I PRINTED IT AS THE DATABASE WAS BEING CREATED.
	// key: oohiqwurgzsllzvhgigpxqwzbenyyjxuczmewrecjmxuvgjlzrnfxlmgzoilphatfquyyaadzvnztflneudhykt
	// data: hnrkmhfcwwzplsykkcaxqdtsnlenyanztgszjgnzvdgpidenkicynfdsgeyvgbnbaxdxlodtwexdlp

	// key: hgfjlbziiqxoqcpdsfginenvozqvyhnsdzwnzmubbxybauzdsolvbtxqqsbdqznvkjixrnaxfmcx
	// data: zqmornwieprpscrhbldbvrnexrqwxtkzufejctzshhhmblhfiwzwnhcadcotyueskadatwtowrfkxf
	
	// key: szysjqctdhewbmldkyzojvnpatqlhofdiavalosfwidkrppmshbcmrihpxuqdmyhvyvpuplerjsqxaseuetlsznwfclfytahorfqfgagjslzkqmlebh
	// data: fgzqeklegbkhfmvybrrlaeqprkwchikudsvdgksxkoxpmeaeqchluypdvatveqreeevnnbqszeykoyddsflsgnktspcfpgggmvkricdrlzfamhisqyyuljptcsnc
	Scan scan = Scan.getInstance();
	// creates database or gets instance if it was already created
	DataBase db = DataBase.getInstance();
	Database database = null;
	String filename = "answers";
    	WriteToFile fileWrite = new WriteToFile();
	
	public KeyRetrieve(){	
		if(Pref.getDbType() != 3){
			database = db.getPrimaryDb();
			if(database == null){
				db.initDataBase();
				database = db.getPrimaryDb();
			}
		}else{
			database = db.getIndexTree();
			if(database == null){
				db.initDataBase();
				database = db.getIndexTree();
			}
		}
	}

	// main of KeyRetrieve
	public void getRecords(boolean test, String testKey) {
		String key = null;
		// check if databse was populated by user yet
		if (database == null) {
			System.out.println("Database needs to be populated first!");
			return;
		}
		if(!test){
			// gets user input for record to search for                               
			System.out.print("Please enter key you want to search for: ");
			key = scan.getString();
		}else{
			key = testKey;
		}

		// set up inputed key and data
		DatabaseEntry dbKey = new DatabaseEntry();
		dbKey.setData(key.getBytes()); 
		dbKey.setSize(key.length());
		DatabaseEntry data = new DatabaseEntry();

		// start timer, end before returns
		long timeStart = System.nanoTime();
		
		System.out.println("Searching for key in database");
		// get the data record
		try {
			database.get(null, dbKey, data, LockMode.DEFAULT);
		}	
		catch (DatabaseException e) {
			System.err.println("unable to get key/value record!");
		}

		// end timer and print time
		long timeEnd = System.nanoTime();
    		long time = TimeUnit.MICROSECONDS.convert(timeEnd - timeStart, TimeUnit.NANOSECONDS);
		// prints the records found
		if (data.getData() == null) {
			System.out.println("Records found: 0");
		}
		else {
			System.out.println("Records found: 1");
			String sData = new String (data.getData());
			String fKey = new String (dbKey.getData());
			System.out.println("Key: " + fKey);
			System.out.println("Data: " + sData);
			fileWrite.writeString(filename, fKey); 
	    		fileWrite.writeString(filename, sData);
	    		fileWrite.writeString(filename, "");
		}

    		System.out.println("Search time = " + time + " Microseconds");
		return;

	}

	public void getIndexFileRecord(boolean test, String testKey) throws DatabaseException{
		String key;		
		if(!test){
			System.out.print("Enter key: ");
			key = scan.getString();
		}
		else{
			key = testKey;
		}
		String data = null;
		int recordsFound = 0;
		Cursor c = database.openCursor(null, null);		
		DatabaseEntry cKey = new DatabaseEntry();
		DatabaseEntry cData = new DatabaseEntry();
		cKey.setSize(key.length());	
		cKey.setData(key.getBytes());	
		OperationStatus status;

		long startTime = System.nanoTime();
		status = c.getSearchKey(cKey, cData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			data = new String (cData.getData());
		}
		long endTime = System.nanoTime();
		long duration = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		if(status == OperationStatus.SUCCESS){
			recordsFound = 1;
			fileWrite.writeString(filename, key); 
	   		fileWrite.writeString(filename, data);
	   		fileWrite.writeString(filename, "");
		}
		System.out.println("Records found: " + recordsFound + " Search time: " + duration + " Microseconds");
		System.out.println("Record information");
		System.out.println("key: " + key);
		System.out.println("data: " + data);
	}
}
