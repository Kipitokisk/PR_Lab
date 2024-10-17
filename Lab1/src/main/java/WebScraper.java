import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class WebScraper {

    // Function to send HTTP/HTTPS request over TCP and return the response body
    private String getHttpResponse(String url, Set<String> visitedUrls, int redirectCount) throws IOException {
        if (redirectCount > 5) {  // Change 5 to whatever limit you prefer
            throw new RuntimeException("Too many redirects");
        }

// Parse the URL
        String protocol = url.startsWith("https") ? "https" : "http";

// Validate and extract the host
        String[] urlParts = url.split("/");
        if (urlParts.length < 3) {
            throw new RuntimeException("Invalid URL format: " + url);
        }
        String host = urlParts[2];  // Extract the host

// Extract the path
        int pathIndex = url.indexOf("/", 8);  // 8 skips the "https://" or "http://"
        String path = (pathIndex != -1) ? url.substring(pathIndex) : "/";

        int port = protocol.equals("https") ? 443 : 80;

        Socket socket;

// Set a timeout and use SSLSocket for HTTPS and regular Socket for HTTP
        if (protocol.equals("https")) {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket();
            sslSocket.connect(new InetSocketAddress(host, port), 10000);  // 10 seconds timeout
            socket = sslSocket;
        } else {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 10000);  // Set timeout to 10 seconds
        }

        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // HTTP GET Request
        String request = "GET " + path + " HTTP/1.1\r\n"
                + "Host: " + host + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        // Send HTTP request
        outputStream.write(request.getBytes());
        outputStream.flush();

        // Read the HTTP response headers
        String line;
        StringBuilder headers = new StringBuilder();
        Map<String, String> headerMap = new HashMap<>();
        boolean isBody = false;
        StringBuilder responseBody = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                isBody = true;  // Headers are done, start reading body
                continue;
            }

            if (!isBody) {
                headers.append(line).append("\n");

                // Split headers into key-value pairs
                if (line.contains(": ")) {
                    String[] headerParts = line.split(": ", 2);
                    headerMap.put(headerParts[0], headerParts[1]);
                }
            } else {
                responseBody.append(line).append("\n");  // Reading the body
            }
        }

        // Check for 301/302 redirects
        if (headers.toString().contains("HTTP/1.1 301") || headers.toString().contains("HTTP/1.1 302")) {
            String location = "https://999.md" + headerMap.get("Location");  // Get the new location URL
            if (location != null) {
                // Check if the URL has already been visited to prevent loops
                if (visitedUrls.contains(location)) {
                    throw new RuntimeException("Redirect loop detected for URL: " + location);
                }
                visitedUrls.add(location);  // Add the current location to visited URLs

                // Follow the redirect
                socket.close();
                return getHttpResponse(location, visitedUrls, redirectCount + 1);  // Recurse with the new URL
            }
        }

        // Close the socket and return the body content
        socket.close();
        return responseBody.toString();
    }

    // Modify scrapeWebsite to initialize the visitedUrls set
    public void scrapeWebsite(String url, double minPrice, double maxPrice) {
        try {
            // Initialize visited URLs set to keep track of redirects
            Set<String> visitedUrls = new HashSet<>();
            String responseBody = getHttpResponse(url, visitedUrls, 0);

            // Proceed with the existing scraping logic using the responseBody
            Document document = Jsoup.parse(responseBody);
            Elements products = document.select("ul.ads-list-photo.large-photo > li.ads-list-photo-item");

            List<Product> filteredProducts = products.stream()
                    .map(product -> {
                        String name = product.select(".ads-list-photo-item-title").text();
                        String priceString = product.select(".ads-list-photo-item-price-wrapper").text();
                        String productUrl = "https://999.md" + product.select(".js-item-ad").attr("href");

                        ProductValidator productValidator = new ProductValidator();
                        name = productValidator.cleanString(name);
                        Integer price = productValidator.validatePrice(priceString);

                        if (price == null) {
                            return null;
                        }
                        int priceInMdl = productValidator.convertPrice(price, priceString);

                        try {
                            // Fetch the product page via TCP
                            String productPageBody = getHttpResponse(productUrl, visitedUrls, 0);
                            Document productPage = Jsoup.parse(productPageBody);

                            String description = productPage.select(".adPage__content__description.grid_18").text();
                            return new Product(name, priceInMdl, description, productUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .filter(product -> product != null && product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                    .collect(Collectors.toList());

            long totalPrice = filteredProducts.stream().map(Product::getPrice).reduce(0L, Long::sum);

            ProductSummary productSummary = new ProductSummary(filteredProducts, totalPrice, Instant.now().toString());

            System.out.println("JSON Representation:");
            System.out.println(productSummary.toJson());
            System.out.println("XML Representation:");
            System.out.println(productSummary.toXml());
            System.out.println("Pipe Separated Representation:");
            System.out.println(productSummary.toPipeSeparated());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
