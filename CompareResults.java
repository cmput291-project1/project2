
public class CompareResults{
	public ResultSet set_1;
	public ResultSet set_2;

	public CompareResults(ResultSet set_1, ResultSet set_2){
		this.set_1 = set_1;
		this.set_2 = set_2;
	}

	public final boolean eqv(){
		return false;
	}
}
