package com.example.aiesec;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewStudentDetail extends AppCompatActivity {
    private Student student;
    private EditText name, dob, id, uni, major, phone;
    private Button confirm, back, join, cancel;
    private ListView joinedProgram, lv;
    private Program choosen;
    private ArrayList<Program> progList, tmp;
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
        join = findViewById(R.id.stud_join_event);


        joinedProgram = findViewById(R.id.stud_joined_events);
    }


    private void choiceEventList(){
        Dialog dlg = new Dialog(this);
        dlg.setTitle("Chọn hoạt động sẽ tham gia");

        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.program_search, null, false);
        tmp = ConnectProtocol.searchProgramActive();
        dlg.setContentView(v);
        cancel = (Button) dlg.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.cancel();
            }
        });

        lv = (ListView)dlg.findViewById(R.id.prog_result);
        if (tmp.size() != 0){
            ArrayAdapter<Program> arrayAdapter = new ArrayAdapter<Program>(this,
                    android.R.layout.simple_list_item_1,
                    tmp);
            lv.setAdapter(arrayAdapter);
        }else lv.setAdapter(null);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosen = tmp.get(i);
                for (Program walker : progList){
                    if (walker.getId().equals(choosen.getId())){
                        Toast.makeText(ViewStudentDetail.this,
                                "Bạn đã tham gia chương trình này rồi",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (ConnectProtocol.joinProgram(choosen, student)){
                    Toast.makeText(ViewStudentDetail.this,
                            "Tham gia chương trình thành công",
                            Toast.LENGTH_LONG).show();
                    assignJoinedList();
                    dlg.cancel();
                }
                else {
                    Toast.makeText(ViewStudentDetail.this,
                            "Tham gia chương trình thất bại",
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        lv.setOnItemClickListener(onItemClickListener);
        dlg.show();

    }

    private void initInfo(){

        name.setText(student.getFullName());
        dob.setText(student.getDob());
        id.setText(student.getId().toString());
        uni.setText(student.getUni());
        major.setText(student.getMajor());
        phone.setText(student.getPhoneNum());

        assignJoinedList();
    }

    private void assignJoinedList(){
        progList = ConnectProtocol.searchJoinedProgram(student);
        if (progList.size() != 0){
            ArrayAdapter<Program> arrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    progList);
            joinedProgram.setAdapter(arrayAdapter);
        }
        else joinedProgram.setAdapter(null);
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

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceEventList();
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
