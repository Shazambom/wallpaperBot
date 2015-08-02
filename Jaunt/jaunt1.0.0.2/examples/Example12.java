import com.jaunt.*;
import com.jaunt.component.*;

public class Example12{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples/signup.htm");
      
      userAgent.doc.apply(     //fill-out the form by applying a sequence of inputs
        "tom@mail.com",        //string input is applied to textfield
        "(advanced)",          //bracketed string (regular expression) selects a menu item
        "no comment",          //string input is applied to textarea
        1                      //integer specifies index of radiobutton choice
      );  
      userAgent.doc.submit("create trial account"); //press the submit button labelled 'create trial account'
      System.out.println(userAgent.getLocation());  //print the current location (url)
    }
    catch(JauntException e){ 
      System.out.println(e);
    }
  }
}