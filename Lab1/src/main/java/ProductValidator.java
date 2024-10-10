public class ProductValidator {
    public String cleanString(String input) {
        return input.trim();
    }

    public Integer validatePrice(String priceString) {
        try {
            priceString = priceString.trim();
            return Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            System.out.println("Price is not a valid integer: " + priceString);
            return null;
        }
    }
}
