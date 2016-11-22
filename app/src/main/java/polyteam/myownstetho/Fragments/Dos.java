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

public class Dos extends Fragment implements AsyncResponseString {
    private RadioGroup bouttons_Dos;
    private int currentCheckedPatho=0;

    public static Dos newInstance(boolean choixHomme) {
        Dos myFragment = new Dos();
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
            rootView = inflater.inflate(R.layout.vue_dos_homme, container, false);
        }else{
            rootView = inflater.inflate(R.layout.vue_dos_femme, container, false);
        }
        setListeners(rootView);
        return rootView;
    }


    public void setListeners(View v) {
        bouttons_Dos = (RadioGroup) v.findViewById(R.id.groupe_dos);
        bouttons_Dos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable patho
                // Check the corresponding button
                short idPatho = -1;
                switch (checkedId) {
                    case R.id.apex:
                        idPatho = 23;
                        break;

                    case R.id.lobe_sup_droit:
                        idPatho = 25;
                        break;

                    case R.id.lobe_sup_gauche:
                        idPatho = 24;
                        break;

                    case R.id.champ_moyen_gauche:
                        idPatho = 26;
                        break;

                    case R.id.champ_moyen_droit:
                        idPatho = 27;
                        break;

                    case R.id.base_inf_gauche:
                        idPatho = 28;
                        break;

                    case R.id.base_inf_droit:
                        idPatho = 29;
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
        System.out.println("Dos réponse= " + output);
    }
}
