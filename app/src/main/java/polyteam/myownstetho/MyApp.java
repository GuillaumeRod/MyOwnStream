package polyteam.myownstetho;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import polyteam.myownstetho.Activities.ActivityDialogs.ProblemDialog;
import polyteam.myownstetho.Activities.ActivityDialogs.SavePathoDialog;
import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.HandlerTCP.CommandTCP.GetResponse;
import polyteam.myownstetho.HandlerTCP.CommandTCP.SendString;

/**
 * Created by Tristan on 14/02/2016.
 */
public class MyApp extends Application  {
    private Socket socket = null;
    private boolean isConnected = false;
    private Activity mCurrentActivity = null;
    private Boolean choixHomme = true;


    public Activity getCurrentActivity(){
        return this.mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    public void setConnected(boolean co){
        this.isConnected=co;
    }

    public boolean isConnected(){
        return this.isConnected;
    }


    public Socket getSocket(){
        return this.socket;
    }

    public void setSocket(Socket socket){
        this.socket=socket;
    }


    public Boolean getChoixHomme() {
        return choixHomme;
    }

    public void setChoixHomme(Boolean choixHomme) {
        this.choixHomme = choixHomme;
    }



    public void sendMessage(String message, Activity mActivity){
        Log.v("MyApp", "sendMessage de " + message + " par " + mActivity);
        SendString asyncTask =new SendString(mActivity,message);
        asyncTask.delegateResponse = (AsyncResponseString) mActivity;
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getResponse(Activity mActivity){
        Log.v("MyApp", "getResponse de " + mActivity);
        GetResponse asyncTask =new GetResponse(mActivity);
        asyncTask.delegateResponse = (AsyncResponseString) mActivity;
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void resetApp(Context context){
        this.socket=null;
        this.isConnected=false;
        this.mCurrentActivity=null;
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void showProblemDialog(Context context){
        DialogFragment newFragment = ProblemDialog.newInstance();
        newFragment.setCancelable(false);
        newFragment.show(getCurrentActivity().getFragmentManager(), "Problem Dialog");
    }
}
