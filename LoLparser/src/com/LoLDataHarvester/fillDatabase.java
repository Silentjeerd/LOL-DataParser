package com.LoLDataHarvester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class fillDatabase {

    private databaseConnection dbConn;

    public fillDatabase(databaseConnection dbConn){
        this.dbConn = dbConn;
    }

    public void run(){
        fillSummonerTable();
        fillTeamDataTable();
        fillItemsTable();
        fillSpellsTable();
        fillChampionTable();
        fillMatchHistoryTable();
        updateMatchHistoryTable();
        fillChampionMasteryTable();

        /** niet nodig?
         *
         *         //fillTeamTable();
         */
    }

    /**
     * function to fill the summonerTable
     */
    private void fillSummonerTable(){
        // Select right url for array
        String csvUrl = "LoLparser/CSVs/AllPlayersWithIDs.csv";

        String sql_INSERT = "INSERT INTO SUMMONER" +
                "(AccountID,SummonerID,Name,Rank,Tier,SummonerLevel,LeaguePoints," +
                "TotalGamesPlayed,Wins,Losses,Veteran,FreshBlood)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();// Skip the first line

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setString(1,data[17]); // AccountID
                stmt.setString(2,data[4]);  // SummonerID
                stmt.setString(3,data[5]);  // Name
                stmt.setString(4,data[3]);  // Rank
                stmt.setString(5,data[2]);  // Tier
                stmt.setFloat(6,Float.parseFloat(data[18]));  // SummonerLevel
                stmt.setFloat(7,Float.parseFloat(data[6]));  // LeaguePoints
                stmt.setFloat(8,Float.parseFloat(data[8])+ Float.parseFloat(data[7]));  // TotalGamesPlayedPoints
                stmt.setFloat(9,Float.parseFloat(data[7]));  // Wins
                stmt.setFloat(10,Float.parseFloat(data[8]));  // Losses
                stmt.setBoolean(11,parseBoolean(data[9]));  // Veteran
                stmt.setBoolean(12,parseBoolean(data[11])); // Freshblood
                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();
            System.out.println("Summoner table filled");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillChampionTable(){
        // Select right url for array
        String csvUrl = "LoLparser/CSVs/Champions.csv";

        String sql_INSERT = "INSERT INTO CHAMPION" +
                "(ChampionID,Name) VALUES(?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(1,Float.parseFloat(data[17]));    // ChampionID
                stmt.setString(2,data[4]);                      // Name

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillTeamDataTable(){
        String csvUrl = "LoLparser/CSVs/AllParticipantTeamData.csv";

        String sql_INSERT = "INSERT INTO teamData" +
                "(MachtTeamID,MatchID,TeamID,Win,firstBloodTeam,firstRiftTeam," +
                "countRift,firstBaronTeam,countBaron,firstDragonTeam,countDragon," +
                "firstInhibitorTeam,countInhibitor,firstTowerTeam,countTower)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(1,Float.parseFloat(data[0]+data[1])); // MachtTeamID
                stmt.setFloat(2,Float.parseFloat(data[0]));           // MatchID
                stmt.setFloat(3,Float.parseFloat(data[1]));           // TeamID
                stmt.setString(4,data[2]);                            // Win
                stmt.setBoolean(5,parseBoolean(data[3]));             // firstBloodTeam
                stmt.setBoolean(6,parseBoolean(data[4]));             // firstRiftTeam
                stmt.setFloat(7,Float.parseFloat(data[6]));           // countRift
                stmt.setBoolean(8,parseBoolean(data[3]));             // firstBaronTeam
                stmt.setFloat(9,Float.parseFloat(data[7]));           // countBaron
                stmt.setBoolean(10,parseBoolean(data[3]));            // firstDragonTeam
                stmt.setFloat(11,Float.parseFloat(data[9]));          // countDragon
                stmt.setBoolean(12,parseBoolean(data[11]));           // firstInhibitorTeam
                stmt.setFloat(13,Float.parseFloat(data[11]));         // countInhibitor
                stmt.setBoolean(14,parseBoolean(data[11]));           // firstTowerTeam
                stmt.setFloat(15,Float.parseFloat(data[11]));         // countTower

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillChampionMasteryTable(){
        String csvUrl = "LoLparser/CSVs/AllPlayerMasteries.csv";

        String sql_INSERT = "INSERT INTO CHAMPIONMASTERY" +
                "(ChampionID,AccountID,Name,Rank,Tier,SummonerLevel," +
                "LeaguePoints,TotalGamesPlayed,Wins,Losses,Veteran,FreshBlood)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(1,Float.parseFloat(data[0]));     // ChampionID
                stmt.setFloat(2,Float.parseFloat(data[0]));     // AccountID
                stmt.setFloat(3,Float.parseFloat(data[1]));     // Name
                stmt.setString(4,data[2]);                      // Rank
                stmt.setBoolean(5,parseBoolean(data[3]));       // Tier
                stmt.setBoolean(6,parseBoolean(data[6]));       // SummonerLevel
                stmt.setFloat(7,Float.parseFloat(data[6]));     // LeaguePoints
                stmt.setBoolean(8,parseBoolean(data[3]));       // TotalGamesPlayed
                stmt.setFloat(9,Float.parseFloat(data[7]));     // Wins
                stmt.setBoolean(10,parseBoolean(data[3]));      // Losses
                stmt.setFloat(11,Float.parseFloat(data[9]));    // Veteran

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillItemsTable(){
        String csvUrl = "LoLparser/CSVs/Items.csv";

        String sql_INSERT = "INSERT INTO ITEM" +
                "(ItemID,Name) VALUES(?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(1,Float.parseFloat(data[0]));     // SpellID
                stmt.setString(2,data[0]);                      // Name

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillSpellsTable(){
        String csvUrl = "LoLparser/CSVs/spells.csv";

        String sql_INSERT = "INSERT INTO SPELL" +
                "(SpellID,Name) VALUES(?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(1,Float.parseFloat(data[0]));     // SpellID
                stmt.setString(2,data[0]);                      // Name

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillMatchHistoryTable(){
        String csvUrl = "LoLparser/CSVs/AllMatchHistory.csv";

        String sql_INSERT = "INSERT INTO MATCHHISTORY" +
                "(MatchID,ChampionID,AccountID,Lane,Role,Region)" +
                "VALUES(?,?,?,?,?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(1,Float.parseFloat(data[0]));      // MatchID
                stmt.setFloat(2,Float.parseFloat(data[3]));      // ChampionID
                stmt.setString(3,data[2]);                       // AccountID
                stmt.setString(4,data[7]);                       // Lane mist
                stmt.setString(5,data[2]);                       // Role mist
                stmt.setString(6,data[4]);                       // Region mist

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateMatchHistoryTable(){
        String csvUrl = "LoLparser/CSVs/AllParticipantData.csv"; // AllParticipantData.csv

        String sql_UPDATE = "UPDATE MATCHHISTORY SET " +
                "Spell1=?, Spell2=?, FirstBlood=?, FirstInhibitor=?, FirstTower=?," +
                "GoldEarned=?,CreepKills=?,PlayerKills=?,PlayerAssists=?,Item0=?," +
                "Item1=?,Item2=?,Item3=?,Item1=?,Item5=?,Item6=?" +
                "WHERE MatchID == ? AND AccountID == ?";

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_UPDATE);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(7,Float.parseFloat(data[5]));      // Spell1
                stmt.setFloat(8,Float.parseFloat(data[6]));      // Spell2
                stmt.setBoolean(9,parseBoolean(data[7]));        // FirstBlood
                stmt.setBoolean(10,parseBoolean(data[8]));       // FirstInhibitor
                stmt.setBoolean(11,parseBoolean(data[9]));       // FirstTower
                stmt.setFloat(12,Float.parseFloat(data[10]));     // GoldEarned
                stmt.setFloat(13,Float.parseFloat(data[11]));    // CreepKills
                stmt.setFloat(14,Float.parseFloat(data[12]));    // PlayerKills
                stmt.setFloat(15,Float.parseFloat(data[14]));    // PlayerAssists
                stmt.setFloat(16,Float.parseFloat(data[15]));    // Item0
                stmt.setFloat(17,Float.parseFloat(data[16]));    // Item1
                stmt.setFloat(18,Float.parseFloat(data[17]));    // Item2
                stmt.setFloat(19,Float.parseFloat(data[18]));    // Item3
                stmt.setFloat(20,Float.parseFloat(data[19]));    // Item4
                stmt.setFloat(21,Float.parseFloat(data[20]));    // Item5
                stmt.setFloat(22,Float.parseFloat(data[21]));    // Item6

                // WHERE clause
                stmt.setString(17,data[0]);                      // MatchID
                stmt.setFloat(18,Float.parseFloat(data[1]));     // AccountID

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //TODO test if needed or not
    private void fillTeamTable(){
        String csvUrl = "AllParticipantTeamData";

        String sql_INSERT = "INSERT INTO TEAM" +
                "(MatchID,AccountID,MachtTeamID,ChampionID)" +
                "VALUES(?,?,?,?)"; // a ? is a placeholder we will fill later

        dbConn.connectToDatabaseServer();
        try{
            // This statment we will fill with the right values
            PreparedStatement stmt = dbConn.getConn().prepareStatement(sql_INSERT);
            // This will read line by line through the CSV file
            BufferedReader lineReader = new BufferedReader(new FileReader(csvUrl));
            String lineText = null;

            lineReader.readLine();

            // While the current line isnt null read csv line by line
            while((lineText = lineReader.readLine()) != null){
                // This holds the data and we tell it to fill the columns and split them by ,
                String[] data = lineText.split(",");
                // Put the right csv value with te right placeholder
                stmt.setFloat(1,Float.parseFloat(data[1]));     // MatchID
                stmt.setFloat(2,Float.parseFloat(data[5]));     // AccountID
                stmt.setString(3,data[0]);                      // MatchTeamID
                stmt.setString(4,data[7]);                      // ChampionID

                // We will execute when all lines are read
                stmt.addBatch();
            }

            lineReader.close();
            // Execute all sql statements
            stmt.executeBatch();
            dbConn.getConn().close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
