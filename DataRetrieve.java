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
    String filename = "answers.txt";
    WriteToFile fileWrite = new WriteToFile();
    Map<String, String> records = new HashMap<String, String>();

    public DataRetrieve(){
	if (Pref.getDbType() == 3) {
	    DataBase db = DataBase.getInstance();
	    database = db.getPrimaryDb();
	    database = IndexFile.getInstance().getDataSecondary();     
	}else{
	    DataBase db = DataBase.getInstance();
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
	DatabaseEntry dbKey = new DatabaseEntry();
	DatabaseEntry pKey = new DatabaseEntry();
	
	//OperationStatus success = ;

	DatabaseEntry data = new DatabaseEntry();
	SecondaryDatabase database2 = null;

	String sData = null;
	String sKey = null;
		
	System.out.println("Searching for data in database");
	
	try {
	    //if there is a index file then we can use key search
	    if (Pref.getDbType() == 3) {
		//database = IndexFile.getInstance().getDataSecondary(); 	
		Cursor c = database.openCursor(null, null);
		c.getSearchKey(dbKey, data, LockMode.DEFAULT);
		sKey = new String (data.getData());
		sData = new String (dbKey.getData());
		records.put(sKey, sData);
		while(c.getNextDup(dbKey, data, LockMode.DEFAULT) == OperationStatus.SUCCESS){
		    sData = new String (data.getData());
		    sKey = new String (dbKey.getData());
		    records.put(sKey, sData);
		}
	    }else{    
		Cursor c = database.openCursor(null, null);
		c.getFirst(dbKey, data, LockMode.DEFAULT);
	    	 
		for(int i = 0; i < 100000; i++){
		    sData = new String (data.getData());
		    sKey = new String (dbKey.getData());
		
		    if(sData.equals(searchData)){
			records.put(sKey, sData);
		    }
		    c.getNext(dbKey, data, LockMode.DEFAULT);
		}
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
	//prints the number of records found
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
