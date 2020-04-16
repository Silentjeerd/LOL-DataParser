package com.LoLDataHarvester;

import java.io.IOException;

public class harvestData {

    private String api_key, region ;
    private String[] tiers, divisions;

    /**
     * Main constructor to setup the harvesting
     * @param api_key = api key provided by roit games
     * @param region = the 'region' we want to retrieve data from
     * @param tiers = the 'tiers' we want te retrieve data from
     * @param divisions = the 'divisions' we want to retrieve data from
     */
    public harvestData(String api_key, String region, String[] tiers, String[] divisions) {
        this.api_key = api_key;
        this.region = region;
        this.tiers = tiers;
        this.divisions = divisions;
    }

    /**
     * This function makes acuates all functions within this class to start harvesting data.
     * Here is were we setup what we want to scrape or not.
     * @throws IOException
     */
    public void run() throws IOException {
        //getAllPlayerRanks();
        //appendBracketsWithIDs();
        //getMatchHistoryByAccountID();
        //getChallengerData();
        //getMatchDataByID();
        //getAllMasteries();
        getChampions();
    }

    private void getChampions()throws IOException {
        System.out.println("Get all Champions data");
        champspellitems champs = new champspellitems("10.6.1");
        champs.getData();
    }

    private void getAllPlayerRanks()  throws IOException {
        System.out.println("Get all player rank data");
        leagueV4EntriesQueTierDivision leaderboard = new leagueV4EntriesQueTierDivision(divisions,tiers,api_key,region);
        leaderboard.getData();
    }

    private void getMatchHistoryByAccountID() throws IOException{
        System.out.println("Getting all match history");
        MatchHistory matchHistory = new MatchHistory(divisions,tiers,api_key,region);
        matchHistory.getdata();
    }

    private void getMatchDataByID() throws IOException {
        System.out.println("Getting all match data");
        matchv4MatchesByMatchID getMatchData = new matchv4MatchesByMatchID(api_key,region);
        getMatchData.writeToOneCSV();

    }

    private void getChallengerData() throws  IOException{
        challengerSoloQue5x5 output = new challengerSoloQue5x5( "LoLparser/CSVs/PlayersPerBracket/");
        output.getData(region,api_key);
    }

    /**
     * Haalt alle accountID's op en vult deze aan bij bijbehorende speler.
     * @throws IOException
     */
    private void appendBracketsWithIDs() throws IOException{
        System.out.println("Appending players with IDs");
        appendCSVs append = new appendCSVs(api_key,region);
        append.getAllIDs();
    }

    private void getAllMasteries() throws IOException {
        System.out.println("Getting all player masteries");
        playerMasteries masteries = new playerMasteries(api_key, region);
        masteries.getdata();
    }

    private void getChampionRotation() throws IOException{
        System.out.println("Getting Champion Rotation");
        championRotations rotation = new championRotations(api_key,region);
        rotation.getData();
    }
}
