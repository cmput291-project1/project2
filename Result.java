
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

	public void toString(){
		System.out.println("count = " + this.count);
		System.out.println("key = " + this.key);
		System.out.println("data = " + this.data);
	}
}
