package com.dmi.smartux.admin.commonMng.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmi.smartux.admin.commonMng.vo.CategoryAlbumListVo;
import com.dmi.smartux.admin.commonMng.vo.ChannelVO;
import com.dmi.smartux.admin.commonMng.vo.EventCategoryAlbumVo;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.admin.commonMng.vo.MenuTreeVo;


public interface CommonModuleService {
	
	/**
	 * 컨텐츠 연동형 매핑할 컨텐츠 검색 팝업
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public List<EventCategoryAlbumVo> getEventCategoryAlbumList(CategoryAlbumListVo categoryalbumlistVo) throws Exception;
	
	/**
	 * 컨텐츠 검색
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public List<EventCategoryAlbumVo> searchEventCategoryAlbumList(CategoryAlbumListVo categoryalbumlistVo)throws Exception ;
		
	
	/**
	 * 실시간 채널 리스트 조회
	 *
	 * @return 실시간 채널 리스트
	 * @throws Exception
	 */
	public List<ChannelVO> getLiveCHList() throws Exception;

	/**
	 * 월정액 상품 리스트 조회
	 *
	 * @return 상품 리스트
	 * @throws Exception
	 */
	public List<FlatRateVO> getFlatRateList() throws Exception;

	/**
	 * 컨텐츠명 조회
	 *
	 * @return 컨텐츠명
	 * @throws Exception
	 */
	public EventCategoryAlbumVo getContentsView(String categoryId, String contentsId) throws Exception;

	/**
	 * 카테고리명 조회
	 *
	 * @return 카테고리명
	 * @throws Exception
	 */
	public EventCategoryAlbumVo getCategoryView(String categoryId) throws Exception;

	/**
	 * 배너 마스터 조회
	 *
	 * @return 배너 마스터
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getBannerMasterPop() throws Exception;
	
	/**
	 * 메뉴트리 정보조회
	 *
	 * @return 메뉴트리 정보
	 * @throws Exception
	 */
	public MenuTreeVo selectMenuTreeInfo(String menu_id) throws Exception ;
	
	/**
	 * MIMS카테고리 조회
	 *
	 * @return MIMS 카테고리
	 * @throws Exception
	 */
	List<HashMap<String, String>> getMimsCategoryList(Map param) throws Exception ;
	
	/**
     * (MIMS) 하위에 카테고리가 있는 카테고리 아이디 인지 유무
     * @param categoryId
     * @return
     * @throws Exception
     */
	String getMimsCategorySubYn(Map<String, String> param);
	
	/**
	 * IPTV MIMS카테고리 조회
	 *
	 * @return MIMS 카테고리
	 * @throws Exception
	 */
	List<HashMap<String, String>> getMimsIptvCategoryList(Map param) throws Exception ;
	
	/**
     * (IPTV MIMS) 하위에 카테고리가 있는 카테고리 아이디 인지 유무
     * @param categoryId
     * @return
     * @throws Exception
     */
	String getMimsIptvCategorySubYn(Map<String, String> param);

	/**
     * (MIMS) 월정액 상품 조회
     * @param categoryId
     * @return
     * @throws Exception
     */
	public List<FlatRateVO> getFlatRateList2();
	
}
