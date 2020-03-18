import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.ArrayList;


public class MatchHistory {

    private String outPutFilePath;
    private String[] divisions, tiers;
    private String apiKey,region;

    public MatchHistory(String outPutFilePath, String[] divisions, String[] tiers, String apiKey,String region) {
        this.outPutFilePath = outPutFilePath;
        this.divisions = divisions;
        this.tiers = tiers;
        this.apiKey = apiKey;
        this.region = region;
    }

    public void getData() throws IOException {
        final String lineSep=System.getProperty("line.separator");
        for (String tier: tiers) {
            for (String division: divisions) {

                //file die gelezen moet worden.
                String readFile = outPutFilePath + "/LoLparser/CSVs/PlayersPerBracket/" + tier + "-" + division + "-IDs.csv";
                //reader die de file kan lezen.
                BufferedReader reader = new BufferedReader(new FileReader(readFile));
                //filenaam waar het wordt weggeschreven.
                String outputFile = outPutFilePath + "/LoLparser/CSVs/MatchHistory/History " + tier + "-" + division + ".csv";
                String placeHolder = "";
                String line = null;
                String[] tokens;
                int i = 0;

                for (line = reader.readLine(); line != null;line = reader.readLine(),i++){
                    if(i > 0){
                        parser.sleep(1500);
                        tokens = line.split(",");
                        String urlWhole = "https://" + region + ".api.riotgames.com/lol/match/v4/matchlists/by-account/" + tokens[17] + "?api_key=" + apiKey;

                        String jsonString = parser.returnJsonStringFromUrl(urlWhole);
                        jsonString = jsonString.replace('"' + "gameId" +'"','"' + "accountId" + '"' + ':' + '"' + tokens[17] + '"' + ',' + '"' + "gameId" +'"');
                        if(i == 1){
                            placeHolder = jsonString.substring(0,jsonString.length() -1);
                        }else{
                            placeHolder = placeHolder + "," + jsonString.substring(1,jsonString.length()-1);
                        }

                    }
                }
                placeHolder = placeHolder +"]";
                parser.generateCSVFromJString(placeHolder,outputFile);
            }
        }
    }
}
