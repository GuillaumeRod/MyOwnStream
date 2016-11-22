package polyteam.myownstetho.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.Timer;
import java.util.TimerTask;

import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.HandlerTCP.ConfigTCP;
import polyteam.myownstetho.HandlerTCP.CommandTCP.InitTCP;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;

public class MainActivity extends AppCompatActivity implements AsyncResponseString {

    public static SharedPreferences prefs = null;  // Déclaration des sharedPreferences
    public final String MyPref = "MyPrefsFile";

    private Handler handler = new Handler();  // Handler pour exception de communication
    private Button modeEnregistrement; //
    private Button modeSimulation;
    private RadioGroup choixSexe;
    private boolean choixHomme = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        prefs = getSharedPreferences(MyPref, MODE_PRIVATE); // Instanciation des preferences
        ((MyApp) getApplicationContext()).setCurrentActivity(this); // Suiveur d'activité, toujours utile

        setContentView(polyteam.myownstetho.R.layout.launcher);
        setUI();

        // Lancement de la tache de connexion
        InitTCP asyncTask =new InitTCP(this);
        asyncTask.delegate =this;
        asyncTask.execute();

    }

    public void setUI(){
        // Liaison UI
        modeEnregistrement = (Button) findViewById(R.id.beginEnregistrement);
        modeSimulation = (Button) findViewById(R.id.beginSimulation);
        choixSexe = (RadioGroup) findViewById(R.id.choixSexe);

        // Mise en inactif tant que la connexion n'a pas été effectuée
        modeEnregistrement.setEnabled(false);
        modeSimulation.setEnabled(false);
        choixSexe.setEnabled(false);
    }

    protected void onStart(){
        super.onStart();
    }



    // Listener pour le choix du sexe
    // Par défaut, homme
    public void setListener(){
        choixSexe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.homme:
                        choixHomme=true;
                        break;

                    case R.id.femme:
                        choixHomme=false;
                        break;
                }
                ((MyApp) getApplicationContext()).setChoixHomme(choixHomme);
            }
        });
    }



    // Appelé quand appui sur "Démarrer la simulation"
    public void launch_simu(View view) {
        Intent launch_Simu = new Intent(this, ChooseScenario.class);
        startActivity(launch_Simu);
    }

    // Repris de l'activité
    protected void onResume(){
        super.onResume();
        System.out.println("onResume de MainActivity");
        if(((MyApp) getApplicationContext()).isConnected()){
            modeSimulation.setEnabled(true);
            modeEnregistrement.setEnabled(true);
            choixSexe.setEnabled(true);
            setListener();
        }
    }


    // Tache périodique pour la gestion d'erreurs de comm
    public void setExceptionHandler(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //new ExceptionHandler(getApplicationContext()).execute();
                        try{
                            ((MyApp) getApplicationContext()).sendMessage("Test_Connexion",((MyApp) getApplicationContext()).getCurrentActivity());
                            /*
                            // Socket non atteignable
                            if(!((MyApp) getApplicationContext()).getSocket().getInetAddress().isReachable(5000)){
                                ((MyApp) getApplicationContext()).showProblemDialog(getApplicationContext());
                            }*/
                        }catch (Exception e){
                            ((MyApp) getApplicationContext()).resetApp(getApplicationContext());
                        }
                    }
                });
            }
        };
        timer.schedule(task, 8000, 100000); // Execution toutes les 10s
    }



    // Gestionnaire de réponse de la tache initTCP
    @Override
    public void processFinishSendString(String output){
        if(!((MyApp) getApplicationContext()).isConnected()){
            startActivity(new Intent(getApplicationContext(), ConfigTCP.class));
        }else{
            modeSimulation.setEnabled(true);
            modeEnregistrement.setEnabled(true);
            choixSexe.setEnabled(true);
            setListener();
            //setExceptionHandler();
        }
    }

    // Appelé quand appui sur "Enregistrement"
    public void launch_enregistrement(View view) {
        Intent launch_enregistrement = new Intent(this, NewSound.class);
        startActivity(launch_enregistrement);
    }

    // Menu d'options
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        return true;
    }


    // Gestion des options
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                return true;

            case android.R.id.home:
                finish();
                return true;

            case R.id.configTCP:
                startActivity(new Intent(getApplicationContext(), ConfigTCP.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}



