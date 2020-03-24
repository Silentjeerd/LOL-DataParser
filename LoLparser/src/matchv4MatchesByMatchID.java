import java.io.*;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jdk.jshell.spi.ExecutionControlProvider;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.io.FileUtils;

public class matchv4MatchesByMatchID {

    private String apiKey,region;
    private String fileToWrite1,fileToWrite2,fileToWrite3,fileToWrite4,fileToGetAll;

    public matchv4MatchesByMatchID(String apiKey,String region) throws IOException {
        this.region = region;
        this.apiKey = apiKey;

        //De CSV filepaths waarin de data moet worden opgeslagen.
        this.fileToWrite1 = "LoLparser/CSVs/AllParticipantIDs.csv";
        this.fileToWrite2 = "LoLparser/CSVs/AllParticipantTeamData.csv";
        this.fileToWrite3 = "LoLparser/CSVs/AllParticipantData.csv";
        this.fileToWrite4 = "LoLparser/CSVs/AllMatchesBans.csv";
        //De CSV file waaruit de individuele matchID's moeten worden gehaald voor de URL voor het HTTP request.
        this.fileToGetAll = "LoLparser/CSVs/AllMatchHistory.csv";
    }

    //Deze functie haalt alle teamData uit het JSONObject dat hij meekrijgt.
    private String teamDataToWrite(JSONObject object){
        int teamId = object.getInt("teamId");
        String win = object.getString("win");
        boolean firstBlood = object.getBoolean("firstBlood");
        boolean firstRift = object.getBoolean("firstRiftHerald");
        int riftHeraldKills = object.getInt("riftHeraldKills");
        boolean firstBaron = object.getBoolean("firstBaron");
        int baronKills = object.getInt("baronKills");
        boolean firstDragon = object.getBoolean("firstDragon");
        int dragonKills = object.getInt("dragonKills");
        boolean firstInhib = object.getBoolean("firstInhibitor");
        int inhibKills = object.getInt("inhibitorKills");
        boolean firstTower = object.getBoolean("firstTower");
        int towerKills = object.getInt("towerKills");

        return teamId + "," + win + "," + firstBlood + "," + firstRift + "," + riftHeraldKills + "," +
                firstBaron + "," + baronKills + "," + firstDragon + "," + dragonKills + "," +
                firstInhib + "," + inhibKills + "," + firstTower + "," + towerKills;
    }

    //Deze functie haalt alle participant identities uit het JSONObject.
    private String participantsIDsToWrite(JSONObject object){
        int participantId = object.getInt("participantId");
        JSONObject playerData = object.getJSONObject("player");
        String accountId = playerData.getString("accountId");
        String regionId = playerData.getString("platformId");
        return participantId + "," + accountId + "," + regionId;
    }

    //Deze functie haalt alle participant data uit het JSONObject.
    private String participantsDataToWrite(JSONObject object){
        int participantId = object.getInt("participantId");
        int championId = object.getInt("championId");
        int teamId = object.getInt("teamId");
        int spell1 = object.getInt("spell1Id");
        int spell2 = object.getInt("spell2Id");
        JSONObject playerStats = object.getJSONObject("stats");
        boolean firstBlood = playerStats.getBoolean("firstBloodKill");
        boolean firstInhibitor;
        try{
            firstInhibitor = playerStats.getBoolean("firstInhibitorKill");
        }catch (Exception e){
            firstInhibitor = false;
        }
        boolean firstTower = playerStats.getBoolean("firstTowerKill");
        int goldEarned = playerStats.getInt("goldEarned");
        int creepKills = playerStats.getInt("totalMinionsKilled");
        int kills = playerStats.getInt("kills");
        int deaths = playerStats.getInt("deaths");
        int assists = playerStats.getInt("assists");
        int item0 = playerStats.getInt("item0");
        int item1 = playerStats.getInt("item1");
        int item2 = playerStats.getInt("item2");
        int item3 = playerStats.getInt("item3");
        int item4 = playerStats.getInt("item4");
        int item5 = playerStats.getInt("item5");
        int item6 = playerStats.getInt("item6");
        long visionScore = playerStats.getLong("visionScore");

        return  participantId + "," + championId + "," + teamId + "," + spell1 + "," + spell2 + "," +
                firstBlood + "," + firstInhibitor + "," + firstTower + "," + goldEarned + "," + creepKills + "," +
                kills + "," + deaths + "," + assists + "," + item0 + "," + item1 + "," + item2 + "," + item3 + "," +
                item4 + "," + item5 + "," + item6 + "," + visionScore;
    }

