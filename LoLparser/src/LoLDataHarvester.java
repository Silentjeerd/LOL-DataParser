import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LoLDataHarvester {

    private static String api_key = "";
    private static String region = "euw1";
    private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"};
    private static String[] divisions = {"I","II","III","IV"};

    public static void main(String[] args) throws IOException {

        //leaderboardTest();
        //appendBracketsWithIDs();
        //getMatchHistoryByAccountID();
        //challengerData();
        matchidtest();
        //championTest();

    }

    public static void championTest()throws IOException{
        System.out.println("ChampionTest");
        champions champs = new champions();
        champs.getData();
    }

    public static void leaderboardTest()  throws IOException {
        System.out.println("LeaderboardTest");
        leagueV4EntriesQueTierDivision test = new leagueV4EntriesQueTierDivision(divisions,tiers,api_key,region);
    }

    public static void getMatchHistoryByAccountID() throws IOException{
        System.out.println("MatchHistoryTest");
        MatchHistory matchHistory = new MatchHistory(divisions,tiers,api_key,region);
        matchHistory.getdata();
    }

    public static void matchidtest() throws IOException {
        System.out.println("MatchID test");
        matchv4MatchesByMatchID test = new matchv4MatchesByMatchID(api_key,region);
        test.writeToOneCSV();
    }

    public static void challengerData() throws  IOException{
        challengerSoloQue5x5 output = new challengerSoloQue5x5( "LoLparser/CSVs/PlayersPerBracket/");
        output.getData(region,api_key);
    }

    public static void appendBracketsWithIDs() throws IOException{
        System.out.println("Append with IDs test");
        appendCSVs test = new appendCSVs(api_key,region);
        test.getAllIDs();
    }
}
