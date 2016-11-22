package polyteam.myownstetho.HandlerTCP;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import polyteam.myownstetho.HandlerTCP.CommandTCP.InitTCP;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;
import polyteam.myownstetho.Activities.MainActivity;

public class ConfigTCP extends AppCompatActivity  implements AsyncResponseString{

    // Récupération des préférences
    SharedPreferences prefs = MainActivity.prefs;

    // Champ de texte pour IP et Port
    EditText ip_view, port_view;
    String host; int port;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tcp_config); // Init interface graphique
        ((MyApp) getApplicationContext()).setCurrentActivity(this); // Suiveur d'activité
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Boutton de retour
        // Init des EditText aux ressources
        ip_view = (EditText) findViewById(R.id.edit_raspi_ip);
        ip_view.setText(prefs.getString("host", "10.0.0.1"));
        port_view = (EditText) findViewById(R.id.edit_raspi_port);
        port_view.setText( ""+(prefs.getInt("port", 8000))+"" );
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!((MyApp) getApplicationContext()).isConnected()){
                    Toast.makeText(getBaseContext(), "Vous ne pouvez pas quitter tant que vous n'avez pas configuré le raspberry", Toast.LENGTH_LONG).show();
                }else{
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!((MyApp) getApplicationContext()).isConnected()){
            Toast.makeText(getBaseContext(), "Vous ne pouvez pas quitter tant que vous n'avez pas configuré le raspberry", Toast.LENGTH_LONG).show();
        }else{
            finish();
        }
    }



    public void getTextFromView(){
        host=ip_view.getText().toString();
        try{
            port = Integer.parseInt(port_view.getText().toString());
        }catch (Exception e){
            port=0;
            Toast.makeText(this, "Veuillez entrer un entier " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void savePreferences(){
        prefs.edit().putString("host", host).commit();
        prefs.edit().putInt("port", port).commit();
    }

    public void valid_config(View v) {
        getTextFromView();
        savePreferences();
        InitTCP asyncTask =new InitTCP(this);
        asyncTask.delegate =this;
        asyncTask.execute();
    }


    @Override
    public void processFinishSendString(String output){
        System.out.println("Tache init terminée");
        if(((MyApp) getApplicationContext()).isConnected()){
            // Fermeture de l'activité
            finish();
        }else{
            // Infication d'un problème
            Toast.makeText(this, "Problème lors de la connexion, veuillez vérifier vos identifiants et revalider" , Toast.LENGTH_LONG).show();
        }
    }

}