package fr.astfaster.skyblock.util;

public class MoneyUtils {

    public static double roundMoney(String amountString) {
        final String[] splitedNumber = amountString.split(",");
        final String baseNumber = splitedNumber[0];

        String decimal = "0";
        if (splitedNumber.length > 1) {
            decimal = splitedNumber[1];
            if (decimal.length() > 2) {
                decimal = decimal.substring(0, decimal.length() - 2);
            }
        }


        return Double.parseDouble(baseNumber + "." + decimal);
    }

}
