import com.sleepycat.db.*;
import java.util.*;
import java.lang.Object;
import java.io.*;


public class DataRetrieve {
    //example key that has data: thndjefjyfwgpbmhzbfsfkubphiyvqirxwggmuxhvqnfmczshjaldffddmqyylwfmvbttcpvdjjffawzrdmwzykaspfugguntavetgdszamyogibkekcrvjuf

    //Example data values:
    //xeihommyihejcahcpnllqzsorhkervpimgxmfpqpaggerskevsnfcmdwjvtkulrxxckhxcavizjifgewprzxnkfgwdtaqltyxftdsymhmdyagsogpvhoc
    //hksbsenlqywqfnyephtunhyknikcpfnaxsvpngwiktgbmiyyjgqdauvbdbpdtixbekbfgrbnjtsafqlahbntvxybvnrfxehkiikmnuakxwwlxtcgpvhoc
    //dhjrxtadxewnvcpkhrgrbowprzblpslzszxhymcofszmagzbwdczkxyonshlmwroavtfpgigddzamcrbqisymyxnpwurpsbeekqvwviskwwlxtcgpvhoc
    //hvcthszqqdgppcwrcukjggolzfquilcxsssonsfsmgwvpuqthdyyvjsqidgiiyzssxptzmxilxocdqnysrjpetfejkzanpgxfhdubexahmsnfoqvuevfc
    //oedayeovsnubwvewkqwvdvvzticaluuzbcpabwxvsdszypbbxhsafxwftouhrmhowqozxsenbagzvfztlnhhlpnxrkojhscmmglitncrldmznkzcdwrpjaewdawvwe
    //cjfqwskqagppvgunbuuwjvehsytvyqvgmwxndniiebuccafpweohyzpzcpvpoltixalduhqyjktftelrhyrhbpnxrkojhscmmglitncrldmznkzcdwrpjaewdawvwe
    //cjfqwskqagppvgunbuuwjvehsytvyqvgmwxndniiebuccafpweohyzpzcpvpoltixalduhqyjktftelrhyrhbpnxrkojhscmmglitncrldmznkzcdwrpjaewdawvwe


    Scan scan = Scan.getInstance();
    Database database = null;
    SecondaryDatabase db2 = null;
    String filename = "answers";
    WriteToFile fileWrite = new WriteToFile();
    Map<String, String> records = new HashMap<String, String>();

    public DataRetrieve(){

			DataBase db = DataBase.getInstance();
	

		if (Pref.getDbType() == 3) {
			  IndexFile index = IndexFile.getInstance();
				if(index.getDataSecondary() == null){
			  	index.configureDataSecondary();
				}
			  db2 = index.getDataSecondary();

		}else{
			  database = db.getPrimaryDb();
		}	

		  }

		  //
		  public void getRecords() {
				// gets user input for record to search for  
				System.out.print("Please enter data you want to search for: ");
				String searchData = scan.getString();
	
				// start timer, end before returns
				long timeStart = System.nanoTime();
				DatabaseEntry secKey = new DatabaseEntry();
				DatabaseEntry pKey = new DatabaseEntry();
				DatabaseEntry dataEntry = new DatabaseEntry();
				//OperationStatus success = ;
				secKey.setReuseBuffer(false);
				pKey.setReuseBuffer(false);
				dataEntry.setReuseBuffer(false);
				
				System.out.println("DatabaseEntries are set to reuse byte array, true or false? " + dataEntry.getReuseBuffer());
	
				SecondaryDatabase database2 = null;
				
				String data = null;
				String key = null;
		
				System.out.println("Searching for data: " + searchData + " in database");
				try {
					//if there is a index file then we can use key search
					if (Pref.getDbType() == 3) {
						System.out.println("searching in index file");
						SecondaryCursor c = db2.openSecondaryCursor(null, null);
						//set dbKey to the data value we are searching for then move cursor
						secKey.setData(searchData.getBytes());
						secKey.setSize(searchData.length());
						if(c.getSearchKey(secKey, pKey, dataEntry, LockMode.DEFAULT)==OperationStatus.SUCCESS){
							data = new String (secKey.getData());
							key = new String (pKey.getData());
							records.put(key, data);
							/*
							secKey = new DatabaseEntry();
							pKey = new DatabaseEntry();
							dataEntry = new DatabaseEntry();*/
						//next if there are duplicate keys after the first get them 		
							while(c.getNextDup(secKey, pKey, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS){
								//if(sData == null){
								//    break;
								//}
								data = new String (secKey.getData());
								key = new String (pKey.getData());
								records.put(key, data);
								/*secKey = new DatabaseEntry();
								pKey = new DatabaseEntry();
								dataEntry = new DatabaseEntry();*/
							}
						}
						else{
							System.out.println("data " + searchData + " is not found");
						}
						c.close(); 
					}
					else{    
						//if BTREE or HASH then search all records using cursor and return matches 
						Cursor c = database.openCursor(null, null);
						c.getFirst(pKey, dataEntry, LockMode.DEFAULT);
						 
						for(int i = 0; i < 100000; i++){
								data = new String (dataEntry.getData());
								key = new String (pKey.getData());
								if(data.equals(searchData)){
									records.put(key, data);
								}
							c.getNext(pKey, dataEntry, LockMode.DEFAULT);
						}
						c.close(); 
					}
				   
			}
			catch (DatabaseException e) {
				System.err.println("unable to get key/value record!");
			}
	
	// end timer and print time
	long timeEnd = System.nanoTime();
	long time = timeEnd - timeStart;
	//time = time/1000000;
	System.out.println("TOTAL SEARCH TIME = " + time);
	System.out.println("Records found: "+ records.size());

	//get the key values of all records returned
	Set keys = records.keySet();
	Integer len = keys.size();
	String[] hashMapKeys = records.keySet().toArray(new String[len]);
	//iterate through hashMapKeys and write key/data pairs to answers.txt
	for(int i = 0; i < len; i++){
	    fileWrite.writeString(filename, hashMapKeys[i]); 
	    fileWrite.writeString(filename, records.get(hashMapKeys[i]));
	    fileWrite.writeString(filename, ""); 
    	 }
	return;
    }
  

}