    private String banData(JSONObject object,String matchID){
        final String lineSep=System.getProperty("line.separator");
        JSONArray bannedChamps = object.getJSONArray("bans");
        String bansToWrite = "";
            for(int i = 0 ; i < 5 ; i++){
                try{
                    JSONObject banLoop = bannedChamps.getJSONObject(i);
                    int champID = banLoop.getInt("championId");
                    bansToWrite = bansToWrite + matchID + "," + champID + lineSep;
                }catch (Exception e){
                }
            }
        return bansToWrite;
    }

    //Hoofdfunctie die alle gevraagde data wegschrijft in de CSV files.
    public void writeToOneCSV() throws IOException{
        final String lineSep=System.getProperty("line.separator");
        int lineCountAll = parser.countLines(fileToGetAll);
        BufferedReader fileReaderMatchIDs = new BufferedReader(new FileReader(fileToGetAll)); //Opent de reader waar de matchIDs uit worden gelezen.
        BufferedWriter fileWriter1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite1))); //open de writer
        BufferedWriter fileWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite2))); //open de writer
        BufferedWriter fileWriter3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite3))); //open de writer
        BufferedWriter fileWriter4 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite4))); //open de writer

        List<String> matchesToCheck = new ArrayList<String>();

        String readerLine = null;
        String[] readerTokens;
        int count = 0;

        for ( readerLine = fileReaderMatchIDs.readLine(); count < 3; readerLine = fileReaderMatchIDs.readLine(),count++){
            readerTokens = readerLine.split(",");
            String matchID = readerTokens[1].replace(".","").replace("E9","");
            if(count > 0){
                if(matchID.length() == 9) matchID = matchID + "0";
                if(!matchesToCheck.contains(matchID)) matchesToCheck.add(matchID);
            }
        }

        fileWriter1.write("matchID,participantID,accountID,originalRegion"+lineSep);
        fileWriter2.write("matchID,teamID,win,firstBlood,firstRiftHerald,riftHeraldKills,firstBaron,baronKills," +
                "firstDragon,dragonKills,firstInhib,inhibKills,firstTower,towerKills,matchDurationSeconds"+lineSep);
        fileWriter3.write("matchID,participantID,championId,teamId,spell1,spell2,firstBlood,firstInhib,firstTower,goldEarned," +
                "creepKills,kills,deaths,assists,item0,item1,item2,item3,item4,item5,item6,visionScore"+lineSep);
        fileWriter4.write("matchID,bannedChampion"+lineSep);
        int matchCount = matchesToCheck.size();
        int counter = 0;
        for (String matchID:matchesToCheck) {

            int tryCount = 0;
            int maxTries = 5;

            while(tryCount < maxTries){
                try{
                        parser.sleep(1500); //slaapt 1.5seconden vanwege api limitaties.
                        String urlWhole = "https://" + region + ".api.riotgames.com/lol/match/v4/matches/" + matchID + "?api_key=" + apiKey;
                        JSONObject obj = new JSONObject(parser.returnJsonStringFromUrl(urlWhole,""));
                        JSONArray identities = obj.getJSONArray("participantIdentities"); //Dit haalt een subArray uit het JSONObject.
                        // Elke match heeft 10 participants dus loopen we voor elke participant door de array.
                        for(int i = 0 ; i <= 9 ; i++){
                            JSONObject object = identities.getJSONObject(i); //Dit pakt een JSONObject uit het array.
                            String toWrite = matchID + "," + participantsIDsToWrite(object); //Haalt de data die in de CSV moet komen op als 1 string en voegt het matchID eraantoe.
                            fileWriter1.write(toWrite+lineSep); //Schrijft de regel weg in het CSV bestand.
                        }

                        JSONArray teamData = obj.getJSONArray("teams");
                        Long matchDuration = obj.getLong("gameDuration");

                        for(int i = 0;i <= 1;i++){
                            JSONObject object = teamData.getJSONObject(i);

                            String toWrite2 = matchID + "," + teamDataToWrite(object) + "," + matchDuration;
                            fileWriter2.write(toWrite2+lineSep);

                            String toWrite4 = banData(object,matchID);
                            fileWriter4.write(toWrite4);
                        }

                        JSONArray playerStats = obj.getJSONArray("participants");
                        for(int i = 0; i <= 9;i++){
                            JSONObject object = playerStats.getJSONObject(i);
                            String toWrite = matchID + "," + participantsDataToWrite(object);
                            fileWriter3.write(toWrite+lineSep);
                        }
                    counter++;
                    System.out.println("Done with: " + counter + " out of " + matchCount);
                    tryCount = maxTries;
                }catch (Exception e){
                    parser.sleep(2000);
                    tryCount++;
                    if(tryCount == maxTries) System.out.println(e.toString());
                }
            }
        }

        //Sluit alle Writers en de reader.
        fileWriter1.close();
        fileWriter2.close();
        fileWriter3.close();
        fileWriter4.close();
        fileReaderMatchIDs.close();
    }
}

