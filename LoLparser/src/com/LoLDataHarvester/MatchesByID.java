package com.LoLDataHarvester;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

import com.LoLDataHarvester.StrategyPattern.Strategy;
import org.json.JSONArray;
import org.json.JSONObject;

public class MatchesByID implements Strategy {

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
        //De CSV filepaths waarin de data moet worden opgeslagen.
        String fileToWrite2 = "LoLparser/CSVs/AllParticipantTeamData.csv";
        String fileToWrite3 = "LoLparser/CSVs/AllParticipantData.csv";
        String fileToWrite4 = "LoLparser/CSVs/AllMatchesBans.csv";
        //De CSV file waaruit de individuele matchID's moeten worden gehaald voor de URL voor het HTTP request.
        String fileToGetAll = "LoLparser/CSVs/AllMatchHistory.csv";
        final String lineSep=System.getProperty("line.separator");
        int lineCountAll = Parser.countLines(fileToGetAll);
        System.out.println(lineCountAll);

        boolean firstRun = !doesFileExists(fileToWrite2);

        BufferedReader fileReaderMatchIDs = new BufferedReader(new FileReader(fileToGetAll)); //Opent de reader waar de matchIDs uit worden gelezen.
        BufferedWriter fileWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite2,true))); //open de writer
        BufferedWriter fileWriter3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite3,true))); //open de writer
        BufferedWriter fileWriter4 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite4,true))); //open de writer


        if(firstRun){
            fileWriter2.write("matchID,teamID,win,firstBlood,firstRiftHerald,riftHeraldKills,firstBaron,baronKills," +
                    "firstDragon,dragonKills,firstInhib,inhibKills,firstTower,towerKills,matchDurationSeconds"+lineSep);
            fileWriter3.write("matchID,accountID,participantID,championId,teamId,spell1,spell2,firstBlood,firstInhib,firstTower,goldEarned," +
                    "creepKills,kills,deaths,assists,item0,item1,item2,item3,item4,item5,item6,visionScore"+lineSep);
            fileWriter4.write("matchID,bannedChampion"+lineSep);
            fileWriter2.flush();
            fileWriter3.flush();
            fileWriter4.flush();
        }

        List<String> checkedMatches = getMatchList(fileToWrite2,0);
        System.out.println("Check matches: " + checkedMatches.size());
        List<String> matchesToCheck = getMatchList(fileToGetAll,1);
        System.out.println("matches to check: " + matchesToCheck.size());

        matchesToCheck.removeAll(checkedMatches);


        int matchCount = matchesToCheck.size();
        System.out.println("Matches remaining: " + matchCount);

        int counter = 0;
        for (String matchID:matchesToCheck) {
            System.out.println(matchID);

            int tryCount = 0;
            int maxTries = 5;

            while(tryCount < maxTries){
                try{
                    Parser.sleep(1500); //slaapt 1.5seconden vanwege api limitaties.
                    String urlWhole = "https://" + region + ".api.riotgames.com/lol/match/v4/matches/" + matchID + "?api_key=" + api_key;
                    JSONObject obj = new JSONObject(Parser.returnJsonStringFromUrl(urlWhole,""));

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
                    JSONArray identities = obj.getJSONArray("participantIdentities");
                    for(int i = 0; i <= 9;i++){
                        JSONObject object1 = identities.getJSONObject(i); //Dit pakt een JSONObject uit het array.
                        String accountID = participantsIDsToWrite(object1);
                        JSONObject object = playerStats.getJSONObject(i);
                        String toWrite = matchID + "," + accountID + "," + participantsDataToWrite(object);
                        fileWriter3.write(toWrite+lineSep);
                    }

                    counter++;
                    System.out.println("Done with: " + counter + " out of " + matchCount);
                    tryCount = maxTries;

                }catch (Exception e){
                    Parser.sleep(2000);
                    tryCount++;
                    if(tryCount == maxTries)
                        counter++;
                    System.out.println(e.toString());
                }
            }

            if(counter % 20 == 0){
                System.out.println("Flushing");
                fileWriter2.flush();
                fileWriter3.flush();
                fileWriter4.flush();
            }
        }

        //Sluit alle Writers en de reader.
        fileWriter2.close();
        fileWriter3.close();
        fileWriter4.close();
        fileReaderMatchIDs.close();
    }

    /**
     * Deze functie haalt alle teamData uit het JSONObject dat hij meekrijgt.
     * @param object Het JSONObject dat moet worden ontleed.
     * @return Een string met daarin alle data die we weg willen schrijven naar een .CSV bestand
     */
    private String teamDataToWrite(JSONObject object){
        int teamId = object.getInt("teamId");
        String win = object.getString("win");
        boolean firstBlood = object.getBoolean("firstBlood");
        boolean firstRift;
        try{
            firstRift = object.getBoolean("firstRiftHerald");
        }catch (Exception e){
            firstRift = false;
        }
        int riftHeraldKills;
        try{
            riftHeraldKills = object.getInt("riftHeraldKills");
        }catch(Exception e){
            riftHeraldKills = 0;
        }
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

    /**
     * Deze functie haalt het goede accountID op dat bij de speler hoort.
     * @param object Het JSONObject dat moet worden ontleed.
     * @return Een string met daarin alle data die we weg willen schrijven naar een .CSV bestand
     */
    private String participantsIDsToWrite(JSONObject object){
        JSONObject playerData = object.getJSONObject("player");
        String accountId = playerData.getString("currentAccountId");
        return accountId;
    }

    /**
     * Deze functie haalt alle data per speler uit het JSONObject dat hij meekrijgt.
     * @param object Het JSONObject dat moet worden ontleed.
     * @return Een string met daarin alle data die we weg willen schrijven naar een .CSV bestand
     */
    //Deze functie haalt alle participant data uit het JSONObject.
    private String participantsDataToWrite(JSONObject object){
        int participantId = object.getInt("participantId");
        int championId = object.getInt("championId");
        int teamId = object.getInt("teamId");
        int spell1 = object.getInt("spell1Id");
        int spell2 = object.getInt("spell2Id");
        JSONObject playerStats = object.getJSONObject("stats");
        boolean firstBlood;
        try{
            firstBlood = playerStats.getBoolean("firstBloodKill");
        }catch (Exception e){
            firstBlood = false;
        }
        boolean firstInhibitor;
        try{
            firstInhibitor = playerStats.getBoolean("firstInhibitorKill");
        }catch (Exception e){
            firstInhibitor = false;
        }
        boolean firstTower;
        try{
            firstTower = playerStats.getBoolean("firstTowerKill");
        }catch (Exception e){
            firstTower = false;
        }
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

    /**
     * Deze functie haalt de gebannde champions op per match.
     * @param object Het JSONObject dat moet worden ontleed.
     * @param matchID Het matchID waar het JSONObject bijhoort,
     *                dit ID zit erbij omdat deze data in een apart .CSV bestand word gezet.
     * @return Een String die weg moet worden geschreven naar een .CSV bestand.
     */
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

    /**
     * Deze functie haalt een lijst op met matches vanuit een .CSV bestand
     * @param file Het bestand dat moet worden gelezen.
     * @param matchIDColumn De index van de kolom waarin het matchID zicht bevind.
     * @return Dit geeft een Lijst terug van alle matchID's in het gelezen .CSV bestand.
     * @throws IOException
     */
    private List<String> getMatchList(String file,int matchIDColumn) throws IOException {
        List<String> checkedMatches = new ArrayList<>();
        final String lineSep=System.getProperty("line.separator");
        BufferedReader fileReaderMatchIDs = new BufferedReader(new FileReader(file));
        String readerLine = null;
        String[] readerTokens;
        int count = 0;

        for (readerLine = fileReaderMatchIDs.readLine(); readerLine != null; readerLine = fileReaderMatchIDs.readLine(),count++){
            readerTokens = readerLine.split(",");

            String matchID = readerTokens[matchIDColumn].replace(".","").replace("E9","");
            matchID = matchID.replace(".","").replace("E8","");

            if(count > 0){
                while(matchID.length() < 10){
                    matchID = matchID + "0";
                }
                if(matchIDColumn == 1 && count % 50 == 0){
                    if(!checkedMatches.contains(matchID)) checkedMatches.add(matchID);
                }
                if(matchIDColumn == 0){
                    if(!checkedMatches.contains(matchID)) checkedMatches.add(matchID);
                }
            }
        }

        return checkedMatches;
    }

    /**
     * Deze functie kijkt of een bestand al bestaat,
     * is dit het geval dan hoeft deze niet opnieuw aangemaakt te worden.
     * @param file Het pad van het bestand waar op gecheckt word.
     * @return Een boolean die aangeeft of de file bestaat of niet.
     */
    private boolean doesFileExists(String file){
        File tempDir = new File(file);
        return tempDir.exists();
    }



}

