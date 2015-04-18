package de.ruf2.posa_assignment3;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends GenericImageActivity {
    public static String ACTION_DOWNLOAD_IMAGE = "vandy.mooc.action.DOWNLOAD_IMAGE";

    @Override
    protected Uri doInBackgroundHook(Context context, Uri url) {
        Log.d(TAG, "downloading " + url.toString());

        return Utils.downloadImage(context, url);
    }

}