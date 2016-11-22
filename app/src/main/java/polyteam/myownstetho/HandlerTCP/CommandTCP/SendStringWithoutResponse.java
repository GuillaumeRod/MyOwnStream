package polyteam.myownstetho.HandlerTCP.CommandTCP;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.MyApp;

/**
 * Created by Tristan on 08/03/2016.
 */

public class SendStringWithoutResponse extends AsyncTask<Void, Void, Void> {
    private String command;
    private Socket socket;
    private Context context;

    List<Exception> Exceptions = new ArrayList<>();

    public SendStringWithoutResponse(Context context, String command){
        this.command=command;
        this.context=context;
        socket = ((MyApp) context.getApplicationContext()).getSocket();
    }




    @Override
    protected void onPostExecute(Void param) {
        if(!Exceptions.isEmpty()) {
            System.out.println("Problème de communication");
            Toast.makeText(context, "Problème lors de l'envoi de l'envoi de la chaine de caractères", Toast.LENGTH_LONG).show();
            ((MyApp) context.getApplicationContext()).showProblemDialog(context.getApplicationContext());
        }
    }



    protected Void doInBackground(Void... param) {
        try{
            DataOutputStream outToServer =  new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(command + '\n');
            outToServer.flush();
        } catch (Exception e1) {
            Exceptions.add(e1);
        }
        return null;
    }

}