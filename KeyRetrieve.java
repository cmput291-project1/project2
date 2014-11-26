import com.sleepycat.db.*;

//TODO - Append key/value pair to file named "answers" in local directory
//     - get IndexFile search working
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
		DatabaseEntry sKey = new DatabaseEntry();
		dbKey.setData(key.getBytes()); 
		dbKey.setSize(key.length());
		
		DatabaseEntry data = new DatabaseEntry();
		SecondaryDatabase database2 = null;
		if (Pref.getDbType() == 3) {
			//TODO STEVE COMMENTS
			//use IndexFile.getInstance().getDataSecondary() for the secondary table you wanted
			// or IndexFIle.getInstance().getLengthSecondary() for the other one
			//database2 = db.getSecondaryDb();
		}
		
		System.out.println("Searching for key in database");
		// get the data record using the secondary index if IndexFile database or primary if hash/btree database
		try {
			if (database2 == null) {
				database.get(null, dbKey, data, LockMode.DEFAULT);
			}
			// may not be correct way to do this
			else {	
				//TODO get code underneath to work for multiple instances of a letter being a secondary index key,
				// code only works for first instance found currently and finding the right key won't work if it's
				// in a different key of the same letter
				/*
				// parses the first char from the given string
				String secKey = key.substring(0,1);
				sKey.setData(secKey.getBytes()); 
				sKey.setSize(secKey.length());

				Cursor c = database2.openSecondaryCursor(null, null);
				OperationStatus oprStatus = c.getSearchKeyRange(sKey, data, LockMode.DEFAULT);
				while (oprStatus == OperationStatus.SUCCESS) {
					String dCheck = new String (data.getData());
					if (!(dCheck.equals(key))) {
						oprStatus = c.getNextDup(sKey, data, LockMode.DEFAULT);
						continue;
					}
					else {
						System.out.println(dCheck);
						System.out.println("MATCH");
						break;
					}
				}
				*/

				// Should work but doesn't find anything
				//database2.getSearchBoth(null, sKey, dbKey, data, LockMode.DEFAULT);

				// only finds first key with correct letter, can't iterate through
				//database2.get(null, sKey, dbKey, data, LockMode.DEFAULT);

				Cursor c = database2.openSecondaryCursor(null, null);
				OperationStatus oprStatus = c.getFirst(dbKey, data, LockMode.DEFAULT);
				while (oprStatus == OperationStatus.SUCCESS) {
					String s = new String(dbKey.getData());
					System.out.println(s);
					oprStatus = c.getNext(dbKey, data, LockMode.DEFAULT);
				}
			}		
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
			System.out.println(sData);
		}

		// end timer and print time
		long timeEnd = System.nanoTime();
    		long time = timeEnd - timeStart;
		//time = time/1000000;
    		System.out.println("TOTAL SEARCH TIME = " + time);
		return;

	}
}
