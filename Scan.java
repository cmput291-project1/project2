import java.util.Scanner;

public class Scan{
	private static Scan scan = null;
	private static Scanner scanner = null;

	protected Scan(){
		try{		
			this.scanner = new Scanner(System.in);
		}catch(Exception e){
			System.out.println("Unable to instantiate Scanner! Check console output");
			e.printStackTrace();
		}	
	}

	public static Scan getInstance(){
		if(scan == null){
			scan = new Scan();
		}
		return scan;
	}

	public void close(){
		try{
			this.scanner.close();
		}catch(Exception e){
			System.out.println("Unable to close Scanner! Check console output");
			
		}
	}

	public static Scanner getScanner(){
		if(scanner == null){
			throw new RuntimeException("attempting to retrieve null scanner! Make sure to call Scan.getInstance() prior to retrieving scanner!");
		}

		return scanner;
	}

	public int getInt(){
		int number = -1;
		try{
			number = scanner.nextInt();
		}catch(Exception e){
			// some type of i/o or class error
			System.out.println("Error reading number input!");
			
		}
		scanner.nextLine();
		return number;
	}

	public String getString(){
		String string = null;
		try{
			string = scanner.nextLine();
		}catch(Exception e){
			System.out.println("Error reading text input!");
		}
		return string;
	}
}
