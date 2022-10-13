package com.dmi.smartux.admin.login.service;

import java.util.List;

import com.dmi.smartux.admin.abtest.vo.BPASListVo;
import com.dmi.smartux.admin.abtest.vo.BPASSearchVo;
import com.dmi.smartux.admin.abtest.vo.CategoryListVo;
import com.dmi.smartux.admin.abtest.vo.PanelListVo;
import com.dmi.smartux.admin.abtest.vo.TestListVo;
import com.dmi.smartux.admin.login.vo.LoginVO;
import com.dmi.smartux.admin.login.vo.PassLogVO;

/**
 *  2021.08.23 password log
 * password log Service
 * @author medialog
 *
 */
public interface PassLogService {
	
	/**
	 * 2021.08.23 password log 목록
	 * ABMS 호출
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	public List<PassLogVO> getPassList(PassLogVO searchVo) throws Exception;
	
	/**
	 * password log update 기능
	 * @param vo	password log  객체
	 * @throws Exception
	 */
	public void insertPass(PassLogVO vo) throws Exception;
	
	/**
	 * password log delete 기능
	 * @param vo	password log  객체
	 * @throws Exception
	 */
	public void deletePass(int pid) throws Exception;
}
