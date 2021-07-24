package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {
    public static String authorize = "AUTHORIZE";
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment;
    public static String temp = null;
    public static Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        account = (Account) getIntent().getSerializableExtra(authorize);

        chipNavigationBar = (ChipNavigationBar)findViewById(R.id.chip_bar);
        chipNavigationBar.setItemSelected(R.id.program, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new ProgramFragment()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment temp = new Fragment();
                switch (i){
                    case R.id.program:
                        fragment = new ProgramFragment();
                        break;
                    case R.id.member:
                        fragment = new MemberFragment();
                        break;
                    case R.id.sponsor:
                        fragment = new SponsorFragment();
                        break;
                    case R.id.student:
                        fragment = new StudentFragment();
                        break;
                }
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment).commit();
            }
        });
    }
}