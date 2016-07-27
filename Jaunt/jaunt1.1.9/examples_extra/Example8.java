import com.jaunt.*;
import com.jaunt.util.*;

public class Example8{

  public static void main(String[] args){
    try{
      //specify http proxy at System level.
      System.setProperty("http.proxyHost", "3.14.159.68"); 
      System.setProperty("http.proxyPort", "8080");
      //specify username/password credentials at System level.
      ProxyAuthenticator.setCredentials("tom", "secret");

      UserAgent userAgent = new UserAgent();
      userAgent.visit("http://amazon.com");               
      System.out.println(userAgent.doc.innerXML());   //print the retrieved document    
    }
    catch(JauntException j){
      System.err.println(j);
    }
  }
}