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
}
