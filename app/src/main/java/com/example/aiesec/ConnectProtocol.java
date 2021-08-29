package com.example.aiesec;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static com.example.aiesec.MainActivity.con;

public class ConnectProtocol {
    public static boolean addStudentInfor(Student student){
        StringBuilder sql = new StringBuilder("INSERT INTO [Sinh viên] VALUES (");
        boolean added = false;
        if(student.getId()!=0){
            sql.append(student.getId().toString());
            added=true;
        }
        if(!student.getUniAbbreviation().equals("")){
            if(added)sql.append(",");
            sql.append("N\'"+student.getUniAbbreviation()+"\'");
            added=true;
        }
        if(student.getUni()!=""){
            if(added)sql.append(",");
            sql.append("N\'"+student.getUni()+"\'");
            added=true;
        }
        if(student.getFullName()!=""){
            if(added)sql.append(",");
            sql.append("N\'"+student.getFullName()+"\'");
            added=true;
        }

        if(student.getDob()!=""){
            if(added   )sql.append(",");
            sql.append("\'"+student.getDob()+"\'");
            added=true;
        }
        if(student.getMajor()!=""){
            if(added)sql.append(",");
            sql.append("N\'"+student.getMajor()+"\'");
            added=true;
        }
        if(student.getPhoneNum()!=""){
            if(added   )sql.append(",");
            sql.append("\'"+student.getPhoneNum()+"\')");
            added=true;
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean modStudent(Student s){
        StringBuilder sql = new StringBuilder("UPDATE [Sinh viên] SET ");
        boolean add=false;
        if(!s.getUni().equals("")){
            sql.append("[Tên trường]=N\'"+s.getUni()+"\'");
            add=true;
        }
        if(!s.getPhoneNum().equals("")){
            if(add)sql.append(",");
            sql.append("[SĐT]=\'"+s.getPhoneNum()+"\'");
            add=true;
        }
        if(!s.getDob().equals("")){
            if(add)sql.append(",");
            sql.append("[Ngày sinh]=N\'"+s.getDob()+"\'");
            add=true;
        }
        if(!s.getFullName().equals(""))
        {
            if(add)sql.append(",");
            sql.append("[Họ và tên]=N\'"+s.getFullName()+"\'");
            add=true;
        }
        if(!s.getMajor().equals("")){
            if(add)sql.append(",");
            sql.append("[Ngành học]=N\'"+s.getMajor()+"\'");
        }
        sql.append ("WHERE ([MSSV]="+s.getId().toString()+" AND [Tên viết tắt của trường]=N\'"+s.getUniAbbreviation()+"\')");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public static boolean rmvStudent(Student s){
        StringBuilder sql = new StringBuilder("DELETE FROM dbo.[Sinh viên] ");
        sql.append ("WHERE [MSSV]="+s.getId().toString()+" AND [Tên trường]=N\'"+s.getUni()+"\'");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static ArrayList<Student> searchStudentInfor(Student student, boolean order){
        ArrayList<Student> resultList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM [Sinh viên]");
        if (!student.getFullName().equals("") || student.getId() != null ||
                !student.getUni().equals("") || !student.getUniAbbreviation().equals("")){
            sql.append(" WHERE");
            boolean added = false;
            if (!student.getFullName().equals("")){
                sql.append(" [Họ và tên] = N\'" + student.getFullName() + "\'");
                added = true;
            }
            if (student.getId() != null) {
                if (added == true)
                    sql.append(" AND ");
                sql.append(" MSSV = " + String.valueOf(student.getId()));
                added = true;
            }
            if (!student.getUniAbbreviation().equals("")) {
                if (added == true)
                    sql.append(" AND ");
                sql.append(" [Tên viết tắt của trường] = N\'" + String.valueOf(student.getUniAbbreviation())+ "\'") ;
                added = true;
            }
            if (!student.getUni().equals("")) {
                if (added == true)
                    sql.append(" AND ");
                sql.append(" [Tên trường] = N\'" + student.getUni() + "\'");
                added = true;
            }
        }

        if (order == true)
            sql.append(" ORDER BY [Họ và tên]");
        else sql.append(" ORDER BY [Ngày sinh]");
        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Student temp = new Student();
                temp.setId(rs.getInt("MSSV"));
                temp.setUni(rs.getString("Tên trường"));
                temp.setUniAbbreviation(rs.getString("Tên viết tắt của trường"));
                temp.setUni(rs.getString("Tên trường"));
                temp.setFullName(rs.getString("Họ và tên"));
                temp.setDob(rs.getDate("Ngày sinh").toString());
                temp.setMajor(rs.getString("Ngành học"));
                temp.setPhoneNum(rs.getString("SĐT"));

                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }

    public static boolean joinProgram(Program p, Student s){
        String sql = "INSERT INTO [Đánh giá] VALUES (" + s.getId() + ", N'" + s.getUniAbbreviation()
                + "', " + p.getId() + ", 1, 1)";
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addProgramInfor(Program program){
        StringBuilder sql = new StringBuilder( "INSERT INTO dbo.[Chương trình] VALUES (");
        sql.append("N\'"+program.getName()+"\'");
        sql.append(",N\'"+program.getType()+"\'");
        sql.append(",N\'"+program.getCoordinate()+"\'");
        sql.append(",N\'"+program.getNation()+"\'");
        sql.append(",\'"+program.getStartTime()+"\'");
        sql.append(",\'"+program.getEndTime()+"\',");
        sql.append(program.getMoney().toString()+")");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static ArrayList<Program> searchProgramInfor(Program program){
        ArrayList<Program> resultList = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT [Chương trình].[ID_chương trình], dbo.[Chương trình].[Tên chương trình], " +
                "dbo.[Chương trình].[Loại chương trình], " +
                "dbo.[Chương trình].[Tọa độ], dbo.[Chương trình].[Tên quốc gia], dbo.[Chương trình].[Thời gian bắt đầu], [THời gian kết thúc]," +
                " dbo.[Chương trình].[Số tiền chi], COUNT(dbo.[Đánh giá].MSSV) AS 'Số sinh viên' " +
                "FROM dbo.[Chương trình] FULL JOIN dbo.[Đánh giá] ON [Chương trình].[ID_chương trình] = [Đánh giá].[ID_chương trình]");
        boolean added = false;
        if (!program.getName().equals("") || !program.getStartTime().equals("")
                || !program.getEndTime().equals("") || !program.getType().equals("")){
            sql.append(" WHERE");
            if (!program.getName().equals("")){
                sql.append(" [Tên chương trình] = N\'" + program.getName() + "\'");
                added = true;
            }

            if (!program.getStartTime().equals("")){
                if (added)  sql.append(" AND");
                sql.append(" [Thời gian bắt đầu] = \'" + program.getStartTime() + "\'");
                added = true;
            }

            if (!program.getEndTime().equals("")){
                if (added) sql.append(" AND");
                sql.append(" [Thời gian kết thúc] = \'" + program.getEndTime() + "\'");
                added = true;
            }

            if (!program.getType().equals("")){
                if (program.getType().contains("GD")){
                    if (added) sql.append(" AND");
                    sql.append(" [Loại chương trình] LIKE \'%GD%\'");
                    added = true;
                }

                if (program.getType().contains("TN")){
                    if (added) sql.append(" AND");
                    sql.append(" [Loại chương trình] LIKE \'%TN%\'");
                    added = true;
                }

                if (program.getType().contains("TT")){
                    if (added) sql.append(" AND");
                    sql.append(" [Loại chương trình] LIKE \'%TT%\'");
                }
            }
        }

        sql.append("GROUP BY [Chương trình].[ID_chương trình], dbo.[Chương trình].[Tên chương trình], dbo.[Chương trình].[Loại chương trình],\n" +
                "dbo.[Chương trình].[Tọa độ], dbo.[Chương trình].[Tên quốc gia], dbo.[Chương trình].[Thời gian bắt đầu], [THời gian kết thúc], \n" +
                "dbo.[Chương trình].[Số tiền chi]");

        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Program temp = new Program();
                temp.setId(rs.getInt("ID_chương trình"));
                temp.setName(rs.getString("Tên chương trình"));
                temp.setType(rs.getString("Loại chương trình"));
                temp.setCoordinate(rs.getString("Tọa độ"));
                temp.setNation(rs.getString("Tên quốc gia"));
                temp.setStartTime(rs.getDate("Thời gian bắt đầu").toString());
                temp.setEndTime(rs.getString("Thời gian kết thúc"));
                temp.setMoney(rs.getInt("Số tiền chi"));
                temp.setNumOfStudent(rs.getInt("Số sinh viên"));

                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    public static boolean modProgramInfor(Program p){
        StringBuilder sql = new StringBuilder("UPDATE dbo.[Chương trình] SET ");
        boolean add=false;
        if(p.getName()!=""){
            sql.append("[Tên chương trình]=N\'"+p.getName()+"\'");
            add=true;
        }
        if(p.getType()!=""){
            if(add) sql.append(",");
            sql.append("[Loại chương trình]=N\'"+p.getType()+"\'");
            add=true;
        }
        if(p.getCoordinate()!=""){
            if(add) sql.append(",");
            sql.append("[Tọa độ]=N\'"+p.getCoordinate()+"\'");
            add=true;
        }
        if(p.getNation()!=""){
            if(add) sql.append(",");
            sql.append(" [Tên quốc gia]=N\'"+p.getNation()+"\'");
            add=true;
        }
        if(p.getStartTime()!=""){
            if(add) sql.append(",");
            sql.append("[Thời gian bắt đầu] = \'"+p.getStartTime()+"\'");
        }
        if(p.getEndTime()!=""){
            if(add) sql.append(",");
            sql.append("[Thời gian kết thúc]=\'"+p.getEndTime()+"\',"+ "[Số tiền chi] = "+p.getMoney().toString());
            add=true;
        }
        if(p.getId()!=0){
            sql.append(" WHERE [ID_chương trình]="+p.getId().toString());
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public static ArrayList<Program> searchProgramActive(){
        String sql = "SELECT [ID_chương trình], [Tên chương trình], [Loại chương trình], " +
                "[Tọa độ], [Tên quốc gia], [Thời gian bắt đầu], [THời gian kết thúc]" +
                "FROM dbo.[Chương trình] " +
                "WHERE [THời gian kết thúc] > GETDATE()";

        ArrayList<Program> resultList = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Program temp = new Program();
                //TITLE DOI THANH TEN CHUONG TRINH
                //DUNG HIEN ID PHONG BAN
//                temp.setIdProg(rs.getInt("ID_chương trình"));
//                temp.setDate(rs.getString("Thời gian"));
//                temp.setJob(rs.getString("Công việc"));
                temp.setId(rs.getInt("ID_chương trình"));
                temp.setName(rs.getString("Tên chương trình"));
                temp.setType(rs.getString("Loại chương trình"));
                temp.setCoordinate(rs.getString("Tọa độ"));
                temp.setNation(rs.getString("Tên quốc gia"));
                temp.setStartTime(rs.getString("Thời gian bắt đầu"));
                temp.setEndTime(rs.getString("Thời gian kết thúc"));
                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    public static ArrayList<Program> searchJoinedProgram(Student s){
        StringBuilder sql = new StringBuilder("SELECT * FROM dbo.[Chương trình] JOIN dbo.[Đánh giá] " +
                "ON dbo.[Chương trình].[ID_chương trình] = dbo.[Đánh giá].[ID_chương trình]  " +
                "WHERE dbo.[Đánh giá].MSSV = " + s.getId() +
                " AND dbo.[Đánh giá].[Tên viết tắt của trường] = N\'"+ s.getUniAbbreviation()+"\'");
        ArrayList<Program> resultList = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Program temp = new Program();
                temp.setId(rs.getInt("ID_chương trình"));
                temp.setName(rs.getString("Tên chương trình"));
                temp.setType(rs.getString("Loại chương trình"));
                temp.setCoordinate(rs.getString("Tọa độ"));
                temp.setNation(rs.getString("Tên quốc gia"));
                temp.setStartTime(rs.getString("Thời gian bắt đầu"));
                temp.setEndTime(rs.getString("Thời gian kết thúc"));
                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    public static boolean deleteProgram(Program program){
        StringBuilder sql = new StringBuilder("DELETE FROM dbo.[Chương trình] ");
        sql.append("WHERE [ID_chương trình] = " + program.getId());

        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static boolean addMember (Member m, String user, String password){
        StringBuilder sql = new StringBuilder ("INSERT INTO [Thành viên] VALUES (");
        sql.append("N\'"+m.getFullName()+"\'");
        sql.append(",N\'"+m.getPhoneNum()+"\'");
        sql.append(",N\'"+m.getDob()+"\'");
        sql.append(","+m.getIdDep());
        sql.append(",N\'"+m.getRole()+"\')");
        boolean success = false;


        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            success = ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!success)   return  success;
        sql = new StringBuilder("SELECT TOP 1 ([ID_thành viên]) FROM dbo.[Thành viên] ORDER BY [ID_thành viên] DESC");

        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                m.setId(Integer.parseInt(rs.getString("ID_thành viên")));
        }catch (Exception e){
            e.printStackTrace();
        }

        sql = new StringBuilder("INSERT INTO [Tài khoản] VALUES (");
        sql.append(m.getId().toString());
        sql.append(", N\'"+user+"\'");
        sql.append(", N\'"+password+"\')");

        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public static boolean modMember(Member m){
        StringBuilder sql = new StringBuilder("UPDATE [Thành viên] SET ");
        boolean add=false;
        if(m.getFullName()!=""){
            sql.append("[Họ và tên]=N\'"+m.getFullName()+"\'");
            add=true;
        }
        if(m.getIdDep()!=0){
            if(add)sql.append(",");
            sql.append("[ID_phòng ban] = "+m.getIdDep());
            add=true;
        }
        if(m.getPhoneNum()!=""){
            if(add)sql.append(",");
            sql.append("[SĐT] = N\'"+m.getPhoneNum()+"\'");
            add=true;
        }
        if(m.getDob()!=""){
            if(add)sql.append(",");
            sql.append("[Ngày sinh] = \'"+m.getDob()+"\'");
        }
        sql.append(" WHERE [ID_thành viên]="+m.getId().toString()+"");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean rmvMember(Member m){
        StringBuilder sql = new StringBuilder("DELETE FROM dbo.[Thành viên] ");
        sql.append ("WHERE [ID_thành viên]="+m.getId().toString());
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static ArrayList<Member> searchMemberInfor(Member mem, String groupby){
        ArrayList<Member> resultList = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM [Thành viên]");
        boolean added = false;
        if(!mem.getId().equals(0)){
            sql.append("WHERE [ID_thành viên]="+mem.getId().toString()+" ");
            if(!mem.getFullName().equals(""))
                sql.append("AND [Họ và tên]=N\'"+mem.getFullName()+"\' ");
        }
        else if(!mem.getFullName().equals(""))
            sql.append("WHERE [Họ và tên]=N\'"+mem.getFullName()+"\' ");
        sql.append(groupby);
        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Member temp = new Member();
                temp.setId(rs.getInt("ID_thành viên"));
                temp.setFullName(rs.getString("Họ và tên"));
                temp.setPhoneNum(rs.getString("SĐT"));
                temp.setDob(rs.getString("Ngày sinh"));
                temp.setRole(rs.getString("Vai trò"));
                temp.setIdDep(rs.getInt("ID_phòng ban"));
                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }

    public static boolean addPerson(Sponsor s){
        Sponsor.Person p=s.person;
        StringBuilder sql=new StringBuilder("INSERT [Cá nhân] VALUES (-1,");
        sql.append("\'"+p.getCMND().toString()+"\'");
        sql.append(",N'"+p.getFullName()+"\'");
        sql.append(",N\'"+p.getVillage()+"\'");
        sql.append(",N\'"+p.getDistrict()+"\'");
        sql.append(",N\'"+p.getProvince()+"\'");
        sql.append(",\'"+p.getDob()+"\')");
        try{
            PreparedStatement ps=con.prepareStatement(sql.toString());
            return ps.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean modPerson (Sponsor p){
        StringBuilder sql = new StringBuilder("UPDATE [Cá nhân] SET ");
        boolean add=false;
        if(p.person.getFullName()!=""){
            sql.append("[Họ và tên]=N\'"+p.person.getFullName()+"\'");
            add=true;
        }
        if(p.person.getDob()!=""){
            if(add)sql.append(", ");
            sql.append("[Ngày sinh]=\'"+p.person.getDob()+"\'");
            add=true;
        }if(p.person.getDistrict()!=""){
            if(add)sql.append(", ");
            sql.append("[Quận/huyện/thị xã]=\'"+p.person.getDistrict()+"\'");
            add=true;
        }if(p.person.getVillage()!=""){
            if(add)sql.append(", ");
            sql.append("[Xã/phường]=\'"+p.person.getVillage()+"\'");
            add=true;
        }if(p.person.getProvince()!=""){
            if(add)sql.append(", ");
            sql.append("[Tỉnh/thành phố]=\'"+p.person.getProvince()+"\'");
        }
        sql.append(" WHERE [ID_Nhà tài trợ]=\'"+p.getIdSponsor().toString()+"\'");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addCom(Sponsor.Company c){
        StringBuilder sql=new StringBuilder("INSERT [Công ty] VALUES (-1,");
        sql.append("N\'"+c.getCompName()+"\'");
        sql.append(", \'"+c.getMS()+"\'");
        sql.append(", N\'"+c.getRepName()+"\'");
        sql.append(", N\'"+c.getRepPhoneNum()+"\')");
        try{
            PreparedStatement ps=con.prepareStatement(sql.toString());
            return ps.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean modCom(Sponsor c){
        StringBuilder sql = new StringBuilder("UPDATE [Công ty] SET ");
        boolean add=false;
        if(c.company.getCompName()!=""){
            sql.append("  [Tên công ty]=N\'"+c.company.getCompName()  +"\',");
            add=true;
        }if(c.company.getMS()!=0){
            sql.append("  [Mã số kinh doanh]=\'"+c.company.getMS().toString()  +"\'");
            add=true;
        }if(c.company.getRepName()!=""){
            if(add) sql.append(", ");
            sql.append("  [Tên người đại diện]=N\'"+c.company.getRepName()  +"\'");
            add=true;
        }if(c.company.getRepPhoneNum()!=""){
            if(add) sql.append(", ");
            sql.append("  [SĐT người đại diện]=N\'"+c.company.getRepPhoneNum()  +"\'");
            add=true;
        }
        sql.append(" WHERE [ID_Nhà tài trợ]=\'"+c.getIdSponsor().toString()+"\'");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean moreMoney(Sponsor s, Integer amount){
        /*StringBuilder sql = new StringBuilder("UPDATE [Nhà tài trợ] SET [Tổng tiền]=?");
        sql.append(" WHERE [ID_Nhà tài trợ]=?");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setInt(1,s.getMoney());
            ps.setInt(2,s.getIdSponsor());
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        StringBuilder sql2 = new StringBuilder("INSERT INTO [Quyên tiền] VALUES(");
        sql2.append(s.getIdSponsor());
        sql2.append(" ,3 ");
        sql2.append(" ,"+amount.toString()+", ");
        sql2.append(java.time.LocalDate.now().toString() + ")");

        try {
            PreparedStatement ps = con.prepareStatement(sql2.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static ArrayList<Sponsor> searchPerson(Sponsor s, String ord){
        boolean added = false;
        StringBuilder sql=new StringBuilder("SELECT * FROM [Nhà tài trợ] JOIN [Cá nhân] ON ([Nhà tài trợ].[ID_nhà tài trợ]=[Cá nhân].[ID_nhà tài trợ]) ");
        if (!s.person.getFullName().equals("")) {
            sql.append(" WHERE [Cá nhân].[Họ và tên]=N\'" + s.person.getFullName() + "\' ");
            added = true;
        }

        if (s.getIdSponsor() != null){
            if(added)   sql.append(" AND");
            sql.append(" [Nhà tài trợ].[ID_nhà tài trợ] = " + s.getIdSponsor());
        }

        sql.append(ord);
        ArrayList<Sponsor> resultList = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Sponsor temp = new Sponsor();
                temp.person=new Sponsor.Person();
                temp.setIdSponsor(rs.getInt("ID_nhà tài trợ"));
                temp.setIdDept(3);
                temp.setMoney(rs.getInt("Số tiền quyên góp"));
                temp.person.setCMND(rs.getInt("CMND"));
                temp.person.setFullName(rs.getString("Họ và tên"));
                temp.person.setVillage(rs.getString("Xã/phường"));
                temp.person.setDistrict(rs.getString("Quận/huyện/thị xã"));
                temp.person.setProvince(rs.getString("Tỉnh/thành phố"));
                temp.person.setDob(rs.getString("Ngày sinh"));
                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    public static ArrayList<Sponsor> searchCom(Sponsor s, String ord){
        StringBuilder sql= new StringBuilder("SELECT * FROM [Nhà tài trợ] JOIN [Công ty] ON ([Nhà tài trợ].[ID_nhà tài trợ]=[Công ty].[ID_Nhà tài trợ]) ");
        boolean added = false;
        if(!s.company.getCompName().isEmpty()) {
            sql.append(" WHERE [Công ty].[Tên công ty]= N\'" + s.company.getCompName() + "\' ");
            added = true;
        }

        if (s.getIdSponsor() != null){
            if (added)  sql.append(" AND");
            sql.append(" [Nhà tài trợ].[ID_nhà tài trợ] = " +s.getIdSponsor());
        }
        sql.append(ord);
        ArrayList<Sponsor> resultList = new ArrayList<>();

        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Sponsor temp = new Sponsor();
                temp.company=new Sponsor.Company();
                temp.setIdSponsor(rs.getInt("ID_nhà tài trợ"));
                temp.setIdDept(3);
                temp.setMoney(rs.getInt("Số tiền quyên góp"));
                temp.company.setCompName(rs.getString("Tên công ty"));
                temp.company.setMS(rs.getInt("Mã số kinh doanh"));
                temp.company.setRepName(rs.getString("Tên người đại diện"));
                temp.company.setRepPhoneNum(rs.getString("SĐT người đại diện"));
                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    public static boolean deleteSponsor(Sponsor s){
        if(s.person!=null){
            StringBuilder sql = new StringBuilder("DELETE FROM dbo.[Cá nhân] ");
            sql.append ("WHERE [ID_nhà tài trợ]="+s.getIdSponsor().toString());
            try {
                PreparedStatement ps = con.prepareStatement(sql.toString());
                ps.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(s.company!=null){
            StringBuilder sql1 = new StringBuilder("DELETE FROM dbo.[Công ty] ");
            sql1.append ("WHERE [ID_nhà tài trợ]="+s.getIdSponsor().toString());
            try {
                PreparedStatement ps = con.prepareStatement(sql1.toString());
                ps.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        StringBuilder sql3 = new StringBuilder("DELETE FROM dbo.[Quyên tiền] ");
        sql3.append ("WHERE [ID_nhà tài trợ]="+s.getIdSponsor().toString());
        try {
            PreparedStatement ps = con.prepareStatement(sql3.toString());
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }

        StringBuilder sql2 = new StringBuilder("DELETE FROM dbo.[Nhà tài trợ] ");
        sql2.append ("WHERE [ID_nhà tài trợ]="+s.getIdSponsor().toString());
        try {
            PreparedStatement ps = con.prepareStatement(sql2.toString());
            return ps.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Event> searchEvent(Program program){
        ArrayList<Event> resultList = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM [Mốc sự kiện] JOIN [Chương trình] ON ");
        sql.append( "[Mốc sự kiện].[ID_chương trình]=[Chương trình].[ID_chương trình]");

        boolean added = false;
        if (!program.getName().equals("") || !program.getId().equals(0)){
            sql.append(" WHERE");
            if (!program.getName().equals("")){
                sql.append(" [Tên chương trình] = N\'" + program.getName() + "\'");
                added = true;
            }
            if(!program.getId().equals(0)){
                if(added) sql.append(" AND ");
                sql.append(" [Mốc sự kiện].[ID_chương trình]=\'"+program.getId()+"\'");
            }
        }
        sql.append(" ORDER BY [Thời gian]");

        try{
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Event temp = new Event();
                //TITLE DOI THANH TEN CHUONG TRINH
                //DUNG HIEN ID PHONG BAN
                temp.setIdProg(rs.getInt("ID_chương trình"));
                temp.setDate(rs.getString("Thời gian"));
                temp.setJob(rs.getString("Công việc"));
                resultList.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    public static boolean addEvent(int idProg, Event event) {
        StringBuilder sql = new StringBuilder("INSERT INTO [Mốc sự kiện] VALUES (2,");
        sql.append(idProg);
        sql.append(", N'" + event.getJob() + "', '" +event.getDate() + "')");
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public static boolean deleteEvent (Event s){
        StringBuilder sql3 = new StringBuilder("DELETE FROM dbo.[Mốc sự kiện] ");
        sql3.append ("WHERE ([ID_chương trình]="+s.getIdProg().toString());
        sql3.append(" AND [Thời gian]=\'"+s.getDate()+"\'");
        sql3.append(" AND [Công việc]=N\'"+s.getJob()+"\')");
        try {
            PreparedStatement ps = con.prepareStatement(sql3.toString());
            return ps.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static Account searchAccount(String user, String pass){
        String sql = "SELECT * FROM [Tài khoản] " +
                "JOIN [Thành viên] ON [Thành viên].[ID_thành viên] = [Tài khoản].[ID_thành viên]" +
                "WHERE [Tài khoản].[Tên đăng nhập] = N\'" + user +
                "\' AND [Tài khoản].[Mật khẩu] = N\'" + pass +"\'";

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                Account temp = new Account();
                temp.setUserName(rs.getString("Tên đăng nhập"));
                temp.setPassword(rs.getString("Mật khẩu"));
                String role = rs.getString("Vai trò");
                if (role.equalsIgnoreCase("truong ban")
                        || role.equalsIgnoreCase("trưởng ban"))
                    temp.setRole(Account.DEPT_MANAGER);
                else temp.setRole(Account.DEPT_MEMBER);
                temp.setDept(rs.getInt("ID_phòng ban"));
                return temp;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
