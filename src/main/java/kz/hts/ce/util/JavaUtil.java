package kz.hts.ce.util;

import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Product;
import kz.hts.ce.model.entity.WarehouseProduct;

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

    public static ProductDto createProductDtoFromWarehouseProduct(WarehouseProduct warehouseProduct, int amount) {
        ProductDto productDto = new ProductDto();
        productDto.setId(warehouseProduct.getId());
        productDto.setName(warehouseProduct.getProduct().getName());
        productDto.setAmount(amount);
        productDto.setPrice(new BigDecimal(0));
        productDto.setPrice(warehouseProduct.getPrice());
        return productDto;
    }

    public static ProductDto createProductDtoFromProduct(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setBarcode(product.getBarcode());
        productDto.setUnitName(product.getUnit().getName());
        productDto.setCategoryName(product.getCategory().getName());
        return productDto;
    }
}