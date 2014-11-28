
public class Result{
	private String key;
	private String data;
	private int count;

	public Result(String key, String data, int count){
		this.count =  count;
		this.data = new String(data);
		this.key = new String(key);
	}

	public int getCount(){
		return this.count;
	}

	public String getData(){
		return this.data;
	}

	public String getKey(){
		return this.key;
	}

	public String toString(){
		String newLine = new String("\n");
		String str = new String();
		str.concat("count = " + this.count);
		str.concat(newLine);
		str.concat("key = " + this.key);
		str.concat(newLine);
		str.concat("data = " + this.data);
		str.concat(newLine);
		return str;
	}
}
