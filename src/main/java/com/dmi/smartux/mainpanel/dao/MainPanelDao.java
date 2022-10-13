package com.dmi.smartux.mainpanel.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmi.smartux.common.util.GlobalCom;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.mainpanel.vo.CategoryInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelInfoVO;

@Repository
public class MainPanelDao extends CommonDao {

	/**
	 * 패널 테이블에서 Main Panel 버전 정보 조회
	 * @return		Main Panel 버전 정보
	 * @throws Exception
	 */
	public String getMainPanelVersionInfo() throws Exception{
		String result = (String)(getSqlMapClientTemplate().queryForObject("mainpanel.getMainPanelVersionInfo"));
		return result;
	}
	
	/**
	 * 패널 지면 테이블에서 Main Panel 연동 정보 조회
	 * @return
	 * @throws Exception
	 */
	public List<MainPanelInfoVO> getMainPanelInterlockingInfo() throws Exception {
		
		//String img_url = SmartUXProperties.getProperty("imgserver.weburl") + SmartUXProperties.getProperty("imgserver.imgpath") + "/";
		Map<String, String> param = new HashMap<String, String>();
		//param.put("img_url", img_url);
		List<MainPanelInfoVO> result = getSqlMapClientTemplate().queryForList("mainpanel.getMainPanelInterlockingInfo", param);
		return result;
	}
	
	/**
	 * I20 편성 앨범 조회
	 * @return				I20 편성 앨범 조회
	 * @throws Exception
	 */
	public Map<String, List<AlbumInfoVO>> getI20AlbumList() throws Exception{
		// PT_UX_PANEL_TITLE에서 category_type이 CAT_MAP인것을 조회(category_id, album_cnt 값 조회)
		// category_id 와 album_cnt 값을 split
		// category_id 값으로 앨범 조회를 하면서 album_cnt에 정의된 갯수만큼 담는다
		// 그렇게 해서 나온 결과를 map에 담는다
		
		//버전을 업데이트 한다.
		Map<String, String> param2 = new HashMap<String, String>();
		param2.put("category_type", SmartUXProperties.getProperty("Versionitemtype.code3"));
		getSqlMapClientTemplate().update("mainpanel.setMainPanelTitleVersion", param2);
		
		Map<String, List<AlbumInfoVO>> result = new HashMap<String, List<AlbumInfoVO>>();
		
		// PT_UX_PANEL_TITLE 테이블에서 CATEGORY_TYPE 컬럼이 CAT_MAP인 것을 조건으로 주어 CATEGORY_ID 컬럼과 ALBUM_CNT 컬럼값을 조회한다
		List<CategoryInfoVO> categoryalbumresult = getSqlMapClientTemplate().queryForList("mainpanel.getCategoryIdAlbumcnt");
		
		for(CategoryInfoVO item : categoryalbumresult){
			String pt_id = item.getPt_id();
			String category_id = item.getCategory_id();
			String album_cnt = item.getAlbum_cnt();
			
			String [] category_ids = category_id.split("\\|\\|");			// CATEGORY_ID 컬럼 조회 결과를 ||로 split을 한다
			String [] album_cnts = album_cnt.split("\\|\\|");				// ALBUM_CNT 컬럼 조회 결과를 ||로 split을 한다
			
			List<AlbumInfoVO> categoryresult = new ArrayList<AlbumInfoVO>();

			boolean isNSC = GlobalCom.isNSCPanel(pt_id);
			
			int idx = -1;
			for(String category_item : category_ids){
				idx++;
				Map<String, String> param = new HashMap<String, String>();
				param.put("category_id", category_item);

				List<AlbumInfoVO> tmpresult;

				if (isNSC) {
					tmpresult = getSqlMapClientTemplate().queryForList("mainpanel.getNSCAlbumList", param);
				} else {
					tmpresult = getSqlMapClientTemplate().queryForList("mainpanel.getI20AlbumList", param);
				}

				if(tmpresult != null){
					int end = -1;
					// 해당 카테고리에 대한 조회 결과의 갯수가 DB에 저장된 album_cnt 컬럼의 갯수값보다 크면 album_cnt 컬럼의 갯수값으로 설정한다
					if(tmpresult.size() > Integer.parseInt(album_cnts[idx])){
						end = Integer.parseInt(album_cnts[idx]);
					}else{		// 해당 카테고리에 대한 조회 결과의 갯수가 DB에 저장된 album_cnt 컬럼의 갯수값보다 작거나 같으면 조회 결과 갯수값으로 설정한다	
						end = tmpresult.size();
					}
					
					for(int i=0; i < end; i++){
						categoryresult.add(tmpresult.get(i));
					}
				}
				
			}
			
			result.put(pt_id + category_id, categoryresult);
		}
		
		return result;
	}
}
