package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEvent extends AppCompatActivity {

    public static String KEY = "AUTHORIZED";

    Button confirm, back;
    EditText name, time;
    TextView title;
    Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        confirm = findViewById(R.id.confirm_add_event);
        back = findViewById(R.id.back_add_event);
        name = findViewById(R.id.event_name_add);
        time = findViewById(R.id.event_time_add);
        title = findViewById(R.id.add_event_title);

        program = (Program)getIntent().getSerializableExtra(KEY);
        title.setText("Tên chương trình: " + program.getName());
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event();
                event.setDate(String.valueOf(time.getText()));
                event.setJob(String.valueOf(name.getText()));
                if(ConnectProtocol.addEvent(program.getId(), event)){
                    Toast.makeText(AddEvent.this, "Thêm sự kiện thành công",
                            Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                else
                    Toast.makeText(AddEvent.this,
                            "Không thành công, vui lòng thử lại sau",
                            Toast.LENGTH_LONG).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }
}