package com.dmi.smartux.smartstart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;
import com.dmi.smartux.smartstart.vo.EcrmCategoryInfoVO;

@Repository
public class GenreVodBestListDao extends CommonDao{

	private final Log logger = LogFactory.getLog(this.getClass()); 
	
	/**
	 * GenreVodBestList을 조회하기 위해 사용되는 정보를 조회하는 DAO 클래스
	 * genre_code			장르코드
	 * @return				GenreVodBestList을 정보가 들어있는 GenreVodBestListVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	public Map<String, List<GenreVodBestListVO>> getGenreVodBestList() throws Exception {
		// TODO Auto-generated method stub
		// #를 구분자로 해서 장르코드를 장르데이터로 나눈다. 
		
		//버전을 업데이트 한다.
		Map<String, String> param2 = new HashMap<String, String>();
		param2.put("category_type", SmartUXProperties.getProperty("Versionitemtype.code2"));
		getSqlMapClientTemplate().update("mainpanel.setMainPanelTitleVersion", param2);
		param2.put("category_type", "CA_RANK");
		getSqlMapClientTemplate().update("mainpanel.setMainPanelTitleVersion", param2);
		
		
		Map<String, List<GenreVodBestListVO>> result = new HashMap<String, List<GenreVodBestListVO>>();
		
		// PT_UX_PANEL_TITLE 테이블에서 CATEGORY_TYPE 컬럼이 CAT_MAP인 것을 조건으로 주어 CATEGORY_ID 컬럼과 ALBUM_CNT 컬럼값을 조회한다
		List<EcrmCategoryInfoVO> categoryalbumresult = getSqlMapClientTemplate().queryForList("genrevodbestlist.getCheckVODList");
	
		for(EcrmCategoryInfoVO item : categoryalbumresult){
			String category_id = item.getCategory_id();
			
			String [] category_ids = category_id.split("\\|\\|");			// CATEGORY_ID 컬럼 조회 결과를 ||로 split을 한다
				
			List<GenreVodBestListVO> categoryresult = new ArrayList<GenreVodBestListVO>();
			
			int idx = -1;
			int [] album_cnts = new int[100];
		
			for(String category_item : category_ids){
				idx++;

				// getGenreVodBestList 조회
				Map<String, String> param = new HashMap<String, String>();
				param.put("genre_code", category_item);

				List<GenreVodBestListVO> tmpresult = getSqlMapClientTemplate().queryForList("genrevodbestlist.getGenreVodBestList", param);
		
				// ALBUM_CNT 개수 조회
				Map<String, String> parama = new HashMap<String, String>();
				parama.put("genre_code", category_item);
				
				int album_cnt_result = (Integer) getSqlMapClientTemplate().queryForObject("genrevodbestlist.getVodAlbumCount", parama);
				album_cnts[idx] = album_cnt_result;

				if(tmpresult != null){
					int end = 0;			//int end = -1;
						// 해당 카테고리에 대한 조회 결과의 갯수가 DB에 저장된 album_cnt 컬럼의 갯수값보다 크면 album_cnt 컬럼의 갯수값으로 설정한다
						if(tmpresult.size() > album_cnt_result){		//Integer.parseInt(album_cnts[idx])
							end = album_cnt_result;						//Integer.parseInt(album_cnts[idx])
							//logger.info("tmpresult.size() : " + end);
						}else{		// 해당 카테고리에 대한 조회 결과의 갯수가 DB에 저장된 album_cnt 컬럼의 갯수값보다 작거나 같으면 조회 결과 갯수값으로 설정한다	
							end = tmpresult.size();
							//logger.info("tmpresult.size()1 : " + end);
						}
					
						// Best VOD3 getGenreVodBestList를 받아서 랭킹조정프로세싱
						for(int i=0; i < end; i++){
							if( idx > 0 ){
								tmpresult.get(i).setRank_no(Integer.toString(Integer.parseInt(tmpresult.get(i).getRank_no()) + album_cnts[idx-1] ));		
							}
							categoryresult.add(tmpresult.get(i));
							//logger.info("tmpresult.get(i) : " + tmpresult.get(i));							
						}						
				}				
			}
			
			result.put(category_id, categoryresult);
		}
		
		return result;
	}
	
	/**
	 * GenreVodBestList을 조회하기 위해 사용되는 정보를 사전체크하는 DAO 클래스
	 * genre_code			장르코드
	 * @return				EcrmRank 정보가 들어있는 EcrmRankVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getCheckVODList() throws Exception {
		// TODO Auto-generated method stub
		List<EcrmRankVO> result = null;
		result = getSqlMapClientTemplate().queryForList("genrevodbestlist.getCheckVODList");
		
		return result;
	}
	
	
}



