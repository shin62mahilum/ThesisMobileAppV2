package com.example.thesisprojectmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReserveActivity<Calendar> extends AppCompatActivity {

    private EditText editTextDate;
    private Button btnSubmit;
    private String roomNumber;
    private DatabaseReference mDatabaseRef;
    private ArrayList<String> mRoomNumbersList;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Intent intent = getIntent();
        roomNumber = intent.getStringExtra("roomNumber");

        // Find the EditText view by its ID
        EditText editTextDate = findViewById(R.id.editTextDate);

        // Get the current date and format it as "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Set the current date as the text of the EditText
        editTextDate.setText(currentDate);

        // Get the user's name from the EditText view
        Spinner spinnerRoomNumber = findViewById(R.id.spinnerRoomNumber);


        String date = editTextDate.getText().toString();
        String roomNumber = spinnerRoomNumber.getSelectedItem().toString();

        String weekDay = getDayOfWeek(date);


        btn_back = (Button) findViewById(R.id.back_button);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Submit Button
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the user's name from the EditText view
                EditText editTextDate = findViewById(R.id.editTextDate);
                EditText editTextName = findViewById(R.id.editText_fullname);
                Spinner spinnerRoomNumber = findViewById(R.id.spinnerRoomNumber);
                Spinner spinnerEvent = findViewById(R.id.spinnerEvent);
                EditText editTextTimeStart = findViewById(R.id.editTextTimeStart);
                EditText editTextTimeEnd = findViewById(R.id.editTextTimeEnd);
                EditText editTextSubjectCode = findViewById(R.id.editTextSubjectCode);

                String date = editTextDate.getText().toString();
                String fullName = editTextName.getText().toString();
                String roomNumber = spinnerRoomNumber.getSelectedItem().toString();
                String event = spinnerEvent.getSelectedItem().toString();
                String timeStart = editTextTimeStart.getText().toString();
                String timeEnd = editTextTimeEnd.getText().toString();
                String subjectCode = editTextSubjectCode.getText().toString();
                String weekDay = getDayOfWeek(date);

                // Check if the time inputs are in 24-hour format
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                try {
                    Date startTime = timeFormat.parse(timeStart);
                    Date endTime = timeFormat.parse(timeEnd);
                } catch (ParseException e) {
                    Toast.makeText(ReserveActivity.this, "Please enter the time in 24-hour format.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if all EditText fields are filled up
                if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(roomNumber) || TextUtils.isEmpty(event)
                        || TextUtils.isEmpty(timeStart) || TextUtils.isEmpty(timeEnd) || TextUtils.isEmpty(subjectCode)) {
                    Toast.makeText(ReserveActivity.this, "Please fill up all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an intent to start the confirmation activity and pass the user's data
                    Intent intent = new Intent(ReserveActivity.this, ReserveConfirmation_activity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("roomNumber", roomNumber);
                    intent.putExtra("event", event);
                    intent.putExtra("timeStart", timeStart);
                    intent.putExtra("timeEnd", timeEnd);
                    intent.putExtra("subjectCode", subjectCode);
                    startActivity(intent);
                }
            }
        });
    }

    //METHODS HERE!!!

    public void openSubmitConfirmation() {
        Intent intent = new Intent(this, ReserveConfirmation_activity.class);
        startActivity(intent);
    }

    //Converts a Date into day of the Week
    public String getDayOfWeek(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = format.parse(dateStr);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
            return new DateFormatSymbols(Locale.US).getWeekdays()[dayOfWeek];
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}