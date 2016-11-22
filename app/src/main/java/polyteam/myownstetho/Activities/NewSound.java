package polyteam.myownstetho.Activities;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import polyteam.myownstetho.Activities.ActivityDialogs.ProblemDialog;
import polyteam.myownstetho.Activities.ActivityDialogs.SavePathoDialog;
import polyteam.myownstetho.Activities.ActivityDialogs.SetTimeRecordDialog;
import polyteam.myownstetho.Activities.ActivityDialogs.WaitForOnBody;
import polyteam.myownstetho.Activities.ActivityDialogs.WaitForPulseMeasured;
import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.HandlerTCP.CommandTCP.GetResponse;
import polyteam.myownstetho.HandlerTCP.CommandTCP.SendString;
import polyteam.myownstetho.HandlerTCP.CommandTCP.SendStringWithoutResponse;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;


public class NewSound extends AppCompatActivity
        implements SavePathoDialog.SavePathoDialogListener,
                   AsyncResponseString, SetTimeRecordDialog.SetTimeForRecordListener {

    /// ------ Partie concernant la gestion des résultats du dialogue ------- ///
    private String Type = null;
    private String Name = null;


    private CircleProgressView mCircleView;
    private long startTime = 0;
    private int max_Value=30;
    private boolean chronoLance=false;
    private Handler timerHandler = new Handler();
    private WaitForOnBody waitForOnBody;



    @Override
    public void onDialogValidClick(DialogFragment dialog, String value){
        Log.d("Nom du fichier = ", value);
        if(value.isEmpty() || Type==null){
            this.Type="Autre";
            this.Name="Autre";
        }else{
            this.Name=value;
            // Type déja defini à l'aide de l'autre listener
        }
        // Il faut envoyer ici le nom du fichier désiré
        ((MyApp) getApplicationContext()).sendMessage("ValidSaveFile", this);

    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog){
        // Il faut annuler l'enregistrement
        ((MyApp) getApplicationContext()).sendMessage("CancelSaveFile", this);
        setChronoOptions(); // Reset du chrono
    }

    @Override
    public void onDialogTypeClick(DialogFragment dialog, String value){
        Log.d("Type = ", value);
        this.Type=value;
    }

    @Override
    public void onSetTimeForRecordValidClick(DialogFragment dialog, int value){
        max_Value=value;
        mCircleView.setMaxValue(value);
        mCircleView.setValue(0);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enregistrement);
        ((MyApp) getApplicationContext()).setCurrentActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCircleView = (CircleProgressView) findViewById(R.id.chrono);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setChronoOptions();

        ((MyApp) getApplicationContext()).sendMessage("StartRecord", this);
    }


    public void setChronoOptions() {
        mCircleView.setMaxValue(max_Value);
        mCircleView.setValue(0);
        mCircleView.setTextColorAuto(true);
        mCircleView.setTextMode(TextMode.VALUE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MyApp) getApplicationContext()).sendMessage("StopRecord", this);
                return true;

            case R.id.set_time:
                DialogFragment newFragment = SetTimeRecordDialog.newInstance();
                newFragment.show(getFragmentManager(), "Set Time Record");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void launch_chrono(View v){
        String command;
        if(chronoLance){
            // Si le chrono était déja lancé, il faut arreter l'enregistrement
            timerHandler.removeCallbacks(timerRunnable);
            chronoLance=false;
            SendStringWithoutResponse asyncTask = new SendStringWithoutResponse(((MyApp) getApplicationContext()).getCurrentActivity(),"StopSave");
            asyncTask.execute();
        }else{
            // Affiche d'un dialogue en attendant que le stethoscope soit pret
            waitForOnBody = waitForOnBody.newInstance();
            waitForOnBody.show(getFragmentManager(), "Stetho Ready");
            waitForOnBody.setCancelable(false);
            ((MyApp) getApplicationContext()).sendMessage("LaunchSave", this);
        }
    }


    @Override
    public void onBackPressed() {
        //SendStringWithoutResponse asyncTask =new SendStringWithoutResponse(this,"StopRecord");
        //asyncTask.execute();
        ((MyApp) getApplicationContext()).sendMessage("StopRecord", this);
    }


    //this override the implemented method from asyncTask
    public void processFinishSendString(String output){
        // Plusieurs cas à traiter
        if(output != null) {
            System.out.println("Reception de " + output + " dans NewSound");
            switch (output) {

                case "Ok_StopRecord":
                    finish();
                    break;

                case "Ok_LaunchSave":
                    waitForOnBody.dismiss();
                    animateChrono(true);
                    ((MyApp) getApplicationContext()).getResponse(this); // Attente de StopChrono
                    break;

                case "Ok_StopSave":
                    animateChrono(false);
                    DialogFragment newFragment = SavePathoDialog.newInstance();
                    newFragment.setCancelable(false);
                    newFragment.show(getFragmentManager(), "Validation patho");
                    break;

                case "Ok_CancelSaveFile":
                    System.out.println("Annulation sauvegarde fichier");
                    break;


                case "Ok_ValidSaveFile":
                    System.out.println("Envoi de validation");
                    ((MyApp) getApplicationContext()).sendMessage(Type + "_" + Name, this);
                    setChronoOptions(); // Reset du chrono
                    break;

                case "Ok_FileSaved":
                    Toast.makeText(this, "Fichier sauvegardé", Toast.LENGTH_LONG).show();
                    break;

                case "Ok_StartRecord":
                    System.out.println("Start Record ok");
                    break;

                case "Ok_Stop_Record":
                    System.out.println("Stop Record ok");
                    break;


                default:
                    // Reset de l'activité ?
                    System.out.println("Problème de réponse");
                    Toast.makeText(this, "Problème de communication avec le stéthoscope", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.enregistrement, menu);
        return true;
    }





    public void animateChrono(boolean bool){
        if(bool){
            startTime = System.currentTimeMillis();
            chronoLance=true;
            timerHandler.postDelayed(timerRunnable, 0);
        }else {
            timerHandler.removeCallbacks(timerRunnable);
            chronoLance=false;
        }
    }


    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            float millis = System.currentTimeMillis() - startTime;
            float seconds =  (millis / 1000);
            mCircleView.setValueAnimated(seconds, 2);
            if((int)seconds>=max_Value){
                animateChrono(false);
                SendStringWithoutResponse asyncTask = new SendStringWithoutResponse(((MyApp) getApplicationContext()).getCurrentActivity(),"StopSave");
                asyncTask.execute();
                return;
            }
            timerHandler.postDelayed(this,50);
        }
    };

}