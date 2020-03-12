import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class leagueV4EntriesQueTierDivision {

    private String outPutFilePath;
    private String[] divisions, tiers;
    private String apiKey, region;

    public leagueV4EntriesQueTierDivision(String outPutFilePath, String[] division, String[] tier, String apiKey, String region) throws IOException {
        this.outPutFilePath = outPutFilePath;
        this.divisions = division;
        this.tiers = tier;
        this.apiKey = apiKey;
        this.region = region;
        getData();
    }

    public void getData() throws IOException {
        for (String tier: tiers) {
            for (String division: divisions) {
                parser.sleep(1500);
                String outputFile = outPutFilePath + tier + "-" + division + ".csv";
                String urlWhole = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/"+
                        division + "?page=1" +  "&api_key=" + apiKey;
                parser.generateCSV(urlWhole,outputFile);
            }
        }
    }
}
