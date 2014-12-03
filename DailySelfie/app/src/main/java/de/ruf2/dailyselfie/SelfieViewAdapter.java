package de.ruf2.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by becks on 19.11.2014.
 */
public class SelfieViewAdapter extends BaseAdapter {

    private ArrayList<SelfieRecord> list =  new ArrayList<SelfieRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    private String mCurrentPhotoPath;
    private ImageView mImageView;

    public SelfieViewAdapter(Context mContext) {
        inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            newView = inflater.inflate(R.layout.selfie_list_item, null);
            holder.picture = (ImageView) newView.findViewById(R.id.thumbnail);
            holder.dateTaken = (TextView) newView.findViewById(R.id.date_taken);
            newView.setTag(holder);
        }else{
            holder = (ViewHolder) newView.getTag();
        }

        SelfieRecord curr = list.get(i);
        mCurrentPhotoPath = curr.getPictureUri().getPath();
        mImageView = holder.picture;
        setPic();
        holder.dateTaken.setText("Date: " + curr.getDateTaken());
        return newView;
    }

    static class ViewHolder {

        ImageView picture;
        TextView dateTaken;
    }

    public void addAllViews(File storageDirectory){
        removeAllViews();
        List<SelfieRecord> records = new ArrayList<SelfieRecord>();
        if(storageDirectory.listFiles() != null){
                for(File f : storageDirectory.listFiles()) {
                    SelfieRecord rec = new SelfieRecord(Uri.fromFile(f));
                    add(rec);
                    records.add(rec);
                }
        }
    }


    public void add(SelfieRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public void removeAllViews(){
        list.clear();
        this.notifyDataSetChanged();
    }

    private void setPic() {
        // Get the dimensions of the View
        //TODO: use non static vaules
        int targetW = mImageView.getLayoutParams().width;
        int targetH = mImageView.getLayoutParams().height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        mImageView.setImageBitmap(bitmap);

        //get pic rotation
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                mImageView.setRotation(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                mImageView.setRotation(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                mImageView.setRotation((270));
                break;
        }
    }
}
