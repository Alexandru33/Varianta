package ro.pub.cs.systems.eim.practicaltest003.communication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest003.general.Constants;
import ro.pub.cs.systems.eim.practicaltest003.general.Utilities;

public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    public CommunicationThread( ServerThread serverThread, Socket socket)
    {
        this.serverThread = serverThread;
        this.socket = socket;

    }

    @Override
    public void run() {

        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (currency) !");
            String currency = bufferedReader.readLine();
            if (currency == null || currency.isEmpty() ) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (currency) !");
                return;
            }

            HashMap<String, String> data = serverThread.getData();
            String balanceInformation = "";
            if (data.containsKey(currency)) {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                balanceInformation = data.get(currency);
            }
            if (data.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Data hashmap is empty!");
                return;
            }
            printWriter.println(balanceInformation);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }
    }
}
