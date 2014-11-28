
public class CompareResults{
	public ResultSet hashSet;
	public ResultSet treeSet;
	public ResultSet difference;

	public CompareResults(ResultSet hashSet, ResultSet treeSet){
		this.set_1 = hashSet;
		this.set_2 = treeSet;
		this.difference = new ResultSet();
	}

	public final boolean eqv(){
		for(Result r : treeSet){
			if(!hashSet.containt(r)){
				difference.add(r);
			}
		}

		if(difference.size() == 0){
			return true;
		}

		System.out.println("=================================");
		System.out.println("there are " + difference.size() + " results found in tree that are not in hash");		
		
		for(Result r : difference){
			r.toString();
		}
		return false;
	}
}
