import com.jaunt.*;
import java.io.*;

public class Example4{

  public static void main(String[] args) throws IOException{
    try{
      UserAgent userAgent = new UserAgent();         
      userAgent.openJSON(new File("example4.json"));  //open JSON from a file
  
      JNode node = userAgent.json.findFirst("firstName");
      System.out.println("node name: " + node.getName());
      System.out.println("node type: " + node.getType());
      System.out.println("parent node name: " + node.getParent().getName());
      System.out.println("node as string: " + node.toString());
      System.out.println("------------");
   
      node = userAgent.json.getFirst("author");
      System.out.println("node name: " + node.getName());
      System.out.println("node type: " + node.getType());
      System.out.println("node as string:\n" + node.toString());  
      System.out.println("last name: " + node.getFirst("lastName"));  //or node.get("lastName")
      System.out.println("------------");
  
      node = userAgent.json.getFirst("editions");
      System.out.println("node name: " + node.getName());
      System.out.println("node type: " + node.getType());
      System.out.println("node as string:\n" + node.toString());   
    }
    catch(JauntException e){         
      System.err.println(e);
    }
  }
}