import com.jaunt.*;
import com.jaunt.component.*;

public class Example7{
  public static void main(String[] args){
    try{
      UserAgent userAgent = new UserAgent(); 
      userAgent.setProxyHost("3.14.159.68");             //specify the proxy host (ip address)
      userAgent.setProxyPort(8080);                      //specify the proxy port
                                                         //visit a (non-https://) URL through the proxy
      userAgent.visit("http://amazon.com");               
      System.out.println(userAgent.doc.innerXML());      //print the retrieved document
    }
    catch(JauntException j){
      System.err.println(j);
    }
  }
}