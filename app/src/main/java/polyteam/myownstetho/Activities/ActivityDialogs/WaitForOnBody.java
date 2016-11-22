package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Tristou on 13/05/2016.
 */
public class WaitForOnBody extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Veuillez poser le stéthoscope sur le corps du patient");
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static WaitForOnBody newInstance(){
        WaitForOnBody myDialogFragment = new WaitForOnBody();
        return myDialogFragment;
    }
}
