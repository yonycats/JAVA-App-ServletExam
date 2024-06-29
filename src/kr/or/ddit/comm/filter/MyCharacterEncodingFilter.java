package kr.or.ddit.comm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyCharacterEncodingFilter implements Filter {

	private String encoding;// 인코딩 방식
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		// 각 서블릿마다 인코딩을 할 필요 없이 필터에다가 인코딩 작업을 넣어놓기
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		// 중간에 가로채서 다음 필터에게 전달하고, 마지막 필터면 작업을 넘김
		// 체인 작업을 하지 않으면 해당 필터에서 작업을 먹어버림, 전달되지 않는다.
		chain.doFilter(req, resp);
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// xml의 init 파라미터 값을 지정해줄 수 있음
		if (filterConfig.getInitParameter("encoding") == null) { // 인코딩 정보가 존재하지 않으면
			this.encoding = "UTF-8"; // 기본 인코딩 설정하기
		} else {
			this.encoding = filterConfig.getInitParameter("encoding");
		}
		System.out.println("현재 필터에서 설정된 인코딩 정보 : " + this.encoding);
		
		
	}

}
