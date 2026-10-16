import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    static class MarketDataPoint {
        double price;
        Instant timestamp;

        MarketDataPoint(double price, Instant timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "price=" + price + ", timestamp=" + timestamp;
        }
    }

    private static final Pattern PRICE_PATTERN =
            Pattern.compile("\"price\"\\s*:\\s*\"?([0-9]+(?:\\.[0-9]+)?)\"?");

    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("TWELVE_DATA_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("API key not found. Please set the TWELVE_DATA_API_KEY environment variable.");
            return;
        }

        Queue<MarketDataPoint> marketDataQueue = new LinkedList<>();
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 1; i <= 20; i++) {
            System.out.println("\nRequest " + i + " of 20");

            String url = "https://api.twelvedata.com/price?symbol=DIA&apikey=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("HTTP status: " + response.statusCode());
            System.out.println("Raw response: " + response.body());

            if (response.statusCode() == 200) {
                Matcher matcher = PRICE_PATTERN.matcher(response.body());

                if (matcher.find()) {
                    double price = Double.parseDouble(matcher.group(1));
                    MarketDataPoint dataPoint = new MarketDataPoint(price, Instant.now());

                    marketDataQueue.add(dataPoint);

                    System.out.println("Added data point: " + dataPoint);
                    System.out.println("Current queue size: " + marketDataQueue.size());
                } else {
                    System.out.println("Could not find a price field in the response.");
                }
            } else {
                System.out.println("Unable to retrieve market data. HTTP status code: " + response.statusCode());
            }

            if (i < 20) {
                System.out.println("Waiting 15 seconds before the next request...");
                Thread.sleep(15000);
            }
        }

        System.out.println("\nFinal queue contents:");
        for (MarketDataPoint point : marketDataQueue) {
            System.out.println(point);
        }
    }
}
