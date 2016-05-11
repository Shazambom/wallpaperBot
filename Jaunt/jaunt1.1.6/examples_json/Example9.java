import com.jaunt.*;
import java.io.*;

public class Example9{

  public static void main(String[] args) throws IOException{
    try{ 
      UserAgent userAgent = new UserAgent(); 
      userAgent.openJSON(new File("example9.json"));  

      //Search for every director or assistant producer object having 'email' attribute
      JNode searchResults = userAgent.json.findEvery("(director|assistant\\s*producer) { email: }");  
      System.out.println("Number of results: " + searchResults.size());
 
      //Search for every producer object having 'email' or 'Email' attribute 
      searchResults = userAgent.json.findEvery("producer: { (email|Email): }");   //quotes optional
      System.out.println("Number of results: " + searchResults.size());
  
      //Search for every object having title 'Mr' or 'Mrs'
      searchResults = userAgent.json.findEvery("{ title: Mrs? }");
      System.out.println("Number of results: " + searchResults.size());
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}