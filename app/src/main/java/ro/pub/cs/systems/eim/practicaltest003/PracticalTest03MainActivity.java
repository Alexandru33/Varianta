package ro.pub.cs.systems.eim.practicaltest003;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest003.communication.ClientThread;
import ro.pub.cs.systems.eim.practicaltest003.communication.ServerThread;
import ro.pub.cs.systems.eim.practicaltest003.general.Constants;

public class PracticalTest03MainActivity extends AppCompatActivity {

    private EditText editTextServerPort = null;
    private Spinner spinnerClient = null;
    private Button buttonServer = null;
    private Button buttonClient = null;

    private TextView textViewResponse = null;

    private ServerThread serverThread = null ;

    private class ClientButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            String clientPort = editTextServerPort.getText().toString();

            if (clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String informationType = spinnerClient.getSelectedItem().toString();
            if (informationType.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            textViewResponse.setText(Constants.EMPTY_STRING);

            ClientThread clientThread = new ClientThread(
                     Integer.parseInt(clientPort), informationType, textViewResponse
            );
            clientThread.start();

        }
    }
    private class ServerButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            String serverPort = editTextServerPort.getText().toString();

            if( serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));

            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextServerPort = (EditText) findViewById(R.id.editTextPortServer);
        spinnerClient = (Spinner) findViewById(R.id.spinner);
        buttonClient = (Button) findViewById(R.id.buttonClient);
        buttonServer = (Button) findViewById(R.id.buttonServer);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);

        buttonClient.setOnClickListener( new ClientButtonListener());
        buttonServer.setOnClickListener(new ServerButtonListener());





    }
}