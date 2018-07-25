package com.example.android.busbookings.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SeatModel {

    String seatNo;

    public SeatModel() {
    }

    public SeatModel(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
}
