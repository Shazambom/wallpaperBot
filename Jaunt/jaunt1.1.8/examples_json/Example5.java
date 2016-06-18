import com.jaunt.*;
import java.io.*;

public class Example5{

  public static void main(String[] args) throws IOException{
    try{ 
      UserAgent userAgent = new UserAgent();
      userAgent.openJSON("{ \"editions\": [1985, 2003, 2010, 2014] }");  //open JSON from String

      JNode editionsArray = userAgent.json.getFirst("editions");  
      System.out.println("size of array: " + editionsArray.size());
      JNode firstArrayElement = editionsArray.get(0);
      int value = firstArrayElement.toInt();
      System.out.println("first array value: " + value);  // or firstArrayElement.toString()
  
      System.out.println("all array elements:");
      for(JNode node : editionsArray){
        System.out.println("edition year: " + node);   
      }  
    }  
    catch(JauntException e){
      System.err.println(e);
    }
  }
}