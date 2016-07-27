import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;
import java.util.List;

public class Example1{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();
      userAgent.openContent("<html>Welcome!<div>Under Construction</div><!-- todo: add more --></html>");
      
      Element html = userAgent.doc.findFirst("<html>");     //find the html element
      List<Node> childNodes = html.getChildNodes();         //retrieve the child nodes as a List 
      
      for(Node node : childNodes){                          //iterate through the list of Nodes.
        if(node.getType() == Node.ELEMENT_TYPE){            //determine whether the node is an Element
          System.out.println("element: " + ((Element)node).outerHTML());   //print the element and its content
        }
        else if(node.getType() == Node.TEXT_TYPE){          //determine whether the node is Text
          System.out.println("text: " + ((Text)node).toString());          //print the text
        }
        else if(node.getType() == Node.COMMENT_TYPE){       //determine whether the node is a Comment
          System.out.println("comment: " + ((Comment)node).toString());    //print the comment
        } 
      }
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}