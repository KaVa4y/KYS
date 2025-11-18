package com.example.kys.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.kys.utils.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("medicine_name");
        String dosage = intent.getStringExtra("medicine_dosage");
        long id = intent.getLongExtra("medicine_id", 0);

        NotificationHelper.showMedicineNotification(context, name, dosage, id);
    }
}