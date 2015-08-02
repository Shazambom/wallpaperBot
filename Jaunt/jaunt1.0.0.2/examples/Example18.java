import com.jaunt.*;
import com.jaunt.component.*;

public class Example18{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples/schedule.htm");
      Table table = userAgent.doc.getTable("<table class=schedule>");   //get Table component via search query
      
      System.out.println("\nColumn having 'Mon':");
      Elements elements = table.getCol("mon");                                  //get entire column containing 'Mon'
      for(Element element : elements) System.out.println(element.outerHTML());  //iterate through &amp; print elements
      
      System.out.println("\nColumn below 'Tue':");                              
      elements = table.getColBelow("tue");                                      //get column elements below 'Tue'
      for(Element element : elements) System.out.println(element.outerHTML());  //iterate through &amp; print elements
      
      System.out.println("\nFirst row:");
      elements = table.getRow(0);                                               //get row at row index 0.
      for(Element element : elements) System.out.println(element.outerHTML());  //iterate through &amp; print elements
      
      System.out.println("\nRow right of '2:00pm':");
      elements = table.getRowRightOf("2:00pm");                                 //get row elements right of 2:00pm
      for(Element element : elements) System.out.println(element.outerHTML());  //iterate through &amp; print elements
      
      System.out.println("\nCell for fri at 10:00am:");                        
      Element element = table.getCell("fri", "10:00am");             //get element at intersection of col/row
      System.out.println(element.outerHTML());                       //print element
      
      System.out.println("\nCell at position 3,3:");
      element = table.getCell(3,3);                                  //get element at col index 3, row index 3
      System.out.println(element.outerHTML());                       //print element
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}