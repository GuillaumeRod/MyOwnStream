package polyteam.myownstetho.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


import polyteam.myownstetho.Activities.ActivityDialogs.ProblemDialog;
import polyteam.myownstetho.Activities.ActivityDialogs.WaitForPulseMeasured;
import polyteam.myownstetho.Activities.ActivityDialogs.WaitForScenarios;
import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.HandlerTCP.AsyncResponseStringList;
import polyteam.myownstetho.HandlerTCP.CommandTCP.GetScenarios;
import polyteam.myownstetho.HandlerTCP.CommandTCP.SendString;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;


public class ChooseScenario extends AppCompatActivity
        implements AsyncResponseStringList, AsyncResponseString {

    private ListView scenaAvailable;
    private WaitForScenarios waiForScenarios;
    private int choixScenario = -1;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_scenario);
        ((MyApp) getApplicationContext()).setCurrentActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scenaAvailable = (ListView) findViewById(R.id.scenasAvailable);

    }


    protected void onStart(){
        super.onStart();
        waiForScenarios = waiForScenarios.newInstance();
        waiForScenarios.show(getFragmentManager(), "Stetho Ready");
        waiForScenarios.setCancelable(false);
        getScenarios();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_scena, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.refresh:
                getScenarios();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void getScenarios(){
        GetScenarios asyncTask =new GetScenarios(this);
        asyncTask.delegateResponse =this;
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    // Il faut tout d'abord indiquer que l'on choisit le scénario
    public void sendScenarioChoisi(){
        ((MyApp) getApplicationContext()).sendMessage("modeChoixScenario", this);
    }


    // Afin de gérer la bonne execution de l'envoie du scénario choisi ou non
    public void processFinishSendString(String output){
        if(output!=null){
            switch(output){
                case "Ok_validChoixScenario":
                    Intent launch_Simu = new Intent(this, Simulation.class);
                    startActivity(launch_Simu);
                    break;
                case "Ok_modeChoixScenario":
                    System.out.println("Envoi du choix scenario " + choixScenario);
                    ((MyApp) getApplicationContext()).sendMessage(String.valueOf(choixScenario), this);
                    break;
            }
        }
    }


    //this override the implemented method from asyncTask
    public void processFinishGetStringList(ArrayList<String> Scenas){
        if(Scenas.isEmpty()){
            Toast.makeText(ChooseScenario.this, "Problème de reception des scénarios, veuillez configurer des scénarios avant", Toast.LENGTH_LONG).show();
            finish();
        }else{
            waiForScenarios.dismiss();
            ArrayAdapter adapter = new ArrayAdapter<String>(ChooseScenario.this,android.R.layout.simple_list_item_1, Scenas);
            scenaAvailable.setAdapter(adapter);
            scenaAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Position cliquée = " + position);
                    // Il faut envoyer la position du scénario selectionné
                    choixScenario = position;
                    sendScenarioChoisi();
                }
            });

        }
    }


}