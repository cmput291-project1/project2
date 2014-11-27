import java.nio.ByteBuffer;
import com.sleepycat.db.*;
import java.lang.String;

public class Explore{
	Scan scan; 
	DataBase db;
	IndexFile indexFile;
	public Explore(){
		scan = Scan.getInstance();
		indexFile = indexFile.getInstance();
	}

	public void printHeader(){
		System.out.println("======================================");
		System.out.println("|\tExplore Generic File Database");
		System.out.println("======================================");
		System.out.println("|Options:");
		System.out.println("|\t1) Create and populate a database with secondary database");
		System.out.println("|\t2) Compare string generators between given and developed");
		System.out.println("|\t3) look for secondary key containing 100 primary keys");
		System.out.println("|\t4) examine 64a to 64b");
		System.out.println("|\t5) test btree search");
		System.out.println("|\t6) find min primary key in 64a");
		System.out.println("|\t7) find max primary key in 64b");
		System.out.println("|\t8) find 100 key interval in firstCharKey secondary databse for secondary key 'a'");
		System.out.println("|\t9) exit");
		System.out.println("======================================");
	}

	public void menu(){
		this.printHeader();
		int option = this.select();

		switch(option){
			case 1:
							Pref.setDbType(2);
							db.getInstance();
							this.menu();
							break;
			case 2:
							System.out.println("not implemented");
							this.menu();
							break;
			case 3:
							inspectSecondaryKeys();
							this.menu();
							break;
			case 4:
							examineTargetKeys();
							this.menu();
							break;
			case 5:
							testBtreeSearch();
							this.menu();
							break;
			case 6:
							getMinKeyA_64();
							this.menu();
							break;
			case 7:
							getMaxKeyB_64();
							this.menu();
							break;
			case 8:
							determineIntervalFirstCharKey();
							this.menu();
							break;
			case 9:
							IndexFile.getInstance().close();
							db.getInstance().close();
							System.exit(1);
			default:
							System.out.println("invalid selection");
							this.menu();
		}
	}

	public int select(){
		int selection = -1;
		try{
			selection = scan.getInt();
		}catch(Exception e){
			System.out.println("Menu selection failed! Check output console");
			e.printStackTrace();
		}
		return selection;		
	}
	
	public void determineIntervalFirstCharKey(){
		String firstChar = "a";
		DatabaseEntry data = new DatabaseEntry();
		DatabaseEntry pdbKey = new DatabaseEntry();
		DatabaseEntry sdbkey = new DatabaseEntry();
		//set secondary database key = 'a'
		sdbkey.setData(firstChar.getBytes());
		sdbkey.setSize(1);
		//set primary database to hash with 100k records		
		Pref.setDbType(2);
		db.getInstance();
		
		if(indexFile.getFirstCharSecondary() == null){
			indexFile.configureFirstCharSecondary();
		}
		
		SecondaryDatabase secdatabase = indexFile.getFirstCharSecondary();
		OperationStatus oprStatus;
		int count = 0;
		String lowerLimit = null;
		String upperLimit = null;
		try{
			SecondaryCursor cursor = secdatabase.openSecondaryCursor(null, null);
			oprStatus = cursor.getSearchKey(sdbkey, pdbKey, data, LockMode.DEFAULT);
			if(oprStatus == OperationStatus.SUCCESS){
				count++;
				lowerLimit = new String(pdbKey.getData());
			}
			while(oprStatus == OperationStatus.SUCCESS && count <= 150){
				oprStatus = cursor.getNextDup(sdbkey, pdbKey, data, LockMode.DEFAULT);
				if(oprStatus == OperationStatus.SUCCESS){
					count++;
				}
			}
			upperLimit = new String(pdbKey.getData());
		}catch(DatabaseException dbe){
			dbe.printStackTrace();
		}
				
		System.out.println("Lower limit = " + lowerLimit);
		System.out.println("Upper limit = " + upperLimit);
	}	


