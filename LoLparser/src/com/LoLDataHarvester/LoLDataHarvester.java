package com.LoLDataHarvester;

import java.io.IOException;
import java.sql.SQLException;


public class LoLDataHarvester {

    private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"}; //De tiers die beschikbaar zijn om te scannen.
    private static String[] divisions = {"I","II","III","IV"}; //De brackets die beschikbaar zijn om te scannen.

    //private  static String[] tiers = {"DIAMOND"}; //Single tier voor test.
    //private  static String[] divisions = {"I"}; //Single bracket voor test.

    public static void main(String[] args) throws IOException, SQLException {
        createDatabase db = new createDatabase("postgres", "!RappaR1964", "lolparserdata",5432, "localhost");


        /**
         * Section to start the data harvester
         */
        //harvestData harvester = new harvestData("RGAPI-3fd1aa73-1ab7-4311-a1fa-7813fe8c1cd9","euw1",tiers,divisions);
        //harvester.run();

        /**
         * Section to fill the database with collected database
         */
        fillDatabase fillDB = new fillDatabase(db.getDbConn());
        fillDB.run();
        queries test = new queries("postgres", "!RappaR1964", "lolparserdata",5432, "localhost");
        //test.getMeSomeData();
    }
}
