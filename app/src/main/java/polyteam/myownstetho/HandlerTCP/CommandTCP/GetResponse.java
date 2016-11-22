package polyteam.myownstetho.HandlerTCP.CommandTCP;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.MyApp;

/**
 * Created by Tristan on 09/05/2016.
 */

    public class GetResponse extends AsyncTask<Void, Void, Void> {
        public AsyncResponseString delegateResponse = null;
        private String result;
        private Socket socket;
        private Context context;

        List<Exception> Exceptions = new ArrayList<>();

    public GetResponse(Context context){
        this.context=context;
        socket = ((MyApp) context.getApplicationContext()).getSocket();
    }

        @Override
        protected void onPostExecute(Void param) {
            if (Exceptions.isEmpty()) {
                delegateResponse.processFinishSendString(result);
            } else {
                System.out.println("Problème de communication");
                Toast.makeText(context, "Problème lors de la demande de réponse", Toast.LENGTH_LONG).show();
                ((MyApp) context.getApplicationContext()).showProblemDialog(context.getApplicationContext());
            }
        }


        protected Void doInBackground(Void... param) {
            try {
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                result = inFromServer.readLine();
            } catch (Exception e1) {
                Exceptions.add(e1);
            }
            return null;
        }
    }


