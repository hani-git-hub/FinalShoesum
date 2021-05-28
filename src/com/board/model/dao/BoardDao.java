package com.board.model.dao;

import static com.common.JDBCTemplate.close;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.board.model.vo.Board;
import com.member.model.dao.MemberDao;

public class BoardDao {
	
	private Properties prop = new Properties();
	
	public BoardDao() {
		try {
			String filePath=MemberDao.class.getResource("/sql/board_sql.properties").getPath();
			prop.load(new FileReader(filePath));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Board> allMyBoards(Connection conn, int memberNo){
		//내가쓴 게시글 가져오기
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<Board> list=new ArrayList();
		Board b=null;
		try {
			pstmt=conn.prepareStatement(prop.getProperty("allMyBoards"));
			pstmt.setInt(1, memberNo);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				b= new Board();
				b.setQabNo(rs.getInt("qab_number"));
				b.setQabTitle(rs.getString("qab_title"));
				b.setQabWriter(rs.getString("qab_writer"));
				b.setQabContent(rs.getString("qab_content"));
				b.setQabDate(rs.getDate("qab_date"));
				b.setQabPw(rs.getInt("qab_pw"));
				b.setQabState(rs.getInt("qab_state"));
				list.add(b);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}
	
	/* 질문게시판 가져오기 */
	public List<Board> boardList(Connection conn){
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<Board> list = new ArrayList();
		Board b=null;
		try {
			pstmt = conn.prepareStatement(prop.getProperty("boardList"));
			result = pstmt.executeQuery();
			while(result.next()) {
				b = new Board();
				b.setQabNo(result.getInt("QAB_NUMBER"));
				b.setQabTitle(result.getString("QAB_TITLE"));
				b.setQabWriter(result.getString("QAB_WRITER"));
				b.setQabDate(result.getDate("QAB_DATE"));
				b.setQabState(result.getInt("QAB_STATE"));
				list.add(b);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(result);
			close(pstmt);
		}
		return list;

	}

	/* 글쓰기 */
	public int insertBoard(Connection conn, Board b) {
		PreparedStatement pstmt = null;
		int result=0;
		try {
			pstmt=conn.prepareStatement(prop.getProperty("insertBoard"));
			pstmt.setString(1,b.getQabTitle());
			pstmt.setString(2, b.getQabContent());
			pstmt.setString(3, b.getQabWriter());
			pstmt.setInt(4,b.getQabPw());
			result=pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}return result;
		
		
	}

}
