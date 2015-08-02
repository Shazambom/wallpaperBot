import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;

public class Example5{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();
      userAgent.open(new File("images.htm"));  //open the HTML (or XML) from a file (throws IOException)
    
      Element div = userAgent.doc.findFirst("<div class=images>"); //find first div who's class matches 'images' 
      System.out.println("div as HTML:\n" + div.outerHTML());      
      System.out.println("div's content as HTML:\n" + div.innerHTML());
      System.out.println("div as XML:\n" + div.outerXML(2));             //2 chars of indent added at each node level
      System.out.println("div's content as XML:\n" + div.innerXML(2));   //2 chars of indent added at each node level
  
      //make some changes
      div.innerHTML("<img src='presto.gif'><br>Presto!");          //replace div's content with different elements.
      System.out.println("Altered document as HTML:\n" + userAgent.doc.innerHTML());  //print the altered document.
    }
    catch(JauntException e){
      System.err.println(e);
    }
    catch(IOException e){
      System.err.println(e);
    }
  }
}