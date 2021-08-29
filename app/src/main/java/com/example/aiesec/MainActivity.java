package com.example.aiesec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private static String ip = "192.168.1.3";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String db = "AIESEC";
    private static String user = "sa";
    private static String pass = "123456";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+db;
    private TextView textView;
    public static Connection con = null;
    public void onClickForgot(View view){
        Toast.makeText(this, "Tạm thời chưa có chức năng này", Toast.LENGTH_LONG).show();
    }

    public void onClickLogin(View view){
        EditText acc = (EditText)findViewById(R.id.editTextTextPersonName);
        EditText pw = (EditText)findViewById(R.id.editTextTextPassword);
        String sql = "SELECT * FROM [Tài khoản] " +
                "JOIN [Thành viên] ON [Thành viên].[ID_thành viên] = [Tài khoản].[ID_thành viên]" +
                "WHERE [Tài khoản].[Tên đăng nhập] = N\'" + acc.getText() + "\' AND [Tài khoản].[Mật khẩu] = N\'" + pw.getText() +"\'";

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                Account temp = new Account();
                temp.setUserName(rs.getString("Tên đăng nhập"));
                temp.setPassword(rs.getString("Mật khẩu"));
                temp.setName(rs.getString("Họ và tên"));
                String role = rs.getString("Vai trò");
                if (role.equalsIgnoreCase("truong ban")
                        || role.equalsIgnoreCase("trưởng ban"))
                    temp.setRole(Account.DEPT_MANAGER);
                else temp.setRole(Account.DEPT_MEMBER);
                temp.setDept(rs.getInt("ID_phòng ban"));
//                textView.setText("Đăng nhập thành công");
                Intent intent = new Intent(this, MainScreen.class);
                intent.putExtra(MainScreen.authorize, temp);
                startActivityForResult(intent, 1);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Toast toast = Toast.makeText(this,
                "Tài khoản/mật khẩu không đúng, xin thử lại",
                Toast.LENGTH_LONG);
        toast.show();
    }
    public void onClickSignUp(View view){
        startActivity(new Intent(MainActivity.this, SignUp.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.welcome_text);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},
                PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            con = DriverManager.getConnection(url, user,pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Vui lòng thử lại", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Kết nối tới máy chủ thất bại", Toast.LENGTH_LONG).show();
        }
    }
}