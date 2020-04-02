import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LoLDataHarvester {

    private static String api_key = "RGAPI-a73cbf1b-5549-4205-8f05-e5e14cace95c"; //Hier voer je je API key in, deze is nodig in de URLs om toegang te verkrijgen tot de server.
    private static String region = "euw1"; //De region waarvoor we de data ophalen.
    private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"}; //De tiers die we scannen.
    private static String[] divisions = {"I","II","III","IV"}; //De brackets die we scannen.

    //private static String[] tiers = {"DIAMOND"}; //Single tier voor test.
    //private static String[] divisions = {"I"}; //Single bracket voor test.

    public static void main(String[] args) throws IOException {
        //createDatabase db = new createDatabase("postgres", "passwordhere", "lolparserdata",5432, "localhost");
       //Done getAllPlayerRanks(); //calld de functie die alle spelers ophaalt per bracket/tier.
       //Done appendBracketsWithIDs(); //Calld de functie die alle speler data met accountID's aanvult, deze ID fungeert als Key voor elke tabel.
        //DOne getMatchHistoryByAccountID(); //Calld de functie die de matchhistory per speler ophaalt.
       matchDataByID(); //Calld de functie die alle data van de gespeelde matches ophaalt.
       //TODO //getAllMasteries(); //Calld de functie die per speler ophaalt hoe begaafd hij is met elke champion.
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

        for(int i = 5;i <= 100;i++){
            System.out.println("Getting all match data runNumber: " + i);
            matchv4MatchesByMatchID getMatchData = new matchv4MatchesByMatchID(api_key,region,i);
            getMatchData.writeToOneCSV();
        }
    }

    public static void challengerData() throws  IOException{
        challengerSoloQue5x5 output = new challengerSoloQue5x5("LoLparser/CSVs/PlayersPerBracket/");
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
