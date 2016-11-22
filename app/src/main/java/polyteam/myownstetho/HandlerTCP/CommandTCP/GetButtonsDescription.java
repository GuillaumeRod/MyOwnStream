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

public class GetButtonsDescription extends AsyncTask<Void, Void, Void> {
    public AsyncResponseStringList delegateResponse = null;
    private String result;
    private Socket socket;
    private Context context;

    List<Exception> Exceptions = new ArrayList<>();
    ArrayList<String> buttonsDescription = new ArrayList<>();

    public GetButtonsDescription(Context context){
        this.context=context;
        socket = ((MyApp) context.getApplicationContext()).getSocket();
    }



    @Override
    protected void onPostExecute(Void param) {
        if(Exceptions.isEmpty()){
            delegateResponse.processFinishGetStringList(buttonsDescription);
        }else{
            System.out.println("Problème de communication");
            Toast.makeText(context, "Problème lors de la récupération des description des bouttons", Toast.LENGTH_LONG).show();
            ((MyApp) context.getApplicationContext()).showProblemDialog(context.getApplicationContext());
        }
    }



    protected Void doInBackground(Void... param) {
        try{
            DataInputStream inFromServer =  new DataInputStream(socket.getInputStream());
            DataOutputStream outToServer =  new DataOutputStream(socket.getOutputStream());

            outToServer.writeBytes("GetButtonsDescription" + '\n');
            outToServer.flush();
            int numberOfButtons = inFromServer.readInt();
            if(numberOfButtons>0){
                for(int i=0; i<numberOfButtons;i++){
                    buttonsDescription.add(inFromServer.readUTF());
                }
            }
            outToServer.flush();
        } catch (Exception e1) {
            Exceptions.add(e1);
        }
        return null;
    }
}