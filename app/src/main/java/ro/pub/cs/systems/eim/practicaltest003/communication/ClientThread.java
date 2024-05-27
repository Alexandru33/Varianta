package ro.pub.cs.systems.eim.practicaltest003.communication;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest003.general.Constants;
import ro.pub.cs.systems.eim.practicaltest003.general.Utilities;

public class ClientThread extends  Thread{

    private final String address = "localhost";
    private final int port;

    private final String informationType;
    private final TextView responseTextView;

    private Socket socket;

    public ClientThread(int port, String informationType, TextView responseTextView) {
        this.port = port;
        this.informationType = informationType;
        this.responseTextView = responseTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(informationType);

            printWriter.flush();
            String responseInformation;
            while ((responseInformation = bufferedReader.readLine()) != null) {
                final String finalizedResponseInformation = responseInformation;
                responseTextView.post(() -> responseTextView.setText(finalizedResponseInformation));
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }
}
