package polyteam.myownstetho.Fragments;

import polyteam.myownstetho.HandlerTCP.AsyncResponseString;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;
import polyteam.myownstetho.HandlerTCP.CommandTCP.SendString;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;


public class Torse extends Fragment implements AsyncResponseString{
	private RadioGroup bouttons_Torse;
    private short currentCheckedPatho=0;

    public static Torse newInstance(boolean choixHomme) {
        Torse myFragment = new Torse();
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
            rootView = inflater.inflate(R.layout.vue_torse_homme, container, false);
        }else{
            rootView = inflater.inflate(R.layout.vue_torse_femme, container, false);
        }
        setListeners(rootView);
        return rootView;
    }



    public void setListeners(View v) {
        bouttons_Torse = (RadioGroup) v.findViewById(R.id.groupe_torse);
        bouttons_Torse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable patho
                // Check the corresponding button
                short idPatho = -1;
                switch (checkedId) {
                    case R.id.souffle_carotidien_gauche:
                        idPatho = 1;
                        break;

                    case R.id.souffle_carotidien_droit:
                        idPatho = 2;
                        break;

                    case R.id.irradiation_foyer_aortique:
                        idPatho = 3;
                        break;

                    case R.id.apex_gauche:
                        idPatho = 4;
                        break;

                    case R.id.apex_droit:
                        idPatho = 5;
                        break;

                    case R.id.lobe_sup_gauche:
                        idPatho = 6;
                        break;

                    case R.id.foyer_aortique:
                        idPatho = 7;
                        break;

                    case R.id.foyer_pulmonaire:
                        idPatho = 8;
                        break;

                    case R.id.lobe_sup_droit:
                        idPatho = 9;
                        break;

                    case R.id.lobe_moyen_gauche:
                        idPatho = 10;
                        break;

                    case R.id.lobe_moyen_droit:
                        idPatho = 12;
                        break;

                    case R.id.foyer_tripuscide:
                        idPatho = 11;
                        break;

                    case R.id.irradiation_mitrale:
                        idPatho = 13;
                        break;

                    case R.id.foyer_mitral:
                        idPatho = 14;
                        break;

                    case R.id.lobe_inf_gauche:
                        idPatho = 15;
                        break;
                    case R.id.lobe_inf_droit:
                        idPatho = 16;
                        break;

                    case R.id.souffle_epigastrique:
                        idPatho = 17;
                        break;

                    case R.id.souffle_aorte_abdo:
                        idPatho = 18;
                        break;

                    case R.id.souffle_iliaque_gauche:
                        idPatho = 19;
                        break;

                    case R.id.souffle_iliaque_droit:
                        idPatho = 20;
                        break;

                    case R.id.souffle_femoral_gauche:
                        idPatho = 21;
                        break;

                    case R.id.souffle_femoral_droit:
                        idPatho = 22;
                        break;
                }
                // Il faut tester ici deux cas, soit activation, soit désactivation à l'aide du menu
                System.out.println("Torse :  idPatho =" + idPatho + ", last =" + currentCheckedPatho);
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
        System.out.println("Torse réponse= " + output);
	}

}
