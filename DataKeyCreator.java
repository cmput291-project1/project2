import java.nio.ByteBuffer;
import com.sleepycat.db.*;
import java.io.UnsupportedEncodingException;
public class DataKeyCreator implements SecondaryKeyCreator {
	
	public DataKeyCreator(){

	}
	
	public boolean createSecondaryKey(SecondaryDatabase secondary,
                                      DatabaseEntry key,
                                      DatabaseEntry data,
                                      DatabaseEntry result){
				String dataStr = null;	
				try{
					str = new String(data.getData(), "UTF-8");
				}catch (UnsupportedEncodingException uee){
					uee.printStackTrace();
					System.exit(-1);
				}
 				int strlen = str.length();
				byte[] byteArray = new byte[strlen];
				byteArray = str.getBytes();
        result.setData(byteArray);
				return true;
   }
}
