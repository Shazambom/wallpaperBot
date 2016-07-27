import com.jaunt.*;
import com.jaunt.component.*;

public class Example14{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples/signup.htm");
      Document doc = userAgent.doc;
   
      doc.fillout("E-mail:", "tom@mail.com");  //fill out the (textfield) component labelled "E-mail:"
      doc.choose("Account Type:", "advanced"); //choose "advanced" from the menu labelled "Account Type:"
      doc.fillout("Comments:", "no comment");  //fill out the (textarea) component labelled "Comments:"
      doc.choose(Label.RIGHT, "No thanks");    //choose the (radiobutton) component right-labelled "No thanks"
      doc.submit("create trial account");      //press the submit button labelled 'create trial account'
      System.out.println(userAgent.getLocation());  //print the current location (url)
    }
    catch(JauntException e){ 
      System.out.println(e);
    }
  }
}