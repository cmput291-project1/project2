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
		return new String(Integer.toString(count));
	}

	public void examine(){
		countKeysLonger_64();
		countKeysStartingGreater_b();
	}

	public void countKeysLonger_64(){
		int count = 0;
		for(String key : keyResults){
			if(key.length() > 64){
				count++;
			}
		}
		System.out.println("there are " + count + " keys with length greater than 64.");
	}

	public void countKeysStartingGreater_b(){
		int count = 0;
		int c = ' ';
		for(String key : keyResults){
			c = key.charAt(0);
			if(c > 98){
				count++;
			}
		}
		System.out.println("there are " + count + " keys with that start with a char greater than 'b'");
	}
}
