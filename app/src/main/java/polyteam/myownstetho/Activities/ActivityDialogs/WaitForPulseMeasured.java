package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import polyteam.myownstetho.MyApp;

/**
 * Created by Tristan on 12/05/2016.
 */
public class WaitForPulseMeasured extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Mesure du pouls, veuillez patienter");
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static WaitForPulseMeasured newInstance(){
        WaitForPulseMeasured myDialogFragment = new WaitForPulseMeasured();
        return myDialogFragment;
    }
}
