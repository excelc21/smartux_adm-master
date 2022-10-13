package com.dmi.smartux.admin.imcs.service;

import com.dmi.smartux.admin.commonMng.vo.MenuTreeVo;

/**
 * 2020-06-25 추가
 */
public interface ImcsService {
	
	/**
	 * imcs 매핑정보 등록
	 * @param param
	 * @return int
	 * @throws Exception
	 */
	int insertImcs(String gubun, String mims_type, String mims_id, String imcs_type, String imcs_id, String ab_yn) throws Exception;

	/**
	 * imcs 여부 체크
	 * @param param
	 * @return int
	 * @throws Exception
	 */
	int checkImcs(String mims_type, String menu_id);

	/**
	 * imcs 지면삭제
	 * @param param
	 * @return int
	 * @throws Exception
	 */
	void deletePap(String panel_id, String title_id);

}
