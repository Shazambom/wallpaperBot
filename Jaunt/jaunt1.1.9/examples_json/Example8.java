import com.jaunt.*;
import java.io.*;

public class Example8{

  public static void main(String[] args) throws IOException{
    try{ 
      UserAgent userAgent = new UserAgent(); 
      userAgent.openJSON(new File("example8.json"));  

      JNode searchResults = userAgent.json.findEvery("producer: { email: }");  //quotes around JSON Strings optional
      System.out.println("Found producers having email: " + searchResults.size() + " result(s)");
 
      searchResults = userAgent.json.findEvery("'producer': { 'title': 'Mr' }");  //apostrophes can replace quotes
      System.out.println("Found producers having title 'Mr': " + searchResults.size() + " result(s)");

      searchResults = userAgent.json.findEvery("{ title:, email: }");
      System.out.println("Found objects having both title and email: " + searchResults.size() + " result(s)");
  
      searchResults = userAgent.json.findEach("producer { title: }");
      System.out.println("Found non-nested producers having a title: " + searchResults.size() + " result(s)");
  
      searchResults = userAgent.json.getEach("{ title: }");
      System.out.println("Found child objects having a title: " + searchResults.size() + " result(s)");
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}