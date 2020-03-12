import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.io.FileUtils;

public class matchv4MatchesByMatchID {

    private String outPutFilePath;

    public matchv4MatchesByMatchID(String outputFilePath) throws IOException {
        outPutFilePath = outputFilePath;
    }

    public void getData(String region, String matchID, String apiKey) throws IOException {
        String outputFile = outPutFilePath + "matchID.csv";
        String urlWhole = "https://" + region + ".api.riotgames.com/lol/match/v4/matches/" + matchID + "?api_key=" + apiKey;
        parser.generateCSV(urlWhole, outputFile);
    }


}
