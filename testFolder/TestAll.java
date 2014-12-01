import org.junit.* ;
import static org.junit.Assert.*;
import java.util.LinkedList;
import com.sleepycat.db.*;
import java.util.NoSuchElementException;

public class TestAll{
	private static final String KEY = "szysjqctdhewbmldkyzojvnpatqlhofdiavalosfwidkrppmshbcmrihpxuqdmyhvyvpuplerjsqxaseuetlsznwfclfytahorfqfgagjslzkqmlebh";
	private static final String DATA = "fgzqeklegbkhfmvybrrlaeqprkwchikudsvdgksxkoxpmeaeqchluypdvatveqreeevnnbqszeykoyddsflsgnktspcfpgggmvkricdrlzfamhisqyyuljptcsnc";
	
	private static String LOW_RNG_LOWER_LIMIT = "aaa";
	private static String LOW_RNG_UPPER_LIMIT = "aab";
	private static String[][] LOW_RNG = {
														
														{"aaaoeztvenkpyebsoqbzdfbecmysnrmlzmrfverrxkzedfchzbojattswkwjyudcrifdyvonzlyepnfexzdtwdvbchyjoqqtdjgwjfkddjeboddxymcbbthzdjjapdu",
														 "eeogrkthqvrzfhowlqplewedpqlqwrfqkunigrliufptrxrxxeuihmdzwvsgoucocgprcwztcmsjfltiykrtiedrxtszdrpqfbdjc"},
														{"aaaolttiiajrynqvdhqlhyandjcvhyurmckurmkilqvxxmwcihxslqqrasufabqfmizczofkvuiy",
														 "onnwiwvdfsaelihjprakjxvlopthmiaicowbscmibdwrsurimlnigaaavtomqtgkmlwetzfwpix"},
														{"aaaapjjknhqcfdaoxdbghixrpkjnvautkjsjkqqbxhcgbgozowkpldjqnwfkhbveggqkbgtrnwruakhumnwguck",
														 "xeihommyihejcahcpnllqzsorhkervpimgxmfpqpaggerskevsnfcmdwjvtkulrxxckhxcavizjifgewprzxnkfgwdtaqltyxftdsymhmdyagsogpvhoc"}
													};
	private static String EMPTY_STRING = "";
	private static int LINES_RETURNED = 15;

	@Test
	public void testIntegrationHash(){
		Pref.setDbType(2);
		DataBase db = DataBase.getInstance();
		db.initDataBase();
		
		

		//test the keyretrieve
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, KEY);

		//test data retrieve
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, DATA);

		//test range retrieve
		RangeSearch rs = new RangeSearch();
		rs.execute(true, LOW_RNG_LOWER_LIMIT, LOW_RNG_UPPER_LIMIT);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}
		assertTrue( LINES_RETURNED + " lines are not returned " + answers.toString(), answers.size() == LINES_RETURNED );

		assertTrue(answers.get(0).equals(KEY));
		assertTrue(answers.get(1).equals(DATA));
		assertTrue(answers.get(2).equals(EMPTY_STRING));

		assertTrue(answers.get(3).equals(KEY));
		assertTrue(answers.get(4).equals(DATA));
		assertTrue(answers.get(5).equals(EMPTY_STRING));
		
		assertTrue(answers.get(8).equals(EMPTY_STRING));
		assertTrue(answers.get(11).equals(EMPTY_STRING));
		assertTrue(answers.get(14).equals(EMPTY_STRING));
	
		assertTrue( answers.contains(LOW_RNG[0][0]) );
		assertTrue( answers.contains(LOW_RNG[0][1]) );

		assertTrue( answers.contains(LOW_RNG[1][0]) );
		assertTrue( answers.contains(LOW_RNG[1][1]) );

		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.contains(LOW_RNG[2][0]) );
		
		DataBase.getInstance().close(true);	
	}

	@Test
	public void testIntegrationTree(){
		Pref.setDbType(1);
		DataBase db = DataBase.getInstance();
		db.initDataBase();
		
		

		//test the keyretrieve
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, KEY);

		//test data retrieve
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, DATA);

		//test range retrieve
		RangeSearch rs = new RangeSearch();
		rs.execute(true, LOW_RNG_LOWER_LIMIT, LOW_RNG_UPPER_LIMIT);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}
		assertTrue( LINES_RETURNED + " lines are not returned " + answers.toString(), answers.size() == LINES_RETURNED );

		assertTrue(answers.get(0).equals(KEY));
		assertTrue(answers.get(1).equals(DATA));
		assertTrue(answers.get(2).equals(EMPTY_STRING));

		assertTrue(answers.get(3).equals(KEY));
		assertTrue(answers.get(4).equals(DATA));
		assertTrue(answers.get(5).equals(EMPTY_STRING));
		
		assertTrue(answers.get(8).equals(EMPTY_STRING));
		assertTrue(answers.get(11).equals(EMPTY_STRING));
		assertTrue(answers.get(14).equals(EMPTY_STRING));
	
		assertTrue( answers.contains(LOW_RNG[0][0]) );
		assertTrue( answers.contains(LOW_RNG[0][1]) );

		assertTrue( answers.contains(LOW_RNG[1][0]) );
		assertTrue( answers.contains(LOW_RNG[1][1]) );

		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.contains(LOW_RNG[2][0]) );
		
		DataBase.getInstance().close(true);	
	}

	@Test
	public void testIntegrationIndex(){
		Pref.setDbType(3);
		DataBase db = DataBase.getInstance();
		db.initDataBase();
		
		

		//test the keyretrieve
		KeyRetrieve kr = new KeyRetrieve();
		kr.getRecords(true, KEY);

		//test data retrieve
		DataRetrieve dr = new DataRetrieve();
		dr.getRecords(true, DATA);

		//test range retrieve
		RangeSearch rs = new RangeSearch();
		rs.execute(true, LOW_RNG_LOWER_LIMIT, LOW_RNG_UPPER_LIMIT);

		WriteToFile reader = new WriteToFile();
		LinkedList<String> answers = null;
		try{
			answers = reader.readAnswerFile();
		}catch (Exception e){
			e.printStackTrace();
		}
		assertTrue( LINES_RETURNED + " lines are not returned " + answers.toString(), answers.size() == LINES_RETURNED );

		assertTrue(answers.get(0).equals(KEY));
		assertTrue(answers.get(1).equals(DATA));
		assertTrue(answers.get(2).equals(EMPTY_STRING));

		assertTrue(answers.get(3).equals(KEY));
		assertTrue(answers.get(4).equals(DATA));
		assertTrue(answers.get(5).equals(EMPTY_STRING));
		
		assertTrue(answers.get(8).equals(EMPTY_STRING));
		assertTrue(answers.get(11).equals(EMPTY_STRING));
		assertTrue(answers.get(14).equals(EMPTY_STRING));
	
		assertTrue( answers.contains(LOW_RNG[0][0]) );
		assertTrue( answers.contains(LOW_RNG[0][1]) );

		assertTrue( answers.contains(LOW_RNG[1][0]) );
		assertTrue( answers.contains(LOW_RNG[1][1]) );

		assertTrue( answers.contains(LOW_RNG[0][1]) );
		assertTrue( answers.contains(LOW_RNG[2][0]) );
		
		DataBase.getInstance().close(true);	
	}
}

