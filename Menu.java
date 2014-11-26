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
		int option = this.select();
		switch(option){
			case 1 : 
						if(Pref.getDbType() == 1 || Pref.getDbType() == 2){
							DataBase.getInstance();
						}else if(Pref.getDbType() == 3){
							Pref.setDbType(2);
							DataBase.getInstance();							
							IndexFile indexFile = IndexFile.getInstance();
							if(indexFile.checkDirectory()){
								indexFile.configureDataSecondary();
								indexFile.configureLengthSecondary();
							}
						}
						printHeader();
						makeSelection();
						break;
			case 2 : 
						System.out.println("Retrieving records with given key");
						KeyRetrieve kr = new KeyRetrieve();	
						kr.getRecords();
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
						break;
			case 5:
						System.out.println("Database is destroyed");	
						if(DataBase.getInstance().getPrimaryDb() != null){
							DataBase.getInstance().close();
						}
						IndexFile.getInstance().close();
						this.printHeader();
						this.makeSelection();
						break;
			case 6:
						System.out.println("Exiting data base");
						DataBase.getInstance().close();
						IndexFile.getInstance().close();
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
