import java.nio.ByteBuffer;
import com.sleepycat.db.*;
import java.io.UnsupportedEncodingException;
public class FirstCharLengthKeyCreator implements SecondaryKeyCreator {
	
	public FirstCharLengthKeyCreator(){

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
				byte firstChar = key.getData()[0];
				byte[] resultArray = new byte[5];
				for(int i = 0; i < 4; i++){
						resultArray[i] = stringLength[i];
				}
				resultArray[4] = firstChar;
				result.setSize(5);
        result.setData(resultArray);
				return true;
   }
}
