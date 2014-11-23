package de.ruf2.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
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
            holder.name = (TextView) newView.findViewById(R.id.picture_name);
            newView.setTag(holder);
        }else{
            holder = (ViewHolder) newView.getTag();
        }

        SelfieRecord curr = list.get(i);
        mCurrentPhotoPath = curr.getPictureUri().getPath();
        mImageView = holder.picture;
        setPic();
        holder.name.setText(curr.getPictureUri().getLastPathSegment());
        return newView;
    }

    static class ViewHolder {

        ImageView picture;
        TextView name;
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

    private List<SelfieRecord> getRecords(File storageDirectory) {
        List<SelfieRecord> records = new ArrayList<SelfieRecord>();
        for(File f : storageDirectory.listFiles()){
            SelfieRecord rec = new SelfieRecord(f, f.getName());
            add(rec);
            records.add(rec);
        }
        return records;

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
        int targetW = 160;//mImageView.getWidth();
        int targetH = 120;//mImageView.getHeight();

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
