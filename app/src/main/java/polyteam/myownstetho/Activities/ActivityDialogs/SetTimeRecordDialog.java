package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import polyteam.myownstetho.R;

/**
 * Created by Tristan on 10/04/2016.
 */
public class SetTimeRecordDialog extends DialogFragment {
    private SetTimeForRecordListener mListener;
    AlertDialog myDialog;
    private View dialogView;
    private AlertDialog.Builder builder;
    private Button buttonValid;
    private NumberPicker timeForRecord;


    public static SetTimeRecordDialog newInstance(){
        SetTimeRecordDialog myDialogFragment = new SetTimeRecordDialog();
        return myDialogFragment;
    }

    public interface SetTimeForRecordListener {
        public void onSetTimeForRecordValidClick(DialogFragment dialog, int value);
    }



    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SetTimeForRecordListener) activity;
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
        dialogView = inflater.inflate(R.layout.record_time, null);
        timeForRecord = (NumberPicker) dialogView.findViewById(R.id.numberPicker);
        timeForRecord.setMinValue(3);
        timeForRecord.setMaxValue(120);
    }

    public void setUI() {
        builder.setTitle(R.string.record_time_title)
                .setView(dialogView)
                .setPositiveButton(R.string.valid_insert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mListener.onSetTimeForRecordValidClick(SetTimeRecordDialog.this, timeForRecord.getValue());
                            dismiss();
                        } catch (Exception e) {

                        }
                    }
                });
    }

}
