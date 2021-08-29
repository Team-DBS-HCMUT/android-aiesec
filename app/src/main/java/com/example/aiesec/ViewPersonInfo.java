package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ViewPersonInfo extends AppCompatActivity {
    public static String KEY = "authorized";
    EditText name, cmnd, dob, village, district, province;
    Button confirm, back;
    Sponsor s;

    private void findReference(){
        name = findViewById(R.id.edit_per_name);
        cmnd = findViewById(R.id.edit_per_cmnd);
        dob = findViewById(R.id.edit_per_dob);
        village = findViewById(R.id.edit_per_village);
        district = findViewById(R.id.edit_per_district);
        province = findViewById(R.id.edit_per_province);

        confirm = findViewById(R.id.edit_per_confirm_bttn);
        back = findViewById(R.id.edit_per_back_bttn);
    }

    private void setOnClick(){
        findReference();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SponsorFragment.checkAuthorize()){
                    Toast.makeText(ViewPersonInfo.this,
                            "Bạn không có thẩm quyền làm việc này",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                s.person = new Sponsor.Person();

                s.person.setFullName(name.getText().toString());
                s.person.setCMND(Integer.parseInt(cmnd.getText().toString()));
                s.person.setDob(dob.getText().toString());
                s.person.setVillage(village.getText().toString());
                s.person.setDistrict(district.getText().toString());
                s.person.setProvince(province.getText().toString());

                if (ConnectProtocol.modPerson(s)){
                    Toast.makeText(ViewPersonInfo.this,
                            "Chỉnh sửa thành công",
                            Toast.LENGTH_LONG).show();
                    back.performClick();
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

    private void initState(){
        name.setText(s.person.getFullName());
        cmnd.setText(s.person.getCMND().toString());
        dob.setText(s.person.getDob());
        village.setText(s.person.getVillage());
        district.setText(s.person.getDistrict());
        province.setText(s.person.getProvince());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_person_info);
        s = (Sponsor)getIntent().getSerializableExtra(KEY);
        findReference();
        setOnClick();
        initState();
    }
}