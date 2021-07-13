package ir.avesta.pro_doctor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, RecyclerAdapter.RecyclerAdapterInterface {

    private RecyclerView recycler;
    private RecyclerAdapter recyclerAdapter;
    private Toolbar toolbar;
    private TextView stateTv;
    private ImageView managerImg;
    private FloatingActionButton addBtn;
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private SqliteHelper sqliteHelper;
    public static boolean managerModeEnabled = false;
    public static String doctorKey = "ir.avesta.afsoon.MainActivity.doctorKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //get doctors from database
        doctors.addAll(sqliteHelper.getAllDoctors());
        if (doctors.isEmpty()) {
            recycler.setVisibility(View.GONE);
        }
        recyclerAdapter = new RecyclerAdapter(doctors, this);
        recycler.setAdapter(recyclerAdapter);

        toolbar.setOnMenuItemClickListener(this);
    }

    private ItemTouchHelper.Callback itemTouchHelperCallback = new
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();

                    //remove from the database
                    sqliteHelper.remove(doctors.get(position));

                    doctors.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    if (doctors.isEmpty())
                        recycler.setVisibility(View.GONE);
                }

                @Override
                public boolean isItemViewSwipeEnabled() {
                    return managerModeEnabled;
                }
            };

    private ActivityResultLauncher<Intent> loginActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        setManagerMode(true);
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> newDoctorActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        Doctor toInsert = data.getParcelableExtra(NewDoctorActivity.doctorKey);
                        long insertResult = sqliteHelper.insert(toInsert);
                        if (insertResult == -1) {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.data_was_not_saved), Toast.LENGTH_SHORT).show();
                        } else {

                            doctors.add(toInsert);
                            recyclerAdapter.notifyDataSetChanged();

                            recycler.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> editActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();

                        Doctor previous = data.getParcelableExtra(NewDoctorActivity.previousDoctorKey);
                        Doctor updated = data.getParcelableExtra(NewDoctorActivity.updatedDoctorKey);

                        SqliteHelper sqliteHelper = SqliteHelper.getInstance(MainActivity.this);
                        sqliteHelper.update(previous, updated);

                        int position = doctors.indexOf(previous);
                        doctors.set(position, updated);
                        recyclerAdapter.notifyItemChanged(position);
                    }
                }
            }
    );

    public void changeToolbarMenu(int menu) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(menu);
    }

    public void setManagerMode(Boolean isEnabled) {
        recyclerAdapter.notifyDataSetChanged();

        if (isEnabled) {
            stateTv.setText(getString(R.string.managerMode));
            managerImg.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.VISIBLE);
            changeToolbarMenu(R.menu.activity_main_menu_managermode_enabled);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler);

        } else {
            stateTv.setText(getString(R.string.userMode));
            managerImg.setVisibility(View.GONE);
            addBtn.setVisibility(View.GONE);
            changeToolbarMenu(R.menu.activity_main_menu_managermode_disabled);

            Toast.makeText(this, getString(R.string.exited_the_manager_mode), Toast.LENGTH_SHORT).show();
        }

        managerModeEnabled = !managerModeEnabled;
    }

    //the editImg of recyclerView item clicked
    @Override
    public void onEditClicked(int position) {
        Doctor item = doctors.get(position);

        Intent intent = new Intent(this, NewDoctorActivity.class);
        intent.putExtra(doctorKey, item);
        editActivity.launch(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;

            case R.id.loginAsManager:
                loginActivity.launch(new Intent(this, LoginActivity.class));
                break;

            case R.id.signOut:
                setManagerMode(false);
                break;

            case R.id.communicate:
                new CommunicateDialog(new CommunicateDialog.CommunicateDialogInterfaces() {
                    @Override
                    public void onItemClicked(View view) {
                        switch (view.getId()) {

                            case R.id.emailLy:
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL,
                                        new String[] { "afsoonaki22@gmail.com" });
                                emailIntent.setType("message/rfc822");
                                startActivity(emailIntent);
                                break;

                            case R.id.phoneLy:
                                Intent phoneIntent = new Intent(Intent.ACTION_VIEW);
                                phoneIntent.setData(Uri.parse("tel:09154874547"));
                                startActivity(phoneIntent);
                                break;

                        }
                    }
                }).show(getSupportFragmentManager(), "communicateDialog");
                break;

        }

        return true;
    }

    public void onAddBtnClicked(View view) {
        Intent intent = new Intent(this, NewDoctorActivity.class);
        newDoctorActivity.launch(intent);
    }

    public void init() {
        recycler = findViewById(R.id.recycler);
        addBtn = findViewById(R.id.addBtn);
        toolbar = findViewById(R.id.toolbar);
        stateTv = findViewById(R.id.stateTv);
        managerImg = findViewById(R.id.managerImg);
        sqliteHelper = SqliteHelper.getInstance(this);
    }
}