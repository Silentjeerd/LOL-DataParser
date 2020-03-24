import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class playerMasteries {
    private String apiKey,region;

    public playerMasteries(String apiKey,String region) {

        this.apiKey = apiKey;
        this.region = region;
    }

    public void getdata() throws IOException {

        final String lineSep=System.getProperty("line.separator");
        String readFile = "LoLparser/CSVs/AllPlayersWithIDs.csv";
        String outputFile = "LoLparser/CSVs/AllPlayerMasteries.csv";
        String placeHolder = "";

        int lineCount = parser.countLines(readFile);
        BufferedReader reader = new BufferedReader(new FileReader(readFile));

        String line = null;
        String[] tokens;
        int i = 0;

        for (line = reader.readLine(); i < 3;line = reader.readLine(),i++){

            int tryCount = 0;
            int maxTries = 5;

            tokens = line.split(",");
            String summoner = tokens[4].trim().substring(1,tokens[4].trim().length() - 1);
            while(tryCount < maxTries){
                try{
                    if(i > 0){
                        parser.sleep(1500);
                        String urlWhole = "https://" + region + ".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/" + summoner + "?api_key=" + apiKey;
                        //haalt een subarray op.
                        String jsonString = parser.returnJsonStringFromUrl(urlWhole, "");

                        if (placeHolder == "") {
                            placeHolder = jsonString.substring(0, jsonString.length() - 1);
                        } else {
                            placeHolder = placeHolder + "," + jsonString.substring(1, jsonString.length() - 1);
                        }

                    }
                    tryCount = maxTries;
                    System.out.println("Done with: " + (i+1) + " out of " + lineCount);
                }catch (Exception e){
                    parser.sleep(2000);
                    tryCount++;
                    if(tryCount == maxTries) System.out.println(e.toString());
                }
            }
        }
        if(placeHolder != ""){
            placeHolder = placeHolder +"]";
            parser.generateCSVFromJString(placeHolder,outputFile);
        }else{
            System.out.println("Failed to retrieve player masteries!");
        }
    }
}
