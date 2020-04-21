package com.LoLDataHarvester;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class createDatabase {

    private databaseConnection dbConn;

    public createDatabase(String user, String password, String databaseName , int port, String ipAdress){
        this.dbConn = new databaseConnection(user, password, databaseName, port, ipAdress);

        dropAllTables();

        if(!tablesAreMade()){
            System.out.println("Tables are not made yet");
            createChampionsTable();
            createSummonerTable();
            createChampionMasteryTable();
            createMatchHistoryTable();
            createTeamDataTable();
            //createTeamTable();
            createSpellTable();
            createItemTable();
            System.out.println("Tables are made!");
        }else{
            System.out.println("Tables are already made!");
        }
    }

    public databaseConnection getDbConn() {
        return dbConn;
    }

    public boolean tablesAreMade(){
        dbConn.connectToDatabaseServer();
        try{
            Statement stmt =dbConn.getConn().createStatement();
            String sql = "SELECT * FROM CHAMPION";
            ResultSet rs = stmt.executeQuery(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void createChampionsTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE CHAMPION " +
                    "(ChampionID       INT           PRIMARY KEY," +
                    "Name              TEXT             NOT NULL " +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created ChampionsTable");
    }

    public void createSummonerTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE SUMMONER " +
                    "(" +
                    " AccountID         TEXT            PRIMARY KEY," +
                    " SummonerID        TEXT            NOT NULL, " +
                    " Name              TEXT            NOT NULL, " +
                    " Rank              TEXT            NOT NULL, " +
                    " Tier              TEXT            NOT NULL, " +
                    " SummonerLevel     INT             NOT NULL, " +
                    " LeaguePoints      INT             NOT NULL, " +
                    " TotalGamesPlayed  INT             NOT NULL, " +
                    " Wins              INT             NOT NULL, " +
                    " Losses            INT             NOT NULL, " +
                    " Veteran           BOOLEAN                 , " +
                    " FreshBlood        BOOLEAN                 " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created SummonerTable");
    }

    public void createChampionMasteryTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE CHAMPIONMASTERY " +
                    "(" +
                    "PRIMARY KEY (ChampionID,AccountID)," +
                    " ChampionID        INT  REFERENCES CHAMPION," +
                    " AccountID         TEXT REFERENCES SUMMONER, " +
                    " Name              TEXT            NOT NULL, " +
                    " Rank              TEXT            NOT NULL, " +
                    " Tier              TEXT            NOT NULL, " +
                    " SummonerLevel     INT             NOT NULL, " +
                    " LeaguePoints      INT             NOT NULL, " +
                    " TotalGamesPlayed  INT             NOT NULL, " +
                    " Wins              INT             NOT NULL, " +
                    " Losses            INT             NOT NULL, " +
                    " Veteran           BOOLEAN                 , " +
                    " FreshBlood        BOOLEAN                 " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created ChampionMasteryTable");
    }

    public void createMatchHistoryTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE MATCHHISTORY " +
                    "(" +
                    " MatchAccountID    TEXT  PRIMARY KEY     , " +
                    " MatchID           TEXT                  , " +
                    " ChampionID        INT  REFERENCES CHAMPION, " +
                    " AccountID         TEXT                    , " +
                    " Lane              TEXT            NOT NULL, " +
                    " Role              TEXT            NOT NULL, " +
                    " Region            TEXT            NOT NULL, " +
                    " Spell1            INT                    , " +
                    " Spell2            INT                     , " +
                    " FirstBlood        BOOLEAN                    , " +
                    " FirstInhibitor    BOOLEAN                    , " +
                    " FirstTower        BOOLEAN                    , " +
                    " GoldEarned        INT                     , " +
                    " CreepKills        INT                     , " +
                    " PlayerKills       INT                     , " +
                    " PlayerAssists     INT                     , " +
                    " Item0             INT                     , " +
                    " Item1             INT                     , " +
                    " Item2             INT                     , " +
                    " Item3             INT                     , " +
                    " Item4             INT                     , " +
                    " Item5             INT                     , " +
                    " Item6             INT                       " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created MatchHistoryTable");
    }

    public void createTeamTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE TEAM " +
                    "(" +
                    " MatchID       TEXT  REFERENCES MATCHHISTORY," +
                    " AccountID     TEXT REFERENCES SUMMONER    ," +
                    " ChampionID    INT  REFERENCES CHAMPION     " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created TeamTable");
    }

    public void createTeamDataTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE TEAMDATA " +
                    "(" +
                    " MatchTeamID        TEXT PRIMARY KEY         , " +
                    " MatchID            TEXT                     , " +
                    " TeamID             INT                     , " +
                    " Win                TEXT                    , " +
                    " firstBloodTeam     BOOLEAN                 , " +
                    " firstRiftTeam      BOOLEAN                 , " +
                    " countRift          INT                     , " +
                    " firstBaronTeam     BOOLEAN                 , " +
                    " countBaron         INT                     , " +
                    " firstDragonTeam    BOOLEAN                 , " +
                    " countDragon        INT                     , " +
                    " firstInhibitorTeam BOOLEAN                 , " +
                    " countInhibitor     INT                     , " +
                    " firstTowerTeam     BOOLEAN                 , " +
                    " countTower         INT                       " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created TeamTable");
    }

    public void createSpellTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE SPELL " +
                    "(" +
                    " SpellID            INT PRIMARY KEY         , " +
                    " Name               TEXT                      " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created SpellTable");
    }

    public void createItemTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE ITEM " +
                    "(" +
                    " ItemID             INT PRIMARY KEY         , " +
                    " Name               TEXT                      " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created Item");
    }


    public void dropAllTables(){
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql_champion =           "DROP TABLE CHAMPION        CASCADE ";
            String sql_championmastery =    "DROP TABLE CHAMPIONMASTERY CASCADE ";
            String sql_matchhistory =       "DROP TABLE MATCHHISTORY    CASCADE ";
            String sql_summoner =           "DROP TABLE SUMMONER        CASCADE ";
            String sql_teamdata =           "DROP TABLE TEAMDATA        CASCADE ";
            //String sql_team =               "DROP TABLE TEAM            CASCADE ";
            String sql_item =               "DROP TABLE ITEM            CASCADE ";
            stmt.executeUpdate(sql_item);
            String sql_spell =              "DROP TABLE SPELL          CASCADE ";
            stmt.executeUpdate(sql_spell);
            stmt.executeUpdate(sql_champion);
            stmt.executeUpdate(sql_championmastery);
            stmt.executeUpdate(sql_matchhistory);
            stmt.executeUpdate(sql_summoner);
            stmt.executeUpdate(sql_teamdata);
            //stmt.executeUpdate(sql_team);
            stmt.close();
            dbConn.getConn().close();
            System.out.println("Succesfully droped all tables");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

}
