import java.io.*;
import java.util.LinkedList;
//class for writing to file 
public class WriteToFile{
  private static File FILE = new File("answers");
    
  public void writeString(String file, String output){
		try{
			  PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			  writer.println(output);
			  writer.close();
		}catch (IOException e){
			  System.out.println(e.getMessage());
		}
	}
        
	public LinkedList<String> readAnswerFile() throws FileNotFoundException, IOException{
		LinkedList<String> ll = new LinkedList<String>();
		String line = null;
		if(FILE.exists()){
			BufferedReader br = new BufferedReader(new FileReader(FILE));
			while( (line = br.readLine()) != null ){
				ll.add(line);
			}
			br.close();
			return ll;
		}
		return null;
	}

	public static final boolean deleteAnswerFile(){
		if(FILE.exists()){
			FILE.delete();
			return true;
		}
		return true;
	}

	public static final boolean answersExist(){
		boolean success = FILE.exists();
		return success;
	}
}
