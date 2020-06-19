package com.example.bios.musicalstructure;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

public class Audio implements Parcelable,Serializable {

    private String aPath;
    private String aName;
    private String aAlbum;
    private String aArtist;
    private Bitmap albumArt;
    private Long aDuration;

    protected Audio(Parcel in) {
        aPath = in.readString();
        aName = in.readString();
        aAlbum = in.readString();
        aArtist = in.readString();
        albumArt = in.readParcelable(Bitmap.class.getClassLoader());
        aDuration = in.readLong();
    }
    public Audio(String apath,String aName,String aAlbum,String aArtist,Bitmap ablumArt,Long aDuration){
        this.aPath=apath;
        this.aName=aName;
        this.aAlbum=aAlbum;
        this.aArtist=aArtist;
        this.albumArt=ablumArt;
        this.aDuration=aDuration;
    }
    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };

    public String getaPath() {
        return aPath;
    }

    public void setaPath(String aPath) {
        this.aPath = aPath;
    }

    public Long getDuration() {
        return aDuration;
    }

    public void setDuration(Long duration) {
        this.aDuration = duration;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaAlbum() {
        return aAlbum;
    }

    public void setaAlbum(String aAlbum) {
        this.aAlbum = aAlbum;
    }

    public String getaArtist() {
        return aArtist;
    }

    public void setaArtist(String aArtist) {
        this.aArtist = aArtist;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aPath);
        dest.writeString(aName);
        dest.writeString(aAlbum);
        dest.writeString(aArtist);
        dest.writeParcelable(albumArt, flags);
        dest.writeLong(aDuration);
    }
}