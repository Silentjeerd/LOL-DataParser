package com.LoLDataHarvester;

import org.json.JSONObject;

import java.io.*;


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

        for (line = reader.readLine(); line != null;line = reader.readLine(),i++){
            tokens = line.split(",");
            String summoner = tokens[17];
            int tryCount = 0;
            int maxTries = 5;
            while(tryCount < maxTries){
             try{
                 if(i > 0){
                     parser.sleep(1500);
                     String urlWhole = "https://" + region + ".api.riotgames.com/lol/match/v4/matchlists/by-account/" + summoner + "?queue=420&api_key=" + apiKey;
                     //haalt een subarray op.
                     String jsonString = parser.returnJsonStringFromUrl(urlWhole,"matches");
                     JSONObject fixMatchIDs = new JSONObject(jsonString);
                     jsonString = jsonString.replace('"' + "gameId" +'"','"' + "accountId" + '"' + ':' + '"' + summoner + '"' + ',' + '"' + "gameId" +'"');
                     if(placeHolder == ""){
                         placeHolder = jsonString.substring(0,jsonString.length() -1);
                     }else{
                         placeHolder = placeHolder + "," + jsonString.substring(1,jsonString.length()-1);
                     }
                 }
                 tryCount = maxTries;
             }catch (Exception e){
                 parser.sleep(2000);
                 tryCount++;
                 if(tryCount == maxTries) System.out.println(e.toString());
             }
            }
            System.out.println("Done with: " + (i+1) + " out of " + lineCount);
        }

        if(placeHolder != ""){
            placeHolder = placeHolder +"]";
            parser.generateCSVFromJString(placeHolder,outputFile);
        }else{
            System.out.println("Failed to retrieve any match history");
        }
    }

}
