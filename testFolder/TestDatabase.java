import org.junit.* ;
import static org.junit.Assert.*;
import com.sleepycat.db.*;
import java.io.*;

public class TestDatabase{

	// probability that two key data pairs that are randomly generated is very small
	// (1/64) * (26)^(-length) * (1/64) * (26)^(-length)
	// when combined with other pairs this gets so infinitesimal
	@Test
	public void testHashContainsSamplePairs(){
		Database sampleDb = null;
		DataBase db = DataBase.getInstance();
		Pref.setDbType(2);
		db.initDataBase();
		String[][] samplePairs =	{
											/*key*/	{"oohiqwurgzsllzvhgigpxqwzbenyyjxuczmewrecjmxuvgjlzrnfxlmgzoilphatfquyyaadzvnztflneudhykt",
											/*data*/	"hnrkmhfcwwzplsykkcaxqdtsnlenyanztgszjgnzvdgpidenkicynfdsgeyvgbnbaxdxlodtwexdlp"},

														{"bmphuxdzdptmwfjkmswpmglqaugolcfkigzilpezvauvsmzgfaouwkocudbtikcflwtefzrfogttksokdbqmxlypejbeav",
														"lxqohkaipnfqolcvcpuasjhkuyogqyilbtiygwpwpnvrsfvgxgqmoknslwahseqgtukfojzfrvpennfvcsrasydam"},

														{"ljgqqmsfxichjlmffquellmckrpxejrixcacoidudhecpekusbfailponelrjsladcwypghpolmwyefizrwfsujfinwqqblqrd",
														"ypgncqqsbfdsqkdsjkwedsylaemsdjixeexjhbizztusifwhagtnfpxmmdcowygomiybpkynkjaivdevloyqdszlybgqhffp"}
											};
										
		
		Database testHash = db.getPrimaryDb();	
		assertTrue("Hash database is null", testHash != null);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		key.setReuseBuffer(false);
		data.setReuseBuffer(false);
		String dataString;
		OperationStatus result;

		try{
			Cursor c = testHash.openCursor(null, null);
			for(int i = 0; i < samplePairs.length; i++){
				key.setSize(samplePairs[i][0].length());
				key.setData(samplePairs[i][0].getBytes());
				assertTrue( "key " + samplePairs[i][0] + " is not in database.", 
							(result = c.getSearchKey(key, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS);
				dataString = new String(data.getData());
				assertTrue("key/data pair " + i + " does not match key/data pair in database.", dataString.equals(samplePairs[i][1]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		db.close(true);
	}

	@Test
	public void primaryIndexContains_100000_records(){
		Database sampleDb = null;
		DataBase db = DataBase.getInstance();
		Pref.setDbType(2);
		db.initDataBase();

		int count = 0;	
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		OperationStatus result;
		Database testHash = db.getPrimaryDb();
		try{
			Cursor c = testHash.openCursor(null, null);
			while( (result = c.getNext(key, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS){
				count++;
			}
			assertTrue("there are only " + count + " records in database.", count == 100000); 
		}catch(Exception e){
			e.printStackTrace();
		}
		db.close(true);
	}
	

	@Test
	public void testIndexContainsSamplePairs(){
		Database sampleDb = null;
		DataBase db = DataBase.getInstance();
		Pref.setDbType(3);
		db.initDataBase();
		String[][] samplePairs =	{
											/*key*/	{"oohiqwurgzsllzvhgigpxqwzbenyyjxuczmewrecjmxuvgjlzrnfxlmgzoilphatfquyyaadzvnztflneudhykt",
											/*data*/	"hnrkmhfcwwzplsykkcaxqdtsnlenyanztgszjgnzvdgpidenkicynfdsgeyvgbnbaxdxlodtwexdlp"},

														{"bmphuxdzdptmwfjkmswpmglqaugolcfkigzilpezvauvsmzgfaouwkocudbtikcflwtefzrfogttksokdbqmxlypejbeav",
														"lxqohkaipnfqolcvcpuasjhkuyogqyilbtiygwpwpnvrsfvgxgqmoknslwahseqgtukfojzfrvpennfvcsrasydam"},

														{"ljgqqmsfxichjlmffquellmckrpxejrixcacoidudhecpekusbfailponelrjsladcwypghpolmwyefizrwfsujfinwqqblqrd",
														"ypgncqqsbfdsqkdsjkwedsylaemsdjixeexjhbizztusifwhagtnfpxmmdcowygomiybpkynkjaivdevloyqdszlybgqhffp"}
											};
										
		
		Database testIndex = db.getIndexTree();	
		assertTrue("Index database is null", testIndex != null);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		key.setReuseBuffer(false);
		data.setReuseBuffer(false);
		String dataString;
		OperationStatus result;

		try{
			Cursor c = testIndex.openCursor(null, null);
			for(int i = 0; i < samplePairs.length; i++){
				key.setSize(samplePairs[i][0].length());
				key.setData(samplePairs[i][0].getBytes());
				assertTrue( "key " + samplePairs[i][0] + " is not in database.", 
							(result = c.getSearchKey(key, data, LockMode.DEFAULT)) == OperationStatus.SUCCESS);
				dataString = new String(data.getData());
				assertTrue("key/data pair " + i + " does not match key/data pair in database.", dataString.equals(samplePairs[i][1]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		db.close(true);
	}

	@Test
	public void testClose(){
		DataBase db = DataBase.getInstance();
		db.close(true);
		Pref.setDbType(1);
		db.initDataBase();
		db.close(true);
		assertTrue("primary DB is not null upon close", db.getPrimaryDb() == null);
		assertFalse("database directory exists after close", new File(DataBase.DATABASE_DIR).exists());
		assertFalse("answers file exists upon close", new File(DataBase.DATABASE_DIR).exists());
		Pref.setDbType(3);
		db.initDataBase();
		db.close(true);
		assertTrue("Index Tree DB is not null upon close", db.getIndexTree() == null);
		assertFalse("database directory exists after close", new File(DataBase.DATABASE_DIR).exists());
		assertFalse("answers file exists upon close", new File(DataBase.DATABASE_DIR).exists());
	}
}
