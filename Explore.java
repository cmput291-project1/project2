import java.nio.ByteBuffer;
import com.sleepycat.db.*;

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
		System.out.println("|\t4) examine 64a to 64c");
		System.out.println("|\t5) test btree search");
		System.out.println("|\t6) exit");
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
	
	public void examineTargetKeys(){
		byte[] size = new byte[4];
		byte firstChar = 34;
		byte[] secKey_1 = new byte[5];		
		byte[] secKey_2 = new byte[5];
		
		size = ByteBuffer.allocate(4).putInt(64).array();
		for(int i = 0; i < 4; i++){
			secKey_1[i] = size[i];
			secKey_2[i] = size[i];
		}

		secKey_1[4] = 97;
		secKey_2[4] = 99;

		Pref.setDbType(2);
		db.getInstance();
		DatabaseEntry data = new DatabaseEntry();
		DatabaseEntry pdbKey1 = new DatabaseEntry();
		DatabaseEntry sdbkey1 = new DatabaseEntry();
		DatabaseEntry pdbKey2 = new DatabaseEntry();
		DatabaseEntry sdbkey2 = new DatabaseEntry();
		if(indexFile.getLengthSecondary() == null){
			indexFile.configureLengthSecondary();
		}
		SecondaryDatabase secdatabase = indexFile.getLengthSecondary();
		
		OperationStatus oprStatus;
		
		sdbkey1.setData(secKey_1);
		sdbkey2.setData(secKey_2);
		try{
			SecondaryCursor c_1 = secdatabase.openSecondaryCursor(null, null);
			SecondaryCursor c_2 = secdatabase.openSecondaryCursor(null, null);
			oprStatus = c_1.getSearchKey(sdbkey1, pdbKey1, data, LockMode.DEFAULT);
			String primaryKey1 = new String(pdbKey1.getData());
			if( oprStatus == OperationStatus.SUCCESS ){
				System.out.println("primary key for cursor one = " + primaryKey1);
			}
			oprStatus = c_1.getSearchKey(sdbkey2, pdbKey2, data, LockMode.DEFAULT);
			String primaryKey2 = new String(pdbKey2.getData());
			if( oprStatus == OperationStatus.SUCCESS ){
				System.out.println("primary key for cursor two = " + primaryKey2);
			}
		}catch(DatabaseException dbe){
			System.out.println("error inspecting target secondary db keys: " + dbe.toString());
		}
		int count = 0;
		
		try{
			SecondaryCursor c_1 = secdatabase.openSecondaryCursor(null, null);
			c_1.getSearchKey(sdbkey1, pdbKey1, data, LockMode.DEFAULT);
			String currentKey = new String(pdbKey1.getData());
			int itterations = 2;
			int i = 1;
			count++;
			
			while(i <= itterations){
				oprStatus = c_1.getNextDup(sdbkey1, pdbKey1, data, LockMode.DEFAULT);
				if(oprStatus == OperationStatus.SUCCESS){ 
					count++;
				}else{
					oprStatus = c_1.getNextNoDup(sdbkey1, pdbKey1, data, LockMode.DEFAULT);
					String primaryKey1 = new String(pdbKey1.getData());
					System.out.println("primary key  = " + primaryKey1);
					if(oprStatus == OperationStatus.SUCCESS){
						count++;
					}
					i++;
					if(i == 3){
						primaryKey1 = new String(pdbKey1.getData());
						System.out.println("primary key  = " + primaryKey1);
					}
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
				System.out.println("key: " + key + (char)firstChar + " has " + count + " primary keys");
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
			String currentKey = new String();
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry pdbKey1 = new DatabaseEntry();
			pdbKey1.setData(key1.getBytes());
		
			oprStatus = c_1.getSearchKey(pdbKey1, data, LockMode.DEFAULT);
			if( oprStatus == OperationStatus.SUCCESS ){
				count++;
				currentKey = new String(pdbKey1.getData());
				System.out.println("start key = " + currentKey);
				System.out.println("length = " + currentKey.length());
			}
			while(!currentKey.equals("cagoxktnhjzemzyhrkcuicrxvogrrdzwbsyoqgqzeitzewbvdrdsdgafvfifocuz")){
				oprStatus = c_1.getNext(pdbKey1, data, LockMode.DEFAULT);
				if( oprStatus == OperationStatus.SUCCESS ){
				count++;
				currentKey = new String(pdbKey1.getData());
				}
				if (count <= 10)
					System.out.println(currentKey);
				if (currentKey.length() != 64){
					System.out.println("length is not 64");
				}
				if (count == 100)
					break;			
			}
			System.out.println("end key = " + currentKey);
			System.out.println("there are " + count + " records on this interval");
		}catch(DatabaseException dbe){
			System.out.println("error inspecting target secondary db keys: " + dbe.toString());
		}
	}
}
