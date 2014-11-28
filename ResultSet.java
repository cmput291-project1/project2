import java.util.ArrayList;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;
public class ResultSet{
	private static final String ANSWERS = "answers";	
	private ArrayList<Result> results;
	private WriteToFile fw;

	public ResultSet(){
		this.results = new ArrayList<Result>();
		fw = new WriteToFile();
	}

	public void addResult(String key, String data){
		results.add(new Result(key, data, this.results.size() + 1));
	}

	public final boolean verifyKeyRange(String lowerLimit, String upperLimit){
		boolean inRange = true;
		ArrayList<String> keyResults = new ArrayList<String>();
		for(Result r : results){
			keyResults.add(r.getKey());
		}
		for(String s : keyResults){
			if( (s.compareTo(lowerLimit) < 0) || (s.compareTo(upperLimit) > 0) ){
				System.out.println("this key is out of range: " + s);
				inRange = false;
			}
		}
		return inRange;
	}

	public void duplicateKeys(){
		boolean duplicateDetected = false;
		final HashSet<Result> duplicates = new HashSet<Result>();
		final HashSet<Result> singles = new HashSet<Result>();

		for (Result result : results){
			if(!singles.add(result)){
				duplicateDetected = true;
				duplicates.add(result);
			}
		}
		
		if(duplicateDetected){
			System.out.println(duplicates.size() + " duplicates detected.");
			for(Result dup : duplicates){
				System.out.println("key:\t" + dup.getKey());
				System.out.println();
				System.out.println("Count:\t" + dup.getCount());
			}
		}
		
	}

	public int getCount(){
		return this.results.size();
	}

	public void writeResults(String file){
		if(file == null){
			file = ANSWERS;
		}
	
		for (Result result : results){
			fw.writeString(file, result.getKey());
			fw.writeString(file, result.getData());
			fw.writeString(file, "");
		}
	}

	public void clear(){
		this.results.clear();
	}
}
