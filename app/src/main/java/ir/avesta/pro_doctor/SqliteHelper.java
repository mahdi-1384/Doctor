package ir.avesta.pro_doctor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {
    private static SqliteHelper INSTANCE = null;
    private static String DOCTORS_TABLE = "DOCTORS";

    public static synchronized SqliteHelper getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new SqliteHelper(context.getApplicationContext());

        return INSTANCE;
    }

    public SqliteHelper(@Nullable Context context) {
        super(context, "mySqliteHelper", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DOCTORS_TABLE +
                " (name VARCHAR, address VARCHAR, speciality VARCHAR, reservation VARCHAR)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DOCTORS_TABLE);
            onCreate(db);
        }
    }

    public long insert(Doctor item) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(DOCTORS_TABLE, null, item.toContentValues());
        db.close();

        return result;
    }

    public ArrayList<Doctor> getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Doctor> result = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DOCTORS_TABLE, null);

        if (cursor.moveToFirst()) {

            do {

                Doctor doctor = new Doctor();

                doctor.setName(cursor.getString(cursor.getColumnIndex("name")));
                doctor.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                doctor.setSpeciality(cursor.getString(cursor.getColumnIndex("speciality")));
                doctor.setReservation(Converter.stringToReservation(cursor.getString(cursor.getColumnIndex("reservation"))));

                result.add(doctor);

            } while (cursor.moveToNext());

        }

        Log.d("myAppLog", String.valueOf(result.size()));

        return result;
    }

    public void remove(Doctor item) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DOCTORS_TABLE, "name = ? AND address = ? AND speciality = ? AND reservation = ?",
                new String[]{item.getName(), item.getAddress(),
                        item.getSpeciality(), item.getReservation().toString()});
    }

    public void update(Doctor previous, Doctor updated) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(DOCTORS_TABLE, updated.toContentValues(),
                "name = ? AND address = ? AND speciality = ? AND reservation = ?",
                new String[]{
                        previous.getName(),
                        previous.getAddress(),
                        previous.getSpeciality(),
                        previous.getReservation().toString()
                });
    }
}
