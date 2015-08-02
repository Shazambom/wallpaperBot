import com.jaunt.*;
import com.jaunt.component.*;

public class Example13{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples/login.htm");
 
      userAgent.doc.fillout("Username:", "tom");       //fill out the component labelled 'Username' with "tom"
      userAgent.doc.fillout("Password:", "secret");    //fill out the component labelled 'Password' with "secret"
      userAgent.doc.choose(Label.RIGHT, "Remember me");//choose the component right-labelled 'Remember me'.
      userAgent.doc.submit();                          //submit the form
      System.out.println(userAgent.getLocation());     //print the current location (url)
    }
    catch(JauntException e){ 
      System.err.println(e);
    }
  }
}