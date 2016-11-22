package polyteam.myownstetho.HandlerTCP.CommandTCP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.HandlerTCP.ConfigTCP;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.Activities.MainActivity;



public class InitTCP extends AsyncTask<Void, Void, Void> {
    // Delegation init à null
    public AsyncResponseString delegate = null;

    // Preferences pour ne pas avoir à retaper tout le temps l'adresses
    SharedPreferences prefs = MainActivity.prefs;
    private String host;
    private int port;
    Socket socket;

    // Liste d'exceptions => array, pour le debug
    private List<Exception> Exceptions = new ArrayList<>();
    private Context context;


    // Constructeur pour init context, host et port
    public InitTCP(Context context){
        super();
        this.context = context;
        this.host= prefs.getString("host", "10.0.0.1");
        this.port = prefs.getInt("port", 8000);
    }


    protected Void doInBackground(Void... param) {
        try{
            // Création du socket
            socket = new Socket();
            // Connexion à l'adresse IP et au port
            socket.connect(new InetSocketAddress(host, port), 7000);
        } catch (Exception e1) {
            Exceptions.add(e1);
        }
        return null;
    }


    protected void onPostExecute(Void param) {
        if(Exceptions.isEmpty()){ // Aucune exception => connexion OK
            Toast.makeText(context, "Connection reussie !", Toast.LENGTH_LONG).show();
            ((MyApp) context.getApplicationContext()).setSocket(socket);
            ((MyApp) context.getApplicationContext()).setConnected(true);
        }else{ // Sinon, informer l'utilisateur
            Toast.makeText(context, "Problème lors de la connexion avec le raspberry ", Toast.LENGTH_LONG).show();
        }
        delegate.processFinishSendString("Ok_InitTCP");
    }

}
