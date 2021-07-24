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

public class ViewStudentDetail extends AppCompatActivity {
    private Student student;
    private EditText name, dob, id, uni, major, phone;
    private Button confirm, back;

    public static String KEY = "AUTHORIZED";

    private boolean checkAuthorize(){
        return MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                MainScreen.account.getDept() == Account.MANAGEMENT_DEPT;
    }

    private void findReference(){

        name = findViewById(R.id.stud_name_detail);
        dob = findViewById(R.id.stud_dob_detail);
        id = findViewById(R.id.stud_id_detail);
        uni = findViewById(R.id.stud_school_detail);
        major = findViewById(R.id.stud_major_detail);
        phone = findViewById(R.id.stud_phone_detail);

        confirm = findViewById(R.id.stud_detail_confirm);
        back = findViewById(R.id.stud_detail_back);

    }

    private void initInfo(){

        name.setText(student.getFullName());
        dob.setText(student.getDob());
        id.setText(student.getId().toString());
        uni.setText(student.getUni());
        major.setText(student.getMajor());
        phone.setText(student.getPhoneNum());

    }

    private void setOnClick(){

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAuthorize() == false){
                    Toast.makeText(ViewStudentDetail.this,
                            "Bạn phải thuộc ban quản lý để thực hiện chỉnh sửa",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Student temp = new Student();
                temp.setUniAbbreviation(student.getUniAbbreviation());
                temp.setFullName(String.valueOf(name.getText()));
                temp.setDob(String.valueOf(dob.getText()));
                temp.setId(Integer.parseInt(String.valueOf(id.getText())));
                temp.setUni(String.valueOf(uni.getText()));
                temp.setMajor(String.valueOf(major.getText()));
                temp.setPhoneNum(String.valueOf(phone.getText()));

                if (ConnectProtocol.modStudent(temp)){
                    Toast.makeText(ViewStudentDetail.this,
                            "Chỉnh sửa thông tin thành công",
                            Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                else {
                    Toast.makeText(ViewStudentDetail.this,
                            "Không thành công, vui lòng thử lại",
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
        setContentView(R.layout.activity_view_student_detail);

        student = (Student)getIntent().getSerializableExtra(KEY);
        findReference();
        initInfo();
        setOnClick();

    }
}