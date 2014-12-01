import org.junit.* ;
import static org.junit.Assert.*;
import java.util.LinkedList;
import com.sleepycat.db.*;

public class TestKeyRetrieval{
	private static final String KEY = "szysjqctdhewbmldkyzojvnpatqlhofdiavalosfwidkrppmshbcmrihpxuqdmyhvyvpuplerjsqxaseuetlsznwfclfytahorfqfgagjslzkqmlebh";
	private static final String DATA = "fgzqeklegbkhfmvybrrlaeqprkwchikudsvdgksxkoxpmeaeqchluypdvatveqreeevnnbqszeykoyddsflsgnktspcfpgggmvkricdrlzfamhisqyyuljptcsnc";
	private static final String NOT_KEY = "not key";
	private static final String NOT_DATA = "not data";
	private static final int LINES_RETURNED = 3;	
	
	@Test
	public void testIndexMiss(){
		Pref.setDbType(3);
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, NOT_KEY);
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}
		assertFalse("Answer file exists even though no keys found", reader.answersExist());
		DataBase.getInstance().close(true);
	}

	@Test
	public void testIndexRetrival(){
		Pref.setDbType(3);
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, KEY);
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
	public void testHashRetrival(){
		Pref.setDbType(2);
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, KEY);
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
	public void testTreeRetrival(){
		Pref.setDbType(1);
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, KEY);
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
	public void testHashMiss(){
		Pref.setDbType(1);
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, NOT_KEY);
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}
		assertFalse("Answer file exists even though no keys found", reader.answersExist());
		DataBase.getInstance().close(true);
	}

	@Test
	public void testTreeMiss(){
		Pref.setDbType(2);
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, NOT_KEY);
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}
		assertFalse("Answer file exists even though no keys found", reader.answersExist());
		DataBase.getInstance().close(true);
	}

	
}
