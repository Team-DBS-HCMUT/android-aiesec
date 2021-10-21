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

import android.util.Log;
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
 * Use the {@link MemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView loginName;
    private TextView logOut;
    private EditText id;
    private EditText name;
    private Button findButton;
    private RadioGroup radioGroup;
    private ListView memList;
    static int check = -1;
    private int removeIndex;
    public MemberFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemberFragment newInstance(String param1, String param2) {
        MemberFragment fragment = new MemberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean checkAuthorizeEvent(){
        return MainScreen.account.getDept() == Account.MANAGEMENT_DEPT ||
                (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                        MainScreen.account.getDept() == Account.EVENT_DEPT);
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.mem_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.event_choice_list, menu);
            removeIndex = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.delete_event &&
                MainScreen.account.getDept() == Account.MANAGEMENT_DEPT &&
                MainScreen.account.getRole() == Account.DEPT_MANAGER){
            Member member = Member.resultList.get(removeIndex);
            if (member.getRole().equalsIgnoreCase("Trưởng ban")
                    || member.getRole().equalsIgnoreCase("Truong ban")){
                Toast.makeText(getActivity(), "Không thể xóa trưởng ban", Toast.LENGTH_LONG).show();
                assignMemList();
                return false;
            }
            else
                confirmDialog();
        }
        else {
            Toast.makeText(getActivity(),
                    "Bạn phải thuộc ban quản lý hoặc trưởng ban sự kiện để làm việc này",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
    private void setOnClick(){

        id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Member.resultList = new ArrayList<>();
                assignMemList();
                return false;
            }
        });

        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Member.resultList = new ArrayList<>();
                assignMemList();
                return false;
            }
        });

        loginName.setText(MainScreen.account.getName());
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
                Member temp = new Member();

                if (!id.getText().toString().equals(""))
                    temp.setId(Integer.parseInt(id.getText().toString()));
                else {
                    temp.setId(0);
                }
                temp.setFullName(name.getText().toString());

                int tempNum = radioGroup.getCheckedRadioButtonId();
                String grp;

                if (radioGroup.getCheckedRadioButtonId() == R.id.grp_id) {
                    grp = " ORDER BY [ID_thành viên]";
                    check = 1;
                }
                else {
                    check = 0;
                    grp = " ORDER BY [Họ và tên]";
                }
                Member.resultList = ConnectProtocol.searchMemberInfor(temp, grp);
                assignMemList();
            }
        });
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Member member = Member.resultList.get(i);
                Intent intent = new Intent(getActivity(), ViewMemberDetail.class);
                intent.putExtra(ViewMemberDetail.KEY, member);
                startActivityForResult(intent, 1);
            }
        };
        memList.setOnItemClickListener(onItemClickListener);
        registerForContextMenu(memList);
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure? ");

        final TextView textView = new TextView(getActivity());
        textView.setText("");
        builder.setView(textView);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Member member = Member.resultList.get(removeIndex);
                if(ConnectProtocol.rmvMember(member)){
                    Toast.makeText(getActivity(),
                            "Xóa thành viên thành công",
                            Toast.LENGTH_LONG).show();
                    Member.resultList.remove(removeIndex);
                    assignMemList();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Không thể xóa thành viên này, vui lòng thử lại sau",
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

    private void findReference(View view){
        findButton = (Button)view.findViewById(R.id.find_mem);
        loginName = (TextView)view.findViewById(R.id.loginName_mem);
        //loginName.setText("Bạn đăng đăng nhập với tên " + MainScreen.account.getUserName());
        logOut = (TextView)view.findViewById(R.id.log_out_mem);
        memList = (ListView)view.findViewById(R.id.mem_list);
        id = (EditText)view.findViewById(R.id.edit_mem_id);
        name = (EditText)view.findViewById(R.id.edit_name_mem);
        radioGroup = (RadioGroup)view.findViewById(R.id.check_radio_mem);
    }
    public void assignMemList(){
        if (Member.resultList.size() != 0) {
            ArrayAdapter<Member> arrayAdapter = new ArrayAdapter<Member>(getContext(),
                    android.R.layout.simple_list_item_1,
                    Member.resultList);
            memList.setAdapter(arrayAdapter);
        }
        else memList.setAdapter(null);

        if(check == 0)
            radioGroup.check(R.id.grp_name);
        else if(check == 1)
            radioGroup.check(R.id.grp_id);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (ViewGroup)inflater.inflate(R.layout.fragment_member, container, false);
        findReference(view);
        assignMemList();
        setOnClick();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }
}
