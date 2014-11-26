import java.nio.ByteBuffer;
import com.sleepycat.db.*;
public class Explore{
	Scan scan; 
	DataBase db;
	public Explore(){
		scan = Scan.getInstance();
	}

	public void printHeader(){
		System.out.println("======================================");
		System.out.println("|\tExplore Generic File Database");
		System.out.println("======================================");
		System.out.println("|Options:");
		System.out.println("|\t1) Create and populate a database with secondary database");
		System.out.println("|\t2) Compare string generators between given and developed");
		System.out.println("|\t3) look for secondary key containing 100 primary keys");
		System.out.println("|\t4) Exit");
		System.out.println("======================================");
	}

	public void menu(){
		this.printHeader();
		int option = this.select();

		switch(option){
			case 1:
							Pref.setDbType(3);
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
	
	public void inspectSecondaryKeys(){
		System.out.println("Counting number of unique secondary keys");
		int count = 0;
		Pref.setDbType(3);
		SecondaryDatabase secdatabase = db.getInstance().getSecondaryDb();
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
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry pdbKey = new DatabaseEntry();
			DatabaseEntry sdbkey = new DatabaseEntry();
			SecondaryCursor c = secdatabase.openSecondaryCursor(null, null);
			OperationStatus oprStatus = c.getFirst(sdbkey, pdbKey, data, LockMode.DEFAULT);
			key = ByteBuffer.wrap(sdbkey.getData()).getInt();
			while(oprStatus == OperationStatus.SUCCESS){
				count++;
				while( oprStatus == OperationStatus.SUCCESS ) {
					oprStatus = c.getNextDup(sdbkey,pdbKey, data, LockMode.DEFAULT);
					if(oprStatus == OperationStatus.SUCCESS){
						count++;
					}
				}
				System.out.println("key: " + key + " has " + count + " primary keys");
				if(count <= 200){
					System.out.println("secondary key found: " + key);
				}
				count = 0;
				oprStatus = c.getNextNoDup(sdbkey,pdbKey, data, LockMode.DEFAULT);
				key = ByteBuffer.wrap(sdbkey.getData()).getInt();
			}
		}catch(DatabaseException dbe){
			System.out.println("error counting number of primary keys pointed at by secondary db keys: " + dbe.toString());
		}

	}
}
