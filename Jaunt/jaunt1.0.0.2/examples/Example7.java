import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;

public class Example7{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();  //find the first anchor having href, get href value (below)
      String firstAnchorUrl = userAgent.visit("http://amazon.com").findFirst("<a href>").getAt("href");
      userAgent.visit(firstAnchorUrl);
      System.out.println("location:" + userAgent.getLocation());    //print the current location (url).
    }
    catch(SearchException e){        //if an element or attribute isn't found, catch the exception.
      System.err.println(e);         //printing exception shows details regarding origin of error
    }
    catch(ResponseException e){      //in case of HTTP/Connection error, catch ResponseExeption
      System.err.println(e);         //printing exception shows HTTP error information or connection error
    }
  }
}