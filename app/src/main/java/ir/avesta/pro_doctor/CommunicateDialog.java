package ir.avesta.pro_doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CommunicateDialog extends DialogFragment {
    private CommunicateDialogInterfaces communicateDialogInterfaces;

    public CommunicateDialog (CommunicateDialogInterfaces communicateDialogInterfaces) {
        this.communicateDialogInterfaces = communicateDialogInterfaces;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.communicate_layout, container, false);

        view.findViewById(R.id.emailLy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicateDialogInterfaces.onItemClicked(v);
            }
        });

        view.findViewById(R.id.phoneLy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicateDialogInterfaces.onItemClicked(v);
            }
        });

        return view;
    }

    interface CommunicateDialogInterfaces {
        void onItemClicked(View view);
    }
}
