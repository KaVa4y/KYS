package com.example.kys.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "medicines")
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;

    @NonNull public String name = "";
    @NonNull public String dosage = "";
    @NonNull public String form = "tablet";          // для иконки
    @NonNull public String times = "";               // "08:30,14:00,20:00"
    @NonNull public String startDate = "";           // YYYY-MM-DD
    public String endDate = "";
    public int totalAmount = 30;
    public int takenAmount = 0;
    public boolean isActive = true;

    public Medicine() {}

    // геттеры-сеттеры (можно сгенерировать Alt+Insert)
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @NonNull public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    @NonNull public String getDosage() { return dosage; }
    public void setDosage(@NonNull String dosage) { this.dosage = dosage; }

    @NonNull public String getForm() { return form; }
    public void setForm(@NonNull String form) { this.form = form; }

    @NonNull public String getTimes() { return times; }
    public void setTimes(@NonNull String times) { this.times = times; }

    @NonNull public String getStartDate() { return startDate; }
    public void setStartDate(@NonNull String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate == null ? "" : endDate; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public int getTakenAmount() { return takenAmount; }
    public void setTakenAmount(int takenAmount) { this.takenAmount = takenAmount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}