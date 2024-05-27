package ro.pub.cs.systems.eim.practicaltest003.communication;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practicaltest003.general.Constants;

public class UpdateThread extends Thread{

    private final ServerThread serverThread;

    public UpdateThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public void run() {

        try {
            while (!Thread.currentThread().isInterrupted()) {

                Log.i(Constants.TAG, "[UPDATE THREAD] Doing update...");

                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";
                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                if (httpGetEntity != null) {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                }

                JSONObject content = new JSONObject(pageSourceCode);

                JSONObject bpiArray = content.getJSONObject("bpi");
                JSONObject eurObject = bpiArray.getJSONObject("EUR");
                JSONObject usdObject = bpiArray.getJSONObject("USD");

                JSONObject timestampObject = content.getJSONObject("time");

                HashMap<String, String> data = new HashMap<>();
                data.put("time" , timestampObject.getString("updated"));
                data.put(eurObject.getString("code"),  eurObject.getString("rate"));
                data.put(usdObject.getString("code"),  usdObject.getString("rate"));

                serverThread.setData(data);
                Log.i(Constants.TAG, "[UPDATE THREAD] Update done!");


                Thread.sleep(60 * 1000);

            }
        } catch (Exception ioException ) {
            Log.e(Constants.TAG, "[UPDATE THREAD] An exception has occurred: " + ioException.getMessage());
        }
    }

}
