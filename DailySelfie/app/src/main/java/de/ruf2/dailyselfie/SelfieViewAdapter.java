package de.ruf2.dailyselfie;

import android.content.Context;
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
        }



        return null;
    }

    static class ViewHolder {

        ImageView picture;
        TextView name;
    }
}
