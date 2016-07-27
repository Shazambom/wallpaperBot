import com.jaunt.*;
import com.jaunt.component.*;
import java.util.List;

public class Example16{
  public static void main(String[] args){
    try{ 
      UserAgent userAgent = new UserAgent(); 
      userAgent.visit("http://jaunt-api.com/examples/search.htm");
 
      Form form = userAgent.doc.getForm("<form name=srch>");            //retrieve Form object by its name.
      form.addPermutationTarget("keyword", new String[]{"cat", "dog"}); //specify keyword field values to permute thru
      form.addPermutationTarget("movieType");            //specify that movietype field will be permuted (all values)
      form.addPermutationTarget("lang");                 //specify that lang field will be permuted (all values)
      List<HttpRequest> requests = form.getRequestPermutations();       //generate list of request permutations
   
      System.out.println("request permutations:");
      for(HttpRequest request : requests){               //print the list of request permutation
        System.out.println(request.asUrl());
      } 
    }
    catch(JauntException e){
      System.err.println(e);
    }
  }
}