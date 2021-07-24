package com.example.aiesec;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewProgramDetail extends AppCompatActivity {
    public static String KEY = "AUTHORIZED";
    private Program program;
    Button addEvent, confirm, back;
    ListView eventList;
    TextView progName, startTime, endTime;
    ArrayList<Event> arrayList;
    private int removeIndex;

    private void findReference(){
        addEvent = findViewById(R.id.add_event_prog);
        confirm = findViewById(R.id.confirm_prog_bttn);
        back = findViewById(R.id.back_bttn_prog);
        progName = findViewById(R.id.edit_name_prog_info);
        startTime = findViewById(R.id.edit_start_time_prog);
        endTime = findViewById(R.id.edit_end_time_prog);
        eventList = findViewById(R.id.event_list);

        arrayList = ConnectProtocol.searchEvent(program);
    }

    private void setOnClick(){
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAuthorizeEvent()){
                    startActivityForResult(new Intent(
                            ViewProgramDetail.this, AddEvent.class).
                                    putExtra(AddEvent.KEY, program),
                            1);
                }
                else {
                    Toast toast = Toast.makeText(ViewProgramDetail.this ,
                            "Bạn phải thuộc ban quản lý hoặc trưởng ban sự kiện " +
                                    "để thực hiện chức năng này",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainScreen.account.getDept() == Account.MANAGEMENT_DEPT ||
                        (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                                MainScreen.account.getDept() == Account.EVENT_DEPT)){


                    program.setName(String.valueOf(progName.getText()));
                    program.setStartTime(String.valueOf(startTime.getText()));
                    program.setEndTime(String.valueOf(endTime.getText()));

                    if (ConnectProtocol.modProgramInfor(program))
                        Toast.makeText(ViewProgramDetail.this,
                                "Thay đổi thành công",
                                Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ViewProgramDetail.this,
                                "Không thành công, vui lòng thử lại",
                                Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast toast = Toast.makeText(ViewProgramDetail.this ,
                            "Bạn phải thuộc ban quản lý hoặc trưởng ban sự kiện " +
                                    "để thực hiện chức năng này",
                            Toast.LENGTH_LONG);
                    toast.show();
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

        registerForContextMenu(eventList);
    }
    private boolean checkAuthorizeEvent(){
        return MainScreen.account.getDept() == Account.MANAGEMENT_DEPT ||
                (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                        MainScreen.account.getDept() == Account.EVENT_DEPT);
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.event_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.event_choice_list, menu);
            removeIndex = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        }
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProgramDetail.this);
        builder.setTitle("Are you sure? ");

        final TextView textView = new TextView(ViewProgramDetail.this);
        textView.setText("");
        builder.setView(textView);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Event event = arrayList.get(removeIndex);
                if(ConnectProtocol.deleteEvent(event)){
                    Toast.makeText(ViewProgramDetail.this,
                            "Xóa sự kiện thành công",
                            Toast.LENGTH_LONG).show();
                    arrayList.remove(removeIndex);
                    initInfo();
                }
                else {
                    Toast.makeText(ViewProgramDetail.this,
                            "Không thể xóa sự kiện này, vui lòng thử lại sau",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.delete_event && checkAuthorizeEvent()){
            confirmDialog();
        }
        else {
            Toast.makeText(this,
                    "Bạn phải thuộc ban quản lý hoặc trưởng ban sự kiện để làm việc này",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initInfo(){

        if (arrayList.size() != 0) {
            ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<Event>(this,
                    android.R.layout.simple_list_item_1,
                    arrayList);
            eventList.setAdapter(arrayAdapter);
        }
        else
            eventList.setAdapter(null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_program_detail);
        Intent intent = getIntent();
        program = (Program)intent.getSerializableExtra(KEY);

        findReference();
        setOnClick();
        initInfo();
        this.progName.setText(program.getName());
        this.startTime.setText(program.getStartTime());
        this.endTime.setText(program.getEndTime());



    }
}