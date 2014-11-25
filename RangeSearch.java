import com.sleepycat.db.*;


public class RangeSearch{
	DatabaseEntry entry1;
	DatabaseEntry entry2;
	double recordNumber1;
	double recordNumber2;
	DataBase db;	
	

	public RangeSearch(){
		db = DataBase.getInstance();
		
	}
}
