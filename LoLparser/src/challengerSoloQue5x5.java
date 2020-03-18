import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class challengerSoloQue5x5 {

    private String outPutFilePath;

    public challengerSoloQue5x5(String outputFilePath) throws IOException {
        outPutFilePath  = outputFilePath;
    }

    public void getData(String region, String apiKey) throws IOException {
        String outputFile = outPutFilePath + "ChallengerSoloQue.csv";
        String urlWhole = "https://" + region + ".api.riotgames.com/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5?api_key=" + apiKey;
        parser.generateCSV(urlWhole,outputFile);
    }
}
