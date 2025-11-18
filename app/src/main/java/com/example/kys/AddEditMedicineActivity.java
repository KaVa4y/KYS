package com.example.kys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.kys.database.AppDatabase;
import com.example.kys.model.Medicine;
import com.example.kys.utils.AlarmScheduler;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditMedicineActivity extends AppCompatActivity {

    private TextInputEditText etName, etDosage, etTotalAmount;
    private EditText etStartDate, etEndDate;
    private ChipGroup chipGroupTimes;
    private Button btnAddTime, btnSave;
    private AppDatabase db;
    private Medicine currentMedicine = null;
    private List<String> timesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_medicine);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Добавить лекарство");

        db = AppDatabase.getInstance(this);
        initViews();

        long id = getIntent().getLongExtra("medicine_id", -1);
        if (id != -1) {
            new Thread(() -> {
                currentMedicine = db.medicineDao().getAllSync().stream()
                        .filter(m -> m.id == id).findFirst().orElse(null);
                if (currentMedicine != null) runOnUiThread(this::fillFields);
            }).start();
        }

        btnAddTime.setOnClickListener(v -> showTimePicker());
        btnSave.setOnClickListener(v -> saveMedicine());
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etDosage = findViewById(R.id.etDosage);
        etTotalAmount = findViewById(R.id.etTotalAmount);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        chipGroupTimes = findViewById(R.id.chipGroupTimes);
        btnAddTime = findViewById(R.id.btnAddTime);
        btnSave = findViewById(R.id.btnSave);

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));
    }

    private void fillFields() {
        etName.setText(currentMedicine.name);
        etDosage.setText(currentMedicine.dosage);
        etStartDate.setText(currentMedicine.startDate);
        etEndDate.setText(currentMedicine.endDate);
        etTotalAmount.setText(String.valueOf(currentMedicine.totalAmount));

        String[] times = currentMedicine.times.split(",");
        for (String time : times) {
            addChip(time.trim());
        }
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        new TimePickerDialog(this, (view, hour, minute) -> {
            String time = String.format("%02d:%02d", hour, minute);
            addChip(time);
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    private void addChip(String time) {
        if (timesList.contains(time) || timesList.size() >= 5) return;

        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_time, chipGroupTimes, false);
        chip.setText(time);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            timesList.remove(time);
            chipGroupTimes.removeView(chip);
        });
        chipGroupTimes.addView(chip);
        timesList.add(time);
    }

    private void saveMedicine() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty() || timesList.isEmpty()) {
            Toast.makeText(this, "Заполните название и время", Toast.LENGTH_SHORT).show();
            return;
        }

        Medicine medicine = currentMedicine != null ? currentMedicine : new Medicine();
        medicine.name = name;
        medicine.dosage = etDosage.getText().toString();
        medicine.times = String.join(",", timesList);
        medicine.startDate = etStartDate.getText().toString();
        medicine.endDate = etEndDate.getText().toString();
        medicine.totalAmount = Integer.parseInt(etTotalAmount.getText().toString().isEmpty() ? "30" : etTotalAmount.getText().toString());

        new Thread(() -> {
            if (currentMedicine == null) {
                db.medicineDao().insert(medicine);
            } else {
                db.medicineDao().update(medicine);
            }
            AlarmScheduler.scheduleNextAlarms(this, medicine);
            runOnUiThread(() -> {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void showDatePicker(EditText editText) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                editText.setText(String.format("%d-%02d-%02d", year, month + 1, day)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}