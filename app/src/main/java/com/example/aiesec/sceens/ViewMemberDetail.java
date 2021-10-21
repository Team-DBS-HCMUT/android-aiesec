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

public class ViewMemberDetail extends AppCompatActivity {
    public static String KEY = "AUTHORIZED";
    Member member;
    EditText name, phoneNum, dob;
    TextView role, dept;
    Button confirm, back;

    private void findReference(){
        name = findViewById(R.id.mem_name);
        phoneNum = findViewById(R.id.mem_phone_num);
        dob = findViewById(R.id.dob_mem);

        role = findViewById(R.id.role);
        dept = findViewById(R.id.dept);
        confirm = findViewById(R.id.confirm_mem);
        back = findViewById(R.id.back_mem);
    }
    private void initInfo(){
        name.setText(member.getFullName());
        phoneNum.setText(member.getPhoneNum());
        dob.setText(member.getDob());
        role.setText("Vai tro: "+member.getRole());

        switch (member.getIdDep()){
            case Account.MANAGEMENT_DEPT:
                dept.setText("Phong ban: Ban quan ly");
                break;
            case Account.FINANCE_DEPT:
                dept.setText("Phong ban: Ban tai chinh");
                break;
            default:
                dept.setText("Phong ban: Ban su kien");
        }
    }

    private void setOnClick(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member.setFullName(String.valueOf(name.getText()));
                member.setDob(String.valueOf(dob.getText()));
                member.setPhoneNum(String.valueOf(phoneNum.getText()));
                member.setRole(String.valueOf(role.getText()));

                boolean done = ConnectProtocol.modMember(member);
                if(done){
                    Toast.makeText(ViewMemberDetail.this,
                            "Thay đổi thông tin thành công",
                            Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                else
                    Toast.makeText(ViewMemberDetail.this,
                        "Không thành công, vui lòng thử lại",
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_member_detail);

        member = (Member)getIntent().getSerializableExtra(KEY);
        findReference();
        initInfo();
        setOnClick();
    }
}
