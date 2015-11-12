package kz.hts.ce.util;

import kz.hts.ce.model.entity.ShopProduct;
import kz.hts.ce.model.dto.ProductDto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;

public class JavaUtil {



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
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Integer integer : integers) {
            BigDecimal itemCost = itemPrice.multiply(new BigDecimal(integer));
            totalCost = totalCost.add(itemCost);
        }
        return totalCost;
    }

    public static BigDecimal multiplyIntegerAndBigDecimal(Integer integer, BigDecimal itemPrice) {
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal itemCost = itemPrice.multiply(new BigDecimal(integer));
        totalCost = totalCost.add(itemCost);
        return totalCost;
    }

    public static ProductDto createProductDtoFromShopProduct(ShopProduct shopProduct, int amount) {
        ProductDto productDto = new ProductDto();
        productDto.setName(shopProduct.getProduct().getName());
        productDto.setAmount(0);
        productDto.setAmount(amount);
        productDto.setPrice(new BigDecimal(0));
        productDto.setPrice(shopProduct.getPrice());
        return productDto;
    }
}