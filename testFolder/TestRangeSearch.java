import org.junit.* ;
import static org.junit.Assert.*;
import java.util.LinkedList;
import com.sleepycat.db.*;
import java.util.NoSuchElementException;

public class TestRangeSearch{
	private static String HIGH_RNG_LOWER_LIMIT = "ab";
	private static String HIGH_RNG_UPPER_LIMIT = "ac";	
	private static int HIGH_RNG_LINES_RETURNED = 153*3; //153 results three strings put into answers per result
	private static int HIGH_RNG_RETURN = 153;	

	private static String LOW_RNG_LOWER_LIMIT = "aaa";
	private static String LOW_RNG_UPPER_LIMIT = "aab";
	private static int LOW_RNG_LINES_RETURNED = 3*3;
	private static int LOW_RNG_RETURN = 3;
	private static String[][] LOW_RNG = {
														
														{"aaaoeztvenkpyebsoqbzdfbecmysnrmlzmrfverrxkzedfchzbojattswkwjyudcrifdyvonzlyepnfexzdtwdvbchyjoqqtdjgwjfkddjeboddxymcbbthzdjjapdu",
														 "eeogrkthqvrzfhowlqplewedpqlqwrfqkunigrliufptrxrxxeuihmdzwvsgoucocgprcwztcmsjfltiykrtiedrxtszdrpqfbdjc"},
														{"aaaolttiiajrynqvdhqlhyandjcvhyurmckurmkilqvxxmwcihxslqqrasufabqfmizczofkvuiy",
														 "onnwiwvdfsaelihjprakjxvlopthmiaicowbscmibdwrsurimlnigaaavtomqtgkmlwetzfwpix"},
														{"aaaapjjknhqcfdaoxdbghixrpkjnvautkjsjkqqbxhcgbgozowkpldjqnwfkhbveggqkbgtrnwruakhumnwguck",
														 "xeihommyihejcahcpnllqzsorhkervpimgxmfpqpaggerskevsnfcmdwjvtkulrxxckhxcavizjifgewprzxnkfgwdtaqltyxftdsymhmdyagsogpvhoc"}
													};
	private static String EMPTY_STRING = "";
	
	@Test
	public void testHashSearch_High(){
		Pref.setDbType(2);
		RangeSearch rs = new RangeSearch();
		rs.execute(true, HIGH_RNG_LOWER_LIMIT, HIGH_RNG_UPPER_LIMIT);
	
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( HIGH_RNG_LINES_RETURNED + " lines are not returned " + answers.size() + " returned instead.", answers.size() == HIGH_RNG_LINES_RETURNED );
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry key = new DatabaseEntry();
			data.setReuseBuffer(false);
			key.setReuseBuffer(false);
			OperationStatus status;
			Cursor c = DataBase.getInstance().getPrimaryDb().openCursor(null, null);
			String answerKey;
			String answerData;
			
			try{
				
				while( (answerKey = answers.pop()) != null ){
					key.setSize(answerKey.length());
					key.setData(answerKey.getBytes());
					answerData = answers.pop();
					assertTrue("key is not in database", (status = c.getSearchKey(key, data, LockMode.DEFAULT) ) == OperationStatus.SUCCESS);
					assertTrue("data does not match key", new String(data.getData()).equals(answerData));
					answers.pop();
					
				}
			}catch(NoSuchElementException nse){
				
			}
			c.close();
		}catch (Exception e){
			e.printStackTrace();
		}		
	
