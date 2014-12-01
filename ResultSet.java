import java.util.ArrayList;
import java.lang.String;
import java.util.HashSet;

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
	
	public ArrayList<Result> getResults(){
		return this.results;
	}

	public int getCount(){
		return this.results.size();
	}

	public void writeResults(){
		for (Result result : results){
			fw.writeString(ANSWERS, result.getKey());
			fw.writeString(ANSWERS, result.getData());
			fw.writeString(ANSWERS, "");
		}
		
	}

	public void clear(){
		this.results.clear();
		this.results = new ArrayList<Result>();
	}
}
