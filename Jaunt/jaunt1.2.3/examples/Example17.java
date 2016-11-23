import com.jaunt.*;
import com.jaunt.component.*;
import java.util.*;

public class Example17{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();
      userAgent.visit("http://jaunt-api.com/examples/stocks.htm");
   
      //example without using Table component
      Element table = userAgent.doc.findFirst("<table class=stocks>");  //find table element
      Elements tds = table.findEach("<td|th>");                         //find non-nested td/th elements
      for(Element td: tds){                                             //iterate through td/th's
        System.out.println(td.outerHTML());                             //print each td/th element
      }
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}