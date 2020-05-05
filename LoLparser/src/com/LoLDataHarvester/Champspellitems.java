package com.LoLDataHarvester;

import com.LoLDataHarvester.StrategyPattern.Strategy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Champspellitems implements Strategy {

    /**
     * Deze functie heeft bij elke class een andere betekenis en zorgt ervoor
     * dat de gewenste data wordt opgehaald via een url en wordt weggeschreven in een .CSV file.
     * @param api_key De sleutel die nodig is om data van Riot op te halen.
     * @param region Dit is de region waarvan we data ophalen.
     * @param tiers Dit is een array van verschillende tiers waar doorheen geloopt kan worden.
     * @param divisions Dit is een array van verschillende divisions waar doorheen geloopt kan worden.
     * @throws IOException
     */
    @Override
    public void getData(String api_key, String region, String[] tiers, String[] divisions) throws IOException {
        getSpecificData("item");
        getSpecificData("champion");
        getSpecificData("summoner");
    }

    /**
     * Deze functie haalt data op detreffende Items, Spells of Champions
     * @param categorie Deze parameter bepaalt welk van de drie soorten data hij ophaalt.
     * @throws IOException
     */
    private void getSpecificData(String categorie) throws  IOException{
        String patch = "10.6.1";
        String filename;
        if(categorie.equals("summoner"))
            filename = "spell"; else
                filename = categorie;

        final String lineSep=System.getProperty("line.separator");
        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("LoLparser/CSVs/" + filename + "s.csv",true))); //open de writer
        String header = filename + "ID," + filename + "Name";
        fileWriter.write(header+lineSep);

        //creert de URL waarvan het JSONObject wordt opgehaald.
        String ulrChamps = "http://ddragon.leagueoflegends.com/cdn/" + patch + "/data/en_US/" + categorie + ".json";
        //het JSONObject wordt opgehaald.
        JSONObject object = new JSONObject(Parser.returnJsonStringFromUrl(ulrChamps,"")).getJSONObject("data");
        //Maakt een array aan met daarin de sleutels van alle JSONObjecten in het hoofdobject.
        JSONArray keys = object.names();

        //loopt door alle keys heen en haalt stuk voor stuk de data op om deze in rijen weg te schrijven.
        for(int i = 0; i < keys.length();i++){
            JSONObject temp = object.getJSONObject(keys.getString(i));
            String id;
            if(categorie.equals("item")){
                id = keys.get(i).toString();
            }else{
                id = temp.getString("key");
            }
            String name = temp.getString("name");
            String toWrite = id + "," + name;
            fileWriter.write(toWrite+lineSep);
        }

        fileWriter.close();
    }


}
