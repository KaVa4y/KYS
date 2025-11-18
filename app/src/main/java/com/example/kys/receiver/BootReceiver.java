package com.example.kys.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.kys.database.AppDatabase;
import com.example.kys.model.Medicine;
import com.example.kys.utils.AlarmScheduler;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            // Исправлено: вместо var — обычные Java-переменные
            AppDatabase db = AppDatabase.getInstance(context);
            List<Medicine> medicines = db.medicineDao().getAllSync();

            new Thread(() -> {
                for (Medicine medicine : medicines) {
                    if (medicine.isActive) {
                        AlarmScheduler.scheduleNextAlarms(context, medicine);
                    }
                }
            }).start();
        }
    }
}