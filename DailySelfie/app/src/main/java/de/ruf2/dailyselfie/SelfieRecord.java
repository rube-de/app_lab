package de.ruf2.dailyselfie;

import android.net.Uri;

import java.io.File;

/**
 * Created by Bernhard Ruf on 19.11.2014.
 */
public class SelfieRecord {

    private File pictureFile;
    private String mName;
    private Uri pictureUri;

    private String mImage_File_Path;

    private String mDateTaken;
    private String mImageFileName;

    public SelfieRecord(){

    }

    SelfieRecord(File file, String name){
        this.pictureFile = file;
        this.mName = name;
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

    public String getName() {
        return mName;
    }

    public void setName(String pictureName) {
        this.mName = pictureName;
    }

    public Uri getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }

    public String getImage_File_Path()
    {
        return mImage_File_Path;
    }

    public void setImage_File_Path(String filePath)
    {
        this.mImage_File_Path = filePath;
    }

    public String getImageFileName()
    {
        return mImageFileName;
    }

    public void setImageFileName(String imageFileName)
    {
        this.mImageFileName = imageFileName;
    }

    public String getDateTaken()
    {
        return mDateTaken;
    }

    public void setDateTaken(String dateTaken)
    {
        this.mDateTaken = dateTaken;
    }


    @Override
    public String toString()
    {
        return "Date: " + mDateTaken + " Image: " + mImageFileName;

    }
}
