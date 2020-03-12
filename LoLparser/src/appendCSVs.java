import java.io.*;
import java.util.ArrayList;

public class appendCSVs {

    private String outPutFilePath;
    private String[] divisions, tiers;
    private String apiKey, region;

    public appendCSVs(String outPutFilePath, String[] divisions, String[] tiers, String apiKey, String region) {
        this.outPutFilePath = outPutFilePath;
        this.divisions = divisions;
        this.tiers = tiers;
        this.apiKey = apiKey;
        this.region = region;
    }

    public void getAllAccountIDsTierDivision() throws IOException {
        final String lineSep=System.getProperty("line.separator");
        for (String tier: tiers) {
            for (String division: divisions) {
                System.out.println(tier + " - " + division);
                BufferedReader reader = new BufferedReader(new FileReader(outPutFilePath + tier + "-" + division + ".csv"));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutFilePath + tier + "-" + division + "-IDs" + ".csv")));
                String line = null;
                String[] tokens;
                int i = 0;

                for ( line = reader.readLine(); line != null; line = reader.readLine(),i++)
                {
                    parser.sleep(1500);
                    if(i > 0){
                        tokens = line.split(",");
                        String summoner = tokens[4].trim().substring(1,tokens[4].trim().length() - 1);
                        String url = "https://" + region + ".api.riotgames.com/lol/summoner/v4/summoners/" + summoner + "?api_key=" + apiKey;
                        String addedColumn = parser.getAccountID(url);
                        writer.write(line+","+addedColumn+lineSep);
                    }else{
                        writer.write(line+",/AccountIDs"+lineSep);
                    }
                }
                writer.close();
                reader.close();
            }
        }
    }
}
