import java.io.IOException;

public interface dataCommand {

    void getData(String region,String division, String tier, String apiKey) throws IOException;
}
