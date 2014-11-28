import com.sleepycat.db.*;

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
	String filename = "answers.txt";
    	WriteToFile fileWrite = new WriteToFile();
	
	public KeyRetrieve(){	
		database = db.getPrimaryDb();
	}

	// main of KeyRetrieve
	public void getRecords() {
		// gets user input for record to search for                               
		System.out.print("Please enter key you want to search for: ");
		String key = scan.getString();
		System.out.println(key);
		
		// start timer, end before returns
		long timeStart = System.nanoTime();

		// set up inputed key and data
		DatabaseEntry dbKey = new DatabaseEntry();
		//DatabaseEntry sKey = new DatabaseEntry();
		dbKey.setData(key.getBytes()); 
		dbKey.setSize(key.length());
		System.out.println();
		DatabaseEntry data = new DatabaseEntry();
		
		System.out.println("Searching for key in database");
		// get the data record
		try {
			database.get(null, dbKey, data, LockMode.DEFAULT);
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
			String fKey = new String (dbKey.getData());
			System.out.println("Key: " + fKey);
			System.out.println("Data: " + sData);
			fileWrite.writeString(filename, fKey); 
	    		fileWrite.writeString(filename, sData);
	    		fileWrite.writeString(filename, "");
		}

		// end timer and print time
		long timeEnd = System.nanoTime();
    		long time = timeEnd - timeStart;
		//time = time/1000;
    		System.out.println("TOTAL SEARCH TIME = " + time + " Nanoseconds");
		return;

	}
}
