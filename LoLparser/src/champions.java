import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class champions {

    private String outPutFilePath;

    public champions(String outputfilepath) throws IOException {
        outPutFilePath  = outputfilepath;
    }

    public void getData() throws IOException {
        String outputFile = outPutFilePath + "Champions.csv";
        String urlWhole = "http://ddragon.leagueoflegends.com/cdn/10.4.1/data/en_US/champion.json";
        parser.generateCSV(urlWhole,outputFile);
    }
}