		DataBase.getInstance().close(true);
	}
	
	@Test
	public void testTreeSearch_High(){
		Pref.setDbType(1);
		RangeSearch rs = new RangeSearch();
		rs.execute(true, HIGH_RNG_LOWER_LIMIT, HIGH_RNG_UPPER_LIMIT);
	
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( HIGH_RNG_LINES_RETURNED + " lines are not returned " + answers.size() + " returned instead.", answers.size() == HIGH_RNG_LINES_RETURNED );
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry key = new DatabaseEntry();
			data.setReuseBuffer(false);
			key.setReuseBuffer(false);
			OperationStatus status;
			Cursor c = DataBase.getInstance().getPrimaryDb().openCursor(null, null);
			String answerKey;
			String answerData;
			
			try{
				
				while( (answerKey = answers.pop()) != null ){
					key.setSize(answerKey.length());
					key.setData(answerKey.getBytes());
					answerData = answers.pop();
					assertTrue("key is not in database", (status = c.getSearchKey(key, data, LockMode.DEFAULT) ) == OperationStatus.SUCCESS);
					assertTrue("data does not match key", new String(data.getData()).equals(answerData));
					answers.pop();
					
				}
			}catch(NoSuchElementException nse){
				
			}
			c.close();
		}catch (Exception e){
			e.printStackTrace();
		}			
			
		DataBase.getInstance().close(true);
	}
	

	
	@Test
	public void testIndexSearch_High(){
		Pref.setDbType(3);
		RangeSearch rs = new RangeSearch();
		rs.execute(true, HIGH_RNG_LOWER_LIMIT, HIGH_RNG_UPPER_LIMIT);
	
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( HIGH_RNG_LINES_RETURNED + " lines are not returned " + answers.size() + " returned instead.", answers.size() == HIGH_RNG_LINES_RETURNED );	
		try{
			DatabaseEntry data = new DatabaseEntry();
			DatabaseEntry key = new DatabaseEntry();
			data.setReuseBuffer(false);
			key.setReuseBuffer(false);
			OperationStatus status;
			Cursor c = DataBase.getInstance().getIndexTree().openCursor(null, null);
			String answerKey;
			String answerData;
			
			try{
				
				while( (answerKey = answers.pop()) != null ){
					key.setSize(answerKey.length());
					key.setData(answerKey.getBytes());
					answerData = answers.pop();
					assertTrue("key is not in database", (status = c.getSearchKey(key, data, LockMode.DEFAULT) ) == OperationStatus.SUCCESS);
					assertTrue("data does not match key", new String(data.getData()).equals(answerData));
					answers.pop();
					
				}
			}catch(NoSuchElementException nse){
				
			}
			c.close();
		}catch (Exception e){
			e.printStackTrace();
		}	
		DataBase.getInstance().close(true);
	}
	
	@Test
	public void testHashSearch_Low(){
		Pref.setDbType(2);
		RangeSearch rs = new RangeSearch();
		rs.execute(true, LOW_RNG_LOWER_LIMIT, LOW_RNG_UPPER_LIMIT);
	
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}
		assertTrue( LOW_RNG_LINES_RETURNED + " lines are not returned " + answers.size() + " returned instead.", answers.size() == LOW_RNG_LINES_RETURNED );
		assertTrue( answers.contains(LOW_RNG[0][0]) );
		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.get(2).equals(EMPTY_STRING));
		
		assertTrue( answers.contains(LOW_RNG[1][0]) );
		assertTrue( answers.contains(LOW_RNG[1][1]) );
		assertTrue( answers.get(5).equals(EMPTY_STRING));
		
		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.contains(LOW_RNG[2][0]) );
		assertTrue( answers.get(8).equals(EMPTY_STRING));

		
		DataBase.getInstance().close(true);
	}	
	
	@Test
	public void testTreeSearch_Low(){
		Pref.setDbType(1);
		RangeSearch rs = new RangeSearch();
		rs.execute(true, LOW_RNG_LOWER_LIMIT, LOW_RNG_UPPER_LIMIT);
	
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( LOW_RNG_LINES_RETURNED + " lines are not returned " + answers.size() + " returned instead.", answers.size() == LOW_RNG_LINES_RETURNED );
		assertTrue( answers.contains(LOW_RNG[0][0]) );
		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.get(2).equals(EMPTY_STRING));
		
		assertTrue( answers.contains(LOW_RNG[1][0]) );
		assertTrue( answers.contains(LOW_RNG[1][1]) );
		assertTrue( answers.get(5).equals(EMPTY_STRING));
		
		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.contains(LOW_RNG[2][0]) );
		assertTrue( answers.get(8).equals(EMPTY_STRING));
		DataBase.getInstance().close(true);
	}	

	@Test
	public void testIndexSearch_Low(){
		Pref.setDbType(3);
		RangeSearch rs = new RangeSearch();
		rs.execute(true, LOW_RNG_LOWER_LIMIT, LOW_RNG_UPPER_LIMIT);
	
		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}

		assertTrue( LOW_RNG_LINES_RETURNED + " lines are not returned " + answers.size() + " returned instead.", answers.size() == LOW_RNG_LINES_RETURNED );
		assertTrue( answers.contains(LOW_RNG[0][0]) );
		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.get(2).equals(EMPTY_STRING));
		
		assertTrue( answers.contains(LOW_RNG[1][0]) );
		assertTrue( answers.contains(LOW_RNG[1][1]) );
		assertTrue( answers.get(5).equals(EMPTY_STRING));
		
		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.contains(LOW_RNG[2][0]) );
		assertTrue( answers.get(8).equals(EMPTY_STRING));
		DataBase.getInstance().close(true);
	}	
}
