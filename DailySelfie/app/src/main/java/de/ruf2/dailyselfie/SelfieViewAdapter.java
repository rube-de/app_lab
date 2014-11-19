package de.ruf2.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by becks on 19.11.2014.
 */
public class SelfieViewAdapter extends BaseAdapter {

    private ArrayList<SelfieRecord> mList =  new ArrayList<SelfieRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    private String mCurrentPhotoPath;
    private ImageView mImageView;

    public SelfieViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View newView = view;
        ViewHolder holder;

        if(newView == null){
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.selfie_list_item, viewGroup);
            holder.picture = (ImageView) newView.findViewById(R.id.thumbnail);
            holder.name = (TextView) newView.findViewById(R.id.picture_name);
            newView.setTag(holder);
        }else{
            holder = (ViewHolder) newView.getTag();
        }

        SelfieRecord curr = mList.get(i);
        mCurrentPhotoPath = curr.getPictureFile().getAbsolutePath();
        holder.picture = mImageView;
        holder.name.setText(curr.getPictureName());

        return newView;
    }

    static class ViewHolder {

        ImageView picture;
        TextView name;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
