import com.jaunt.*;
import java.io.*;

public class Example6{

  public static void main(String[] args) throws IOException{
    try{ 
      UserAgent userAgent = new UserAgent(); 
      userAgent.openJSON(new File("example6.json"));  
  
      //find every title 
      JNode searchResults = userAgent.json.findEvery("title");
      System.out.println("Search results for every title:\n" + searchResults);
      System.out.println("number of results: " + searchResults.size());
      System.out.println("------------------");

      //find every title in the movies section
      searchResults = userAgent.json.getFirst("movies").findEvery("title");
      System.out.println("Search results for every movie title:\n" + searchResults);
      System.out.println("number of results: " + searchResults.size());
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}