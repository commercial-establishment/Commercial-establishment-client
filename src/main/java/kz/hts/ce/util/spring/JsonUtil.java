package kz.hts.ce.util.spring;

import kz.hts.ce.controller.ControllerException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class JsonUtil {

    private static final String pathToJson = "settings.json";
    private static final String PRODUCT_MIN = "productMin";
    private static final String PRODUCT_MAX = "productMax";
    private static final String INVOICE_MIN = "invoiceMin";
    private static final String INVOICE_MAX = "invoiceMax";
    private static final String VAT = "vat";
    private static final String EXPORT_DIRECTORY = "pathForExport";

    private File file = new File(pathToJson);
    private int productMinInt;
    private int productMaxInt;
    private int invoiceMinInt;
    private int invoiceMaxInt;
    private boolean vatBoolean;
    private String pathForExport;

    public void fillFields() {
        try {
            FileReader fileReader = new FileReader(pathToJson);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(fileReader);
            long productMinLong = (long) jsonObject.get(PRODUCT_MIN);
            productMinInt = (int) productMinLong;
            long productMaxLong = (long) jsonObject.get(PRODUCT_MAX);
            productMaxInt = (int) productMaxLong;
            long invoiceMinLong = (long) jsonObject.get(INVOICE_MIN);
            invoiceMinInt = (int) invoiceMinLong;
            long invoiceMaxLong = (long) jsonObject.get(INVOICE_MAX);
            invoiceMaxInt = (int) invoiceMaxLong;
            vatBoolean = (boolean) jsonObject.get(VAT);
            pathForExport = (String) jsonObject.get(EXPORT_DIRECTORY);
        } catch (ParseException | IOException e) {
            throw new ControllerException(e);
        }
    }

    private JSONObject read(String filePath) {
        JSONObject jsonObject;
        try {
            FileReader fileReader = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(fileReader);
        } catch (ParseException | IOException e) {
            throw new ControllerException(e);
        }
        return jsonObject;
    }

    public void create(int productMin, int productMax, int invoiceMin, int invoiceMax, boolean vat, String pathForExport) {
        JSONObject json = new JSONObject();
        createOrUpdateJson(json, productMin, productMax, invoiceMin, invoiceMax, vat, pathForExport);
    }

    public void update(int productMin, int productMax, int invoiceMin, int invoiceMax, boolean vat, String pathForExport) {
        JSONObject json = read(pathToJson);
        createOrUpdateJson(json, productMin, productMax, invoiceMin, invoiceMax, vat, pathForExport);
    }

    private void createOrUpdateJson(JSONObject jsonObject, int productMin, int productMax, int invoiceMin,
                                    int invoiceMax, boolean vat, String pathForExport) {
        try {
            jsonObject.put(PRODUCT_MIN, productMin);
            jsonObject.put(PRODUCT_MAX, productMax);
            jsonObject.put(INVOICE_MIN, invoiceMin);
            jsonObject.put(INVOICE_MAX, invoiceMax);
            jsonObject.put(VAT, vat);
            jsonObject.put(EXPORT_DIRECTORY, pathForExport);
            FileWriter fileWriter = new FileWriter(pathToJson);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkJsonFile() {
        return file.isFile();
    }

    public int getProductMinInt() {
        return productMinInt;
    }

    public void setProductMinInt(int productMinInt) {
        this.productMinInt = productMinInt;
    }

    public int getProductMaxInt() {
        return productMaxInt;
    }

    public void setProductMaxInt(int productMaxInt) {
        this.productMaxInt = productMaxInt;
    }

    public int getInvoiceMinInt() {
        return invoiceMinInt;
    }

    public void setInvoiceMinInt(int invoiceMinInt) {
        this.invoiceMinInt = invoiceMinInt;
    }

    public int getInvoiceMaxInt() {
        return invoiceMaxInt;
    }

    public void setInvoiceMaxInt(int invoiceMaxInt) {
        this.invoiceMaxInt = invoiceMaxInt;
    }

    public boolean isVatBoolean() {
        return vatBoolean;
    }

    public void setVatBoolean(boolean vatBoolean) {
        this.vatBoolean = vatBoolean;
    }

    public String getPathForExport() {
        return pathForExport;
    }

    public void setPathForExport(String pathForExport) {
        this.pathForExport = pathForExport;
    }
}
