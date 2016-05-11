import com.jaunt.*;
import com.jaunt.util.*;

public class Example6{

  public static void main(String[] args){
    try{
      //create UserAgent and content handlers.
      UserAgent userAgent = new UserAgent();    
      HandlerForText handlerForText = new HandlerForText();
      HandlerForBinary handlerForBinary = new HandlerForBinary();

      //register each handler with a specific content-type
      userAgent.setHandler("text/css", handlerForText);
      userAgent.setHandler("text/javascript", handlerForText);
      userAgent.setHandler("application/x-javascript", handlerForText);
      userAgent.setHandler("image/gif", handlerForBinary);
      userAgent.setHandler("image/jpeg", handlerForBinary);

      //retrieve CSS content as String
      userAgent.visit("http://jaunt-api.com/syntaxhighlighter/styles/shCore.css");
      System.out.println(handlerForText.getContent());
    
      //retrieve JS content as String
      userAgent.visit("http://jaunt-api.com/syntaxhighlighter/scripts/shCore.js");
      System.out.println(handlerForText.getContent());
    
      //retrieve GIF content as byte[], and print its length
      userAgent.visit("http://jaunt-api.com/background.gif");
      System.out.println(handlerForBinary.getContent().length);    
    }
    catch(JauntException j){
      System.err.println(j);
    }
  }
}