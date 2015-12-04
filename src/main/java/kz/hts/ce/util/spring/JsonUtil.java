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

    public static final String pathToJson = "src/main/resources/settings.json";
    public static final String PRODUCT_MIN = "productMin";
    public static final String PRODUCT_MAX = "productMax";
    public static final String INVOICE_MIN = "invoiceMin";
    public static final String INVOICE_MAX = "invoiceMax";

    private File file = new File(pathToJson);
    private int productMinInt;
    private int productMaxInt;
    private int invoiceMinInt;
    private int invoiceMaxInt;

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
        } catch (ParseException | IOException e) {
            throw new ControllerException(e);
        }
    }

    public JSONObject read(String filePath) {
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

    public void create(int productMin, int productMax, int invoiceMin, int invoiceMax) {
        JSONObject json = new JSONObject();
        createOrUpdateJson(json, productMin, productMax, invoiceMin, invoiceMax);
    }

    public void update(int productMin, int productMax, int invoiceMin, int invoiceMax) {
        JSONObject json = read(pathToJson);
        createOrUpdateJson(json, productMin, productMax, invoiceMin, invoiceMax);
    }

    public void createOrUpdateJson(JSONObject jsonObject, int productMin, int productMax, int invoiceMin, int invoiceMax) {
        try {
            jsonObject.put(PRODUCT_MIN, productMin);
            jsonObject.put(PRODUCT_MAX, productMax);
            jsonObject.put(INVOICE_MIN, invoiceMin);
            jsonObject.put(INVOICE_MAX, invoiceMax);
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
}
