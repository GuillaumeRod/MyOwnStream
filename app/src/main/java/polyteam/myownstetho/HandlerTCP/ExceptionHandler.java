package polyteam.myownstetho.HandlerTCP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import polyteam.myownstetho.MyApp;

/**
 * Created by Tristan on 28/04/2016.
 */
public class ExceptionHandler extends AsyncTask<Void, Void, Void>
{
    Context mContext;

    public ExceptionHandler(Context context){
        super();
        this.mContext=context;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{
            if(!((MyApp) mContext).getSocket().getInetAddress().isReachable(5000)){
                Log.d("Handlers", "Non atteignable");
                ((MyApp) mContext).resetApp(mContext);
            }
        }catch (Exception e){
            Log.d("Handlers", "Exception socket");
            ((MyApp) mContext).resetApp(mContext);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }
}
