import com.jaunt.*;
import com.jaunt.component.*;
import java.util.List;

public class Example10{
  public static void main(String[] args){
    try{ 
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples/food.htm");
      Element element;
   
      element = userAgent.doc.getElement(0);                     //retrieve 1st child element within the doc.      
      System.out.println("result1: " + element);                 //print the element
   
      element = userAgent.doc.getElement(0).getElement(0).getElement(3);   //get 4th child of 1st child of 1st child.
      System.out.println("result2: " + element);                           //print the element
   
      element = userAgent.doc.findFirst("<p class=meat>").getElement(1);   //retrieve 2nd child element of p
      System.out.println("result3: " + element.outerHTML());               //print the element and its content
      
      Elements elements = userAgent.doc.findFirst("<body>").getEach("<div>"); //get body's child divs
      System.out.println("result4 has " + elements.size() + " divs:\n");   //print the search results
      System.out.println(elements.innerHTML(2));                           //print elements, indenting by 2
    }
    catch(JauntException e){                          
      System.out.println(e);
    }    
  }
}