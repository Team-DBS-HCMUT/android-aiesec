package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudent extends AppCompatActivity {

    EditText name, id, phone, dob, uni, uniAbbrev, major;
    Button confirm, back;

    private void findReference(){

        name = findViewById(R.id.add_stud_name);
        id = findViewById(R.id.add_stud_id);
        phone = findViewById(R.id.add_stud_phone);
        dob = findViewById(R.id.add_stud_dob);
        uni = findViewById(R.id.add_stud_uni);
        uniAbbrev = findViewById(R.id.add_stud_abbrev);
        major = findViewById(R.id.add_stud_major);
        confirm = findViewById(R.id.confirm_add_stud);
        back = findViewById(R.id.back_add_stud_bttn);

    }

    private void setOnClick(){

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student = new Student();
                student.setFullName(String.valueOf(name.getText()));
                student.setId(Integer.parseInt(String.valueOf(id.getText())));
                student.setPhoneNum(String.valueOf(phone.getText()));
                student.setDob(String.valueOf(dob.getText()));
                student.setUni(String.valueOf(uni.getText()));
                student.setUniAbbreviation(String.valueOf(uniAbbrev.getText()));
                student.setMajor(String.valueOf(major.getText()));

                if(ConnectProtocol.addStudentInfor(student)){
                    Toast.makeText(AddStudent.this,
                            "Thêm sinh viên thành công",
                            Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                else {
                    Toast.makeText(AddStudent.this,
                            "Thêm sinh viên không thành công, vui lòng thử lại",
                            Toast.LENGTH_LONG).show();
                }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        findReference();
        setOnClick();

    }
}
