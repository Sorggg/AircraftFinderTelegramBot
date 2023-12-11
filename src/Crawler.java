import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Crawler {

    public static List<String> findUrls(String seed) throws IOException {
        List<String> ris = new ArrayList<String>();

        Document doc = Jsoup.connect(seed).get();
        for (Element link : doc.select("a[href]")) {
            String nextLink = link.absUrl("href");
            if(nextLink.contains("https://en.wikipedia.org/wiki/")&& !(nextLink.contains("Wikipedia") || nextLink.contains("List") || nextLink.contains("Maintenance") || nextLink.contains("Special") || nextLink.contains("%")|| nextLink.contains("Template")|| nextLink.contains("null") || nextLink.contains("Help") || nextLink.contains("Category")|| nextLink.contains("File")|| nextLink.contains("Current"))){

                ris.add(nextLink);
            }
        }
        return ris;
    }
    public static String getPicture(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements immagini = doc.select("img.mw-file-element");
        int max = 50;
        int min = 1;
        boolean cerca = true;
        String ris = null;
        while (cerca){
            int n = (int) (Math.random() * (max - min + 1) + min);
            ris = immagini.get(n).absUrl("src");
            if (!ris.toUpperCase().contains("FLAG") && !ris.toUpperCase().contains("LOGO") && !ris.toUpperCase().contains("ICON") && !ris.toUpperCase().contains("BOOK")&& !ris.toUpperCase().contains("SYMBOL")){
                cerca = false;
            }
        }

        System.out.println(ris);
        return ris ;
    }
    public static String getManufacturer(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String ris = null;
        for (Element table : doc.select("table.infobox")) {
            for(Element tr: table.select("tr:contains(Manufacturer)")){
                ris = tr.select("a").text();
                if(ris.contains("]")){
                    ris = ris.substring(0,ris.length()-3);
                }
            }
        }
        return ris;
    }
    public static String getRole(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String ris = null;
        for (Element table : doc.select("table.infobox")) {
            for(Element tr: table.select("tr:contains(Role)")){
                ris = tr.select("p").text();
                if(ris.isEmpty()){
                    ris = tr.select("a").text();

                }
                if(ris.contains("]")){
                    ris = ris.substring(0,ris.length()-3);
                }
            }
        }
        return ris;
    }
    public static String getStatus(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String ris = null;
        for (Element table : doc.select("table.infobox")) {
            for(Element tr: table.select("tr:contains(Status)")){
                ris = tr.select("td").text();
                if (ris.equals("Retired")){
                    ris = "Ritirato";
                }
                else if(ris.equals("In service")){
                    ris = "In servizio";
                }
                if(ris.contains("]")){
                    ris = ris.substring(0,ris.length()-3);
                }
            }
        }
        return ris;
    }
    public static String getVariants(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String ris = null;
        for (Element table : doc.select("table.infobox")) {
            for(Element tr: table.select("tr:contains(Variants)")){
                ris = tr.select("p").text();
                if(ris.isEmpty()){
                    ris = tr.select("a").text();

                }
                if(ris.contains("]")){
                    ris = ris.substring(0,ris.length()-3);
                }
            }
        }
        return ris;
    }
    public static String getNaz(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String ris = null;
        for (Element table : doc.select("table.infobox")) {
            for(Element tr: table.select("tr:contains(National origin)")){
                ris = tr.select("td").text();
                if(ris.contains("]")){
                    ris = ris.substring(0,ris.length()-3);
                }
            }
        }
        return ris;
    }
    public static String getFirst(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String ris = null;
        for (Element table : doc.select("table.infobox")) {
            for(Element tr: table.select("tr:contains(First flight)")){
                ris = tr.select("td").text();
                if(ris.contains("]")){
                    ris = ris.substring(0,ris.length()-3);
                }
            }
        }
        return ris;
    }
}
