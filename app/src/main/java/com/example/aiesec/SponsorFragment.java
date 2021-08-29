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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SponsorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SponsorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView loginName, logout;
    private EditText sponsorName;
    private RadioGroup typeGrp;
    private ListView sponsorList;
    private Button find, addPerson, addCompany;
    public static Boolean typeRequest = null;    // true = person
    private int choosedIndex;
    private String moneyInput, sponsorString;

    // fa;se = company
    public SponsorFragment() {
        // Required empty public constructor
    }

    public static boolean checkAuthorize(){
        return MainScreen.account.getDept() == Account.MANAGEMENT_DEPT ||
                (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                        MainScreen.account.getDept() == Account.FINANCE_DEPT);
    }

    public void assignSponsorList(){
        if (Sponsor.personList.size() != 0 && typeRequest == true) {
            ArrayAdapter<Sponsor> arrayAdapter = new ArrayAdapter<Sponsor>(getContext(),
                    android.R.layout.simple_list_item_1,
                    Sponsor.personList);
            sponsorList.setAdapter(arrayAdapter);
            typeGrp.check(R.id.personal_grp);
        }
        else if (Sponsor.companyList.size() != 0 && typeRequest == false){
            ArrayAdapter<Sponsor> arrayAdapter = new ArrayAdapter<Sponsor>(getContext(),
                    android.R.layout.simple_list_item_1,
                    Sponsor.companyList);
            sponsorList.setAdapter(arrayAdapter);
            typeGrp.check(R.id.company_grp);
        }
        else
            sponsorList.setAdapter(null);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.sponsor_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.sponsor_choice_list, menu);
            choosedIndex = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.delete_sponsor && checkAuthorize()){
            confirmDialog();
        }
        else if(item.getItemId() == R.id.add_money_sponsor && checkAuthorize()){
            showDialog();
        }
        else {
            Toast.makeText(getActivity(),
                    "Bạn phải thuộc ban quản lý hoặc trưởng ban sự kiện để làm việc này",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SponsorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SponsorFragment newInstance(String param1, String param2) {
        SponsorFragment fragment = new SponsorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
                Sponsor sponsor;
                if(typeRequest == true)
                    sponsor = Sponsor.personList.get(choosedIndex);
                else
                    sponsor = Sponsor.companyList.get(choosedIndex);


                if(ConnectProtocol.deleteSponsor(sponsor)){
                    Toast.makeText(getActivity(),
                            "Xóa nhà tài trợ thành công",
                            Toast.LENGTH_LONG).show();
                    find.performClick();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Không thể xóa nhà tài trợ này, vui lòng thử lại sau",
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
    @SuppressLint("RestrictedApi")
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nhập số tiền cần thêm");
        // Set up the input

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(input, 20, 20, 20, 20);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Sponsor sponsor;
                if(typeRequest == true){
                    sponsor = Sponsor.personList.get(choosedIndex);
                }
                else {
                    sponsor = Sponsor.companyList.get(choosedIndex);
                }
                moneyInput = input.getText().toString();
                if (moneyInput.equals("") == false &&
                        ConnectProtocol.moreMoney(sponsor, Integer.valueOf(moneyInput))){
                    Toast.makeText(getActivity(),
                            "Đã thêm thành công " + moneyInput + " đồng",
                            Toast.LENGTH_LONG).show();
                    find.performClick();
                    moneyInput = null;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
        View view = inflater.inflate(R.layout.fragment_sponsor, container, false);

        loginName = (TextView)view.findViewById(R.id.loginName_sponsor);
        logout = (TextView)view.findViewById(R.id.log_out_sponsor);
        sponsorName = (EditText)view.findViewById(R.id.edit_sponsor_name);
        typeGrp = (RadioGroup)view.findViewById(R.id.radio_grp_spon);
        sponsorList = (ListView)view.findViewById(R.id.sponsor_list);
        find = (Button)view.findViewById(R.id.find_sponsor);
        addCompany = (Button)view.findViewById(R.id.add_comp);
        addPerson = (Button) view.findViewById(R.id.add_person);
        assignSponsorList();

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
        sponsorName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Sponsor.personList = new ArrayList<>();
                Sponsor.companyList = new ArrayList<>();
                sponsorList.setAdapter(null);
                return false;
            }
        });
        loginName.setText(MainScreen.account.getName());
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainScreen.account.getDept() == Account.FINANCE_DEPT ||
                        (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                                MainScreen.account.getDept() == Account.MANAGEMENT_DEPT)){

                    startActivityForResult(new Intent(getActivity(), AddPerson.class), 1);
                }
                else {
                    Toast toast = Toast.makeText(getActivity(), "Bạn phải thuộc ban " +
                                    "quản lý hoặc là trưởng ban tài chính " +
                                    "để thực hiện chức năng này",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        addCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainScreen.account.getDept() == Account.FINANCE_DEPT ||
                        (MainScreen.account.getRole() == Account.DEPT_MANAGER &&
                                MainScreen.account.getDept() == Account.MANAGEMENT_DEPT)){
                    startActivityForResult(new Intent(getActivity(), AddCompany.class), 1);
                }
                else {
                    Toast toast = Toast.makeText(getActivity(), "Bạn phải thuộc ban " +
                                    "quản lý hoặc là trưởng ban tài chính " +
                                    "để thực hiện chức năng này",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Sponsor sponsor = new Sponsor();
                if (typeGrp.getCheckedRadioButtonId() == R.id.personal_grp)
                    typeRequest = true;
                else if (typeGrp.getCheckedRadioButtonId() == R.id.company_grp)
                    typeRequest = false;

                if (typeRequest == null){
                    Toast toast = Toast.makeText(getActivity(),
                                            "Vui lòng chọn loại nhà tài trợ",
                                                    Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                else if(typeRequest){
                    sponsor.person = new Sponsor.Person();
                    sponsor.person.setFullName(String.valueOf(sponsorName.getText()));

                    Sponsor.personList = ConnectProtocol.searchPerson(sponsor, "");
                    Sponsor.companyList = new ArrayList<>();
                }
                else{
                    sponsor.company = new Sponsor.Company();
                    sponsor.company.setCompName(String.valueOf(sponsorName.getText()));

                    Sponsor.companyList = ConnectProtocol.searchCom(sponsor, "");
                    Sponsor.personList = new ArrayList<>();
                }
                assignSponsorList();
            }
        });

//        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (typeRequest == true){
//
//                }
//            }
//        };
//        sponsorList.setOnItemClickListener(onItemClickListener);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sponsor s;
                Intent intent;
                if (typeRequest) {
                    s = Sponsor.personList.get(i);
                    intent = new Intent(getActivity(), ViewPersonInfo.class);
                    intent.putExtra(ViewPersonInfo.KEY, s);
                }
                else {
                    s = Sponsor.companyList.get(i);
                    intent = new Intent(getActivity(), ViewCompanyInfo.class);
                    intent.putExtra(ViewCompanyInfo.KEY, s);
                }
                startActivityForResult(intent, 1);
            }
        };

        sponsorList.setOnItemClickListener(onItemClickListener);
        registerForContextMenu(sponsorList);

        return view;
    }

}