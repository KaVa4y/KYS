package com.example.kys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kys.adapter.MedicineAdapter;
import com.example.kys.database.AppDatabase;
import com.example.kys.model.Medicine;
import com.example.kys.utils.AlarmScheduler;
import com.example.kys.utils.NotificationHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MedicineAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        NotificationHelper.createNotificationChannel(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineAdapter(this, medicine -> {
            // Кнопка "Принял"
            medicine.takenAmount++;
            if (medicine.takenAmount >= medicine.totalAmount) {
                medicine.isActive = false;
            }
            db.medicineDao().update(medicine);
            AlarmScheduler.scheduleNextAlarms(this, medicine);
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditMedicineActivity.class)));

        // Наблюдение за списком лекарств
        db.medicineDao().getAllActive().observe(this, medicines -> {
            adapter.setMedicines(medicines);
            // Перепланируем все будильники при запуске приложения
            for (Medicine m : medicines) {
                AlarmScheduler.scheduleNextAlarms(this, m);
            }
        });
    }
}