package ir.avesta.pro_doctor;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {
    private Context context;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor = null;
    private String MANAGER_NAME = "mahdi";
    private String MANAGER_PASSWORD = "1384";

    public SharedPrefHelper(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        this.editor = sp.edit();

        if (!isManager(MANAGER_NAME, MANAGER_PASSWORD)) {
            editor.putString(MANAGER_NAME, MANAGER_PASSWORD).commit();
        }
    }

    public Boolean isManager(String name, String password) {
        return sp.getString(name, "-1").equals(password);
    }
}
