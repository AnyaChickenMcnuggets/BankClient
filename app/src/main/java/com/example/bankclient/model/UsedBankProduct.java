package com.example.bankclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UsedBankProduct implements Parcelable {
    String id, title, time, percentage, sum, startDate;
    Boolean isIncome;

    public UsedBankProduct(BankProduct bp, String sum, String startDate) {
        this.id = bp.getId();
        this.title = bp.getTitle();
        this.time = bp.getTime();
        this.percentage = bp.getPercentage();
        this.sum = sum;
        this.startDate = startDate;
        this.isIncome = bp.getIncome();
    }

    protected UsedBankProduct(Parcel in) {
        id = in.readString();
        title = in.readString();
        time = in.readString();
        percentage = in.readString();
        sum = in.readString();
        startDate = in.readString();
        byte tmpIsIncome = in.readByte();
        isIncome = tmpIsIncome == 0 ? null : tmpIsIncome == 1;
    }

    public static final Creator<UsedBankProduct> CREATOR = new Creator<UsedBankProduct>() {
        @Override
        public UsedBankProduct createFromParcel(Parcel in) {
            return new UsedBankProduct(in);
        }

        @Override
        public UsedBankProduct[] newArray(int size) {
            return new UsedBankProduct[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Boolean getIncome() {
        return isIncome;
    }

    public void setIncome(Boolean income) {
        isIncome = income;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(time);
        parcel.writeString(percentage);
        parcel.writeString(sum);
        parcel.writeString(startDate);
        parcel.writeByte((byte) (isIncome == null ? 0 : isIncome ? 1 : 2));
    }
}
