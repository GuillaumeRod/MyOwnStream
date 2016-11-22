package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Tristou on 14/05/2016.
 */
public class WaitForScenarios extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Récupération des scénarios en cours, veuillez patienter");
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static WaitForScenarios newInstance(){
        WaitForScenarios myDialogFragment = new WaitForScenarios();
        return myDialogFragment;
    }
}