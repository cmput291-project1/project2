import com.sleepycat.db.*;
import java.util.*;
import java.lang.Object;
import java.io.*;



//TODO - Append key/value pair to file named "answers" in local directory
public class DataRetrieve {
    //example key that has data: thndjefjyfwgpbmhzbfsfkubphiyvqirxwggmuxhvqnfmczshjaldffddmqyylwfmvbttcpvdjjffawzrdmwzykaspfugguntavetgdszamyogibkekcrvjuf

    //Example data values:
    //xeihommyihejcahcpnllqzsorhkervpimgxmfpqpaggerskevsnfcmdwjvtkulrxxckhxcavizjifgewprzxnkfgwdtaqltyxftdsymhmdyagsogpvhoc
    //oedayeovsnubwvewkqwvdvvzticaluuzbcpabwxvsdszypbbxhsafxwftouhrmhowqozxsenbagzvfztlnhhlpnxrkojhscmmglitncrldmznkzcdwrpjaewdawvwe
    //etxgkoxrnuvwqfmlmkcpkmqofrtllhbavzcikyfekahlxpmzyrpzbbexpnitpqsflwjwiebcpicqhssrhyrhbpnxrkojhscmmglitncrldmznkzcdwrpjaewdawvwe
    //wymzvrnutnnzitpqkkqbjcsjwzzngbdbqjrixagvcpwudkpxstljtagdsutoijioqbqbpjsudeglpbcyjxiayzcyfbusgdpnsqzujxqzldmznkzcdwrpjaewdawvwe



    Scan scan = Scan.getInstance();
    // creates database or gets instance if it was already created
    DataBase db = DataBase.getInstance();
    Database database = db.getPrimaryDb();
    File answers = new File("answers.txt");
    
    Map<String, String> records = new HashMap<String, String>();
    
    //
    public void getRecords() {
	// gets user input for record to search for  
	System.out.print("Please enter data you want to search for: ");
	String searchData = scan.getString();
		
	// start timer, end before returns
	long timeStart = System.nanoTime();
	DatabaseEntry dbKey = new DatabaseEntry();
	DatabaseEntry pKey = new DatabaseEntry();
		
	DatabaseEntry data = new DatabaseEntry();
	SecondaryDatabase database2 = null;

		
	System.out.println("Searching for data in database");
	//if (Pref.getDbType() == 3) {
	//    database = db.getSecondaryDb(); 
	//}
	try {
	   	    
	    Cursor c = database.openCursor(null, null);
	    c.getFirst(dbKey, data, LockMode.DEFAULT);
	    	 
	    for(int i = 0; i < 100000; i++){
		String sData = new String (data.getData());
		String sKey = new String (dbKey.getData());
		
		if(sData.equals(searchData)){
		    records.put(sKey, sData);
		}
		c.getNext(dbKey, data, LockMode.DEFAULT);
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
	//prints the records found
	System.out.println("Records found: "+ records.size());


	Set hashMapKeys = records.keySet();
	int length = hashMapKeys.size();
	//iterate through hashMapKeys
	
	//try{
	//    FileWriter writer = new FileWriter(answers, true);
	//   for(int i = 0; i < length; i++){
	//	writer.write(hashMapKeys(i)+"\n"); 
	//	System.out.println();
	//	writer.write(records.get(i.next())+"/n");
	//	writer.write("/n"); 
      
	//  }
	//   writer.flush();
	//   writer.close();	
	//}catch(IOException e){
	//    e.printStackTrace();
	//}
	
	return;

    }
}
