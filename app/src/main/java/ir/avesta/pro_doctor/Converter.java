package ir.avesta.pro_doctor;

import android.content.Context;

public class Converter {
    public static Reservation stringToReservation(String input) {
        switch (input) {
            case "morning":
                return Reservation.morning;

            case "noon":
                return Reservation.noon;

            case "afternoon":
                return Reservation.afternoon;
        }
        return null;
    }

    public static String reservationToPersianString(Context context, Reservation reservation) {
        switch (reservation) {
            case morning:
                return context.getString(R.string.morning);

            case noon:
                return context.getString(R.string.noon);

            case afternoon:
                return context.getString(R.string.afternoon);
        }

        return null;
    }
}
