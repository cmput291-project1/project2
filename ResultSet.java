import java.util.ArrayList;

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
		return new String(count);
	}
}
