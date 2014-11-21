
public class Pref{
	private int dbType;
	private static Pref pref = null;
	protected Pref(){
		// empty constuctor
	}

	public static Pref getInstance(){
		if(pref == null){
			pref = new Pref();
		}
		return pref;
	}

	public void setDbType(int type){
		dbType = type;
	}

	public int getDbType(){
		return dbType;
	}
}
