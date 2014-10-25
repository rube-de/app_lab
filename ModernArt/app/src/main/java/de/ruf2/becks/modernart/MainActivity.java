package de.ruf2.becks.modernart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    //MOMA URL
    static private final String URL = "http://www.moma.org";
    // For use with app chooser
    static private final String CHOOSER_TEXT = "Load " + URL + " with:";

    private List<ViewColor> mList;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create list of changeable views
        mList = new ArrayList<ViewColor>();
        TextView mView1 = (TextView) findViewById(R.id.column1Field1);
        int a = mView1.getDrawingCacheBackgroundColor();
        ViewColor mViewColor1 = new ViewColor(mView1, 260, 70);
        mList.add(mViewColor1);

        TextView mView2 = (TextView) findViewById(R.id.column1Field2);
        ViewColor mViewColor2 = new ViewColor(mView2, 300, 50);
        mList.add(mViewColor2);

        TextView mView3 = (TextView) findViewById(R.id.column2Field1);
        ViewColor mViewColor3 = new ViewColor(mView3, 0, 60);
        mList.add(mViewColor3);

        //TextView 4 stays white

        TextView mView5 = (TextView) findViewById(R.id.column2Field3);
        ViewColor mViewColor5 = new ViewColor(mView5, 240, 90);
        mList.add(mViewColor5);


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                updateBackground();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

    }

    //update background depending on progress from seekbar
    private void updateBackground() {
        int progress = seekBar.getProgress();
        for(ViewColor vc : mList) {
            vc.updateColor(progress);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openMoreInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openMoreInfo() {
        AlertDialog builder = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_msg)
                .setPositiveButton(R.string.visit_moma, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                        Intent chooserIntent = Intent.createChooser(baseIntent, CHOOSER_TEXT);
                        startActivity(chooserIntent);
                    }
                })
                .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
