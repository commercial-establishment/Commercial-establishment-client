package kz.hts.ce.util;

import kz.hts.ce.controller.ControllerException;
import kz.hts.ce.model.dto.InvoiceDto;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.dto.ProviderDto;
import kz.hts.ce.model.entity.*;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JavaUtil {

    public static final String URL = "http://localhost:8080";

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
            throw new ControllerException(e);
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
        productDto.setPrice(warehouseProduct.getInitialPrice());
        return productDto;
    }

    public static ProductDto createProductDtoFromProduct(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setBarcode(product.getBarcode());
        productDto.setUnitName(product.getUnit().getName());
        productDto.setCategoryName(product.getCategory().getName());
        return productDto;
    }

    public static InvoiceDto createInvoiceDtoFromInvoice(Invoice invoice, List<InvoiceProduct> invoiceProducts) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoice.getId());
        invoiceDto.setDate(String.valueOf(invoice.getDate()));
        invoiceDto.setPostponement(invoice.getPostponement());
        invoiceDto.setProviderCompanyName(invoice.getProvider().getCompanyName());
        invoiceDto.setVat(invoice.isVat());

        if (invoiceProducts != null) {
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (InvoiceProduct invoiceProduct : invoiceProducts) {
                BigDecimal price = invoiceProduct.getInitialPrice();
                int arrival = invoiceProduct.getAmount();
                BigDecimal productsPrice = multiplyIntegerAndBigDecimal(arrival, price);
                totalPrice = totalPrice.add(productsPrice);
            }
            invoiceDto.setTotalPrice(totalPrice);
        }
        return invoiceDto;
    }

    public static ProviderDto createProviderDtoFromProvider(Provider provider) {
        ProviderDto providerDto = new ProviderDto();
        providerDto.setAddress(provider.getAddress());
        providerDto.setCityName(provider.getCity().getName());
        providerDto.setCompanyName(provider.getCompanyName());
        providerDto.setContactPerson(provider.getContactPerson());
        providerDto.setEmail(provider.getEmail());
        providerDto.setIdentificationNumber(provider.getIdentificationNumber());

        return providerDto;
    }

    public static int countDays(String dateStr, int postponement) {
        try {
            String format = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateStr);

            long postponementDaysInMilliseconds = postponement * 24 * 60 * 60 * 1000L;
            long time = date.getTime();
            time = time + postponementDaysInMilliseconds;
            Date dateWithPostponement = new Date(time);
            Date today = new Date();
            long difference = dateWithPostponement.getTime() - today.getTime();
            return (int) (difference / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            throw new ControllerException(e);
        }
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getFixedDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean checkConnection() {
        try {
            URL url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            return con.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}