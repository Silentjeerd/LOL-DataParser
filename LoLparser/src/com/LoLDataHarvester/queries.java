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

    public void getMeSomeData() throws SQLException, IOException {
        Statement stmt =dbConn.getConn().createStatement();
        System.out.println("Geef je query op.");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        String sql = "SELECT 3 * 5, 3 + 5 AS result";

        String ordering;
        String table;
        boolean asc = false;
        List<String> orderingDesc = Arrays.asList("*meest*","*most*");
        List<String> orderingAsc = Arrays.asList("*minst*","*least*");

        for (String check: orderingDesc) {
            if(Pattern.compile(check).matcher(query).matches())
                asc = true;
        }

        if(asc){
            System.out.println("True");
        }

        if(query.contains("champion") || query.contains("champ")){
            table = "CHAMPION";
        }else{
            table = "SUMMONER";
        }

        String sqlQuery = "SELECT * FROM " + table + " ORDER BY ??? "; //+ ordering;
        System.out.println(sqlQuery);

        /*
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next())
        {
            System.out.println(rs.getString(1));//or rs.getString("column name");
        }
        */
    }
}
