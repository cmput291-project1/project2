import com.sleepycat.db.DatabaseException;
public class Menu{		
	Scan scan;
	public Menu(){
		this.printHeader();
		scan = Scan.getInstance();
		this.makeSelection();
	}

	public void printHeader(){
		System.out.println("======================================");
		System.out.println("|\tGeneric File Database");
		System.out.println("======================================");
		System.out.println("|Options:");
		System.out.println("|\t1) Create and populate a database");
		System.out.println("|\t2) Retrieve records with a given key");
		System.out.println("|\t3) Retrieve records with a given data");
		System.out.println("|\t4) Retrieve records with a given range of key values");
		System.out.println("|\t5) Destroy the database");
		System.out.println("|\t6) Quit");
		System.out.println("======================================");
	}

	public void makeSelection(){
		DataBase db;
		int option = this.select();
		switch(option){
			case 1 : 
						if(Pref.getDbType() == 1 || Pref.getDbType() == 2){
							db = DataBase.getInstance();
							db.initDataBase();
						}else if(Pref.getDbType() == 3){
							IndexFile indexFile = IndexFile.getInstance();
							indexFile.initIndexFile();
						}
						printHeader();
						makeSelection();
						break;
			case 2 : 
						System.out.println("Option 2 executed");
						KeyRetrieve kr = new KeyRetrieve();	
						if(Pref.getDbType() == 3){
							try{
								kr.getIndexFileRecord();
							}catch(DatabaseException dbe){
								dbe.printStackTrace();
							}
						}
						else{
							kr.getRecords();
						}
						printHeader();
						makeSelection();
						break;
			case 3: 
						System.out.println("Option 3 executed");				                        	   
						DataRetrieve dr = new DataRetrieve();	
						dr.getRecords();
						printHeader();
						makeSelection();
						break;
			case 4: 
						System.out.println("Option 4 executed");	
						RangeSearch rs = new RangeSearch();
						rs.execute();
						printHeader();
						makeSelection();
						break;
			case 5:
						System.out.println("Option 5 executed");	
						IndexFile.getInstance().close();
						DataBase.getInstance().close();
						this.printHeader();
						this.makeSelection();
						break;
			case 6:
						//TODO ensure that DataBase.getInstance() does not open create database when unwanted
						System.out.println("Exiting data base");
						IndexFile.getInstance().close();
						DataBase.getInstance().close();
						System.exit(-1);	
						break;
			default: 
						System.out.println("invalid option selected");
						this.makeSelection();
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
}
