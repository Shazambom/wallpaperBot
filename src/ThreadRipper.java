import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shazambom on 7/22/2015.
 */
public class ThreadRipper {
    public static void RipThread(String url, String filePath) {
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(url);
            System.out.println(userAgent.doc.getUrl());
            Elements fileNames = userAgent.doc.findEach("<a class=\"fileThumb\">");
            System.out.println(fileNames.size() + " images found");
            ArrayList<String> links = new ArrayList<String>();
            ArrayList<String> names = new ArrayList<String>();
            for (Element element: fileNames) {
                links.add(element.toString());
            }
            for (int i = 0; i < links.size(); i++) {
                links.set(i, parseLink(links.get(i)));
                names.add(parseFileName(links.get(i)));
            }
            String[] foulder = new File(filePath).list();
            HashMap<Integer, String> files = new HashMap<Integer, String>();
            for (String element: foulder) {
                files.put(element.hashCode(), element);
            }

            System.out.print("[");
            int success = downloadImages(links, names, userAgent, filePath, files);
            System.out.println("]");
            System.out.println(success + " unique images successfully downloaded");


        } catch(JauntException e) {
            e.printStackTrace();
        }
    }
    private static int downloadImages(List<String> links, List<String> names, UserAgent userAgent, String filePath, HashMap<Integer, String> files) {
        int success = 0;
        double percentageComplete;
        if (links.size() < 10) {
            percentageComplete = links.size();
        } else {
            percentageComplete = links.size()/10;
        }
        for (int i = 0; i < links.size(); i++) {
            if (!files.containsValue(names.get(i))) {
                try {
                    userAgent.download(links.get(i), new File(filePath + names.get(i)));
                    success++;
                } catch (JauntException e) {
                    System.out.println("\nFile: " + (i + 1) + " at the url: " + links.get(i) + " failed to download");
                    success += downloadImages(links.subList(i + 1, links.size()), names.subList(i + 1, names.size()), userAgent, filePath, files);
                    i = links.size();
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
        }
        return success;
    }


    private static String parseLink(String link) {
        String toReturn = "";
        for (int i = 0; i < link.length(); i++) {
            if (link.substring(i, i + 7).equals("http://")){
                while (link.charAt(i) != '\"') {
                    toReturn += link.charAt(i);
                    i++;
                }
                return (toReturn);
            }
        }
        return toReturn;
    }
    private static String parseFileName(String link) {
        for (int i = 0; i < link.length(); i++) {
            if (link.substring(i, i + 3).equals("/w/")) {
                return link.substring(i + 4);
            }
        }
        return "";
    }


    public static ArrayList<String> getThreads(String url) {
        ArrayList<String> threadUrls = new ArrayList<String>();
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(url);
            Elements clickHere = userAgent.doc.findEvery("<a class=\"replylink\">");
            for (Element element: clickHere) {
                threadUrls.add(parseLink(element.toString()));
            }
            removeCopyCats(threadUrls);
            try {
                userAgent.doc.submit("Next");
            } catch (JauntException e) {
                return threadUrls;
            }
            threadUrls.addAll(getThreads(userAgent.doc.getUrl()));

        } catch(JauntException e) {
            e.printStackTrace();
        }


        return threadUrls;
    }

    private static void removeCopyCats(ArrayList<String> threadUrls) {
        ArrayList<String> copyCatUrls = new ArrayList<String>();
        for (String element: threadUrls) {
            if (element.matches("http://boards\\.4chan\\.0org/w/thread/[0-9]+/.+")) {
                String copyCatUrl = "";
                int slashCount = 0;
                for (int i = 0; i < element.length(); i++) {
                    if (element.charAt(i) == '/') {
                        slashCount++;
                    }
                    if (slashCount == 6) {
                        break;
                    }
                    copyCatUrl += element.charAt(i);
                }
                copyCatUrls.add(copyCatUrl);
            }
        }
        for (String element: copyCatUrls) {
            threadUrls.remove(element);
        }
    }
}
