package com.LoLDataHarvester;

import java.io.IOException;


public class LoLDataHarvester {

    private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"}; //De tiers die beschikbaar zijn om te scannen.
    private static String[] divisions = {"I","II","III","IV"}; //De brackets die beschikbaar zijn om te scannen.

    //private  static String[] tiers = {"DIAMOND"}; //Single tier voor test.
    //private  static String[] divisions = {"I"}; //Single bracket voor test.

    public static void main(String[] args) throws IOException {
        //createDatabase db = new createDatabase("postgres", "****", "lolparserdata",5432, "localhost");

        /**
         * Section to start the data harvester
         */
        harvestData harvester = new harvestData("RGAPI-f9adcafd-fdd0-48df-aec5-b6c78071dc5b","euw1",tiers,divisions);
        harvester.run();

        /**
         * Section to fill the database with collected database
         */
        //fillDatabase fillDB = new fillDatabase(db.getDbConn());
        //fillDB.run();
    }
}