	public void getMinKeyA_64(){
		byte[] size = new byte[4];
		byte firstChar = 34;
		size = ByteBuffer.allocate(4).putInt(64).array();
		String currentKey = null;
		String minKey = null;
		byte[] secKey = new byte[5];

		for(int i = 0; i < 4; i++){
			secKey[i] = size[i];
		}

		secKey[4] = 97;

		Pref.setDbType(2);
		db.getInstance();

		DatabaseEntry data = new DatabaseEntry();
		DatabaseEntry pdbKey = new DatabaseEntry();
		DatabaseEntry sdbkey = new DatabaseEntry();
		
		if(indexFile.getLengthSecondary() == null){
			indexFile.configureLengthSecondary();
		}
		
		SecondaryDatabase secdatabase = indexFile.getLengthSecondary();
		sdbkey.setData(secKey);
		OperationStatus oprStatus;

		try{
			SecondaryCursor cursor = secdatabase.openSecondaryCursor(null, null);
			oprStatus = cursor.getSearchKey(sdbkey, pdbKey, data, LockMode.DEFAULT);
			if(oprStatus == OperationStatus.SUCCESS){
				minKey = new String(pdbKey.getData());
			}
			while(oprStatus == OperationStatus.SUCCESS){
				oprStatus = cursor.getNextDup(sdbkey, pdbKey, data, LockMode.DEFAULT);
				currentKey = new String(pdbKey.getData());
				if(currentKey.compareTo(minKey) < 0){
					minKey = currentKey;
				}
			}
		}catch(DatabaseException dbe){
			dbe.printStackTrace();
		}
		System.out.println("The min primary key in 64a secondary index is: " + minKey);
	}

	public void getMaxKeyB_64(){
		byte[] size = new byte[4];
		byte firstChar = 34;
		size = ByteBuffer.allocate(4).putInt(64).array();
		String currentKey = null;
		String maxKey = null;
		byte[] secKey = new byte[5];

		for(int i = 0; i < 4; i++){
			secKey[i] = size[i];
		}

		secKey[4] = 98;

		Pref.setDbType(2);
		db.getInstance();

		DatabaseEntry data = new DatabaseEntry();
		DatabaseEntry pdbKey = new DatabaseEntry();
		DatabaseEntry sdbkey = new DatabaseEntry();
		
		if(indexFile.getLengthSecondary() == null){
			indexFile.configureLengthSecondary();
		}
		
		SecondaryDatabase secdatabase = indexFile.getLengthSecondary();
		sdbkey.setData(secKey);
		OperationStatus oprStatus;

		try{
			SecondaryCursor cursor = secdatabase.openSecondaryCursor(null, null);
			oprStatus = cursor.getSearchKey(sdbkey, pdbKey, data, LockMode.DEFAULT);
			if(oprStatus == OperationStatus.SUCCESS){
				maxKey = new String(pdbKey.getData());
			}
			while(oprStatus == OperationStatus.SUCCESS){
				oprStatus = cursor.getNextDup(sdbkey, pdbKey, data, LockMode.DEFAULT);
				currentKey = new String(pdbKey.getData());
				if(currentKey.compareTo(maxKey) > 0){
					maxKey = currentKey;
				}
			}
		}catch(DatabaseException dbe){
			dbe.printStackTrace();
		}
		System.out.println("The max primary key in 64b secondary index is: " + maxKey);
	}

	public void examineTargetKeys(){
		byte[] size = new byte[4];
		byte firstChar = 34;
		byte[] secKey_1 = new byte[5];		
		
		
		size = ByteBuffer.allocate(4).putInt(64).array();
		for(int i = 0; i < 4; i++){
			secKey_1[i] = size[i];
			
		}

		secKey_1[4] = 97;


		Pref.setDbType(2);
		db.getInstance();
		DatabaseEntry data = new DatabaseEntry();
		DatabaseEntry pdbKey1 = new DatabaseEntry();
		DatabaseEntry sdbkey1 = new DatabaseEntry();
		if(indexFile.getLengthSecondary() == null){
			indexFile.configureLengthSecondary();
		}
		SecondaryDatabase secdatabase = indexFile.getLengthSecondary();
		
		OperationStatus oprStatus;
		
		sdbkey1.setData(secKey_1);

		
		int count = 0;
		
		try{
			SecondaryCursor c_1 = secdatabase.openSecondaryCursor(null, null);
			c_1.getSearchKey(sdbkey1, pdbKey1, data, LockMode.DEFAULT);
			String currentKey = new String(pdbKey1.getData());
			count++;
			while(!currentKey.equals("bzxbsxybbxktwmrfrebazoadgtwjuhhoytafflyinicuksmbrhkohcinjzjklluo")){
				c_1.getNext(new DatabaseEntry(), pdbKey1, data, LockMode.DEFAULT);
				currentKey = new String(pdbKey1.getData());
				count++;
				if(currentKey.equals("bzxbsxybbxktwmrfrebazoadgtwjuhhoytafflyinicuksmbrhkohcinjzjklluo")){
					System.out.println(currentKey + " is counted");
				}
			}
			
			System.out.println("there are " + count + " records on this interval");
		}catch(DatabaseException dbe){
			System.out.println("error inspecting target secondary db keys: " + dbe.toString());
		}
	}

