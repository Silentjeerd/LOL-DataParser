package com.LoLDataHarvester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class fillDatabase {

    private databaseConnection dbConn;
    private String[] csvURLs =
            {
            "LoLparser/CSVs/AllMatchesBans.csv",
            "LoLparser/CSVs/AllMatchHistory.csv",
            "LoLparser/CSVs/AllParticipantData.csv",
            "LoLparser/CSVs/AllParticipantIDs.csv",
            "LoLparser/CSVs/AllParticipantTeamData.csv",
            "LoLparser/CSVs/AllPlayerMasteries.csv",
            "LoLparser/CSVs/AllPlayers.csv",
            "LoLparser/CSVs/AllPlayersWithIDs.csv"
            };

    public fillDatabase(databaseConnection dbConn){
        this.dbConn = dbConn;
    }

    public void run(){
        // TODO run commands here
        fillSummonerTable();
        //fillChampionTable()
        //fillTeamDataTable();
        //fillChampionMasteryTable();

        // TODO tables to make
        //fillMatchHistoryTable();
        //fillTeamTable();
    }

    /**
     * function to fill the summonerTable
     */
    private void fillSummonerTable(){
        // Select right url for array
        String csvUrl = csvURLs[7];
        int amountOfValues = 12;

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

            lineReader.readLine();

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
                stmt.setFloat(6,Float.parseFloat(data[6]));  // SummonerLevel missing ???
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

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // TODO: Fix number of data[]'s and csvURL and amount of values
    private void fillChampionTable(){
        // Select right url for array
        String csvUrl = csvURLs[7];
        int amountOfValues = 12;

        String sql_INSERT = "INSERT INTO CHAMPION" +
                "(ChampionID,Name,PrimaryClass,SecondaryClass)" +
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
                stmt.setFloat(1,Float.parseFloat(data[17]));    // ChampionID
                stmt.setString(2,data[4]);                      // Name
                stmt.setString(3,data[5]);                      // PrimaryClass
                stmt.setString(4,data[3]);                         // SecondaryClass

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

    // TODO: Test
    private void fillTeamDataTable(){
        // Select right url for array
        String csvUrl = csvURLs[5];
        int amountOfValues = 15;

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
                stmt.setFloat(2,Float.parseFloat(data[0]));  // MatchID
                stmt.setFloat(3,Float.parseFloat(data[1]));  // TeamID
                stmt.setString(4,data[2]);  // Win
                stmt.setBoolean(5,parseBoolean(data[3]));  // firstBloodTeam
                stmt.setBoolean(6,parseBoolean(data[4]));  // firstRiftTeam
                stmt.setFloat(7,Float.parseFloat(data[6]));  // countRift
                stmt.setBoolean(8,parseBoolean(data[3]));  // firstBaronTeam
                stmt.setFloat(9,Float.parseFloat(data[7]));  // countBaron
                stmt.setBoolean(10,parseBoolean(data[3]));  // firstDragonTeam
                stmt.setFloat(11,Float.parseFloat(data[9]));  // countDragon
                stmt.setBoolean(12,parseBoolean(data[11])); // firstInhibitorTeam
                stmt.setFloat(13,Float.parseFloat(data[11])); // countInhibitor
                stmt.setBoolean(14,parseBoolean(data[11])); // firstTowerTeam
                stmt.setFloat(15,Float.parseFloat(data[11])); // countTower

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

    // TODO: Fix csv source and numbers of data[]'s
    private void fillChampionMasteryTable(){
        // Select right url for array
        String csvUrl = csvURLs[5];
        int amountOfValues = 12;

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
                stmt.setBoolean(6,parseBoolean(data[4]));       // SummonerLevel
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
}
