package utils;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by syj on 16-4-8.
 */
public class httpUtils {
    public static int httpconnection(String path) {
        HttpURLConnection connection = null;
        int responsecode = 0;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.connect();
             responsecode = connection.getResponseCode();

        } catch (MalformedURLException e) {
            //url error
            e.printStackTrace();
        } catch (IOException e) {
            //net error
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responsecode;
    }
}
