import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LoLDataHarvester {

    private static String api_key = "";
    private static String region = "euw1";
    private static String outputFilePath;
    private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"};
    private static String[] divisions = {"I","II","III","IV"};

    public static void main(String[] args) throws IOException {

        outputFilePath  = Paths.get("").toRealPath().getParent().toString() + "/CSVs/";
        challengerData(region,api_key);
        //matchidtest();
        //championTest();
        //leaderboardTest();
        //appendBracketsWithIDs();
    }

    public static void championTest()throws IOException{
        champions champs = new champions(outputFilePath);
        champs.getData();
    }

    public static void leaderboardTest()  throws IOException {
        leagueV4EntriesQueTierDivision test = new leagueV4EntriesQueTierDivision(outputFilePath,divisions,tiers,api_key,region);
    }

    public static void matchidtest() throws IOException {
        matchv4MatchesByMatchID test = new matchv4MatchesByMatchID(outputFilePath);
        String id = "4433359617";
        test.getData(region,id,api_key);
    }

    public static void challengerData(String region,String api_key) throws  IOException{
        challengerSoloQue5x5 output = new challengerSoloQue5x5(outputFilePath);
        output.getData(region,api_key);
    }

    public static void appendBracketsWithIDs() throws IOException{
        appendCSVs test = new appendCSVs(outputFilePath,divisions,tiers,api_key,region);
        test.getAllAccountIDsTierDivision();
    }
}
