package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPerson extends AppCompatActivity {

    EditText name;
    EditText ssn;
    EditText village;
    EditText district;
    EditText province;
    EditText dob;
    Button confirm, back;

    private void findReference(){
        name = findViewById(R.id.add_per_name);
        ssn = findViewById(R.id.add_per_ssn);
        village = findViewById(R.id.add_per_village);
        district = findViewById(R.id.add_per_district);
        province = findViewById(R.id.add_per_prov);
        dob = findViewById(R.id.add_per_dob);

        confirm = findViewById(R.id.add_per_confirm);
        back = findViewById(R.id.add_per_back);
    }

    private void setOnClick(){

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sponsor sponsor = new Sponsor();
                sponsor.person = new Sponsor.Person();

                sponsor.person.setFullName(String.valueOf(name.getText()));
                sponsor.person.setCMND(Integer.parseInt(String.valueOf(ssn.getText())));
                sponsor.person.setVillage(String.valueOf(village.getText()));
                sponsor.person.setDistrict(String.valueOf(district.getText()));
                sponsor.person.setProvince(String.valueOf(province.getText()));
                sponsor.person.setDob(String.valueOf(dob.getText()));

                if(ConnectProtocol.addPerson(sponsor)){
                    Toast.makeText(AddPerson.this,
                            "Thêm cá nhân tài trợ thành công",
                            Toast.LENGTH_LONG).show();
                    sponsor.person = new Sponsor.Person();
                    sponsor.person.setFullName("");
                    Sponsor.personList = ConnectProtocol.searchPerson(sponsor, "");
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                else {
                    Toast.makeText(AddPerson.this,
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
        setContentView(R.layout.activity_add_person);

        findReference();
        setOnClick();

    }
}