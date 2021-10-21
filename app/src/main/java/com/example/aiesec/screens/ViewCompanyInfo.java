package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ViewCompanyInfo extends AppCompatActivity {
    public static String KEY = "authorized";
    private EditText comName, comMS, comRepName, comPhone;
    private Button confirm, back;
    private Sponsor sponsor;
    private void findReference(){

        comName = findViewById(R.id.edit_com_name);
        comMS = findViewById(R.id.edit_com_ms);
        comRepName = findViewById(R.id.edit_com_repName);
        comPhone = findViewById(R.id.edit_com_repPhone);

        confirm = findViewById(R.id.edit_com_confirm_bttn);
        back = findViewById(R.id.edit_com_back_bttn);

    }
    private void initInfo(){
        comName.setText(sponsor.company.getCompName());
        comMS.setText(sponsor.company.getMS().toString());
        comRepName.setText(sponsor.company.getRepName());
        comPhone.setText(sponsor.company.getRepPhoneNum());
    }
    private void setOnClick(){

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sponsor.company = new Sponsor.Company();

                sponsor.company.setCompName(comName.getText().toString());
                sponsor.company.setMS(Integer.parseInt(comMS.getText().toString()));
                sponsor.company.setRepName(comRepName.getText().toString());
                sponsor.company.setRepPhoneNum(comPhone.getText().toString());

                if (ConnectProtocol.modCom(sponsor)){
                    Toast.makeText(ViewCompanyInfo.this,
                            "Chỉnh sửa thành công",
                            Toast.LENGTH_LONG).show();
                    back.performClick();
                }
                else{
                    Toast.makeText(ViewCompanyInfo.this,
                            "Chỉnh sửa thất bại, vui lòng thử lại",
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
        setContentView(R.layout.activity_view_company_info);
        sponsor = (Sponsor)getIntent().getSerializableExtra(KEY);
        findReference();
        initInfo();
        setOnClick();
    }
}
