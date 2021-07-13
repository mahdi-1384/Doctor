package ir.avesta.pro_doctor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements RecyclerAdapter.RecyclerAdapterInterface, TextWatcher {

    private RecyclerView recycler;
    private AppCompatEditText searchEdt;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<Doctor> doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        doctors = new SqliteHelper(this).getAllDoctors();
        recyclerAdapter = new RecyclerAdapter(doctors, this);
        recycler.setAdapter(recyclerAdapter);

        searchEdt.addTextChangedListener(this);
    }

    private ActivityResultLauncher<Intent> editActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();

                        Doctor previous = data.getParcelableExtra(NewDoctorActivity.previousDoctorKey);
                        Doctor updated = data.getParcelableExtra(NewDoctorActivity.updatedDoctorKey);

                        SqliteHelper sqliteHelper = SqliteHelper.getInstance(SearchActivity.this);
                        sqliteHelper.update(previous, updated);

                        int position = doctors.indexOf(previous);
                        doctors.set(position, updated);
                        recyclerAdapter.notifyItemChanged(position);
                    }
                }
            }
    );

    public void init() {
        recycler = findViewById(R.id.recycler);
        searchEdt = findViewById(R.id.searchEdt);
    }

    //the editImg of recyclerView item clicked
    @Override
    public void onEditClicked(int position) {
        Doctor item = doctors.get(position);

        Intent intent = new Intent(this, NewDoctorActivity.class);
        intent.putExtra(MainActivity.doctorKey, item);
        editActivity.launch(intent);
    }

    @Override
    public void afterTextChanged(Editable s) {
        recyclerAdapter.getFilter().filter(s.toString());
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}