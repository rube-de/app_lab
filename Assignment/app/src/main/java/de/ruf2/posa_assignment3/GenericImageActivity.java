package de.ruf2.posa_assignment3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by Bernhard Ruf on 16.04.2015.
 */
public abstract class GenericImageActivity extends LifecycleLoggingActivity {
    /**
     * display progress
     */
    private ProgressBar mLoadingProgressBar;

    /**
     * retain state information between config change
     */
    protected RetainedFragmentManager mRetainedFramentManager =
            new RetainedFragmentManager(this, "GenericImageActivityTag");

    /**
     * constansts for retainframentmanager
     */
    private static String URL = "url";
    private static String IMAGEPATH = "imagePath";
    private static String ASYNCTASK = "asyncTask";

    abstract  protected Uri doInBackgroundHook(Context context, Uri uri);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.generic_image_activity);

        mLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_loading);

        if (mRetainedFramentManager.firstTimeIn()) {
            mRetainedFramentManager.put(URL, getIntent().getData());
            Log.d(TAG, "first time onCreate " + (Uri) mRetainedFramentManager.get(URL));

        } else {
            Log.d(TAG, "second time onCreate " + (Uri) mRetainedFramentManager.get(URL));
            Uri pathToImage = mRetainedFramentManager.get(IMAGEPATH);
            if (pathToImage != null) {
                Log.d(TAG, "finishing actvity " + pathToImage);
                setActivityResult(pathToImage);
                finish();
            } else {
                Log.d(TAG, "continue since result not ready");
            }
        }
    }

    private void setActivityResult(Uri pathToImage) {
        if (pathToImage == null){
            setResult(RESULT_CANCELED);
        }else{
            setResult(RESULT_OK, new Intent("", pathToImage));
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        GenericAsyncTask asyncTask = mRetainedFramentManager.get(ASYNCTASK);

        if(asyncTask == null){
            Log.d(TAG, " on Start() creating and executing an AsyncTask");
            mRetainedFramentManager.put(ASYNCTASK,new GenericAsyncTask().execute((Uri) mRetainedFramentManager.get(URL)));
        }else {
            Log.d(TAG, " onStart() not executing a new AsyncTask");
        }
    }

    public class GenericAsyncTask extends AsyncTask<Uri, Void, Uri>{

        public GenericAsyncTask(){
        }

        @Override
        protected  void onPreExecute(){
            Log.d(TAG, "in onPreExecute");
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Uri doInBackground(Uri... urls) {
            Log.d(TAG, "in doBackground()");

            try {
                return doInBackgroundHook(getApplicationContext(), urls[0]);
            }catch (Exception e){
                Utils.showToast(getApplicationContext(), "async task failed");
                cancel(true);
            }
            return null;
        }

        @Override
        protected  void onPostExecute(Uri result){
            Log.d(TAG, "in onPostExecute");
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            setActivityResult(result);
            finish();
        }
    }
}
