package com.subzero.shares.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * OptionalExperAnalysisRecordBean顾问诊股记录第二层Bean
 * Created by The_p on 2016/5/4.
 */
public class OptionalExperAnalysisRecordDataBean implements Parcelable {


    private String id;
    private String title;
    private String images;
    private String time;
    private String content;

    public OptionalExperAnalysisRecordDataBean() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.images);
        dest.writeString(this.time);
        dest.writeString(this.title);
        dest.writeString(this.id);
        dest.writeString(this.content);
    }


    protected OptionalExperAnalysisRecordDataBean(Parcel in) {
        this.images = in.readString();
        this.time = in.readString();
        this.title = in.readString();
        this.id = in.readString();
        this.content = in.readString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<OptionalExperAnalysisRecordDataBean> CREATOR = new Parcelable.Creator<OptionalExperAnalysisRecordDataBean>() {
        @Override
        public OptionalExperAnalysisRecordDataBean createFromParcel(Parcel source) {
            return new OptionalExperAnalysisRecordDataBean(source);
        }

        @Override
        public OptionalExperAnalysisRecordDataBean[] newArray(int size) {
            return new OptionalExperAnalysisRecordDataBean[size];
        }
    };
}

