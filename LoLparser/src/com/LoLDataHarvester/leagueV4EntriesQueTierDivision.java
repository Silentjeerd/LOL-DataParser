package com.LoLDataHarvester;

import java.io.IOException;

public class leagueV4EntriesQueTierDivision {

    private String[] divisions, tiers;
    private String apiKey, region;
    private  String[] pages = {"1"};


    public leagueV4EntriesQueTierDivision(String[] division, String[] tier, String apiKey, String region) throws IOException {
        this.divisions = division;
        this.tiers = tier;
        this.apiKey = apiKey;
        this.region = region;
    }

    public void getData() throws IOException {
        String outputFile = "LoLparser/CSVs/AllPlayers.csv";
        String placeHolder = "";
        int countTotal = (tiers.length * divisions.length * pages.length);
        int countDone = 0;
        for (String tier: tiers) {
            for (String division: divisions) {
                for(String page:pages) {
                    int tryCount = 0;
                    int maxTries = 5;
                    while(tryCount < maxTries){
                        try{
                            parser.sleep(1500);
                            String urlWhole = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" +
                                    division + "?page=" + page + "&api_key=" + apiKey;
                            String jsonString = parser.returnJsonStringFromUrl(urlWhole, "");
                            if (placeHolder == "") {
                                placeHolder = jsonString.substring(0, jsonString.length() - 1);
                            } else {
                                placeHolder = placeHolder + "," + jsonString.substring(1, jsonString.length() - 1);
                            }
                            tryCount = maxTries;
                        }catch (Exception e){
                            parser.sleep(2000);
                            tryCount++;
                            if(tryCount == maxTries) System.out.println(e.toString());
                        }
                    }
                    countDone++;
                    System.out.println("Done with: " + countDone + " out of " + countTotal);
                }
            }
        }

        if(placeHolder != ""){
            placeHolder = placeHolder +"]";
            parser.generateCSVFromJString(placeHolder,outputFile);
        }else{
            System.out.println("Failed to retrieve player data from brackets!");
        }
    }
}
