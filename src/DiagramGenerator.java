/**
 * Created by huzaifa.aejaz on 2/18/17.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DiagramGenerator {
    public static Boolean imageGenerator(String oPath,String grammarCode) {

        try {
            String websiteLink = "https://yuml.me/diagram/boring/class/" + grammarCode + ".png";
            URL url = new URL(websiteLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200)
            {
                throw new RuntimeException(
                        "Error code : " + connection.getResponseCode());
            }
            OutputStream oStream = new FileOutputStream(new File(oPath));

            byte[] byteHolder;
            byteHolder = new byte[1024];
            int reader = 0;

            while ((reader = connection.getInputStream().read(byteHolder)) != -1)
            {
                oStream.write(byteHolder, 0, reader);
            }
            oStream.close();
            connection.disconnect();
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        return null;
    }
}
