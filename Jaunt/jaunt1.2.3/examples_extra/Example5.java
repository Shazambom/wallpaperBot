import com.jaunt.*;
import com.jaunt.component.*;
import java.util.*;

public class Example5{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();

      System.out.println("SENDING HEAD REQUEST...\n");
      userAgent.sendHEAD("http://jaunt-api.com/examples_advanced/hello.xml");   //send HTTP HEAD Request
      System.out.println("RESPONSE HEADERS:\n" + userAgent.response.getHeaders());
     
      System.out.println("SENDING POST REQUEST...\n");
      userAgent.sendPOST("http://tomcervenka.site90.com/handlePost.php",
        "username=tom&password=secret");                               //send HTTP POST Request with queryString
      System.out.println("DOCUMENT:\n" + userAgent.doc.innerXML());    //print retrieved Document
      
      /** this section requires TARGET_SERVER to support PUT requests
      System.out.println("SENDING PUT REQUEST...\n");
      userAgent.sendPUT("http://TARGET_SERVER/examples_advanced/hello.xml",     //send HTTP PUT Request with updated content
        "<?xml version=\"1.0\"?><message>Hi Mom!</message>");
      System.out.println("DOCUMENT:\n" + userAgent.doc.innerXML());    //print retrieved Document
      */
      
      /** this section requires TARGET_SERVER to support DELETE requests
      System.out.println("SENDING DELETE REQUEST...\n");                  
      userAgent.sendDELETE("http://TARGET_SERVER/examples_advanced/hello.xml"); //send HTTP DELETE request
      System.out.println("DOCUMENT:\n" + userAgent.doc.innerXML());    //print retrieved Document
      */
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}
