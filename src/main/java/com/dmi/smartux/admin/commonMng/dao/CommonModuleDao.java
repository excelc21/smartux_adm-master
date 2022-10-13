package com.dmi.smartux.admin.commonMng.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.dmi.smartux.admin.commonMng.vo.CategoryAlbumListVo;
import com.dmi.smartux.admin.commonMng.vo.ChannelVO;
import com.dmi.smartux.admin.commonMng.vo.EventCategoryAlbumVo;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.admin.commonMng.vo.MenuTreeVo;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class CommonModuleDao extends CommonDao {
	
	/**
	 * 컨텐츠 연동형 매핑할 컨텐츠 검색 팝업
	 * @param categoryId	카테고리 코드
	 * @return				하위 카테고리와 앨범 정보
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EventCategoryAlbumVo> getEventCategoryAlbumList(CategoryAlbumListVo categoryalbumlistVo) throws Exception{
		
		List<EventCategoryAlbumVo> result = getSqlMapClientTemplate().queryForList("admin_common.getEventCategoryAlbumList", categoryalbumlistVo);
		
		return result;
	}
	
	/**
	 * 컨텐츠 검색
	 * @param categoryId	카테고리 코드
	 * @return				하위 카테고리와 앨범 정보
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EventCategoryAlbumVo> searchEventCategoryAlbumList(Map<String, Object> param)throws Exception{
		
		List<EventCategoryAlbumVo> result = getSqlMapClientTemplate().queryForList("admin_common.searchEventCategoryAlbumList", param);
		
		if(!CollectionUtils.isEmpty(result)){
			Comparator<EventCategoryAlbumVo> comparatorCateNm = new Comparator<EventCategoryAlbumVo>() {
				@Override  
	            public int compare(EventCategoryAlbumVo o1, EventCategoryAlbumVo o2) {  
	                 return o1.getCategory_name().compareToIgnoreCase(o2.getCategory_name());  
	            }  
			};  
	         
			Comparator<EventCategoryAlbumVo> comparatorAlbumId = new Comparator<EventCategoryAlbumVo>() {  
	            @Override  
	            public int compare(EventCategoryAlbumVo o1, EventCategoryAlbumVo o2) {  
	                 return o1.getAlbum_id().compareToIgnoreCase(o2.getAlbum_id());  
	            }  
			};  
	         
			ComparatorChain chain = new ComparatorChain();  
			chain.addComparator(comparatorCateNm);  
			chain.addComparator(comparatorAlbumId);  
	         
			Collections.sort(result, chain); 
			Collections.reverse(result);
		}
		
		return result;
	}
	
	

	/**
	 * 월정액 상품 리스트 조회
	 *
	 * @return 상품 리스트
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FlatRateVO> getFlatRateList() throws Exception {
		return getSqlMapClientTemplate().queryForList("admin_common.getFlatRateList");
	}

	/**
	 * 실시간 채널 리스트 조회
	 *
	 * @return 실시간 채널 리스트
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ChannelVO> getLiveCHList() throws Exception {
		return getSqlMapClientTemplate().queryForList("admin_common.getLiveCHList");
	}

	/**
	 * 컨텐츠명 조회
	 *
	 * @return 컨텐츠명
	 * @throws Exception
	 */
	public EventCategoryAlbumVo getContentsView(Map<String, Object> param) {
		return (EventCategoryAlbumVo) getSqlMapClientTemplate().queryForObject("admin_common.getContentsView", param);
	}

	/**
	 * 카테고리명 조회
	 *
	 * @return 카테고리명
	 * @throws Exception
	 */
	public EventCategoryAlbumVo getCategoryView(String categoryId) {
		return (EventCategoryAlbumVo) getSqlMapClientTemplate().queryForObject("admin_common.getCategoryView", categoryId);
	}
	
	/**
	 * 배너마스터 조회
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getBannerMasterPop() {
		return getSqlMapClientTemplate().queryForList("admin_common.getBannerMasterPop");		
	}
	
	/**
	 * 메뉴트리 정보 조회
	 * @param menu_id
	 * @return
	 */
	public MenuTreeVo selectMenuTreeInfo(String menu_id) {
		return (MenuTreeVo)getSqlMapClientTemplate().queryForObject("admin_common.selectMenuTreeInfo", menu_id);		
	}

	/**
	 * MIMS 카테고리 조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getMimsCategoryList(Map param) {		
		List<HashMap<String,String>> result= getSqlMapClientTemplate().queryForList("admin_common.getMimsCategoryList",param);		
		return result;
	}
	
	/**
	 * MIMS 카테고리로 구성된 카테고리 인지 조회
	 * @param param
	 * @return
	 */
	public String getMimsCategorySubYn(Map<String, String> param) {
		return  (String)getSqlMapClientTemplate().queryForObject("admin_common.getMimsCategorySubYn",param);		
	}
	/**
	 * IPTV MIMS 카테고리 조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getMimsIptvCategoryList(Map param) {		
		List<HashMap<String,String>> result= getSqlMapClientTemplate().queryForList("admin_common.getMimsIptvCategoryList",param);		
		return result;
	}
	
	/**
	 * IPTV MIMS 카테고리로 구성된 카테고리 인지 조회
	 * @param param
	 * @return
	 */
	public String getMimsIptvCategorySubYn(Map<String, String> param) {
		return  (String)getSqlMapClientTemplate().queryForObject("admin_common.getMimsIptvCategorySubYn",param);		
	}

	public List<FlatRateVO> getFlatRateList2() {
		return getSqlMapClientTemplate().queryForList("admin_common.getFlatRateList2");
	}
}
