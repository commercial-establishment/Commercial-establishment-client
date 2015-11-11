package kz.hts.ce.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;

public class JavaUtil {

    private static BigDecimal itemCost = BigDecimal.ZERO;
    private static BigDecimal totalCost = BigDecimal.ZERO;

    public static BigDecimal stringToBigDecimal(String value) {
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator('.');
            String pattern = "#,##0.0#";
            DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
            decimalFormat.setParseBigDecimal(true);

            return (BigDecimal) decimalFormat.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);/*TODO add exception*/
        }
    }

    public static BigDecimal calculateCost(List<Integer> integers, BigDecimal itemPrice) {
        for (Integer integer : integers) {
            itemCost = itemPrice.multiply(new BigDecimal(integer));
            totalCost = totalCost.add(itemCost);
        }
        return totalCost;
    }

//    public static BigDecimal calculatesAmountAndPrice(Long amount, BigDecimal itemPrice) {
//        itemCost = itemPrice.multiply(new BigDecimal(amount));
//        totalCost = totalCost.add(itemCost);
//        return totalCost;
//    }
}