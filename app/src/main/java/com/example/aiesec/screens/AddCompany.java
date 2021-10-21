package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCompany extends AppCompatActivity {

    EditText compName, compId, compRep, compRepPhone;
    Button confirm, back;

    private void findReference(){
        compName = findViewById(R.id.add_comp_name);
        compId = findViewById(R.id.add_comp_id);
        compRep = findViewById(R.id.add_comp_rep);
        compRepPhone = findViewById(R.id.add_comp_rep_phone);

        confirm = findViewById(R.id.add_comp_confirm_bttn);
        back = findViewById(R.id.add_comp_back_bttn);
    }

    private void setOnClick(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sponsor sponsor = new Sponsor();
                sponsor.company = new Sponsor.Company();

                sponsor.company.setCompName(String.valueOf(compName.getText()));
                sponsor.company.setMS(Integer.parseInt(String.valueOf(compId.getText())));
                sponsor.company.setRepName(String.valueOf(compRep.getText()));
                sponsor.company.setRepPhoneNum(String.valueOf(compRepPhone.getText()));

                if(ConnectProtocol.addCom(sponsor.company)){
                    Toast.makeText(AddCompany.this,
                            "Thêm công ty/tổ chức tài trợ thành công",
                            Toast.LENGTH_LONG).show();
                    sponsor = new Sponsor();
                    sponsor.company = new Sponsor.Company();
                    sponsor.company.setCompName("");
                    Sponsor.companyList = ConnectProtocol.searchCom(sponsor, "");
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                else {
                    Toast.makeText(AddCompany.this,
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
        setContentView(R.layout.activity_add_company);

        findReference();
        setOnClick();
    }
}
