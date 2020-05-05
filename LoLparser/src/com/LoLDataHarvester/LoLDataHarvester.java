package com.LoLDataHarvester;

import com.LoLDataHarvester.StrategyPattern.Context;

import java.io.IOException;
import java.sql.SQLException;


public class LoLDataHarvester {

    private static String[] tiers = {"DIAMOND","PLATINUM","GOLD","SILVER","BRONZE","IRON"}; //De tiers die beschikbaar zijn om te scannen.
    private static String[] divisions = {"I","II","III","IV"}; //De brackets die beschikbaar zijn om te scannen.
    private static String api_key = "put apikey here";
    private static String region = "euw1";
    //private  static String[] tiers = {"DIAMOND"}; //Single tier voor test.
    //private  static String[] divisions = {"I"}; //Single bracket voor test.

    public static void main(String[] args) throws IOException, SQLException {
        CreateDatabase db = new CreateDatabase("postgres", "postgres", "lolparserdata",5432, "localhost");

        /**
         * Section to start the data harvester
         * Deze sectie voert d.m.v. StrategyPattern het parsen van de data uit.
         */
        Context context = new Context(new PlayerRanks());
        context.executeStrategy(api_key,region,tiers,divisions);

        context = new Context(new PlayerIDs());
        context.executeStrategy(api_key,region,tiers,divisions);

        context = new Context(new MatchHistory());
        context.executeStrategy(api_key,region,tiers,divisions);

        context = new Context(new MatchesByID());
        context.executeStrategy(api_key,region,tiers,divisions);

        context = new Context(new PlayerMasteries());
        context.executeStrategy(api_key,region,tiers,divisions);

        context = new Context(new Champspellitems());
        context.executeStrategy(api_key,region,tiers,divisions);


        /**
         * Section to fill the database with collected database
         */
        FillDatabase fillDB = new FillDatabase(db.getDbConn());
        fillDB.run();

    }
}
