import java.util.ArrayList;
import java.lang.String;

public class ResultSet{
	private ArrayList<String> keyResults;
	private ArrayList<String> dataResults;
	private int count;
	
	public ResultSet(){
		keyResults = new ArrayList<String>();
		dataResults = new ArrayList<String>();
		count = 0;
	}

	public void addResult(String key, String data){
		keyResults.add(key);
		dataResults.add(key);
		count++;
	}

	public String getCount(){
		return new String(Integer.toString(count));
	}

	public final boolean verifyKeyRange(String lowerLimit, String upperLimit){
		boolean inRange = true;
		for(String s : keyResults){
			if( (s.compareTo(lowerLimit) <= 0) || (s.compareTo(upperLimit) >= 0) ){
				System.out.println("this key is out of range: " + s);
				inRange = false;
			}
		}
		return inRange;
	}
}
