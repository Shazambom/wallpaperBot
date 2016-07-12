import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;

public class Example2{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();                       //create new userAgent (headless browser).
      System.out.println("SETTINGS:\n" + userAgent.settings);      //print the userAgent's default settings.
      userAgent.settings.autoSaveAsHTML = true;                    //change settings to autosave last visited page. 
      
      userAgent.visit("http://oracle.com");                        //visit a url.
      String title = userAgent.doc.findFirst("<title>").getText(); //get child text of title element.
      System.out.println("\nOracle's website title: " + title);    //print the title

      userAgent.visit("http://amazon.com");                        //visit another url.
      title = userAgent.doc.findFirst("<title>").getText();        //get child text of first title element.
      System.out.println("\nAmazon's website title: " + title);    //print the title
    }
    catch(JauntException e){   //if title element isn't found or HTTP/connection error occurs, handle JauntException.
      System.err.println(e);          
    }
  }
}