package de.ruf2.dailyselfie;

import java.io.File;

/**
 * Created by becks on 19.11.2014.
 */
public class SelfieRecord {

    private File pictureFile;
    private String pictureName;

    SelfieRecord(File file, String name){
        this.pictureFile = file;
        this.pictureName = name;
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
}
