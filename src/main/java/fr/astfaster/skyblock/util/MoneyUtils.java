package fr.astfaster.skyblock.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MoneyUtils {

    public static double roundMoney(String amountString) {
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");

        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);

        amountString = decimalFormat.format(Double.parseDouble(amountString));

        return Double.parseDouble(amountString.replace(",", "."));
    }

}
