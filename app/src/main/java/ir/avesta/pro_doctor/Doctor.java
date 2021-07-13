package ir.avesta.pro_doctor;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {
    private String name = "";
    private String address = "";
    private String speciality = "";
    private Reservation reservation = null;

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Doctor(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public Doctor(String name, String address, String speciality, Reservation reservation) {
        this.name = name;
        this.address = address;
        this.speciality = speciality;
        this.reservation = reservation;
    }

    public Doctor() {  }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ContentValues toContentValues() {
        ContentValues result = new ContentValues();

        result.put("name", this.getName());
        result.put("address", this.getAddress());
        result.put("speciality", this.getSpeciality());
        result.put("reservation", this.getReservation().toString());

        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(speciality);
        dest.writeString(reservation.toString());
    }

    public Doctor(Parcel parcel) {
        this.name = parcel.readString();
        this.address = parcel.readString();
        this.speciality = parcel.readString();
        this.reservation = Converter.stringToReservation(parcel.readString());
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Doctor item = (Doctor) o;
        return this.getName().equals(item.getName()) &&
                this.getAddress().equals(item.getAddress()) &&
                this.getSpeciality().equals(item.getSpeciality()) &&
                this.getReservation().equals(item.getReservation());
    }
}