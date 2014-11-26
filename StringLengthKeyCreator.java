import java.nio.ByteBuffer;
import com.sleepycat.db.*;
import java.io.UnsupportedEncodingException;
public class StringLengthKeyCreator implements SecondaryKeyCreator {
	
	public StringLengthKeyCreator(){

	}
	
	public boolean createSecondaryKey(SecondaryDatabase secondary,
                                      DatabaseEntry key,
                                      DatabaseEntry data,
                                      DatabaseEntry result){
				String str = null;	
				try{
					str = new String(key.getData(), "UTF-8");
				}catch (UnsupportedEncodingException uee){
					uee.printStackTrace();
					System.exit(-1);
				}
        byte[] stringLength = ByteBuffer.allocate(4).putInt(str.length()).array();
				result.setSize(4);
        result.setData(stringLength);
				return true;
   }
}
