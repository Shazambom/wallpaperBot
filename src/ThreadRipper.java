import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * Next objective: Well... make ThreadRipper an objective based class instead of having it be mainly a static class. It will be a lot easier always having the filePath and url
 */
public class ThreadRipper {
    private int total;
    private HashMap<Integer, String> duplicateNames;
    private String filePath;

    public ThreadRipper(String filePath) {
        this.filePath = filePath;
        initDuplicates();
        total = 0;
    }

    public void RipThread(String url) {
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
            String[] folder = new File(filePath).list();
            HashMap<Integer, String> files = new HashMap<Integer, String>();
            for (String element: folder) {
                files.put(element.hashCode(), element);
            }

            System.out.print("[");
            int success = downloadImages(links, names, userAgent, files);
            System.out.println("]");
            System.out.println(success + " unique images successfully downloaded");


        } catch(JauntException e) {
            e.printStackTrace();
        }
    }
    private int downloadImages(List<String> links, List<String> names, UserAgent userAgent, HashMap<Integer, String> files) {
        int success = 0;
        for (int i = 0; i < links.size(); i++) {
            if (!files.containsValue(names.get(i)) && !duplicateNames.containsValue(names.get(i))) {
                try {
                    userAgent.download(links.get(i), new File(filePath + names.get(i)));
                    success++;
                    total++;
                    System.out.print("∎");
                } catch (JauntException e) {
                    System.out.println("\nFile: " + (i + 1) + " at the url: " + links.get(i) + " failed to download");
                    success += downloadImages(links.subList(i + 1, links.size()), names.subList(i + 1, names.size()), userAgent, files);
                    i = links.size();
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


    private String parseLink(String link) {
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
    private String parseFileName(String link) {
        for (int i = 0; i < link.length(); i++) {
            if (link.substring(i, i + 3).equals("/w/")) {
                return link.substring(i + 4);
            }
        }
        return "";
    }


    public ArrayList<String> getThreads(String url) {
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

    private void removeCopyCats(ArrayList<String> threadUrls) {
        ArrayList<String> copyCatUrls = new ArrayList<String>();
        for (String element: threadUrls) {
            if (element.matches("http://boards\\.4chan\\.org/w/thread/[0-9]+/.+")) {
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
    public int getTotal() {
        return total;
    }

    public void cleanUp() {
        ArrayList<File> folder = new ArrayList<File>();
        for (File element: new File(filePath).listFiles()){
            folder.add(element);
        }
        ArrayList<File> toRemove = new ArrayList<File>();
        try {
            System.out.print("[");
            double percentage = folder.size() / 100;
            for (int i = 0; i < folder.size(); i++) {
                if (!toRemove.contains(folder.get(i))) {
                    for (int j = i + 1; j < folder.size(); j++) {
                        if (FileUtils.contentEquals(folder.get(i), folder.get(j))){
                            toRemove.add(folder.get(j));
                        }
                    }
                }
                if (i % percentage == 0) {
                    System.out.print("∎");
                }
            }
            System.out.println("]");
            System.out.println(toRemove.size() + " duplicates found");
            System.out.println("Removing Duplicates...");
            resolveDuplicates(toRemove);
        } catch (Exception e) {
            System.out.println("Well shit");
            e.printStackTrace();
        }
    }
    private void resolveDuplicates(ArrayList<File> toRemove) {
        try {
            PrintWriter out = new PrintWriter(filePath + "duplicates.txt");
            for (Map.Entry element: duplicateNames.entrySet()) {
                out.println(element.getValue());
            }
            for (File element: toRemove) {
                out.println(element.getName());
                Files.delete(element.toPath());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initDuplicates() {
        try {
            File duplicates = new File(filePath + "duplicates.txt");
            FileReader fileReader = new FileReader(duplicates);
            BufferedReader in = new BufferedReader(fileReader);
            duplicateNames = new HashMap<Integer, String>();
            String line;
            while ((line = in.readLine()) != null) {
                duplicateNames.put(line.hashCode(), line);
            }
            in.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
