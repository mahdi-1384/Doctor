package ir.avesta.pro_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorDetailsActivity extends AppCompatActivity {

    private TextView nameTv, specialityTv, addressTv, reservationTv;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        init();

        Doctor doctor = getIntent().getParcelableExtra(RecyclerAdapter.doctorKey);
        nameTv.setText(doctor.getName());
        addressTv.setText(doctor.getAddress());
        specialityTv.setText(doctor.getSpeciality());
        reservationTv.setText(Converter.reservationToPersianString(this, doctor.getReservation()));
    }

    public void onReserveBtnClicked(View view) {
        Toast.makeText(this, getString(R.string.successfully_done), Toast.LENGTH_SHORT).show();
        finish();
    }

    public void init() {
        nameTv = findViewById(R.id.nameTv);
        specialityTv = findViewById(R.id.specialityTv);
        addressTv = findViewById(R.id.addressTv);
        reservationTv = findViewById(R.id.reservationTv);
    }
}