	public void inspectSecondaryKeys(){
		System.out.println("Counting number of unique secondary keys");
		int count = 0;
		Pref.setDbType(2);
		db.getInstance();
		indexFile.configureLengthSecondary();
		SecondaryDatabase secdatabase = indexFile.getLengthSecondary();
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry pdbKey = new DatabaseEntry();
			DatabaseEntry sdbkey = new DatabaseEntry();
			SecondaryCursor c = secdatabase.openSecondaryCursor(null, null);
			OperationStatus oprStatus = c.getFirst(sdbkey, pdbKey, data, LockMode.DEFAULT);
			//sloppy can be cleaned up
			if(oprStatus == OperationStatus.SUCCESS){
				count++;
			}
			while( oprStatus == OperationStatus.SUCCESS ) {
				oprStatus = c.getNextNoDup(sdbkey,pdbKey, data, LockMode.DEFAULT);
				if(oprStatus == OperationStatus.SUCCESS){
					count++;
				}
			}
		}catch(DatabaseException dbe){
			System.out.println("error scanning unique secondary db keys: " + dbe.toString());
		}
		System.out.println("There are " + count + " unique secondary keys.");
		System.out.println("===============================================");
		System.out.println("printing secondary keys with 100 - 200 primary keys");
		count = 0;
		int key = 0;
		byte firstChar = 32;
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry pdbKey = new DatabaseEntry();
			DatabaseEntry sdbkey = new DatabaseEntry();
			SecondaryCursor c = secdatabase.openSecondaryCursor(null, null);
			OperationStatus oprStatus = c.getFirst(sdbkey, pdbKey, data, LockMode.DEFAULT);
			key = ByteBuffer.wrap(sdbkey.getData()).getInt();
			firstChar = sdbkey.getData()[4];
			while(oprStatus == OperationStatus.SUCCESS){
				count++;
				while( oprStatus == OperationStatus.SUCCESS ) {
					oprStatus = c.getNextDup(sdbkey,pdbKey, data, LockMode.DEFAULT);
					if(oprStatus == OperationStatus.SUCCESS){
						count++;
					}
				}
				String keyString = Integer.toString(key) + (char)firstChar;
				//remove this to print size of all secondary keys
				if(keyString.equals("64a") || keyString.equals("64b") ){
					System.out.println("key: " + key + (char)firstChar + " has " + count + " duplicate keys");
				}
				count = 0;
				oprStatus = c.getNextNoDup(sdbkey,pdbKey, data, LockMode.DEFAULT);
				count++;
				key = ByteBuffer.wrap(sdbkey.getData()).getInt();
				firstChar = sdbkey.getData()[4];
			}
		}catch(DatabaseException dbe){
			System.out.println("error counting number of primary keys pointed at by secondary db keys: " + dbe.toString());
		}

	}

	public void testBtreeSearch(){
		Pref.setDbType(1);
		db.getInstance();
		OperationStatus oprStatus;
		int count = 0;
		try{
			Cursor c_1 = db.getInstance().getPrimaryDb().openCursor(null, null);
			String key1 = "aatewknnlyjqxuadonparefxljasaddccsviqfkqzmpxcrhdegktesxvcfcxlkjx";
			String key2 = "cagoxktnhjzemzyhrkcuicrxvogrrdzwbsyoqgqzeitzewbvdrdsdgafvfifocuz";	
			String previousKey = new String();
			String currentKey = new String();
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry pdbKey1 = new DatabaseEntry();
			//pdbKey1.setData(key1.getBytes());
		
			oprStatus = c_1.getFirst(pdbKey1, data, LockMode.DEFAULT);
			if( oprStatus == OperationStatus.SUCCESS ){
				count++;
				currentKey = new String(pdbKey1.getData());
				System.out.println("start key = " + currentKey);
				System.out.println("length = " + currentKey.length());
			}
				previousKey = currentKey;
			while(count <= 100){
				oprStatus = c_1.getNext(pdbKey1, data, LockMode.DEFAULT);
				if( oprStatus == OperationStatus.SUCCESS ){
				count++;
				currentKey = new String(pdbKey1.getData());
				}
				if(previousKey.compareTo(currentKey) >= 0){
					System.out.println("out of order");
					System.out.println("Current key = " + currentKey);
					System.out.println("Previous key = " + previousKey);
					break;
				}
				previousKey = currentKey;
			}
			System.out.println("end key = " + currentKey);
			System.out.println("there are " + count + " records on this interval");
		}catch(DatabaseException dbe){
			System.out.println("error inspecting target secondary db keys: " + dbe.toString());
		}
	}
}
