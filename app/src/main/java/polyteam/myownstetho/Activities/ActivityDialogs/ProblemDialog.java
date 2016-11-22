package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;

/**
 * Created by Tristan on 10/04/2016.
 */

public class ProblemDialog extends DialogFragment {


    public static ProblemDialog newInstance(){
        ProblemDialog myDialogFragment = new ProblemDialog();
        return myDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Problème de communication avec le stéthoscope. Redémarrage de l'application...");
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    ((MyApp) getActivity().getApplicationContext()).resetApp( getActivity().getApplicationContext());
                } catch (Exception e) {

                }
            }
        });
        return builder.create();
    }
}

