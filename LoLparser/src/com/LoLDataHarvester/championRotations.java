package com.LoLDataHarvester;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class championRotations {
    private String apiKey, region;

    public championRotations(String apiKey, String region) {
        this.apiKey = apiKey;
        this.region = region;
    }

    public void getData() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String outputFile = "LoLparser/CSVs/ChampionRotation" + formatter.format(LocalDate.now()) + ".csv";
        int tryCount = 0;
        int maxTries = 5;
        while(tryCount < maxTries){
            try{
                parser.sleep(1500);
                String urlWhole = "https://" + region + ".api.riotgames.com/lol/platform/v3/champion-rotations?api_key=" + apiKey;
                String toWrite = parser.returnJsonStringFromUrl(urlWhole,"freeChampionIds");
                parser.generateCSVFromJString(toWrite,outputFile);
                tryCount = maxTries;
                System.out.println("Champion data retrieved!");
            }catch (Exception e){
                parser.sleep(2000);
                tryCount++;
                if(tryCount == maxTries) System.out.println(e.toString());
            }
        }
    }
}
