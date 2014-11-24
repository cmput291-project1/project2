import com.sleepycat.db.*;

//TODO Append key/value pair to file named "answers" in local directory
public class KeyRetrieve {
        //example key that has data: thndjefjyfwgpbmhzbfsfkubphiyvqirxwggmuxhvqnfmczshjaldffddmqyylwfmvbttcpvdjjffawzrdmwzykaspfugguntavetgdszamyogibkekcrvjuf
	Scan scan = Scan.getInstance();
	// creates database or gets instance if it was already created
	DataBase db = DataBase.getInstance();
	Database database = db.getPrimaryDb();

	// main of KeyRetrieve
	public void getRecords() {
		// gets user input for record to search for                               
		System.out.print("Please enter key you want to search for: ");
		String key = scan.getString();
		
		// start timer, end before returns
		long timeStart = System.nanoTime();
		DatabaseEntry dbKey = new DatabaseEntry();
		dbKey.setData(key.getBytes()); 
		dbKey.setSize(key.length());
		
		DatabaseEntry data = new DatabaseEntry();
		Database database2 = null;
		if (Pref.getDbType() == 3) {
			database2 = db.getSecondaryDb();
		}
		
		System.out.println("Searching for key in database");
		// get the data record using the secondary index if IndexFile database or primary if hash/btree database
		try {
			if (database2 == null) {
				database.get(null, dbKey, data, LockMode.DEFAULT);
			}
			else
				database2.get(null, dbKey, data, LockMode.DEFAULT);		
		}
		catch (DatabaseException e) {
			System.err.println("unable to get key/value record!");
		}

		// prints the records found
		if (data.getData() == null) {
			System.out.println("Records found: 0");
		}
		else {
			System.out.println("Records found: 1");
			String sData = new String (data.getData());
		}

		// end timer and print time
		long timeEnd = System.nanoTime();
    		long time = timeEnd - timeStart;
		//time = time/1000000;
    		System.out.println("TOTAL SEARCH TIME = " + time);
		return;

	}
}
