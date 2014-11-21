import java.util.Scanner;

import java.util.Arrays;

public class mydbtest {
	public static void main(String[] args) {
		Scan scan = Scan.getInstance();
		int type = 0; 
		if (args.length > 0) {
    		try{
      		type = Integer.parseInt(args[0]);
    		}catch (NumberFormatException e) {
        		System.err.println("Argument" + args[0] + " must be an integer.");
        		System.exit(-1);
    		}
		}
		else{
			System.out.println("database type must be selected.");
			System.exit(-1);
		}
		Pref.setDbType(type);
		Menu menu = new Menu();
		scan.close();
	}
}
