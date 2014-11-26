import java.util.Random;
// singleton
public class StringGenerator{
	private Random random;
	private int RANGE = 64; // all strings must be of size 64-127	
	private static StringGenerator gen = null;
	
	protected StringGenerator(){
		random = new Random(1000000);
	}

	public static StringGenerator(){
		if(gen == null){
			gen = new StringGenerator();
		}
		return gen;
	}
	
	public final String generateString(){
		int range = 64 + random.nextInt( 64 );
		String s = "";
		for ( int j = 0; j < range; j++ ) 
			s+=(new Character((char)(97+random.nextInt(26)))).toString();
		return s;	
	}
}
