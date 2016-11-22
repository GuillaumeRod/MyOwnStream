package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import polyteam.myownstetho.HandlerTCP.AsyncResponseStringList;
import polyteam.myownstetho.HandlerTCP.CommandTCP.GetButtonsDescription;
import polyteam.myownstetho.R;

/**
 * Created by Tristan on 10/04/2016.
 */

public class FloatButtonsDialog extends DialogFragment implements AsyncResponseStringList{
    AlertDialog myDialog;
    private View dialogView;
    private AlertDialog.Builder builder;
    private Button buttonValid;
    private ListView buttonsDescription;


    public static FloatButtonsDialog newInstance(){
        FloatButtonsDialog myDialogFragment = new FloatButtonsDialog();
        return myDialogFragment;
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


    public void processFinishGetStringList(ArrayList<String> List){
        if(List.isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(), "Problème de reception des descriptions", Toast.LENGTH_LONG).show();
            dismiss();
        }else{
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1, List);
            buttonsDescription.setAdapter(adapter);
        }
        buttonValid.setEnabled(true);
    }


    public void setObjectsViews(){
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.buttons_description, null);
        buttonsDescription = (ListView) dialogView.findViewById(R.id.buttonsDescription);
    }

    public void setUI() {
        builder.setTitle(R.string.buttons_description)
                .setView(dialogView)
                .setPositiveButton(R.string.valid_insert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            dismiss();
                        } catch (Exception e) {

                        }
                    }
                });
    }




    public void onStart(){
        super.onStart();
        GetButtonsDescription asyncTask =new GetButtonsDescription(getActivity().getApplicationContext());
        asyncTask.delegateResponse =this;
        asyncTask.execute();
        buttonValid = myDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        buttonValid.setEnabled(false);
    }


}
