package polyteam.myownstetho.HandlerTCP.CommandTCP;

import android.content.Context;
import android.os.AsyncTask;

import android.widget.Toast;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import polyteam.myownstetho.HandlerTCP.AsyncResponseAudio;
import polyteam.myownstetho.MyApp;

/**
 * Created by Tristan on 08/03/2016.
 */

public class GetAudio extends AsyncTask<Void, Void, Void> {
    public AsyncResponseAudio delegateResponse = null;
    //private String AudioName;
    private String result="NotOK";
    private Socket socket;
    private Context context;

    List<Exception> Exceptions = new ArrayList<>();

    public GetAudio(Context context){
        //this.AudioName=Name;
        this.context=context;
        socket = ((MyApp) context.getApplicationContext()).getSocket();
    }


    protected Void doInBackground(Void... param) {
        try{
            int NumberOfBytes;
            long FileSize;
            long FileReceived=0;
            byte Buffer[] = new byte[1024];

            DataInputStream inFromServer =  new DataInputStream(socket.getInputStream());
            DataOutputStream outToServer =  new DataOutputStream(socket.getOutputStream());
            FileOutputStream fileName = context.getApplicationContext().openFileOutput("temp.wav", context.getApplicationContext().MODE_PRIVATE);

            // Declenchement de demande audio
            outToServer.writeBytes("GetAudio" + '\n');
            outToServer.flush();

            // Récupération de la taille du fichier
            FileSize = inFromServer.readLong();
            System.out.println("Taille du fichier = " +FileSize);

            // Envoi du fichier
            while(FileReceived < FileSize) {
                NumberOfBytes= inFromServer.read(Buffer);
                FileReceived= FileReceived + NumberOfBytes;
                fileName.write(Buffer, 0, NumberOfBytes);
            }
            outToServer.flush();
            System.out.println("Transfert de fichier terminé");
            result="Ok_GetAudio";
        } catch (Exception e1) {
            Exceptions.add(e1);

        }
        return null;
    }


    protected void onPostExecute(Void param) {
        if(Exceptions.isEmpty()){
            delegateResponse.processFinishGetAudio(result);
        }else{
            System.out.println("Problème de communication");
            Toast.makeText(context, "Problème lors de la récupération du fichier", Toast.LENGTH_LONG).show();
            ((MyApp) context.getApplicationContext()).showProblemDialog(context.getApplicationContext());
        }
    }
}