import java.util.ArrayList;
import java.lang.String;

public class ResultSet{
	private static final String ANSWERS = "answers";	
	private ArrayList<String> keyResults;
	private ArrayList<String> dataResults;
	private int count;
	private WriteToFile fw;

	public ResultSet(){
		keyResults = new ArrayList<String>();
		dataResults = new ArrayList<String>();
		fw = new WriteToFile();
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
			if( (s.compareTo(lowerLimit) < 0) || (s.compareTo(upperLimit) > 0) ){
				System.out.println("this key is out of range: " + s);
				inRange = false;
			}
		}
		return inRange;
	}

	public final boolean findDuplicateKeys(){
		boolean duplicateDetected = false;
		String[] keys =  keyResults.toArray(new String[keyResults.size()]);
		String str1 = null;	
		String str2 = null;
		int j;

		for(int i = 0; i < keyResults.size() - 1; i++){
			str1 = keys[i];
			j = i + 1;
			while(j < keyResults.size()){
				str2 = keys[j];
				if(str1.compareTo(str2) == 0){
					duplicateDetected = true;
					System.out.println("this key is duplicate: " + str1);
				}
				j++;
			}
		}
		
		//testing last string
		str1 = keys[keyResults.size() - 1];
		str2 = keys[keyResults.size() - 2];
	
		if(str1.compareTo(str2) == 0){
			System.out.println("last key is duplicate: " + str1);
		}
		
		return duplicateDetected;
	}

	public void writeResults(String file){
		//parallel arrays gross....
		if(keyResults.size() != dataResults.size()){
			throw new RuntimeException("unequal keys and data results in result set");
		}
	
		String[] keys =  keyResults.toArray(new String[keyResults.size()]);
		String[] datas =  dataResults.toArray(new String[dataResults.size()]);
		if(file == null){
			file = ANSWERS;
		}
		for(int i = 0; i < keys.length; i++){
			fw.writeString(file, keys[i]);
			fw.writeString(file, datas[i]);
			fw.writeString(file, "");
		}
		
	}
}
