package de.ruf2.dailyselfie;

import android.net.Uri;

import java.io.File;

/**
 * Created by becks on 19.11.2014.
 */
public class SelfieRecord {

    private File pictureFile;
    private String pictureName;
    private Uri pictureUri;

    SelfieRecord(File file, String name){
        this.pictureFile = file;
        this.pictureName = name;
    }
    SelfieRecord(File file){
        this.pictureFile = file;
    }
    SelfieRecord(Uri uri){
        this.pictureUri = uri;
    }

    public File getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(File pictureFile) {
        this.pictureFile = pictureFile;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public Uri getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }
}
