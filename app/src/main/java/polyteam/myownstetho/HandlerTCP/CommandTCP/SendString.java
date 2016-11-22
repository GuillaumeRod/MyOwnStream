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

public class SendString extends AsyncTask<Void, Void, Void> {
    public AsyncResponseString delegateResponse = null;
    private String command;
    private String result;
    private Socket socket;
    private Context context;

    List<Exception> Exceptions = new ArrayList<>();

    public SendString(Context context, String command){
        this.command=command;
        this.context=context;
        socket = ((MyApp) context.getApplicationContext()).getSocket();
    }




    @Override
    protected void onPostExecute(Void param) {
        if(Exceptions.isEmpty()){
            delegateResponse.processFinishSendString(result);
        }else{
            System.out.println("Problème de communication");
            Toast.makeText(context, "Problème lors de l'envoi de la chaine de caractères", Toast.LENGTH_LONG).show();
            ((MyApp) context.getApplicationContext()).showProblemDialog(context.getApplicationContext());
        }
    }



    protected Void doInBackground(Void... param) {
        try{
            BufferedReader inFromServer =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToServer =  new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(command + '\n');
            outToServer.flush();
            result = inFromServer.readLine();
        } catch (Exception e1) {
            Exceptions.add(e1);
        }
        return null;
    }

}