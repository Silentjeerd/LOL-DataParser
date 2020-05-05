package com.LoLDataHarvester.StrategyPattern;

import java.io.IOException;

public class Context {
    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy = strategy;
    }

    public void executeStrategy(String api_key, String region, String[] tiers, String[] divisions) throws IOException {
        strategy.getData(api_key,region,tiers,divisions);
    }
}
