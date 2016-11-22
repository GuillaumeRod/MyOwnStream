package polyteam.myownstetho.HandlerTCP.CommandTCP;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import polyteam.myownstetho.HandlerTCP.AsyncResponseStringList;
import polyteam.myownstetho.MyApp;

/**
 * Created by Tristan on 08/03/2016.
 */

public class GetScenarios extends AsyncTask<Void, Void, Void> {

    public AsyncResponseStringList delegateResponse = null;
    private Socket socket;
    private Context context;

    List<Exception> Exceptions = new ArrayList<>();
    ArrayList<String> Scenas = new ArrayList<>();
    public GetScenarios(Context context){
        this.context=context;
        socket = ((MyApp) context.getApplicationContext()).getSocket();
    }




    @Override
    protected void onPostExecute(Void param) {
        if(Exceptions.isEmpty()){
            delegateResponse.processFinishGetStringList(Scenas);
        }else{
            System.out.println("Problème de communication");
            Toast.makeText(context, "Problème lors de la récupération des scénarios", Toast.LENGTH_LONG).show();
            ((MyApp) context.getApplicationContext()).showProblemDialog(context.getApplicationContext());
        }
    }



    protected Void doInBackground(Void... param) {
        try{
            DataInputStream inFromServer =  new DataInputStream(socket.getInputStream());
            DataOutputStream outToServer =  new DataOutputStream(socket.getOutputStream());

            System.out.println("FROM SERVER: Demande du nombre de scénarios");
            outToServer.writeBytes("GetScenarios" + '\n');
            outToServer.flush();

            int numberOfScenas = inFromServer.readInt();
            System.out.println("FROM SERVER: Réponse nombre de scénarios = " +numberOfScenas);
            if(numberOfScenas>0){
                for(int i=0; i<numberOfScenas;i++){
                    Scenas.add(inFromServer.readUTF());
                }
            }
            outToServer.flush();
        } catch (Exception e1) {
            Exceptions.add(e1);
        }
        return null;
    }
}