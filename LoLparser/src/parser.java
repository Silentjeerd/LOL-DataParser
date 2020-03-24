import java.io.*;

import java.net.URL;
import java.nio.Buffer;
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

        inputfile  = "LoLparser/CSVs/test.json";

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            File jsonFile = new File(inputfile);
            FileUtils.writeStringToFile(jsonFile,jsonText);

        } finally  {
            is.close();
        }
    }

    static public String returnJsonStringFromUrl(String url,String subarray) throws IOException, JSONException{
        InputStream is = new URL(url).openStream();

        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            //return jsonText;
            String wantedString;
            if(subarray == ""){
                wantedString = jsonText;
            }else{
                JSONObject json = new JSONObject(jsonText);
                JSONArray subArray = json.getJSONArray(subarray);
                wantedString = subArray.toString();
            }

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

    static public int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                for (int i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        } finally {
            is.close();
        }
    }

}
