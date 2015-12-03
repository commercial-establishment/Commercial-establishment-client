package kz.hts.ce.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JsonUtil {

    public static final String pathToJson = "src/main/resources/settings.json";
    private File file = new File(pathToJson);

    public void createOrUpdate(int min, int max) throws IOException, ParseException {
        boolean isChecked = checkJsonFile();
        if (isChecked) {
            update(getAbsolutePath(), min, max);
        } else {
            create(getAbsolutePath(), min, max);
        }
    }

    public JSONObject read(String filePath) {
        JSONObject jsonObject = null;
        try {
            FileReader fileReader = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(fileReader);
        } catch (ParseException | IOException e) {
            if (jsonObject == null) {
                create(filePath, 10, 30);
            }
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
            jsonObject.put("productMin", min);
            jsonObject.put("productMax", max);
            FileWriter fileWriter = new FileWriter(filePath);
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

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }
}
