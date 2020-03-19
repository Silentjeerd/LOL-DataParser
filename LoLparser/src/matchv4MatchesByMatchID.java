import java.io.*;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.io.FileUtils;

public class matchv4MatchesByMatchID {

    private String apiKey,region;
    private String fileToWrite1,fileToWrite2,fileToWrite3,fileToGetAll;

    public matchv4MatchesByMatchID(String apiKey,String region) throws IOException {
        this.region = region;
        this.apiKey = apiKey;

        this.fileToWrite1 = "LoLparser/CSVs/MatchDataByMatchID/AllParticipantIDs.csv";
        this.fileToWrite2 = "LoLparser/CSVs/MatchDataByMatchID/AllParticipantTeamData.csv";
        this.fileToWrite3 = "LoLparser/CSVs/MatchDataByMatchID/AllParticipantData.csv";

        this.fileToGetAll = "LoLparser/CSVs/MatchHistory/AllMatchHistory.csv";
    }

    private JSONObject getData(String matchID) throws IOException {
        parser.sleep(1500); //slaapt 1.5seconden vanwege api limitaties.
        String urlWhole = "https://" + region + ".api.riotgames.com/lol/match/v4/matches/" + matchID + "?api_key=" + apiKey;
        JSONObject obj = new JSONObject(parser.returnJsonStringFromUrl(urlWhole,""));

        return obj;
    }

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

    private String participantsIDsToWrite(JSONObject object){
        int participantId = object.getInt("participantId");
        JSONObject playerData = object.getJSONObject("player");
        String accountId = playerData.getString("accountId");
        String regionId = playerData.getString("platformId");
        System.out.println(participantId + " " + accountId + " " + regionId);
        return participantId + "," + accountId + "," + regionId;
    }

    private String participantsDataToWrite(JSONObject object){

        return "";
    }

    public void writeToOneCSV() throws IOException{
        final String lineSep=System.getProperty("line.separator");
        int lineCountAll = parser.countLines(fileToGetAll);
        BufferedReader fileReaderMatchIDs = new BufferedReader(new FileReader(fileToGetAll));
        BufferedWriter fileWriter1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite1))); //open de writer
        BufferedWriter fileWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite2))); //open de writer
        BufferedWriter fileWriter3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite3))); //open de writer


        String readerLine = null;
        String[] readerTokens;
        int count = 0;

        //moet line != null zijn, voor test purpose i < 3
        for ( readerLine = fileReaderMatchIDs.readLine(); count < 2; readerLine = fileReaderMatchIDs.readLine(),count++)
        {
            if(count > 0){
            readerTokens = readerLine.split(",");
            String matchID = readerTokens[1].replace(".","").replace("E9","");
            if(matchID.length() == 9)
                matchID = matchID + "0";

            JSONObject obj = getData(matchID);
            JSONArray identities = obj.getJSONArray("participantIdentities");
            for(int i = 0 ; i < 10 ; i++){
                JSONObject object = identities.getJSONObject(i);
                String toWrite = matchID + "," + participantsIDsToWrite(object);
                fileWriter1.write(toWrite+lineSep);
            }

            JSONArray teamData = obj.getJSONArray("teams");
            for(int i = 0;i <= 1;i++){
                JSONObject object = teamData.getJSONObject((i));
                String toWrite = matchID + "," + teamDataToWrite(object);
                fileWriter2.write(toWrite+lineSep);
            }

            JSONArray playerStats = obj.getJSONArray("participants");
            for(int i = 0; i < 10;i++){
                JSONObject object = playerStats.getJSONObject(i);
                String toWrite = matchID + "," + participantsDataToWrite(object);
                fileWriter3.write(toWrite+lineSep);
            }

        }else{
                fileWriter1.write("matchID,participantID,accountID,originalRegion"+lineSep);
                fileWriter2.write("matchID,teamID,win,firstBlood,firstRiftHerald,riftHeraldKills,firstBaron,baronKills," +
                                      "firstDragon,dragonKills,firstInhib,inhibKills,firstTower,towerKills"+lineSep);
                //fileWriter3.write();
        }

        System.out.println("Done with: " + (count+1) + " out of " + lineCountAll);
        }

        fileWriter1.close(); //sluit de reader en writer.
        fileWriter2.close();
        fileWriter3.close();
        fileReaderMatchIDs.close();
    }
}
