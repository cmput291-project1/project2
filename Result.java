
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
		String str = new String();
		String newLine = new String("\n");
		str.append("count = " + this.count)
		str.append(newLine);
		str.append("key = " + this.key);
		str.append(newLine);
		str.append("data = " + this.data);
		str.append(newLine);
		return str;
	}
}
