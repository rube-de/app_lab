package de.ruf2.becks.modernart;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by becks on 24.10.2014.
 */
public class ViewColor {

    private TextView view;
    private int startColor;
    private int endColor;

    public ViewColor(TextView view, int color1, int color2){
        this.view = view;
        this.startColor = color1;
        this.endColor = color2;
    }

    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    public void updateColor(int progress){
        float[] hsvColor = {0, 1, 1};
        float diff = startColor - endColor;
        if(diff > 0){
            //hue value is less than start value
            hsvColor[0] = startColor -(diff * progress / 100);
        }else{
            //hue value is higher than start value
            hsvColor[0] = startColor +Math.abs(diff * progress / 100);
        }
        view.setBackgroundColor(Color.HSVToColor(hsvColor));

    }
}
