import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LoLDataHarvester {

    private static String api_key = ""; //Hier voer je je API key in, deze is nodig in de URLs om toegang te verkrijgen tot de server.
    private static String region = "euw1"; //De region waarvoor we de data ophalen.
    //private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"}; //De tiers die we scannen.
    //private static String[] divisions = {"I","II","III","IV"}; //De brackets die we scannen.

    private static String[] tiers = {"DIAMOND"}; //Single tier voor test.
    private static String[] divisions = {"I"}; //Single bracket voor test.

    public static void main(String[] args) throws IOException {
        createDatabase db = new createDatabase("postgres", "passwordhere", "lolparserdata",5432, "localhost");


        getAllPlayerRanks(); //calld de functie die alle spelers ophaalt per bracket/tier.
        appendBracketsWithIDs(); //Calld de functie die alle speler data met accountID's aanvult, deze ID fungeert als Key voor elke tabel.
        getMatchHistoryByAccountID(); //Calld de functie die de matchhistory per speler ophaalt.
        //challengerData(); //Calld de functie die de data van de challenger tier ophaalt.
        matchDataByID(); //Calld de functie die alle data van de gespeelde matches ophaalt.
        getAllMasteries(); //Calld de functie die per speler ophaalt hoe begaafd hij is met elke champion.
        //championTest(); //Calld de functie die alle data ophaalt per champion.
        //getChampionRotation();
    }

    public static void championTest()throws IOException{
        System.out.println("ChampionTest");
        champions champs = new champions();
        champs.getData();
    }

    public static void getAllPlayerRanks()  throws IOException {
        System.out.println("Get all player rank data");
        leagueV4EntriesQueTierDivision leaderboard = new leagueV4EntriesQueTierDivision(divisions,tiers,api_key,region);
        leaderboard.getData();
    }

    public static void getMatchHistoryByAccountID() throws IOException{
        System.out.println("Getting all match history");
        MatchHistory matchHistory = new MatchHistory(divisions,tiers,api_key,region);
        matchHistory.getdata();
    }

    public static void matchDataByID() throws IOException {
        System.out.println("Getting all match data");
        matchv4MatchesByMatchID getMatchData = new matchv4MatchesByMatchID(api_key,region);
        getMatchData.writeToOneCSV();
    }

    public static void challengerData() throws  IOException{
        challengerSoloQue5x5 output = new challengerSoloQue5x5( "LoLparser/CSVs/PlayersPerBracket/");
        output.getData(region,api_key);
    }

    public static void appendBracketsWithIDs() throws IOException{
        System.out.println("Appending players with IDs");
        appendCSVs append = new appendCSVs(api_key,region);
        append.getAllIDs();
    }

    private static void getAllMasteries() throws IOException {
        System.out.println("Getting all player masteries");
        playerMasteries masteries = new playerMasteries(api_key, region);
        masteries.getdata();
    }

    private static void getChampionRotation() throws IOException{
        System.out.println("Getting Champion Rotation");
        championRotations rotation = new championRotations(api_key,region);
        rotation.getData();
    }
}
