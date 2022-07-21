package net.basicmodel.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class NetDetailsEntity implements Parcelable {
    String netDetailsBoardName = "";
    String netDetailsPort1 = "";
    String netDetailsPort2 = "";
    int index = 0;

    public NetDetailsEntity() {
    }

    public NetDetailsEntity(String netDetailsBoardName, String netDetailsPort1, String netDetailsPort2, int index) {
        this.netDetailsBoardName = netDetailsBoardName;
        this.netDetailsPort1 = netDetailsPort1;
        this.netDetailsPort2 = netDetailsPort2;
        this.index = index;
    }

    protected NetDetailsEntity(Parcel in) {
        netDetailsBoardName = in.readString();
        netDetailsPort1 = in.readString();
        netDetailsPort2 = in.readString();
        index = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(netDetailsBoardName);
        parcel.writeString(netDetailsPort1);
        parcel.writeString(netDetailsPort2);
        parcel.writeInt(index);
    }

    public static final Creator<NetDetailsEntity> CREATOR = new Creator<NetDetailsEntity>() {
        @Override
        public NetDetailsEntity createFromParcel(Parcel parcel) {
            return new NetDetailsEntity(parcel);
        }

        @Override
        public NetDetailsEntity[] newArray(int i) {
            return new NetDetailsEntity[i];
        }
    };

    public String getNetDetailsBoardName() {
        return netDetailsBoardName;
    }

    public void setNetDetailsBoardName(String netDetailsBoardName) {
        this.netDetailsBoardName = netDetailsBoardName;
    }

    public String getNetDetailsPort1() {
        return netDetailsPort1;
    }

    public void setNetDetailsPort1(String netDetailsPort1) {
        this.netDetailsPort1 = netDetailsPort1;
    }

    public String getNetDetailsPort2() {
        return netDetailsPort2;
    }

    public void setNetDetailsPort2(String netDetailsPort2) {
        this.netDetailsPort2 = netDetailsPort2;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
