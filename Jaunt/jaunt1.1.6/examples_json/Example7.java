import com.jaunt.*;
import java.io.*;

public class Example7{

  public static void main(String[] args) throws IOException{
    try{ 
      UserAgent userAgent = new UserAgent(); 
      userAgent.openJSON(new File("example7.json"));
 
      //find every node named "father" or "mother"
      JNode searchResults = userAgent.json.findEvery("father|mother");
      System.out.println("Results for every father or mother:\n" + searchResults);
  
      //find every non-nested node named "father" or "mother"
      searchResults = userAgent.json.findEach("father|mother"); 
      System.out.println("Results for each father or mother:\n" + searchResults);
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}