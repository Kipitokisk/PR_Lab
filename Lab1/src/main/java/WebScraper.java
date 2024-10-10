import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraper {
    public void scrapeWebsite(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements products = document.select("ul.ads-list-photo.large-photo > li.ads-list-photo-item");
//            System.out.println(products);
            for (Element product : products) {
                String name = product.select(".ads-list-photo-item-title").text();
                String priceString = product.select(".ads-list-photo-item-price-wrapper").text();
                String productUrl = "https://999.md" + product.select("a").attr("href");
                System.out.println("Product: productName:" + name + " " + "prodcutPrice: " + priceString);
                System.out.println(productUrl);
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
