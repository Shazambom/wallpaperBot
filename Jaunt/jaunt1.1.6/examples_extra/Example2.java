import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;
import java.util.List;

public class Example2{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples_advanced/message.xml");
   
      Comment result = userAgent.doc.getComment(2);              //get doc's 3rd immediate child comment      
      System.out.println("result: " + result);                   //print the result

      Comment doctype = userAgent.doc.getFirst(Comment.DOCTYPE); //get doc's 1st immediate child doctype      
      System.out.println("doctype: " + doctype);                 //print the comment
   
      List<Comment> pis = userAgent.doc.getEach(Comment.PROCESSING_INSTRUCTION); //get doc's immediate child PIs
      for(Comment pi : pis) System.out.println("processing instruction: " + pi); //print the list of PIs.
        
      Comment cdata = userAgent.doc.findFirst(Comment.CDATA);    //find first CDATA section in document
      System.out.println("cdata: " + cdata);                     //print the CDATA section
   
      List<Comment> comments = userAgent.doc.findEach(Comment.REGULAR); //find each regular comment in document
      for(Comment comment : comments) System.out.println("comment: " + comment); //print list of regular comments
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}