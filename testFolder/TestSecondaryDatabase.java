import org.junit.* ;
import static org.junit.Assert.*;
import com.sleepycat.db.*;

public class TestSecondaryDatabase{
	

	
	@Test
	public void testContainsAllData(){
		Pref.setDbType(3);
		DataBase db = DataBase.getInstance();
		db.initDataBase();
		Database primaryDb = db.getIndexTree();
		assertTrue("primary database is null",primaryDb != null);
		SecondaryDatabase secondaryDb = db.getIndexSecondary();
		assertTrue("secondary database is null",secondaryDb != null);
		DatabaseEntry secKey = new DatabaseEntry();
		DatabaseEntry pKey = new DatabaseEntry();
		DatabaseEntry secData = new DatabaseEntry();
		
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		secKey.setReuseBuffer(false);
		pKey.setReuseBuffer(false);
		secData.setReuseBuffer(false);
		key.setReuseBuffer(false);
		data.setReuseBuffer(false);

		String dataString;

		OperationStatus primaryStatus;
		OperationStatus secStatus;
	
		
		try{
			Cursor primaryCursor = primaryDb.openCursor(null, null);
			SecondaryCursor secondCursor = secondaryDb.openSecondaryCursor(null, null);
			
			while( (primaryStatus = primaryCursor.getNext(key, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS ){
				dataString = new String(data.getData());
				secKey.setSize(dataString.length());
				secKey.setData(dataString.getBytes());
				
				assertTrue	("Seconday key " + dataString + " is not found.",
								(secStatus = secondCursor.getSearchKey(secKey, pKey, secData, LockMode.DEFAULT)) == OperationStatus.SUCCESS);
			}
		}catch (DatabaseException dbe){
			dbe.printStackTrace();
		}
		db.close(true);
	}
	
}
