package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddProgram extends AppCompatActivity {

    EditText progName, startTime, endTime,
            money, nation, province;
    Button confirm, back;
    CheckBox edu, volun, intern;

    private void findReference(){
        progName = findViewById(R.id.add_prog_name);
        startTime = findViewById(R.id.add_start_prog);
        endTime = findViewById(R.id.add_end_prog);
        money = findViewById(R.id.add_money_prog);
        nation = findViewById(R.id.add_nation_prog);
        province = findViewById(R.id.add_prov_prog);
        confirm = findViewById(R.id.confirm_add_prog);
        back = findViewById(R.id.back_add_prog);
        edu = findViewById(R.id.edu_check_add);
        volun = findViewById(R.id.volun_check_add);
        intern = findViewById(R.id.intern_check_add);
    }
    private void setOnClick(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Program program = new Program();
                program.setName(String.valueOf(progName.getText()));
                program.setStartTime(String.valueOf(startTime.getText()));
                program.setEndTime(String.valueOf(endTime.getText()));
                program.setMoney(Integer.parseInt(String.valueOf(money.getText())));
                program.setNation(String.valueOf(nation.getText()));
                program.setCoordinate(String.valueOf(province.getText()));

                if (edu.isChecked()) program.setType("GD");
                if (volun.isChecked()) program.setType("TN");
                if (volun.isChecked()) program.setType("TT");

                boolean done = ConnectProtocol.addProgramInfor(program);
                if (done){
                    Toast.makeText(AddProgram.this,
                            "Thêm chương trình thành công",
                            Toast.LENGTH_LONG).show();

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
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
        setContentView(R.layout.activity_add_program);

        findReference();
        setOnClick();

    }
}