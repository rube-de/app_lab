package de.becksr.lautlosmodus_umschalter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {
    private static final String TAG = "SilentModeApp";

    private AudioManager mAudioManager;
    private boolean mPhoneIsSilent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        checkIfPhoneIsSilent();

        setButtonClickListener();

        Log.d("Tag", "dies ist ein Test");
    }

    /**
     * Feststellen ob sich des Telefon im Lautlosmodus befindet
     */
    private void checkIfPhoneIsSilent() {
        int ringerMode = mAudioManager.getRingerMode();
        if(ringerMode == AudioManager.RINGER_MODE_SILENT){
            mPhoneIsSilent = true;
        }else {
            mPhoneIsSilent = false;
        }
    }

    /**
     * UI-Bilder der Modi austasuchen
     */
    private void toggleUI(){
        ImageView imageView = (ImageView) findViewById(R.id.phone_icon);
        Drawable newPhoneImage;

        if(mPhoneIsSilent){
            newPhoneImage = getResources().getDrawable(R.drawable.volume_mute);
        }else{
            newPhoneImage = getResources().getDrawable(R.drawable.volume_on);
        }
        imageView.setImageDrawable(newPhoneImage);
    }


    private void setButtonClickListener() {
        Button toogleButton = (Button) findViewById(R.id.toggleButton);
        toogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPhoneIsSilent){
                    //Umschalten auf normal
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    mPhoneIsSilent = false;
                }else {
                    //Umschalten auf silent
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    mPhoneIsSilent = true;
                }
                //UI wieder umschalten
                toggleUI();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkIfPhoneIsSilent();
        toggleUI();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
