package com.example.kys.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.kys.model.Medicine;
import com.example.kys.receiver.AlarmReceiver;
import java.util.Calendar;

public class AlarmScheduler {

    public static void scheduleNextAlarms(Context context, Medicine medicine) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String[] times = medicine.times.split(",");

        for (String timeStr : times) {
            String[] hm = timeStr.trim().split(":");
            if (hm.length != 2) continue;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hm[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hm[1]));
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medicine_name", medicine.name);
            intent.putExtra("medicine_dosage", medicine.dosage);
            intent.putExtra("medicine_id", medicine.id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    (int) medicine.id + timeStr.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }

    public static void cancelAlarmsForMedicine(Context context, Medicine medicine) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String[] times = medicine.times.split(",");

        for (String timeStr : times) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    (int) medicine.id + timeStr.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void rescheduleAll(Context context) {
        // Для BootReceiver, как раньше
    }
}