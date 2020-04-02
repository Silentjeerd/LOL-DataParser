package com.LoLDataHarvester;

import java.io.IOException;


public class LoLDataHarvester {

    //private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"}; //De tiers die beschikbaar zijn om te scannen.
    //private static String[] divisions = {"I","II","III","IV"}; //De brackets die beschikbaar zijn om te scannen.

    private  static String[] tiers = {"DIAMOND"}; //Single tier voor test.
    private  static String[] divisions = {"I"}; //Single bracket voor test.

    public static void main(String[] args) throws IOException {
        createDatabase db = new createDatabase("postgres", "!RappaR1964", "lolparserdata",5432, "localhost");
        fillDatabase fillDB = new fillDatabase(db.getDbConn());
        fillDB.run();

        //harvestData harvester = new harvestData("RGAPI-c5c1e471-f296-40e1-aad2-6dd5fdc030e4","euw1",tiers,divisions);
        //harvester.run();
    }
}
