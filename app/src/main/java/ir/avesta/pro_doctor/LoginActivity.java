package ir.avesta.pro_doctor;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private AppCompatEditText nameEdt, passwordEdt;
    private TextView loginTv;
    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        nameEdt.addTextChangedListener(this);
        passwordEdt.addTextChangedListener(this);
    }

    public void init() {
        nameEdt = findViewById(R.id.nameEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        loginTv = findViewById(R.id.loginTv);
        sharedPrefHelper = new SharedPrefHelper(this);
    }

    public void onLoginTvClicked(View view) {
        String name = nameEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        if (sharedPrefHelper.isManager(name, password)) {
            setResult(Activity.RESULT_OK);
            finish();

        } else {
            nameEdt.requestFocus();
            nameEdt.setError(getString(R.string.name_or_password_is_wrong));
        }
    }

    public void setTextViewEnabled(TextView tv, Boolean ability) {
        tv.setEnabled(ability);

        if (ability)
            loginTv.setTextColor(ContextCompat.getColor(this, R.color.blue));
        else
            loginTv.setTextColor(ContextCompat.getColor(this, R.color.lightGray));
    }

    @Override
    public void afterTextChanged(Editable s) {
        setTextViewEnabled(loginTv, (!nameEdt.getText().toString().isEmpty()
                &&
                !passwordEdt.getText().toString().isEmpty()));
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}