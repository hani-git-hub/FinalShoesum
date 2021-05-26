package com.product.model.service;

import static com.common.JDBCTemplate.close;
import static com.common.JDBCTemplate.commit;
import static com.common.JDBCTemplate.rollback;
import static com.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;

import com.product.model.dao.ProductDao;
import com.product.model.vo.Product;

public class ProductService {

	private ProductDao dao= new ProductDao();
	
	public List<Product> selectProduct(String proNo) {
		//상품 가져오기 =>상품상세페이지
		Connection conn=getConnection();
		List<Product> list = dao.selectProduct(conn, proNo);
		close(conn);
		return list;
	}

	public List<Product> allProduct() {
		// 전체 상품 리스트 가져오기 -> 메인 bestproduct 사용
		Connection conn=getConnection();
		List<Product> list = dao.allProduct(conn);
		close(conn);
		return list;
	}
	
	public List<Product> recentProduct() {
		// 최근 등록한 상품 5개 가져오기 -> 메인 newproduct 사용
		Connection conn=getConnection();
		List<Product> list = dao.recentProduct(conn);
		close(conn);
		return list;
	}
	
	public List<Product> hotProduct() {
		// 랜덤으로 3가지 상품 가져오기 -> 메인 hotdeal 사용
		Connection conn=getConnection();
		List<Product> list = dao.hotProduct(conn);
		close(conn);
		return list;
	}

	public List<Product> allWishes(int userNo){
		//찜상품 가져오기
		Connection conn=getConnection();
		List<Product> list=dao.allWishes(conn,userNo);
		close(conn);
		return list;
	}
	
	public int deleteWish(int userNo, int likeNo) {
		//찜한상품삭제=>테이블 삭제버튼
		Connection conn= getConnection();
		int result = dao.deleteWish(conn, userNo, likeNo);
		if(result>0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}
	
	public int checkDeleteWish(int userNo,String[] checkArr) {
		//찜한상품삭제=>테이블 밑 버튼(체크박스)
		Connection conn=getConnection();
		int result=0;
		int count=0;
		for(String pno : checkArr) {	//체크된 배열 반복문 돌면서 지우기
			int likeNo=Integer.parseInt(pno);
			result+=dao.deleteWish(conn,userNo,likeNo);
			if(result==0) {
				rollback(conn);
				close(conn);
				return 0;
			}
			count++;
		}
		if(result>=count) commit(conn);
		close(conn);
		return result;
	}
}
