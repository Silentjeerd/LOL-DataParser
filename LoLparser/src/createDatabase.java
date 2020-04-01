import java.sql.ResultSet;
import java.sql.Statement;

public class createDatabase {

    private databaseConnection dbConn;

    public createDatabase(String user, String password, String databaseName , int port, String ipAdress){
        this.dbConn = new databaseConnection(user, password, databaseName, port, ipAdress);
        if(!tablesAreMade()){
            System.out.println("Tables are not made yet");
            createChampionsTable();
            createSummonerTable();
            createChampionMasteryTable();
            createMatchHistoryTable();
            createTeamDataTable();
            createTeamTable();
            System.out.println("Tables are made!");
        }else{
            System.out.println("Tables are already made!");
        }
    }

    public boolean tablesAreMade(){
        dbConn.connectToDatabaseServer();
        try{
            Statement stmt =dbConn.getConn().createStatement();
            String sql = "SELECT * FROM CHAMPION";
            ResultSet rs = stmt.executeQuery(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void createChampionsTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE CHAMPION " +
                    "(" +
                    " ChampionID        INT PRIMARY KEY          , " +
                    " Name              TEXT             NOT NULL, " +
                    " PrimaryClass      TEXT             NOT NULL, " +
                    " SecondaryClass    TEXT                       " +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created ChampionsTable");
    }

    public void createSummonerTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE SUMMONER " +
                    "(" +
                    " AccountID         TEXT PRIMARY KEY         ," +
                    " SummonerID        TEXT            NOT NULL, " +
                    " Name              TEXT            NOT NULL, " +
                    " Rank              TEXT            NOT NULL, " +
                    " Tier              TEXT            NOT NULL, " +
                    " SummonerLevel     INT             NOT NULL, " +
                    " LeaguePoints      INT             NOT NULL, " +
                    " TotalGamesPlayed  INT             NOT NULL, " +
                    " Wins              INT             NOT NULL, " +
                    " Losses            INT             NOT NULL, " +
                    " Veteran           BOOLEAN                 , " +
                    " FreshBlood        BOOLEAN                 " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created SummonerTable");
    }

    public void createChampionMasteryTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE CHAMPIONMASTERY " +
                    "(" +
                    "PRIMARY KEY (ChampionID,AccountID)," +
                    " ChampionID        INT  REFERENCES CHAMPION," +
                    " AccountID         TEXT REFERENCES SUMMONER, " +
                    " Name              TEXT            NOT NULL, " +
                    " Rank              TEXT            NOT NULL, " +
                    " Tier              TEXT            NOT NULL, " +
                    " SummonerLevel     INT             NOT NULL, " +
                    " LeaguePoints      INT             NOT NULL, " +
                    " TotalGamesPlayed  INT             NOT NULL, " +
                    " Wins              INT             NOT NULL, " +
                    " Losses            INT             NOT NULL, " +
                    " Veteran           BOOLEAN                 , " +
                    " FreshBlood        BOOLEAN                 " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created ChampionMasteryTable");
    }

    public void createMatchHistoryTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE MATCHHISTORY " +
                    "(" +
                    " MatchID           INT  PRIMARY KEY        , " +
                    " ChampionID        INT  REFERENCES CHAMPION, " +
                    " AccountID         TEXT REFERENCES SUMMONER, " +
                    " Lane              TEXT            NOT NULL, " +
                    " Role              TEXT            NOT NULL, " +
                    " Region            TEXT            NOT NULL, " +
                    " Spell1            TEXT            NOT NULL, " +
                    " Spell2            TEXT            NOT NULL, " +
                    " FirstBlood        TEXT            NOT NULL, " +
                    " FirstInhibitor    TEXT            NOT NULL, " +
                    " FirstTower        TEXT            NOT NULL, " +
                    " GoldeEarned       TEXT            NOT NULL, " +
                    " CreepKills        TEXT            NOT NULL, " +
                    " PlayerKills       TEXT            NOT NULL," +
                    " PlayerAssists     TEXT            NOT NULL," +
                    " Item0             TEXT            NOT NULL," +
                    " Item1             TEXT            NOT NULL," +
                    " Item2             TEXT            NOT NULL," +
                    " Item3             TEXT            NOT NULL," +
                    " Item4             TEXT            NOT NULL," +
                    " Item5             TEXT            NOT NULL," +
                    " Item6             TEXT            NOT NULL " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created MatchHistoryTable");
    }

    // TODO fix sql code
    public void createTeamTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE TEAM " +
                    "(" +
                    " MatchID       INT  REFERENCES MATCHHISTORY," +
                    " AccountID     TEXT REFERENCES SUMMONER    ," +
                    " ChampionID    INT  REFERENCES CHAMPION     " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created TeamTable");
    }

    // TODO fix sql code
    public void createTeamDataTable() {
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql = "CREATE TABLE TEAMDATA " +
                    "(MachtTeamID        INT , " +
                    " MatchID            INT REFERENCES MATCHHISTORY," +
                    " TeamID             INT                     , " +
                    " Win                TEXT                    , " +
                    " firstBloodTeam     BOOLEAN                 , " +
                    " firstRiftTeam      BOOLEAN                 , " +
                    " countRift          INT                     , " +
                    " firstBaronTeam     BOOLEAN                 , " +
                    " countBaron         INT                     , " +
                    " firstDragonTeam    BOOLEAN                 , " +
                    " countDragon        INT                     , " +
                    " firstInhibitorTeam BOOLEAN                 , " +
                    " countInhibitor     INT                     , " +
                    " firstTowerTeam     BOOLEAN                 , " +
                    " countTower         INT                       " +
                    ") ";
            stmt.executeUpdate(sql);
            stmt.close();
            dbConn.getConn().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Succesfully created TeamTable");
    }

    public void dropAllTables(){
        dbConn.connectToDatabaseServer();
        try {
            Statement stmt = dbConn.getConn().createStatement();
            String sql_champion =           "DROP TABLE CHAMPION        CASCADE ";
            String sql_championmastery =    "DROP TABLE CHAMPIONMASTERY CASCADE ";
            String sql_matchhistory =       "DROP TABLE MATCHHISTORY    CASCADE ";
            String sql_summoner =           "DROP TABLE SUMMONER        CASCADE ";
            String sql_teamdata =           "DROP TABLE TEAMDATA        CASCADE ";
            String sql_team =               "DROP TABLE TEAM            CASCADE ";
            stmt.executeUpdate(sql_champion);
            stmt.executeUpdate(sql_championmastery);
            stmt.executeUpdate(sql_matchhistory);
            stmt.executeUpdate(sql_summoner);
            stmt.executeUpdate(sql_teamdata);
            stmt.executeUpdate(sql_team);
            stmt.close();
            dbConn.getConn().close();
            System.out.println("Succesfully droped all tables");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

}
