package com.example.kys.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kys.R;
import com.example.kys.model.Medicine;
import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicines = new ArrayList<>();
    private final Context context;
    private final OnTakeClickListener listener;

    public interface OnTakeClickListener {
        void onTakeClick(Medicine medicine);
    }

    public MedicineAdapter(Context context, OnTakeClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine m = medicines.get(position);
        holder.tvName.setText(m.name);
        holder.tvDosage.setText(m.dosage);
        holder.tvTimes.setText("Время: " + m.times.replace(",", " • "));

        int progress = m.totalAmount > 0 ? (int) ((m.takenAmount * 100f) / m.totalAmount) : 0;
        holder.progressBar.setProgress(progress);
        holder.tvProgress.setText(m.takenAmount + " / " + m.totalAmount);

        // Простая иконка по форме
        if (m.form.contains("capsule")) holder.ivIcon.setImageResource(R.drawable.ic_capsule);
        else if (m.form.contains("syrup")) holder.ivIcon.setImageResource(R.drawable.ic_syrup);
        else holder.ivIcon.setImageResource(R.drawable.ic_pill);

        holder.btnTake.setOnClickListener(v -> listener.onTakeClick(m));
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDosage, tvTimes, tvProgress;
        ProgressBar progressBar;
        ImageView ivIcon;
        com.google.android.material.button.MaterialButton btnTake;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDosage = itemView.findViewById(R.id.tvDosage);
            tvTimes = itemView.findViewById(R.id.tvTimes);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            progressBar = itemView.findViewById(R.id.progressBar);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            btnTake = itemView.findViewById(R.id.btnTake);
        }
    }
}