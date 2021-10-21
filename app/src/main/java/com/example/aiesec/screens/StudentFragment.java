package com.example.aiesec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView loginName;
    private TextView logout;
    private EditText studName, schoolName;
    private RadioGroup orderGrp;
    private ListView resultList;
    private Button find, add;
    public static int grp = -1;
    private int removeIndex;

    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure? ");
        // Set up the input

        final TextView textView = new TextView(getActivity());
        textView.setText("");
        builder.setView(textView);

// Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Student student = Student.resultList.get(removeIndex);
                if(ConnectProtocol.rmvStudent(student)){
                    Toast.makeText(getActivity(),
                            "Xóa sinh viên thành công",
                            Toast.LENGTH_LONG).show();
                    find.performClick();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Không thể xóa sinh viên này, vui lòng thử lại sau",
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
    private void assignStudList(){
        if (Student.resultList.size() != 0) {
            ArrayAdapter<Student> arrayAdapter = new ArrayAdapter<Student>(getContext(),
                    android.R.layout.simple_list_item_1,
                    Student.resultList);
            resultList.setAdapter(arrayAdapter);
        }
        else resultList.setAdapter(null);

        if(grp == 1)
            orderGrp.check(R.id.ord_name_stud);
        else if(grp == 0)
            orderGrp.check(R.id.ord_dob_stud);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initOnClick(){
        studName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Student.resultList = new ArrayList<>();
                assignStudList();
                return false;
            }
        });

        schoolName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Student.resultList = new ArrayList<>();
                assignStudList();
                return false;
            }
        });

        loginName.setText(MainScreen.account.getName());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent returnIntent = new Intent();
                                getActivity().setResult(Activity.RESULT_CANCELED, returnIntent);
                                getActivity().finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Student student = new Student();
                student.setFullName(String.valueOf(studName.getText()));
                student.setUni(String.valueOf(schoolName.getText()));
                boolean order = orderGrp.getCheckedRadioButtonId() == R.id.ord_dob_stud;
                if(order == true)
                    grp = 0;
                else if (order == false)
                    grp = 1;
                else grp = -1;
                Student.resultList = ConnectProtocol.searchStudentInfor(student, order);
                assignStudList();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                        MainScreen.account.getDept() == Account.MANAGEMENT_DEPT){
                    startActivityForResult(new Intent(getActivity(), AddStudent.class), 1);
                }
                else {
                    Toast toast = Toast.makeText(getActivity(), "Bạn phải thuộc ban quản " +
                            "lý để thực hiện chức năng này!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student student = Student.resultList.get(i);
                Intent intent = new Intent(getActivity(), ViewStudentDetail.class);

                intent.putExtra(ViewStudentDetail.KEY, student);
                startActivityForResult(intent, 1);
            }
        };

        resultList.setOnItemClickListener(onItemClickListener);
        registerForContextMenu(resultList);

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.stud_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.event_choice_list, menu);
            removeIndex = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        }
    }

    private boolean checkAuthorize(){
        return MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                MainScreen.account.getDept() == Account.MANAGEMENT_DEPT;
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(checkAuthorize()){
            confirmDialog();
        }
        else {
            Toast.makeText(getActivity(),
                    "Bạn phải thuộc ban quản lý để có thể làm việc này",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void findReference(View view){
        loginName = (TextView) view.findViewById(R.id.loginName_stud);
        logout = (TextView) view.findViewById(R.id.log_out_sponsor);
        studName = (EditText)view.findViewById(R.id.edit_stud_name);
        schoolName = (EditText)view.findViewById(R.id.edit_school_stud);
        orderGrp = (RadioGroup)view.findViewById(R.id.check_radio_stud);
        resultList = (ListView) view.findViewById(R.id.stud_list);
        find = (Button) view.findViewById(R.id.find_stud);
        add = (Button)view.findViewById(R.id.add_stud);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        findReference(view);
        assignStudList();
        initOnClick();

        return view;
    }
}
