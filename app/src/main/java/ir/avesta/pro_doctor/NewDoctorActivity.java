package ir.avesta.pro_doctor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class NewDoctorActivity extends AppCompatActivity {

    private EditText nameEdt, addressEdt, specialityTv;
    private RadioGroup radioGroup;
    private Button addBtn;
    private ImageView saveImg;
    public static String doctorKey = "ir.asadtabadkan.afsoon.NewDoctorActivity.doctorKey";
    public static String previousDoctorKey = "ir.asadtabadkan.afsoon.NewDoctorActivity.previousDoctorKey";
    public static String updatedDoctorKey = "ir.asadtabadkan.afsoon.NewDoctorActivity.updatedDoctorKey";
    public static String itemPositionKey = "ir.asadtabadkan.afsoon.NewDoctorActivity.itemPositionKey";
    private Doctor doctorToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_doctor);

        init();

        doctorToShow = getIntent().getParcelableExtra(MainActivity.doctorKey);
        if (doctorToShow != null) {
            nameEdt.setText(doctorToShow.getName());
            addressEdt.setText(doctorToShow.getAddress());
        }

        if (doctorToShow == null) {
            saveImg.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);

        } else {
            saveImg.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);
        }
    }

    public void init() {
        nameEdt = findViewById(R.id.nameEdt);
        addressEdt = findViewById(R.id.addressEdt);
        specialityTv = findViewById(R.id.specialityEdt);
        addBtn = findViewById(R.id.addBtn);
        saveImg = findViewById(R.id.saveImg);
        radioGroup = findViewById(R.id.radioGroup);
    }

    @SuppressLint("NonConstantResourceId")
    public void onAddBtnClicked(View view) {

        Doctor toInsert = new Doctor(
                nameEdt.getText().toString(),
                addressEdt.getText().toString(),
                specialityTv.getText().toString(),
                getCheckedRbt()
                );

        Intent intent = new Intent();
        intent.putExtra(doctorKey, toInsert);

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public void onSaveImgClicked(View view) {
        Intent intent = new Intent();

        intent.putExtra(previousDoctorKey, doctorToShow);
        intent.putExtra(updatedDoctorKey, new Doctor(
                nameEdt.getText().toString(),
                addressEdt.getText().toString(),
                specialityTv.getText().toString(),
                getCheckedRbt()
        ));

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public Reservation getCheckedRbt() {
        Reservation checkedRbtn = null;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.morningRbtn:
                checkedRbtn = Reservation.morning;
                break;

            case R.id.noonRbtn:
                checkedRbtn = Reservation.noon;
                break;

            case R.id.afternoonRbtn:
                checkedRbtn = Reservation.afternoon;
                break;
        }

        return checkedRbtn;
    }
}