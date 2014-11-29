import java.util.ArrayList;
import java.lang.String;
import java.util.HashSet;
public class ResultSet{
	private static final String ANSWERS = "answers";	
	private ArrayList<Result> results;
	private WriteToFile fw;
	int count;

	public ResultSet(){
		this.results = new ArrayList<Result>();
		fw = new WriteToFile();
		this.count = 0;
	}

	public void addResult(String key, String data){
		count++;
		results.add(new Result(key, data, count));
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
		final Set<Result> duplicates = new HashSet<Result>();
		final Set<Result> singles = new HashSet<Result>();

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
		this.count = 0;
		this.results.clear();
	}
}
