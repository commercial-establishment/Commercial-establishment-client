package kz.hts.ce.util;

import javafx.fxml.Initializable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class JsonUtil implements Initializable {

    public static final String pathToJson = "src/main/resources/settings.json";
    public static final String PRODUCT_MIN = "productMin";
    public static final String PRODUCT_MAX = "productMax";

    private File file = new File(pathToJson);
    private int min;
    private int max;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FileReader fileReader = new FileReader(pathToJson);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(fileReader);
            long minLong = (long) jsonObject.get(PRODUCT_MIN);
            min = (int) minLong;
            long maxLong = (long) jsonObject.get(PRODUCT_MAX);
            max = (int) maxLong;
        } catch (ParseException | IOException e) {
        }
    }

    public JSONObject read(String filePath) {
        JSONObject jsonObject = null;
        try {
            FileReader fileReader = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(fileReader);
        } catch (ParseException | IOException e) {

        }
        return jsonObject;
    }

    public void create(String filePath, int min, int max) {
        JSONObject json = new JSONObject();
        createOrUpdateJson(json, filePath, min, max);
    }

    public void update(String filePath, int min, int max) {
        JSONObject json = read(filePath);
        createOrUpdateJson(json, filePath, min, max);
    }

    public void createOrUpdateJson(JSONObject jsonObject, String filePath, int min, int max) {
        try {
            jsonObject.put(PRODUCT_MIN, min);
            jsonObject.put(PRODUCT_MAX, max);
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
            min = (int) jsonObject.get(PRODUCT_MIN);
            max = (int) jsonObject.get(PRODUCT_MAX);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkJsonFile() {
        return file.isFile();
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
