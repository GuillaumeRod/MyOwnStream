package polyteam.myownstetho.Activities.ActivityDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import polyteam.myownstetho.HandlerTCP.AsyncResponseAudio;
import polyteam.myownstetho.HandlerTCP.CommandTCP.GetAudio;
import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;

/**
 * Created by Tristan on 09/03/2016.
 */

public class SavePathoDialog extends DialogFragment implements AsyncResponseAudio {
    AlertDialog myDialog;
    private SavePathoDialogListener mListener;
    private View dialogView;
    private MediaPlayer mySound;
    private Button playButton;
    private EditText Name;
    private AlertDialog.Builder builder;
    private CharSequence[] typePatho = {"Cardiaque","Pulmonaire", "Vasculaire","Autre"};
    private Button buttonValid;
    private Button buttonNo;

    private boolean musicGotten = false;

    /*
    public static SavePathoDialog newInstance(String file) {

        Bundle args = new Bundle();
        args.putString("file", file);
        myDialogFragment.setArguments(args);
        return myDialogFragment;
    }*/

    public static SavePathoDialog newInstance(){
        SavePathoDialog myDialogFragment = new SavePathoDialog();
        return myDialogFragment;
    }

    public interface SavePathoDialogListener {
        public void onDialogValidClick(DialogFragment dialog, String value);
        public void onDialogTypeClick(DialogFragment dialog, String value);
        public void onDialogCancelClick(DialogFragment dialog);
    }




    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SavePathoDialogListener) activity;
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


    public void processFinishGetAudio(String output){
        buttonNo.setEnabled(true);
        if(output.equals("Ok_GetAudio")){
            Log.e("SavePathoDialog", "Reception du fichier audio terminée");
            musicGotten=true;
            buttonValid.setEnabled(true);
            mySound = MediaPlayer.create(getActivity().getApplicationContext(), Uri.parse(getActivity().getApplicationContext().getFilesDir().getAbsolutePath()+ "/temp.wav"));
            setPlayButtonListeners();
        } else {
            Log.e("SavePathoDialog", "Problème lors de la reception du fichier");
            Toast.makeText(getActivity().getApplicationContext(), "Problème de reception du fichier", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e("SavePathoDialog", "Back button pressed");
        // Activation du listener pour annuler l'enregistrement
        mListener.onDialogCancelClick(SavePathoDialog.this);
    }


    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    public void setObjectsViews(){
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_patho, null);
        Name = (EditText) dialogView.findViewById(R.id.nom_patho);
        playButton = (Button) dialogView.findViewById(R.id.playButton);
    }

    public void setUI() {
        builder.setTitle(R.string.insert_patho_title)
                .setView(dialogView) // Mise en place du layout
                .setSingleChoiceItems(typePatho, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        String value = typePatho[arg1].toString();
                        mListener.onDialogTypeClick(SavePathoDialog.this, value);
                    }
                })
                .setPositiveButton(R.string.valid_insert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogValidClick(SavePathoDialog.this, Name.getText().toString());
                        try {
                            mySound.release();
                            dismiss();
                        } catch (Exception e) {

                        }

                    }
                })
                .setNegativeButton(R.string.annuler_insert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //SavePathoDialog.this.getDialog().cancel();
                        mListener.onDialogCancelClick(SavePathoDialog.this);
                        try {
                            mySound.release();
                            dismiss();
                        } catch (Exception e) {

                        }

                    }
                });
    }




    public void onStart(){
        super.onStart();
        GetAudio asyncTask =new GetAudio(getActivity().getApplicationContext());
        asyncTask.delegateResponse=this;
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        buttonValid = myDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        buttonValid.setEnabled(false);
        buttonNo = myDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        buttonNo.setEnabled(false);
    }


    public void setPlayButtonListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerPlaySound();
            }
        });
    }


    public void handlerPlaySound(){
        if(mySound.isPlaying()){
            mySound.pause();
            playButton.setText("Play");
            //playButton.setImageDrawable(getActivity().getDrawable(R.drawable.play_button));
        }else{
            mySound.start();
            mySound.setLooping(true);
            playButton.setText("Pause");
        }
    }



}