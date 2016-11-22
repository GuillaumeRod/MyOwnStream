package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import polyteam.myownstetho.R;

/**
 * Created by Tristou on 14/05/2016.
 */
public class EnterManuallyPulseDialog extends DialogFragment {

    private EnterManuallyPulseListener mListener;
    AlertDialog myDialog;
    private View dialogView;
    private AlertDialog.Builder builder;
    private EditText pulse;


    public static EnterManuallyPulseDialog newInstance(){
        EnterManuallyPulseDialog myDialogFragment = new EnterManuallyPulseDialog();
        return myDialogFragment;
    }

    public interface EnterManuallyPulseListener {
        public void onUserPulseDetermined(DialogFragment dialog, int value);
    }



    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EnterManuallyPulseListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Mise en place de l'UI
        setObjectsViews();
        setUI();
        // Create the AlertDialog object and return it
        myDialog = builder.create();
        return myDialog;
    }


    public void setObjectsViews(){
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.manual_pulse, null);
        pulse = (EditText) dialogView.findViewById(R.id.pulse);
    }

    public void setUI() {
        builder.setTitle("Mesure du pouls manuelle (battements/minute)")
                .setView(dialogView)
                .setPositiveButton(R.string.valid_insert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mListener.onUserPulseDetermined(EnterManuallyPulseDialog.this, Integer.parseInt(pulse.getText().toString()));
                            dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Veuillez entrer un nombre valide (entier)", Toast.LENGTH_LONG).show();
                        }
                    }
                });
         }

}
