package de.ruf2.posa_assignment3;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Bernhard Ruf on 16.04.2015.
 */
public class FilterImageActivity extends GenericImageActivity {
    public static String ACTION_FILTER_IMAGE = "vandy.mooc.action.FILTER_IMAGE";

    @Override
    protected Uri doInBackgroundHook(Context context, Uri url) {
        Log.d(TAG, "filtering " + url.toString());

        return Utils.grayScaleFilter(context, url);
    }
}
