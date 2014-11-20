package de.ruf2.dailyselfie;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfieViewActivity extends ListActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "Daily Selfie";

    private SelfieViewAdapter mAdapter;
    private String mCurrentPhotoPath;
    private String STORAGE_PATH = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES) + File.separator + "DailySelfie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_selfie_overview);
        final ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "click on list item");
//                Toast.makeText(getApplicationContext(), "Galleyview not implemented", Toast.LENGTH_LONG).show();
                Object item = mAdapter.getItem(position);
                if(item instanceof SelfieRecord) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(((SelfieRecord) item).getPictureUri(), "image/*");
                    startActivity(intent);
                }
            }
        });

        mAdapter = new SelfieViewAdapter(getApplicationContext());
//        mAdapter.addAllViews(new File(STORAGE_PATH));
        setListAdapter(mAdapter) ;

    }

    @Override
    protected void onResume(){
        super.onResume();
        mAdapter.addAllViews(new File(STORAGE_PATH));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selfie_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
//            case R.id.action_settings:
//                return true;
            case R.id.action_camera:
                Log.i(TAG, "Action Camera clicked");
                dispatchTakePictureIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"Entered onActivityResult()");

        // TODO - Check result code and request code
        // if user took pic
        // Create a new pic from the data Intent
        // and then add it to the adapter
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                galleryAddPic();
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                SelfieRecord record = new SelfieRecord(contentUri);

//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                mImageView.setImageBitmap(imageBitmap);
                Log.i(TAG, "add new Selfie");
                mAdapter.add(record);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(TAG, "Could not take Picture");
                Toast.makeText(getApplicationContext(), "Could not take photo", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(STORAGE_PATH);
        storageDir.mkdir();
        Log.i(TAG,"storageDir: " + storageDir);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
