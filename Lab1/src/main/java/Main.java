public class Main {
    public static void main(String[] args) {
        String url = "https://999.md/ro/list/transport/cars";
        WebScraper webScraper = new WebScraper();
        webScraper.scrapeWebsite(url);
    }
}
