package ir.avesta.pro_doctor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Doctor> list;
    private ArrayList<Doctor> allItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapterInterface recyclerAdapterInterface;
    public static String doctorKey = "ir.avesta.afsoon.RecyclerAdapter.doctorKey";

    public RecyclerAdapter(ArrayList<Doctor> l, RecyclerAdapterInterface recyclerAdapterInterface) {
        this.list = l;
        this.allItems.addAll(l);
        this.recyclerAdapterInterface = recyclerAdapterInterface;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;

        if (list.size() == 0)
            recyclerView.setVisibility(View.GONE);
    }

    public interface RecyclerAdapterInterface {
        void onEditClicked(int position);
    }

    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Doctor> toShow = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                toShow.addAll(allItems);

            } else {
                for (int i = 0; i < allItems.size(); i++) {
                    String filterStr = constraint.toString().trim();

                    Doctor item = allItems.get(i);
                    String name = item.getName();

                    if (name.substring(0, filterStr.length()).equals(filterStr))
                        toShow.add(item);
                }
            }


            FilterResults result = new FilterResults();
            result.values = toShow;

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values == null) {
                recyclerView.setVisibility(View.GONE);
                Log.d("myAppLog", "gone");
                return;
            }

            list.clear();
            list.addAll((Collection<? extends Doctor>) results.values);

            if (list.size() == 0)
                recyclerView.setVisibility(View.GONE);
            else
                recyclerView.setVisibility(View.VISIBLE);

            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv, specialityTv;
        private ImageView editImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            specialityTv = itemView.findViewById(R.id.specialityTv);
            editImg = itemView.findViewById(R.id.editImg);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.recycler_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        Doctor item = list.get(position);

        holder.nameTv.setText(item.getName());
        holder.specialityTv.setText(item.getAddress());

        if (MainActivity.managerModeEnabled)
            holder.editImg.setVisibility(View.VISIBLE);
        else
            holder.editImg.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, DoctorDetailsActivity.class);
                intent.putExtra(doctorKey, item);
                context.startActivity(intent);
            }
        });

        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapterInterface.onEditClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
