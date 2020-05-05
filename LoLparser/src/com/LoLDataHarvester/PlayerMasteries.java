package com.LoLDataHarvester;

import com.LoLDataHarvester.StrategyPattern.Strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PlayerMasteries implements Strategy {

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
        final String lineSep=System.getProperty("line.separator");
        String readFile = "LoLparser/CSVs/AllPlayersWithIDs.csv";
        String outputFile = "LoLparser/CSVs/AllPlayerMasteries.csv";
        String placeHolder = "";

        int lineCount = Parser.countLines(readFile);
        BufferedReader reader = new BufferedReader(new FileReader(readFile));

        String line = null;
        String[] tokens;
        int i = 0;

        for (line = reader.readLine(); line != null;line = reader.readLine(),i++){

            int tryCount = 0;
            int maxTries = 5;

            tokens = line.split(",");
            String summoner = tokens[4].trim().substring(1,tokens[4].trim().length() - 1);
            while(tryCount < maxTries){
                try{
                    if(i > 0){
                        Parser.sleep(1500);
                        String urlWhole = "https://" + region + ".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/" + summoner + "?api_key=" + api_key;
                        //haalt een subarray op.
                        String jsonString = Parser.returnJsonStringFromUrl(urlWhole, "");

                        if (placeHolder == "") {
                            placeHolder = jsonString.substring(0, jsonString.length() - 1);
                        } else {
                            placeHolder = placeHolder + "," + jsonString.substring(1, jsonString.length() - 1);
                        }

                    }
                    tryCount = maxTries;
                    System.out.println("Done with: " + (i+1) + " out of " + lineCount);
                }catch (Exception e){
                    Parser.sleep(2000);
                    tryCount++;
                    if(tryCount == maxTries) System.out.println(e.toString());
                }
            }
        }
        if(placeHolder != ""){
            placeHolder = placeHolder +"]";
            Parser.generateCSVFromJString(placeHolder,outputFile);
        }else{
            System.out.println("Failed to retrieve player masteries!");
        }
    }
}
