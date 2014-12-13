package de.ruf2.dailyselfie;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfieViewActivity extends ListActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "Daily Selfie";

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private String STORAGE_PATH = "DailySelfie";

    private SelfieViewAdapter mAdapter;
    private String mCurrentPhotoPath;
    private java.io.File mAlbumDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbumDir = getAlbumDir();
        final ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "click on list item");
                Object item = mAdapter.getItem(position);
                if (item instanceof SelfieRecord) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(((SelfieRecord) item).getPictureUri(), "image/*");
                    startActivity(intent);
                }
            }
        });

        mAdapter = new SelfieViewAdapter(getApplicationContext());
        setListAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
//        mAdapter.addAllViews(new File(STORAGE_PATH));
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
        switch (id) {
            case R.id.action_settings:
                Intent settings = new Intent(SelfieViewActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.action_camera:
                Log.i(TAG, "Action Camera clicked");
                dispatchTakePictureIntent();
                return true;
            case R.id.action_delete:
                Log.i(TAG, "Delete Selfies");
                deleteAllSelfies();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "Entered onActivityResult()");

        // - Check result code and request code
        // if user took pic
        // Create a new pic from the data Intent
        // and then add it to the adapter
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                refreshAdapter();
//                handleCameraPhoto();
            } else {
                File f = new File(mCurrentPhotoPath);
                boolean e = f.exists();
                boolean d = f.delete();
                Log.i(TAG, "Temp file: " + mCurrentPhotoPath + " - is delete " + d);

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
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        Log.i(TAG, "storageDir: " + mAlbumDir);
        File image = File.createTempFile(
                imageFileName,              /* prefix */
                JPEG_FILE_SUFFIX,         /* suffix */
                mAlbumDir               /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void deleteAllSelfies() {
        AlertDialog builder = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_msg)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File dir = mAlbumDir;
                        for (File file : dir.listFiles()) {
                            file.delete();
                        }
                        refreshAdapter();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    private void refreshAdapter() {
        if (mAlbumDir != null) {
            //Clean up the adapter
            mAdapter.removeAllViews();

            File[] files = mAlbumDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.toLowerCase().endsWith(JPEG_FILE_SUFFIX));
                }
            });

            for (File inFile : files) {
                mCurrentPhotoPath = inFile.getAbsolutePath();
                handleCameraPhoto();
            }
        }
    }

    private void handleCameraPhoto() {
        if (mCurrentPhotoPath != null) {
            Log.i(TAG, "add new Selfie");
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);

            SelfieRecord newRecord = new SelfieRecord();

            newRecord.setDateTaken(new Date(f.lastModified()).toString());
            newRecord.setImageFileName(f.getName());
            newRecord.setImage_File_Path(f.getParent());
            newRecord.setPictureUri(contentUri);
            mAdapter.add(newRecord);

            galleryAddPic();
            mCurrentPhotoPath = null;
        }
    }

    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getAlbumName());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d(TAG, "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(TAG, "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return STORAGE_PATH;
    }
}
