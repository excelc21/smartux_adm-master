package com.dmi.smartux.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import com.dmi.smartux.admin.login.dao.AdminMenuDao;
import com.dmi.smartux.admin.login.vo.AdminMenuVO;
import com.dmi.smartux.common.util.CookieUtil;

public class IptvInterceptor implements HandlerInterceptor{
	private final Log logger = LogFactory.getLog(this.getClass());
	
	long startTime;
	long endTime;
	
	@Autowired
	AdminMenuDao adao;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean isAuth = false;
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try {
			

			logger.info("[Interceptor]["+ip+"] ["+url+"][START] ");
			logger.debug("[Interceptor]["+ip+"] ["+url+"][START] ");
			/*
			if (CookieUtil.getCookie(request,"smartUXManager").getValue() == null
					|| CookieUtil.getCookie(request,"smartUXManager").getValue().equals("")) {
				if ("/smartux_adm/admin/login/login.do".equals(url)) {
					return true;
				}
				
				response.sendRedirect("/smartux_adm/admin/login/login.do");
				return false;
			}
			*/
			List<AdminMenuVO> result = null;
			result = adao.getMenuList("", url);
			
			if (!CollectionUtils.isEmpty(result)) {
				String auth = CookieUtil.getCookieUserAuth(request);

				for (int i = 0; i < result.size(); i++) {
					if (auth.equals(result.get(i).getUser_auth())) {
						isAuth = true;
						break;
					}
				}
			}
			else {
				isAuth = true;
			}
			
			if (!isAuth) {
				CookieGenerator cg = new CookieGenerator();
				cg.setCookieName("smartUXManager");
				cg.addCookie(response,"");
				cg.setCookieName("smartUXManagerAuth");
				cg.addCookie(response,"");
				cg.setCookieName("smartUXCookieTimeout");
				cg.addCookie(response,"");
				
				response.sendRedirect("/smartux_adm/admin/login/login.do");
				
				logger.info("[Interceptor]["+ip+"] ["+url+"][페이지 권한없음] ");
				logger.debug("[Interceptor]["+ip+"] ["+url+"][페이지 권한없음] ");
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.info("[Interceptor]["+ip+"] ["+url+"][ERROR] "+ e.getMessage());
			logger.debug("[Interceptor]["+ip+"] ["+url+"][ERROR] "+ e.getMessage());
			
			CookieGenerator cg = new CookieGenerator();
			cg.setCookieName("smartUXManager");
			cg.addCookie(response,"");
			cg.setCookieName("smartUXManagerAuth");
			cg.addCookie(response,"");
			cg.setCookieName("smartUXCookieTimeout");
			cg.addCookie(response,"");
			
			response.sendRedirect("/smartux_adm/admin/login/login.do");
			return false;
		}
		
		logger.info("[Interceptor]["+ip+"] ["+url+"][END] ");
		logger.debug("[Interceptor]["+ip+"] ["+url+"][END] ");
		return isAuth;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
