import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MatchHistory {

    private String[] divisions, tiers;
    private String apiKey,region;

    public MatchHistory( String[] divisions, String[] tiers, String apiKey,String region) {

        this.divisions = divisions;
        this.tiers = tiers;
        this.apiKey = apiKey;
        this.region = region;
    }

    public void getdata() throws IOException{
        final String lineSep=System.getProperty("line.separator");
        String readFile = "LoLparser/CSVs/AllPlayersWithIDs.csv";
        String outputFile = "LoLparser/CSVs/AllMatchHistory.csv";
        String placeHolder = "";
        int lineCount = parser.countLines(readFile);
        BufferedReader reader = new BufferedReader(new FileReader(readFile));

        String line = null;
        String[] tokens;
        int i = 0;

        for (line = reader.readLine(); i < 3;line = reader.readLine(),i++){
            if(i > 0){
                parser.sleep(1500);
                tokens = line.split(",");
                String urlWhole = "https://" + region + ".api.riotgames.com/lol/match/v4/matchlists/by-account/" + tokens[17] + "?api_key=" + apiKey;

                //haalt een subarray op.
                String jsonString = parser.returnJsonStringFromUrl(urlWhole,"matches");

                jsonString = jsonString.replace('"' + "gameId" +'"','"' + "accountId" + '"' + ':' + '"' + tokens[17] + '"' + ',' + '"' + "gameId" +'"');

                if(placeHolder == ""){
                    placeHolder = jsonString.substring(0,jsonString.length() -1);
                }else{
                    placeHolder = placeHolder + "," + jsonString.substring(1,jsonString.length()-1);
                }

            }
            System.out.println("Done with: " + (i+1) + " out of " + lineCount);
        }

        placeHolder = placeHolder +"]";
        parser.generateCSVFromJString(placeHolder,outputFile);
    }
}
