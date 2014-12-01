import org.junit.* ;
import static org.junit.Assert.*;
import java.util.LinkedList;
import com.sleepycat.db.*;
public class TestDataRetrieval{
	private static final String KEY = "szysjqctdhewbmldkyzojvnpatqlhofdiavalosfwidkrppmshbcmrihpxuqdmyhvyvpuplerjsqxaseuetlsznwfclfytahorfqfgagjslzkqmlebh";
	private static final String DATA = "fgzqeklegbkhfmvybrrlaeqprkwchikudsvdgksxkoxpmeaeqchluypdvatveqreeevnnbqszeykoyddsflsgnktspcfpgggmvkricdrlzfamhisqyyuljptcsnc";
	private static final String NOT_KEY = "not key";
	private static final String NOT_DATA = "not data";
	private static final int LINES_RETURNED = 3;	

	@Test
	public void testHashRet(){
		Pref.setDbType(2);
		DataBase.getInstance().initDataBase();
		
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, DATA);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( LINES_RETURNED + " lines are not returned " + answers.toString(), answers.size() == LINES_RETURNED );		
		assertTrue( "key does not match\n" +
					   "test key = " + KEY + "\n" +
					   "key in answer file = " + answers.get(0), answers.get(0).equals(KEY) );
		assertTrue( "data does not match\n" +
					   "test data = " + DATA + "\n" +
					   "data in answer file = " + answers.get(1), answers.get(1).equals(DATA) );
		assertTrue( "new line is not found at specified line", answers.get(2).equals("") );
		
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry key = new DatabaseEntry();
			key.setSize(answers.get(0).length());
			key.setData(answers.get(0).getBytes());
			OperationStatus status;
			Cursor c = DataBase.getInstance().getPrimaryDb().openCursor(null, null);
			assertTrue("key is not in database", (status = c.getSearchKey(key, data, LockMode.DEFAULT) ) == OperationStatus.SUCCESS);
			assertTrue("data does not match key", new String(data.getData()).equals(DATA));
			c.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		DataBase.getInstance().close(true);
	}
	
	@Test
	public void testTreeRet(){
		Pref.setDbType(1);
		DataBase.getInstance().initDataBase();
		
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, DATA);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( LINES_RETURNED + " lines are not returned " + answers.toString(), answers.size() == LINES_RETURNED );		
		assertTrue( "key does not match\n" +
					   "test key = " + KEY + "\n" +
					   "key in answer file = " + answers.get(0), answers.get(0).equals(KEY) );
		assertTrue( "data does not match\n" +
					   "test data = " + DATA + "\n" +
					   "data in answer file = " + answers.get(1), answers.get(1).equals(DATA) );
		assertTrue( "new line is not found at specified line", answers.get(2).equals("") );

		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry key = new DatabaseEntry();
			key.setSize(answers.get(0).length());
			key.setData(answers.get(0).getBytes());
			OperationStatus status;
			Cursor c = DataBase.getInstance().getPrimaryDb().openCursor(null, null);
			assertTrue("key is not in database", (status = c.getSearchKey(key, data, LockMode.DEFAULT) ) == OperationStatus.SUCCESS);
			assertTrue("data does not match key", new String(data.getData()).equals(DATA));
			c.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		DataBase.getInstance().close(true);
	}

	@Test
	public void testIndexRet(){
		Pref.setDbType(3);
		DataBase.getInstance().initDataBase();
		
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, DATA);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( LINES_RETURNED + " lines are not returned " + answers.toString(), answers.size() == LINES_RETURNED );		
		assertTrue( "key does not match\n" +
					   "test key = " + KEY + "\n" +
					   "key in answer file = " + answers.get(0), answers.get(0).equals(KEY) );
		assertTrue( "data does not match\n" +
					   "test data = " + DATA + "\n" +
					   "data in answer file = " + answers.get(1), answers.get(1).equals(DATA) );
		assertTrue( "new line is not found at specified line", answers.get(2).equals("") );

		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry key = new DatabaseEntry();
			key.setSize(answers.get(0).length());
			key.setData(answers.get(0).getBytes());
			OperationStatus status;
			Cursor c = DataBase.getInstance().getIndexTree().openCursor(null, null);
			assertTrue("key is not in database", (status = c.getSearchKey(key, data, LockMode.DEFAULT) ) == OperationStatus.SUCCESS);
			assertTrue("data does not match key", new String(data.getData()).equals(DATA));
			c.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		DataBase.getInstance().close(true);
	}
	
	@Test
	public void testIndexRetMiss(){
		Pref.setDbType(3);
		DataBase.getInstance().initDataBase();
		
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, NOT_DATA);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertFalse("No answers should be found but one answer is", reader.answersExist());
		
		DataBase.getInstance().close(true);
	}

	@Test
	public void testTreeRetMiss(){
		Pref.setDbType(1);
		DataBase.getInstance().initDataBase();
		
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, NOT_DATA);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertFalse("No answers should be found but one answer is", reader.answersExist());
		
		DataBase.getInstance().close(true);
	}

	@Test
	public void testHashRetMiss(){
		Pref.setDbType(2);
		DataBase.getInstance().initDataBase();
		
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, NOT_DATA);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertFalse("No answers should be found but one answer is", reader.answersExist());
		
		DataBase.getInstance().close(true);
	}
}


