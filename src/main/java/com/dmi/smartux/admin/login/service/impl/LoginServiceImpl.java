package com.dmi.smartux.admin.login.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.login.dao.AdminMenuDao;
import com.dmi.smartux.admin.login.dao.LoginDao;
import com.dmi.smartux.admin.login.service.LoginService;
import com.dmi.smartux.admin.login.vo.AdminMenuVO;
import com.dmi.smartux.admin.login.vo.LoginVO;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

/**
 * 관리자 계정 관리 서비스 기능 구현 클래스
 * @author YJ KIM
 *
 */
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginDao dao;
	
	@Autowired
	AdminMenuDao adao;
	
	@Override
	@Transactional
	public String setLogin(String id, String pwd) throws Exception {
		
		String result = "SUCCESS";
		String salt_key = "";
		String orgPw = "";
		
		int ctn = dao.ctnAdminData(id);
		
		if(ctn <= 0){
			//데이터 미존재
			result = "NOT DATA";
		}else{
			LoginVO loginVO = dao.getAdminData(id);
			salt_key = loginVO.getSalt_key();
			orgPw = loginVO.getPassword();
			
			if(!("").equals(salt_key) && salt_key != null) {
				orgPw = orgPw + "" + salt_key;
				pwd = pwd + "" + salt_key;
				
				//비밀번호 불일치
				if(!orgPw.equals(pwd)){
					result = "PASSWORD FAIL";
					dao.setAdminLoginFail(id);
				}else{
					//로그인 시도 3회 실패시 잠금 상태로 인하여 로그인 제한(슈퍼유저로 잠금 해제로 가능)
					if(loginVO.getLoginfailcnt() >= Integer.parseInt(SmartUXProperties.getProperty("login.failCtn"))){
						result = "LOGIN FAIL CTN";
					}else{
						//로그인 성공 시 만료일이 지났으면 만료일 변경하도록 유도
						int todayDate = Integer.parseInt(GlobalCom.getTodayFormat());
						int expDate = Integer.parseInt(loginVO.getExp_date());
						if(todayDate > expDate){
							result = "EXPDATE FAIL"; 
						}else{
							//로그인 성공
							dao.setAdminLoginSuccess(id);
						}
					}
				}
			}else {
				//비밀번호 불일치
				if(!loginVO.getPassword().equals(pwd)){
					result = "PASSWORD FAIL";
					dao.setAdminLoginFail(id);
				}else{
					//로그인 시도 3회 실패시 잠금 상태로 인하여 로그인 제한(슈퍼유저로 잠금 해제로 가능)
					if(loginVO.getLoginfailcnt() >= Integer.parseInt(SmartUXProperties.getProperty("login.failCtn"))){
						result = "LOGIN FAIL CTN";
					}else{
						//로그인 성공 시 만료일이 지났으면 만료일 변경하도록 유도
						int todayDate = Integer.parseInt(GlobalCom.getTodayFormat());
						int expDate = Integer.parseInt(loginVO.getExp_date());
						if(todayDate > expDate){
							result = "EXPDATE FAIL"; 
						}else{
							//로그인 성공
							dao.setAdminLoginSuccess(id);
						}
					}
				}
				
			}
			
		}
		
		return result;
	}

	@Override
	@Transactional
	public String setExpDate(String id, String pwd, String expday, String salt)	 throws Exception {
		
		String result = "SUCCESS";
		
		int ctn = dao.ctnAdminData(id);
		if(ctn <= 0){
			//데이터 미존재
			result = "NOT DATA";
		}else{
			dao.setAdminExpDate(id,pwd,expday,salt);
		}
		return result;
	}

	@Override
	public List<LoginVO> getAdminList(LoginVO loginVO) throws Exception {
		
		
		List<LoginVO> result = null;
		
		result = dao.getAdminList(loginVO);
		
		return result;
	}

	@Override
	public int getAdminListCtn(LoginVO loginVO) {
		int result = 0;
		
		result = dao.getAdminListCtn(loginVO);
		
		return result;
	}

	@Override
	@Transactional
	public void setLoginFailClear(String id) throws Exception {
		dao.setAdminLoginSuccess(id);
	}

	@Override
	@Transactional
	public String setAdminInsert(LoginVO vo) throws Exception {
		
		String result = "SUCCESS";
		
		int ctn = dao.ctnAdminData(vo.getUser_id());
		
		if(ctn > 0){
			//데이터 존재
			result = "IS DATA";
		}else{
			dao.setAdminInsert(vo);
		}
		return result;
	}

	@Override
	public LoginVO getAdminView(LoginVO vo) throws Exception {
		LoginVO loginVO = dao.getAdminData(vo.getUser_id());
		return loginVO;
	}

	@Override
	@Transactional
	public void setAdminUpdate(LoginVO vo) throws Exception {
		dao.setAdminUpdate(vo);
	}

	@Override
	@Transactional
	public void setAdminDelete(String id) throws Exception {
		dao.setAdminDelete(id);
	}

	@Override
	@Transactional
	public void setSuperAdminAuthinit() throws Exception {
		dao.setSuperAdminAuthinit();
	}
	
	@Override
	public List<AdminMenuVO> getMenuList(String auth, String strArr) throws Exception {
		List<AdminMenuVO> result = null;
		
		result = adao.getMenuList(auth, strArr);
		
		return result;
	}
	
	@Override
	public String getCodeItem(String code, String item_code_1, String item_code_2) throws Exception {
		String result = "";
		
		result = dao.getCodeItem(code, item_code_1, item_code_2);
		
		return result;
	}
	
	@Override
	public String getCodeItemNm(String code, String item_code) throws Exception {
		String result = "N";
		String code_nm = "";
		code_nm = dao.getCodeItemNm(code, item_code);
		
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        if (StringUtils.isNotBlank(code_nm)) {
        	Date ds = formatter.parse(code_nm);
            Calendar cals = Calendar.getInstance();
            cals.setTime(ds);
            
            Calendar cal = Calendar.getInstance();
            if (cals.equals(cal) || (cals.before(cal))) {
            	result = "Y";
    		}
		}
		return result;
	}
	
}
