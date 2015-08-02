import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;

public class Example4{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();   
      userAgent.visit("http://intel.com");
   
      Element anchor = userAgent.doc.findFirst("<a href>");            //find 1st anchor element with an href attribute
      System.out.println("anchor element: " + anchor);                 //print the anchor element
      System.out.println("anchor's tagname: " + anchor.getName());     //print the anchor's tagname
      System.out.println("anchor's href attribute: " + anchor.getAt("href"));  //print the anchor's href attribute
      System.out.println("anchor's parent Element: " + anchor.getParent());    //print the anchor's parent element
   
      Element meta = userAgent.doc.findFirst("<head>").findFirst("<meta>");    //find 1st meta element in head section.
      System.out.println("meta element: " + meta);                     //print the meta element
      System.out.println("meta's tagname: " + meta.getName());         //print the meta's tagname
      System.out.println("meta's content attribute: " + meta.getAt("content"));//print the meta's content attribute
      System.out.println("meta's parent Element: " + meta.getParent());        //print the meta's parent element
    }
    catch(JauntException e){             
      System.err.println(e);         
    }
  }
}