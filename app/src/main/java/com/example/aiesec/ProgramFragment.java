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

import android.text.InputType;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView loginName;
    private TextView logOut;
    private EditText progName;
    private Button findButton, addProgBttn;
    private ListView progList;
    private CheckBox edu, volun, intern;
    static int check = -1;
    static String input;
    private int removeIndex;
    private Boolean confirmChoice;

    public ProgramFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgramFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramFragment newInstance(String param1, String param2) {
        ProgramFragment fragment = new ProgramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private boolean checkAuthorizeEvent(){
        return MainScreen.account.getDept() == Account.MANAGEMENT_DEPT ||
                (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                        MainScreen.account.getDept() == Account.EVENT_DEPT);
    }
    private void assignProgList(){
        if (Program.resultList.size() != 0) {
            ArrayAdapter<Program> arrayAdapter = new ArrayAdapter<Program>(getContext(),
                    android.R.layout.simple_list_item_1,
                    Program.resultList);
            progList.setAdapter(arrayAdapter);
        }
        else
            progList.setAdapter(null);
    }

    private void initInfo(){
        if (check == 0){
            edu.setChecked(true);
        }else if (check == 1)
            volun.setChecked(true);
        else if (check == 2)
            intern.setChecked(true);

        if (input != null)
            progName.setText(input);
        loginName.setText(MainScreen.account.getName());
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.prog_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.event_choice_list, menu);
            removeIndex = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        }
    }

    private boolean checkAuthorize(){
        return MainScreen.account.getDept() == Account.MANAGEMENT_DEPT ||
                (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                        MainScreen.account.getDept() == Account.EVENT_DEPT);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.delete_event && checkAuthorize()){
            confirmChoice = null;
            confirmDialog();
        }
        else {
            Toast.makeText(getActivity(),
                    "Bạn phải thuộc ban quản lý",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnClick(){
        edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 0;
            }
        });
        volun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 1;
            }
        });
        intern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 2;
            }
        });

        progName.setClickable(true);
        progName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Program.resultList = new ArrayList<>();
                progList.setAdapter(null);
                return false;
            }
        });

        addProgBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAuthorize()){
                    progList.setAdapter(null);
                    startActivityForResult(new Intent(getActivity(), AddProgram.class), 1);
                }
                else {
                    Toast toast = Toast.makeText(getActivity(), "Bạn phải thuộc ban quản " +
                                    "lý hoặc trưởng ban sự kiện để thực hiện chức năng này",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
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

        findButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Program temp = new Program();
                if (intern.isChecked())
                    temp.setType("TT");
                if (volun.isChecked())
                    temp.setType("TN");
                if (edu.isChecked())
                    temp.setType("GD");

                temp.setName(String.valueOf(progName.getText()));
                Program.resultList = ConnectProtocol.searchProgramInfor(temp);
                input = String.valueOf(progName.getText());
                assignProgList();
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Program program = Program.resultList.get(i);
                Intent intent = new Intent(getActivity(), ViewProgramDetail.class);

                intent.putExtra(ViewProgramDetail.KEY, program);
                progList.setAdapter(null);
                startActivityForResult(intent, 1);
            }
        };

        progList.setOnItemClickListener(onItemClickListener);
        registerForContextMenu(progList);
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
                Program program = Program.resultList.get(removeIndex);
                if(ConnectProtocol.deleteProgram(program)){
                    Toast.makeText(getActivity(),
                            "Xóa chương trình thành công",
                            Toast.LENGTH_LONG).show();
                    findButton.performClick();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Không thể xóa chương trình này, vui lòng thử lại sau",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmChoice = false;
            }
        });

        builder.show();
    }
    private void findReference(View view){
        findButton = (Button)view.findViewById(R.id.find_prog_bttn);
        loginName = (TextView)view.findViewById(R.id.loginName_prog);
        //loginName.setText("Bạn đăng đăng nhập với tên " + MainScreen.account.getUserName());
        logOut = (TextView)view.findViewById(R.id.log_out_prog);
        progList = (ListView)view.findViewById(R.id.prog_list);
        progName = (EditText)view.findViewById(R.id.edit_prog_name);
        edu = (CheckBox)view.findViewById(R.id.edu_check);
        volun = (CheckBox)view.findViewById(R.id.volun_check);
        intern = (CheckBox)view.findViewById(R.id.intern_check);
        addProgBttn = view.findViewById(R.id.add_prog_bttn);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        findReference(view);
        assignProgList();
        initInfo();
        initOnClick();

        return view;
    }
}