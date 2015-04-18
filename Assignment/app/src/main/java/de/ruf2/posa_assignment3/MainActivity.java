package de.ruf2.posa_assignment3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends LifecycleLoggingActivity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * A value that uniquely identifies the request to download an
     * image.
     */
    private static final int DOWNLOAD_IMAGE_REQUEST = 1;

    /**
     * A value that uniquely identifies the request to filter an
     * image.
     */
    private static final int FILTER_IMAGE_REQUEST = 2;

    /**
     * EditText field for entering the desired URL to an image.
     */
    private EditText mUrlEditText;

    /**
     * URL for the image that's downloaded by default if the user
     * doesn't specify otherwise.
     */
    private Uri mDefaultUrl =
            Uri.parse("http://www.dre.vanderbilt.edu/~schmidt/robot.png");

    /**
     * key for save/store state
     */
    private static String URI = "uri";

    /**
     * boolean to keeps track if download button is processed or not
     */
    private boolean mProcessButtonClick = true;

    /**
     *
     * @param savedInstanceState object that contains saved state information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        // Set the default layout.
        setContentView(R.layout.activity_main);


        // Cache the EditText that holds the urls entered by the user
        // (if any).
        mUrlEditText = (EditText) findViewById(R.id.url);
    }

    /**
     * Called by the Android Activity framework when the user clicks
     * the "Find Address" button.
     *
     * @param view The view.
     */
    public void downloadImage(View view) {
        try {
            // Hide the keyboard.
            hideKeyboard(this,
                    mUrlEditText.getWindowToken());

            // Call the makeDownloadImageIntent() factory method to
            // create a new Intent to an Activity that can download an
            // image from the URL given by the user.  In this case
            // it's an Intent that's implemented by the
            // DownloadImageActivity.
            startDownloadImageActivity(getUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startDownloadImageActivity(Uri url) {

        if(url != null){
            if(mProcessButtonClick == false){
                Utils.showToast(this, "Already downloading image" + url);
            } else{
                mProcessButtonClick = false;

                Intent intent = makeDownloadImageIntent(url);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, DOWNLOAD_IMAGE_REQUEST);
                }
            }
        }
        return;
    }


    /**
     * Hook method called back by the Android Activity framework when
     * an Activity that's been launched exits, giving the requestCode
     * it was started with, the resultCode it returned, and any
     * additional data from it.
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        // Check if the started Activity completed successfully.
        if (resultCode == RESULT_OK) {
            // Check if the request code is what we're expecting.
            if (requestCode == DOWNLOAD_IMAGE_REQUEST) {
                final Intent intent = makeFilterImageIntent(data.getData());
                Log.d(TAG, "FILTER_IMAGE_REQUEST startActivityForResult " + data.getDataString());

                startActivityForResult(intent, FILTER_IMAGE_REQUEST);
            }
            if(requestCode == FILTER_IMAGE_REQUEST){


                // Call the makeGalleryIntent() factory method to
                // create an Intent that will launch the "Gallery" app
                // by passing in the path to the downloaded image
                // file.
                Intent intent = makeGalleryIntent(data.getDataString());

                Log.d(TAG, "gallery startActivity() " + data.getDataString());
                // Start the Gallery Activity.
                if (intent != null) {
                    startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(getBaseContext(),
                            "Unable to make gallery intent",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }
        // Check if the started Activity did not complete successfully
        // and inform the user a problem occurred when trying to
        // download contents at the given URL.
        else if (resultCode == RESULT_CANCELED) {
            Toast toast = Toast.makeText(getBaseContext(),
                    "Unable to download image",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        mProcessButtonClick = true;
    }


    /**
     * Factory method that returns an implicit Intent for viewing the
     * downloaded image in the Gallery app.
     */
    private Intent makeGalleryIntent(String pathToImageFile) {
        // Create an intent that will start the Gallery app to view
        // the image.
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(pathToImageFile)),"image/*");
        return intent;
    }

    /**
     * Factory method that returns an implicit Intent for downloading
     * an image.
     */
    private Intent makeDownloadImageIntent(Uri url) {
        // Create an intent that will download the image from the web.

        //Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH, url);
        return new Intent(DownloadImageActivity.ACTION_DOWNLOAD_IMAGE, url);
    }

    private Intent makeFilterImageIntent(Uri url) {
        // Create an intent that will download the image from the web.

        //Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH, url);
        return new Intent(FilterImageActivity.ACTION_FILTER_IMAGE).setDataAndType(url, "image/*");
    }

    /**
     * Get the URL to download based on user input.
     */
    protected Uri getUrl() {
        Uri url = null;

        // Get the text the user typed in the edit text (if anything).
        url = Uri.parse(mUrlEditText.getText().toString());

        // If the user didn't provide a URL then use the default.
        String uri = url.toString();
        if (uri == null || uri.equals(""))
            url = mDefaultUrl;

        // Do a sanity check to ensure the URL is valid, popping up a
        // toast if the URL is invalid.
        // proper code.
        if (Patterns.WEB_URL.matcher(url.toString()).matches()){
            return url;
        } else {
            Toast.makeText(this,
                    "Invalid URL",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public void hideKeyboard(Activity activity,
                             IBinder windowToken) {
        InputMethodManager mgr =
                (InputMethodManager) activity.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
