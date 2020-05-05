package com.LoLDataHarvester;

import com.LoLDataHarvester.StrategyPattern.Strategy;

import java.io.IOException;

public class PlayerRanks implements Strategy {

    /**
     * Deze functie heeft bij elke class een andere betekenis en zorgt ervoor
     * dat de gewenste data wordt opgehaald via een url en wordt weggeschreven in een .CSV file.
     * @param api_key De sleutel die nodig is om data van Riot op te halen.
     * @param region Dit is de region waarvan we data ophalen.
     * @param tiers Dit is een array van verschillende tiers waar doorheen geloopt kan worden.
     * @param divisions Dit is een array van verschillende divisions waar doorheen geloopt kan worden.
     * @throws IOException
     */
    @Override
    public void getData(String api_key, String region, String[] tiers, String[] divisions) throws IOException {
        String[] pages = {"1"};
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
                            Parser.sleep(1500);
                            String urlWhole = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" +
                                    division + "?page=" + page + "&api_key=" + api_key;
                            String jsonString = Parser.returnJsonStringFromUrl(urlWhole, "");
                            if (placeHolder == "") {
                                placeHolder = jsonString.substring(0, jsonString.length() - 1);
                            } else {
                                placeHolder = placeHolder + "," + jsonString.substring(1, jsonString.length() - 1);
                            }
                            tryCount = maxTries;
                        }catch (Exception e){
                            Parser.sleep(2000);
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
            Parser.generateCSVFromJString(placeHolder,outputFile);
        }else{
            System.out.println("Failed to retrieve player data from brackets!");
        }
    }
}
