import com.jaunt.*;
import com.jaunt.component.*;

public class Example15{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples/signup2.htm");
 
      Form form = userAgent.doc.getForm(0);       //get the document's first Form
      form.setTextField("email", "tom@mail.com"); //or  form.set("email", "tom@mail.com");
      form.setPassword("pw", "secret");           //or  form.set("pw", "secret");
      form.setCheckBox("remember", true);         //or  form.set("remember", "on");
      form.setSelect("account", "advanced");      //or  form.set("account", "advanced");
      form.setTextArea("comment", "no comment");  //or  form.set("comment", "no comment");
      form.setRadio("inform", "no");              //or  form.setRadio("inform", "no");
      form.submit("create trial account");        //click the submit button labelled 'create trial account'
      System.out.println(userAgent.getLocation());//print the current location (url)
    }
    catch(JauntException e){                   
      System.err.println(e);
    }
  }
}