import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.Node;
import com.jaunt.UserAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Shazambom on 7/22/2015.
 */
public class ThreadRipper {
    public static void RipThread(String url) {
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(url);
            System.out.println(userAgent.doc.getUrl());
            Elements fileNames = userAgent.doc.findEach("<img>");
            System.out.println(fileNames.size());
            ArrayList<String> links = new ArrayList<String>();
            ArrayList<String> names = new ArrayList<String>();
            for (Element element: fileNames) {
                links.add(element.toString());
            }
            for (int i = 0; i < links.size(); i++) {
                links.set(i, parseLink(links.get(i)));
                names.add(parseFileName(links.get(i)));
            }
            downloadImages(links, names, userAgent, 0);


        } catch(JauntException e) {
            e.printStackTrace();
        }
    }
    private static void downloadImages(ArrayList<String> links, ArrayList<String> names, UserAgent userAgent, int current) {
        System.out.print("[");
        double percentageComplete;
        if (links.size() < 10) {
            percentageComplete = 1;
        } else if (links.size() > 10 && links.size() < 20) {
            percentageComplete = links.size()/10;
        } else {
            percentageComplete = links.size()/20;
        }
        for (int i = current; i < links.size(); i++) {
            current = i;
            try {
                userAgent.download(links.get(i), new File("F:\\RippedWallpapers\\" + names.get(i)));
            } catch (JauntException e) {
                System.out.print("!");
                downloadImages(links, names, userAgent, current++);
            }
            if (i != 0 && i % percentageComplete == 0) {
                System.out.print("∎");
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("]");
    }


    public static String parseLink(String link) {
        String toReturn = "";
        for (int i = 0; i < link.length(); i++) {
            if (link.substring(i, i + 7).equals("http://")){
                while (link.charAt(i) != '\"') {
                    toReturn += link.charAt(i);
                    i++;
                }
                return parseLargeImage(toReturn);
            }
        }
        return toReturn;
    }
    public static String parseFileName(String link) {
        for (int i = 0; i < link.length(); i++) {
            if (link.substring(i, i + 3).equals("/w/")) {
                return link.substring(i + 4);
            }
        }
        return "";
    }

    private static String parseLargeImage(String link) {
        return link.replace("s.jpg", ".jpg");
    }


    public static ArrayList<String> getThreads(String url) {
        ArrayList<String> threadUrls = new ArrayList<String>();
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(url);
            System.out.println(userAgent.doc.getUrl());
            Elements dividers = userAgent.doc.findEvery("<div>");
            System.out.println(dividers.size());
            ArrayList<Element> threads = new ArrayList<Element>();
            ArrayList<Element> digging = new ArrayList<Element>();
            for (Element element: dividers) {
                System.out.println(element.toString());
                if (element.toString().equals("<div id=\"content\">")){
                    digging = (ArrayList)element.getChildElements();
                }
            }
            System.out.println("\n" + digging.size());
            for (Element element: digging) {
                System.out.println(element.toString());
                if (element.toString().equals("<div id=\"threads\">")) {
                    threads = (ArrayList)element.getChildElements();
                }
            }
            System.out.println("\n" + threads.size());
            for (Element element: threads) {
                System.out.println(element.toString());
            }
            //For some ungodly reason I can't dig deeper into the threads <div>
            //This really sucks, I need some way of getting the list of thread urls
            //searching for <a> doesn't work either because the program doesn't dig deep enough



        } catch(JauntException e) {
            e.printStackTrace();
        }


        return threadUrls;
    }
//    private static String ripThreadUrl(Element element) {
//        String url = "";
//
//    }

}
