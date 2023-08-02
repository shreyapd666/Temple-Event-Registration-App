package com.project.templeeventregistration.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.templeeventregistration.databinding.ActivityNotificationBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {
    ActivityNotificationBinding notificationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(notificationBinding.getRoot());

        Intent eventIntent = getIntent();
        notificationBinding.name.setText(eventIntent.getStringExtra("name"));
        notificationBinding.date.setText(eventIntent.getStringExtra("date"));

        notificationBinding.addReminderButton.setOnClickListener(v -> {
            long startTime = 0;
            if (!notificationBinding.name.getText().toString().isEmpty() && !notificationBinding.date.getText().toString().isEmpty()) {

                String startDate = notificationBinding.date.getText().toString();
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                    startTime = date.getTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, notificationBinding.name.getText().toString());
                intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                intent.putExtra("beginTime", startTime);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(NotificationActivity.this, "No support for this action", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(NotificationActivity.this, "Fill all fields", Toast.LENGTH_LONG).show();
            }

        });
    }
}