import com.jaunt.*;
import com.jaunt.component.*;

public class Example19{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      String url = "http://northernbushcraft.com";
      userAgent.setCacheEnabled(true);         //caching turned on
      userAgent.visit(url);                    //cache empty, so HTML page requested via http &amp; saved in cache.
      userAgent.visit(url);                    //when revisiting, page pulled from filesystem cache - no http request.
      System.out.println(userAgent.response);  //response object shows that content was cached, note no response headers
      
      userAgent.setCacheEnabled(false);        //caching turned off
      userAgent.visit(url);                    //page is once again retrieved via http request. 
      System.out.println(userAgent.response);  //print response object, which now shows response headers
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}

