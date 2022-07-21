package net.basicmodel.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomFiledEntity implements Parcelable {

    String name;
    String content;

    public CustomFiledEntity() {
    }


    public CustomFiledEntity(String name, String content) {
        this.name = name;
        this.content = content;
    }

    protected CustomFiledEntity(Parcel in) {
        name = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(content);
    }

    public static final Creator<CustomFiledEntity> CREATOR = new Creator<CustomFiledEntity>() {

        @Override
        public CustomFiledEntity createFromParcel(Parcel parcel) {
            return new CustomFiledEntity(parcel);
        }

        @Override
        public CustomFiledEntity[] newArray(int i) {
            return new CustomFiledEntity[i];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
