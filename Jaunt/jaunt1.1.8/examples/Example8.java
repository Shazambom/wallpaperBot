import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;
import java.util.*;

public class Example8{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();
      userAgent.visit("http://amazon.com");    

      Elements tables = userAgent.doc.findEach("<table>");       //find non-nested tables    
      System.out.println("Found " + tables.size() + " tables:");
      for(Element table : tables){                               //iterate through Results
        System.out.println(table.outerHTML() + "\n----\n");      //print each element and its contents
      }    
                                                        
      Elements ols = userAgent.doc.findEach("<table>").findEach("<ol>");//find non-nested ol's within non-nested tables
      System.out.println("Found " + ols.size() + " OLs:");
      for(Element ol : ols){                                     //iterate through Results
        System.out.println(ol.outerHTML() + "\n----\n");         //print each element and its contents
      } 
    }
    catch(ResponseException e){
      System.out.println(e);
    } 
  }
}