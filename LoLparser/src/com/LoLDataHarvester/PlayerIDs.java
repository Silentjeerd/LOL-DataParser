package com.LoLDataHarvester;

import com.LoLDataHarvester.StrategyPattern.Strategy;

import java.io.*;

public class PlayerIDs implements Strategy {

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
        String fileToRead = "LoLparser/CSVs/AllPlayers.csv"; //File die uitgelezen moet worden
        String fileToWrite = "LoLparser/CSVs/AllPlayersWithIDs.csv"; //File waar de nieuwe data heen moet
        BufferedReader fileReader = new BufferedReader(new FileReader(fileToRead)); //opent de reader
        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite))); //opent de writer
        int lineCount = Parser.countLines(fileToRead); //telt het aantal regels van de readfile zodat ik progressie naar de console kan schrijven
        String line = null;
        String[] tokens;
        int i = 0;

        //moet line != null zijn, voor test purpose i < 3
        for ( line = fileReader.readLine(); line != null; line = fileReader.readLine(),i++)
        {
            int tryCount = 0;
            int maxTries = 5;

            tokens = line.split(",");
            String summoner = tokens[4].trim().substring(1,tokens[4].trim().length() - 1);//haalt de summonerid op en trimt deze voor de url

            while(tryCount < maxTries){
                try{
                    Parser.sleep(1500); //slaapt 1.5seconden vanwege api limitaties.
                    if(i > 0){
                        String url = "https://" + region + ".api.riotgames.com/lol/summoner/v4/summoners/" + summoner + "?api_key=" + api_key; //url voor summoner data
                        String accountID = Parser.getAccountID(url); //haalt het accountID op van de summoner
                        fileWriter.write(line+","+accountID+lineSep); //maakt een nieuwe regel en schrijft deze in het nieuwe bestand.
                    }else{
                        fileWriter.write(line+",/AccountID,/SummonerLevel"+lineSep); //als het de eerste regel van de file is wordt er een nieuwe kolom ingevoegd.
                    }
                    System.out.println("Done with: " + (i+1) + " out of " + lineCount); //print de progressie in je console.
                    tryCount = maxTries;
                }catch (Exception e){
                    Parser.sleep(2000);
                    tryCount++;
                    if(tryCount == maxTries) System.out.println(e.toString());
                }
            }
        }
        fileWriter.close(); //sluit de reader en writer.
        fileReader.close();
    }
}
