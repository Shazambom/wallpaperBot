import com.jaunt.*;
import com.jaunt.util.*;

public class Example9{

  public static void main(String[] args){
    try{
      //specify https proxy at System level.
      System.setProperty("https.proxyHost", "12.345.67.8"); 
      System.setProperty("https.proxyPort", "80");         
      
      UserAgent userAgent = new UserAgent();
      userAgent.visit("http://amazon.com");               
      System.out.println(userAgent.doc.innerXML());   //print the retrieved document    
    }
    catch(JauntException j){
      System.err.println(j);
    }
  }
}