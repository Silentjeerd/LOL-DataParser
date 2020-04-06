package com.LoLDataHarvester;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class databaseConnection {
    private Connection conn;

    private String user;
    private String password;
    private String databaseName;
    private int port;
    private String ipAdress;

    public databaseConnection(String user, String password, String databaseName , int port, String ipAdress) {
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
        this.port = port;
        this.ipAdress = ipAdress;
        conn = null;
    }

    public Connection getConn() {
        return conn;
    }


    public void connectToDatabaseServer() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://"+ ipAdress + ":"+ String.valueOf(port) +"/"+ databaseName, user, password);
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage() + ". Will make database now");
            try{
                conn = DriverManager.getConnection("jdbc:postgresql://" + ipAdress + ":"+ String.valueOf(port) + "/", user, password);
                Statement stmt = conn.createStatement();
                String sql = "CREATE DATABASE lolParserData";
                stmt.executeUpdate(sql);
                connectToDatabaseServer();
            }catch (Exception f){
                f.printStackTrace();
                System.err.println(f.getClass().getName() + ": " + f.getMessage());
                connectToDatabaseServer();
            }
        }
        System.out.println("Opened database successfully");
    }
}
