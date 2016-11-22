package polyteam.myownstetho.Fragments;

import polyteam.myownstetho.Activities.ActivityDialogs.ProblemDialog;
import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;
import polyteam.myownstetho.HandlerTCP.CommandTCP.SendString;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Gauche extends Fragment implements AsyncResponseString {
    private RadioGroup bouttons_Cote_Gauche;
    private short currentCheckedPatho =0;


    public static Gauche newInstance(boolean choixHomme) {
        Gauche myFragment = new Gauche();
        Bundle args = new Bundle();
        args.putBoolean("choixHomme", choixHomme);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        Bundle args = getArguments();
        boolean choixHomme = args.getBoolean("choixHomme", true);
        if (choixHomme) {
            rootView = inflater.inflate(R.layout.vue_cote_gauche_homme, container, false);
        }else{
            rootView = inflater.inflate(R.layout.vue_cote_gauche_femme, container, false);
        }
        setListeners(rootView);
        return rootView;
    }


    public void setListeners(View v) {
        bouttons_Cote_Gauche = (RadioGroup) v.findViewById(R.id.groupe_cote_gauche);
        //ptAusculation = (TextView) v.findViewById(R.id.pointAuscultation);
        bouttons_Cote_Gauche.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable patho
                // Check the corresponding button
                short idPatho = -1;
                switch (checkedId) {
                    case R.id.irradiation_mitrale:
                        idPatho = 31;
                        break;

                    case R.id.champ_axiliaire:
                        idPatho = 32;
                        break;
                }

                if (!(idPatho == currentCheckedPatho)){ // Gestion provenance clearCheck() de l'activité
                    clearFloatButtons();
                    currentCheckedPatho = idPatho;
                    ((MyApp) getActivity().getApplicationContext()).sendMessage("en_pathoPoint_" + idPatho, getActivity());
                }else{
                    currentCheckedPatho=-1;
                }
            }
        });
    }

    public void clearFloatButtons(){
        RadioGroup floatButtons = (RadioGroup) getActivity().findViewById(R.id.floatButtons);
        if(floatButtons.getCheckedRadioButtonId() != -1) { // Radiogroup non vide
            // Un boutton est activé
            floatButtons.clearCheck();
        }
    }




    public void processFinishSendString(String output){
        System.out.println("Gauche réponse= " + output);
    }
}