package com.LoLDataHarvester;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class queries {

    private databaseConnection dbConn;

    public queries(String user, String password, String databaseName , int port, String ipAdress){
        this.dbConn = new databaseConnection(user, password, databaseName, port, ipAdress);
        dbConn.connectToDatabaseServer();
    }
    /*
        Lijst van tabellen.
            CHAMPION
            CHAMPIONMASTERY
            MATCHHISTORY
            SUMMONER
            TEAMDATA
            TEAM
            ITEM
            SPELL
     */


    public void getMeSomeData() throws SQLException, IOException {
        Statement stmt = dbConn.getConn().createStatement();

        String query;

        //query = "SELECT * FROM ITEM WHERE ITEMID = 1001";
        //query = "SELECT * FROM CHAMPION ORDER BY ChampionID ASC";
        //query = "SELECT * FROM SPELL ORDER BY SpellID ASC";
        //query = "SELECT * FROM MATCHHISTORY";
        query = "SELECT * FROM MATCHHISTORY WHERE AccountID = '20VITQtkdhDPhj4phneOY0NWmpYHQWh-wVwzJeS-OIPXrrU'";
        // INSERT INTO SUMMONER" +
        //                "(AccountID,SummonerID,Name
        //query = "SELECT champion.name, count(MATCHHISTORY.championID) FROM champion, MATCHHISTORY where champion.championID = matchhistory.championid group by champion.name order by count(MATCHHISTORY.championID) desc fetch first 10 rows only";
        //query = "SELECT * FROM MATCHHISTORY FETCH FIRST ROW ONLY";
        //query = "SELECT ACCOUNTID FROM Summoner";
        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        int count = 0;
        while(rs.next())
        {
            for(int i = 1; i < 16;i++){
                System.out.println(rs.getString(i));
            }
            //System.out.println(rs.getString(1) + " " + rs.getString(2));
            //System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3)+ " " + rs.getString(4));//or rs.getString("column name");
        }
    }
}
