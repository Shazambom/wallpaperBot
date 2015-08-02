import com.jaunt.*;
import com.jaunt.component.*;

public class Example11{
  public static void main(String[] args){
    try{ 
      UserAgent userAgent = new UserAgent();  
      userAgent.visit("http://jaunt-api.com/examples/hello.htm");

      Elements elements = userAgent.doc.findEvery("<div|span>");      //find every element who's tagname is div or span.
      System.out.println("search results:\n" + elements.innerHTML()); //print the search results

      elements = userAgent.doc.findEvery("<p id=1|4>");               //find every p element who's id is 1 or 4
      System.out.println("search results:\n" + elements.innerHTML()); //print the search results

      elements = userAgent.doc.findEvery("< id=[2-6]>");              //find every element (any name) with id from 2-6
      System.out.println("search results:\n" + elements.innerHTML()); //print the search results
  
      elements = userAgent.doc.findEvery("&lt;p>ho");      //find every p who's joined child text contains 'ho' (regex) 
      System.out.println("search results:\n" + elements.innerHTML()); //print the search results
    }
    catch(ResponseException e){                          
      System.out.println(e);
    }
  }
}