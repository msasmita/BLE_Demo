package com.example.techolabz.ble_demo;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.techolabz.ble_demo.BLEAdapter.postData;

public class PostDistance extends AsyncTask<URL, String, Void> {
    protected Void doInBackground(URL... urls) {
        try {
            URL urlForGetRequest = null;
            urlForGetRequest = new URL("http://www.requestwall.com/beacons/save-user-location.php");
            HttpURLConnection postConnection = (HttpURLConnection) urlForGetRequest.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setRequestProperty("Accept", "raw");
            postConnection.setDoOutput(true);

            OutputStream os = postConnection.getOutputStream();
            byte[] input = postData.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            BufferedReader br = new BufferedReader(new InputStreamReader(postConnection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            System.out.println("DDDDDDDDDDD");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    protected void onPostExecute() {

    }
}
