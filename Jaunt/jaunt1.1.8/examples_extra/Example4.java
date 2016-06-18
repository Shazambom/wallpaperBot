import com.jaunt.*;
import com.jaunt.component.*;
import java.util.*;

public class Example4{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.settings.showHeaders = true;      //change settings to auto-print header information
    
      System.out.println("SENDING DEFAULT, AUTO-MANAGED REQUEST HEADERS...\n");
      userAgent.sendGET("http://jaunt-api.com/examples_advanced/hello.xml"); //perform HTTP request
      System.out.println("DOCUMENT:\n" + userAgent.doc.innerXML());   //print retrieved document
    
      System.out.println("SENDING MODIFIED REQUEST HEADERS...\n");
      userAgent.sendGET("http://jaunt-api.com/examples_advanced/hello.xml",    
        "user-agent:Mozilla/4.0", "foo:bar");                         //specify new/modified headers
      System.out.println("DOCUMENT:\n" + userAgent.doc.innerXML());   //print retrieved document
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}