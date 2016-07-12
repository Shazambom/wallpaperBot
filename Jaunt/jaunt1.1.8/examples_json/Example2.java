import com.jaunt.*;

public class Example2{

  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent();         //create new userAgent (headless browser)
      System.out.println("SETTINGS:\n" + userAgent.settings);      //print the userAgent's default settings.
      userAgent.settings.autoSaveJSON = true;        //change settings to autosave last visited page.
   
      userAgent.sendGET("http://jsonplaceholder.typicode.com/posts/1");   //send request
      JNode title = userAgent.json.findFirst("title");
      System.out.println("title: " + title);	

      JNode body = userAgent.json.findFirst("body");
      System.out.println("body:" + body);      
    }
    catch(JauntException e){                         //if an HTTP/connection error occurs, handle JauntException.
      System.err.println(e);
    }
  }
}