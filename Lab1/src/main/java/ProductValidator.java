public class ProductValidator {
    public String cleanString(String input) {
        return input.trim();
    }

    public Integer validatePrice(String priceString) {
        try {
            priceString = priceString.replaceAll("[^0-9]", "").trim();
            return Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public double convertPrice(Integer price, String priceString) {
        double exchangeRate = 19.0; // Example: 1 EUR = 19 MDL
        if (priceString.contains("€")) {
            // Convert EUR to MDL
            return price * exchangeRate;
        } else {
            // Convert MDL to EUR
            return price / exchangeRate;
        }
    }
}
