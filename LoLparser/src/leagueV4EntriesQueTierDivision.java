import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class leagueV4EntriesQueTierDivision {

    private String[] divisions, tiers;
    private String apiKey, region;
    private  String[] pages = {"1"};
    public leagueV4EntriesQueTierDivision(String[] division, String[] tier, String apiKey, String region) throws IOException {
        this.divisions = division;
        this.tiers = tier;
        this.apiKey = apiKey;
        this.region = region;

        getData();
    }

    public void getData() throws IOException {
        String outputFile = "LoLparser/CSVs/PlayersPerBracket/AllPlayers.csv";
        String placeHolder = "";

        for (String tier: tiers) {
            for (String division: divisions) {
                for(String page:pages) {

                    parser.sleep(1500);
                    //String outputFile = "LoLparser/CSVs/PlayersPerBracket/" + tier + "-" + division + ".csv";
                    String urlWhole = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" +
                            division + "?page=" + page + "&api_key=" + apiKey;
                    String jsonString = parser.returnJsonStringFromUrl(urlWhole, "");
                    if (placeHolder == "") {
                        placeHolder = jsonString.substring(0, jsonString.length() - 1);
                    } else {
                        placeHolder = placeHolder + "," + jsonString.substring(1, jsonString.length() - 1);
                    }
                    //parser.generateCSV(urlWhole,outputFile);
                }
            }
        }

        placeHolder = placeHolder +"]";
        parser.generateCSVFromJString(placeHolder,outputFile);
    }
}
