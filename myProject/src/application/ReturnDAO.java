package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controllers.AdminController;

public class ReturnDAO {

	StringBuffer sql = new StringBuffer();
	ResultSet rs = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	
	/** ��ǰ �ֹ��� ���ϸ���Ʈ�� ������ �߰� */
	public int insertData(ReturnVO rt) {
		int i = 0;
		sql.setLength(0);
		sql.append("insert into returnlist values(return_seq.nextval, ?,?,?,?,?,?,?,?,?)");
				//(seq, id, stylenum, rentaldate, returndate, name, phone, address, status, rentalnum)
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, rt.getId());
			pstmt.setString(2, rt.getStylenum());
			pstmt.setDate(3, new java.sql.Date(rt.getRentaldate().getTime()));
			pstmt.setDate(4, new java.sql.Date(rt.getReturndate().getTime()));
			pstmt.setString(5, rt.getName());
			pstmt.setString(6, rt.getPhone());
			pstmt.setString(7, rt.getAddress());
			pstmt.setString(8, rt.getStatus());
			pstmt.setInt(9, rt.getRentalnum());
			i = pstmt.executeUpdate();
			System.out.println(i + "�� DB insert ����");
			
		}catch(SQLException e) {
			System.err.println("������ ���� - DB insert ����");
			e.printStackTrace();
		}finally { ConnUtil.closeAll(con, pstmt, rs);}
		return i;
	}
	
	
	/** ���� ���� ��� ��ȸ �޼��� */
	public void getReturnTable() {
		RecentTableVO rt = null;
		sql.setLength(0);
		sql.append("select rentalnum, returnnum, to_char(returndate, 'RRRR/MM/DD') as returndate, "
				+ "name, id, stylenum, status, address from returnlist " 
				+ "where trunc(returndate) = trunc(sysdate)");
		try {
			con = application.ConnUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				rt = new RecentTableVO();
				
				rt.setRentalnum(rs.getInt("rentalnum"));
				rt.setReturndate(rs.getString("returndate"));
				rt.setAddress(rs.getString("address"));
				rt.setStatus(rs.getString("status"));
				rt.setStylenum(rs.getString("stylenum"));
				rt.setReturnnum(rs.getInt("returnnum")); 
				rt.setId(rs.getString("id"));
				rt.setName(rs.getString("name"));
				
				AdminController.recentList.add(rt);
			}
		}catch (SQLException e) {
			System.out.println("���̺� ���� ����");
			e.printStackTrace();
		}finally {application.ConnUtil.closeAll(con, pstmt, rs);}
	}
	
	/** ���� ��ü ��� ��ȸ �޼��� */
	public void getOverdueTable() {
		RecentTableVO rt = null;
		sql.setLength(0);
		sql.append("select rentalnum, address, status, stylenum, returnnum, id, name, "
				+ "to_char(returndate, 'RRRR/MM/DD') as returndate, "
				+ "to_char(rentaldate, 'RRRR/MM/DD') as rentaldate from returnlist "
				+ "where returndate < trunc(sysdate) "
				+ "order by returndate asc");
		try {
			con = application.ConnUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				rt = new RecentTableVO();
				
				rt.setRentalnum(rs.getInt("rentalnum"));
				rt.setAddress(rs.getString("address"));
				rt.setStatus(rs.getString("status"));
				rt.setStylenum(rs.getString("stylenum"));
				rt.setReturnnum(rs.getInt("returnnum"));
				rt.setId(rs.getString("id"));
				rt.setName(rs.getString("name"));
				rt.setReturndate(rs.getString("returndate"));
				rt.setRentaldate(rs.getString("rentaldate"));
				
				AdminController.recentList.add(rt);
			}
		}catch (SQLException e) {
			System.out.println("���̺� ���� ����");
			e.printStackTrace();
		}finally {application.ConnUtil.closeAll(con, pstmt, rs);}
	}
	
	
}