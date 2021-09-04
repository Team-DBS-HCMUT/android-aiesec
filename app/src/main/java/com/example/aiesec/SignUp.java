package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    EditText name, dob, phone, acc, pass;
    RadioGroup role, dept;
    Button confirm, back;

    private void findReference(){
        name = findViewById(R.id.sign_up_name);
        dob = findViewById(R.id.sign_up_dob);
        phone = findViewById(R.id.sign_up_phone);
        acc = findViewById(R.id.sign_up_acc);
        pass = findViewById(R.id.sign_up_pass);
        role = findViewById(R.id.role_radio_grp);
        dept = findViewById(R.id.dept_radio_grp);
        confirm = findViewById(R.id.confirm_sign_up);
        back = findViewById(R.id.back_sign_up);
    }

    private void setOnClick(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member member = new Member();
                Account account = new Account();
                member.setFullName(String.valueOf(name.getText()));
                member.setDob(String.valueOf(dob.getText()));
                member.setPhoneNum(String.valueOf(phone.getText()));
                if (dept.getCheckedRadioButtonId() == R.id.event_dept){
                    member.setIdDep(Account.EVENT_DEPT);
                    account.setDept(Account.EVENT_DEPT);
                }
                else if(dept.getCheckedRadioButtonId() == R.id.finance_dept){
                    member.setIdDep(Account.FINANCE_DEPT);
                    account.setDept(Account.FINANCE_DEPT);
                }
                else if(dept.getCheckedRadioButtonId() == R.id.manage_dept){
                    member.setIdDep(Account.MANAGEMENT_DEPT);
                    account.setDept(Account.MANAGEMENT_DEPT);
                }
                else {
                    Toast.makeText(SignUp.this,
                            "Vui lòng chọn phòng ban",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (role.getCheckedRadioButtonId() == R.id.manager_role){
                    member.setRole("Truong ban");
                    account.setRole(Account.DEPT_MANAGER);
                }
                else if(role.getCheckedRadioButtonId() == R.id.member_role){
                    member.setRole("Thanh vien");
                    account.setRole(Account.DEPT_MEMBER);
                }
                else {
                    Toast.makeText(SignUp.this,
                            "Vui lòng chọn vai trò",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (ConnectProtocol.addMember(member, String.valueOf(acc), String.valueOf(pass))){
                    Toast.makeText(SignUp.this,
                            "Tạo tài khoản thành công",
                            Toast.LENGTH_LONG).show();
                    account.setName(member.getFullName());
                    account.setUserName(String.valueOf(acc.getText()));
                    account.setPassword(String.valueOf(pass.getText()));
                    Intent intent = new Intent(SignUp.this, MainScreen.class);
                    intent.putExtra(MainScreen.authorize, account);
                    startActivity(intent);
                }
                else
                    Toast.makeText(SignUp.this,
                            "Không thể tạo tài khoản",
                            Toast.LENGTH_LONG).show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findReference();
        setOnClick();

    }
}
