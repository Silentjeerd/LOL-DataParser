package com.LoLDataHarvester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

public class champspellitems {

    private String patch;

    public champspellitems(String patch) throws IOException {
        this.patch = patch;
    }

    private void getSpecificData(String categorie) throws  IOException{
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
        JSONObject object = new JSONObject(parser.returnJsonStringFromUrl(ulrChamps,"")).getJSONObject("data");
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

    public void getData() throws IOException {
        getSpecificData("item");
        getSpecificData("champion");
        getSpecificData("summoner");
    }
}
