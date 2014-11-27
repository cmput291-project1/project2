import java.nio.ByteBuffer;
import com.sleepycat.db.*;
import java.io.UnsupportedEncodingException;
public class FirstCharKeyCreator implements SecondaryKeyCreator {
	
	public FirstCharKeyCreator(){

	}
	
	public boolean createSecondaryKey(SecondaryDatabase secondary,
                                      DatabaseEntry key,
                                      DatabaseEntry data,
                                      DatabaseEntry result){
				
       
				byte firstChar = key.getData()[0];
				byte[] resultArray = new byte[1];
				resultArray[0] = firstChar;
				result.setSize(1);
        result.setData(resultArray);
				return true;
   }
}
