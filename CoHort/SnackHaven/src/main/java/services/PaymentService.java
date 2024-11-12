
package services;

public class PaymentService {

    // CONSTANTS
    private static final int QUARTER_VALUE = 25;
    private static final int NICKEL_VALUE = 5;
    private static final int DIME_VALUE = 10;

    // Methods



    public static class PennyConverter {

        public static String calculateCoins(int pennies) {
            int quarters = pennies / QUARTER_VALUE;

            pennies %= QUARTER_VALUE;

            int dimes = pennies / DIME_VALUE;

            pennies %= DIME_VALUE;

            int nickels = pennies / NICKEL_VALUE;

            pennies %= NICKEL_VALUE;

            return String.format("Quarters: %d, Dimes: %d, Nickels: %d, Pennies: %d", quarters, dimes, nickels, pennies);
        }

        public static String formatCents(int balance) {
            return String.format("%.2f", balance / 100.0);  // Cast to double to retain decimal precision
        }

    }


}
