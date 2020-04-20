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

        query = "SELECT * FROM ITEM WHERE ITEMID = 1001";
        query = "SELECT * FROM CHAMPION ORDER BY ChampionID ASC";
        query = "SELECT * FROM SPELL ORDER BY SpellID ASC";
        query = "SELECT * FROM MATCHHISTORY";
        //query = "SELECT * FROM SUMMONER";
        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next())
        {
            System.out.println(rs.getString(1) + " " + rs.getString(2));//or rs.getString("column name");
        }
    }
}
