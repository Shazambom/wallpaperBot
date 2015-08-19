import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
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

public class ThreadRipper {
    private int total;
    private HashMap<Integer, String> duplicateNames;
    private String filePath;
    private File duplicates;

    public ThreadRipper(String filePath) {
        this.filePath = filePath;
        duplicates = initDuplicates();
        total = 0;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
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
                names.add(parseThreadName(userAgent.doc.getUrl()) + "_" + parseFileName(links.get(i)));
            }
            String[] folder = new File(filePath).list();
            HashMap<Integer, String> files = new HashMap<Integer, String>();
            if (folder != null) {
                for (String element : folder) {
                    files.put(element.hashCode(), element);
                }
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
            if ((files.size() == 0 || !files.containsValue(names.get(i))) && !duplicateNames.containsValue(names.get(i))) {
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
        try {
            PrintWriter out = new PrintWriter(duplicates);
            for (File element : new File(filePath).listFiles()) {
                folder.add(element);
                out.println(element.getName());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<File> toRemove = new ArrayList<File>();
        try {
            System.out.print("[");
            double percentage = folder.size() / 100;
            for (int i = 0; i < folder.size(); i++) {
                if (!toRemove.contains(folder.get(i))) {
                    try {
                        BufferedImage image = ImageIO.read(folder.get(i));
                        if (image.getHeight() < 720 || image.getWidth() < 1080){
                            toRemove.add(folder.get(i));
                        }
                        image = null;
                    } catch (Exception e) {
                        continue;
                    }
                    for (int j = i + 1; j < folder.size(); j++) {
                        if (folder.get(i).isFile() && folder.get(j).isFile()
                                && FileUtils.contentEquals(folder.get(i), folder.get(j))){
                            toRemove.add(folder.get(j));
                        }
                    }
                }
                if (i % percentage == 0) {
                    System.out.print("∎");
                }
            }
            System.out.println("]");
            System.out.println(toRemove.size() + " invalid wallpapers found");
            System.out.println("Resolving invalid wallpapers...");
            resolveDuplicates(toRemove);
        } catch (Exception e) {
            System.out.println("Well shit");
            e.printStackTrace();
        }
    }
    private void resolveDuplicates(ArrayList<File> toRemove) {
        try {
            System.out.print("[");
            for (File element: toRemove) {
                Files.delete(element.toPath());
                System.out.print("∎");
            }
            System.out.println("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private File initDuplicates() {
        try {
            duplicateNames = new HashMap<Integer, String>();
            File duplicates = new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt");
            FileReader fileReader = new FileReader(duplicates);
            BufferedReader in = new BufferedReader(fileReader);

            String line;
            while ((line = in.readLine()) != null) {
                duplicateNames.put(line.hashCode(), line);
            }
            in.close();
            fileReader.close();
            return duplicates;

        } catch (Exception e) {
            try {
                PrintWriter out = new PrintWriter(new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt"));
                out.print("\n");
                out.close();
                return new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt");
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        return new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt");
    }

    private String parseThreadName(String threadName) {
        String name = threadName.substring(33);
        String toReturn = "";
        boolean isName = false;
        for (int i = 0; i < name.length(); i++) {
            if (isName) {
                toReturn += name.charAt(i);
            }
            if (name.charAt(i) == '/') {
                isName = true;
            }
        }
        return toReturn;
    }

}
