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

public class Droit extends Fragment implements AsyncResponseString {
    private RadioGroup bouttons_Cote_Droit;
    private TextView ptAusculation;
    private short currentCheckedPatho=0;

    public static Droit newInstance(boolean choixHomme) {
        Droit myFragment = new Droit();
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
            rootView = inflater.inflate(R.layout.vue_cote_droit_homme, container, false);
        }else{
            rootView = inflater.inflate(R.layout.vue_cote_droit_femme, container, false);
        }
        setListeners(rootView);
        return rootView;
    }


    public void setListeners(View v) {
        bouttons_Cote_Droit = (RadioGroup) v.findViewById(R.id.groupe_cote_droit);
        bouttons_Cote_Droit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable patho
                // Check the corresponding button
                short idPatho = -1;
                switch (checkedId) {
                    case R.id.champ_axiliaire:
                        idPatho = 30;
                        break;
                }

                if (!(idPatho == currentCheckedPatho)){ // Toujours le cas normalement, sauf si provient d'un clearCheck()
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
        if(floatButtons.getCheckedRadioButtonId() != -1) {
            // Un boutton est activé
            floatButtons.clearCheck();
        }
    }



    public void processFinishSendString(String output){
        System.out.println("Droit réponse= " + output);
    }
}