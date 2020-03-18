import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;

import java.net.URL;
import java.nio.charset.Charset;

import com.github.opendevl.JFlat;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.IOUtils;
import org.json.*;

import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

public class parser {

    static private String inputfile;

    static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    static private String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }

    static private void readJsonFromUrl(String url) throws IOException, JSONException{
        InputStream is = new URL(url).openStream();

        inputfile  = Paths.get("").toRealPath().getParent().toString() + "/test.json";

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            File jsonFile = new File(inputfile);
            FileUtils.writeStringToFile(jsonFile,jsonText);

        } finally  {
            is.close();
        }
    }

    static public String returnJsonStringFromUrl(String url) throws IOException, JSONException{
        InputStream is = new URL(url).openStream();

        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            //return jsonText;
            JSONObject json = new JSONObject(jsonText);
            JSONArray subArray = json.getJSONArray("matches");
            String wantedString = subArray.toString();
            return wantedString;

        } finally  {
            is.close();
        }
    }

    static public void generateCSVFromJString(String inputString, String outputFile) throws IOException{

        JFlat flatMe = new JFlat(inputString);
        List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
        flatMe.write2csv(outputFile);
    }

    static public void generateCSV(String url,String outputFile) throws IOException{

        readJsonFromUrl(url);
        String str = new String(Files.readAllBytes(Paths.get(inputfile)));
        JFlat flatMe = new JFlat(str);
        List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
        flatMe.write2csv(outputFile);
    }

    static public String getAccountID(String url) throws IOException{
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JSONObject json = new JSONObject(jsonText);
        String accountId = json.getString("accountId");
        return accountId;
    }
}
