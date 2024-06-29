package kr.or.ddit.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class MyBatisUtil {
	
	private static SqlSessionFactory sqlSessionFactory;
	
	static {
		
		try {
			// 1-1. XML 설정 파일 읽어오기
			Charset charset = Charset.forName("UTF-8"); // 설정 파일의 인코딩 정보 설정 (한글처리 위함) 
			Resources.setCharset(charset);
			
			Reader rd = Resources.getResourceAsReader("config/mybatis-config.xml");
			
			// 1-2. 위에서 읽어온 Reader 객체를 이용하여 SqlSessionFactory 객체를 생성한다.
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(rd);
			
			rd.close(); // 스트림 닫기
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// sqlSessionFactory 객체를 통해 SqlSession 객체를 생성한다.
	// 메서드에 boolean값을 넣으면 오토커밋 여부가 정해짐
	/**
	 * sqlSession객체를 제공하기 위한 팩토리 메서드
	 * @return
	 */
	public static SqlSession getSqlSession() {
		return sqlSessionFactory.openSession();
	}
	
	public static SqlSession getSqlSession(boolean autoCommit) {
		return sqlSessionFactory.openSession(autoCommit);
	}
	
	
	
	
}
