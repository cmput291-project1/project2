import java.io.*;

//class for writing to file 
public class WriteToFile{
    
    
  public void writeString(String file, String output){
		try{
			  PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			  writer.println(output);
			  writer.close();
		}catch (IOException e){
			  System.out.println(e.getMessage());
		}
	}
        

}
