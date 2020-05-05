package com.LoLDataHarvester;

import java.io.*;

import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;

import com.github.opendevl.JFlat;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.IOUtils;
import org.json.*;

import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

public class Parser {
    static private String inputfile;

    //TODO Tjeerd please comment !!

    /**
     * Deze functie wordt aangeroepen zodra we de parser willen laten wachten.
     * @param millis Het aantal milliseconden dat gewacht moet worden.
     */
    static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Deze functie zet alle tekst uit de Reader om naar één String
     * @param rd BufferedReader die moet worden omgezet.
     * @return De String waarmee verder moet worden gewerkt.
     * @throws IOException
     */
    static private String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }


    /**
     * Deze functie haalt een String op op basis van een URL en eventueel een subarray.
     * @param url het URL voor een HTTP request wat gedaan moet worden.
     * @param subarray een String waar direct een subarray vanuit de JSON kan worden gehaald.
     * @return Een String variant van de gevraagde data.
     * @throws IOException
     * @throws JSONException
     */
    static public String returnJsonStringFromUrl(String url,String subarray) throws IOException, JSONException{
        InputStream is = new URL(url).openStream();

        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            //return jsonText;
            String wantedString;
            if(subarray == ""){
                wantedString = jsonText;
            }else{
                JSONObject json = new JSONObject(jsonText);
                JSONArray subArray = json.getJSONArray(subarray);
                wantedString = subArray.toString();
            }

            return wantedString;

        } finally  {
            is.close();
        }
    }

    /**
     * Deze functie zet een String van een JSONObject of JSONArray om naar een .CSV bestand.
     * @param inputString De String van data die weg moet worden geschreven.
     * @param outputFile De String van het pad naar het bestand waar de data in moet worden weggeschreven.
     * @throws IOException
     */
    static public void generateCSVFromJString(String inputString, String outputFile) throws IOException{

        JFlat flatMe = new JFlat(inputString);
        List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
        flatMe.write2csv(outputFile);
    }

    /**
     * Deze functie haalt het AccountID en Summonerlevel van individuen op.
     * @param url het URL voor een HTTP request wat gedaan moet worden.
     * @return Een String met daarin de vraagde data.
     * @throws IOException
     */
    static public String getAccountID(String url) throws IOException{
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JSONObject json = new JSONObject(jsonText);
        String accountId = json.getString("accountId");
        int summonerLVL = json.getInt("summonerLevel");
        return accountId + ',' + summonerLVL;
    }

    /**
     * Deze functie telt het aantal regels in een .CSV bestand.
     * @param filename De String van het pad van het .CSV bestand dat moet worden gelezen.
     * @return De count van het aantal regels. Deze wordt gebruikt om in de console
     *         weer te geven wat de stand van zaken is.
     * @throws IOException
     */
    static public int countLines(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i = 0; i < 1024; ) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        }
    }

}
