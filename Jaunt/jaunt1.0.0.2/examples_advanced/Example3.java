import com.jaunt.*;
import com.jaunt.util.*;

public class Example3{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();
      userAgent.settings.genericXMLMode = true;          //set mode for processing generic XML
       
      userAgent.setFilter(new Filter(){                  //subclass Filter to create custom filter
        public boolean childElementAllowed(Element parent, Element child){ //override callback method
          if(child.getName().equals("message")){         //only allow tags named 'message'
            child.removeAttribute("id");                 //remove 'checked' attribute, if present
            return true;
          }
          else return false;
        }
      });
      userAgent.visit("http://jaunt-api.com/examples_advanced/messageList.xml"); //open content
      System.out.println("Filtered document:\n" + userAgent.doc.innerXML());     //print filtered document.   
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}