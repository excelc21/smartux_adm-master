package com.dmi.smartux.admin.gpack.contents.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackImcsCategoryAlbumVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackImcsCategoryVO;
import com.dmi.smartux.common.dao.CommonMimsDao;

@Repository
public class GpackContentsViewDao extends CommonMimsDao {
	
	/**
	 * 직접편성 목록 조회
	 * @param contentsVO
	 * @return 
	 * @throws Exception
	 */
	public List<GpackContentsVO> getGpackContentsList(GpackContentsVO contentsVO) throws Exception {
		List<GpackContentsVO> result = getSqlMapClientTemplate().queryForList("gpack_contents.getGpackContentsList", contentsVO);
		return result;
	}
	
	/**
	 * 직접편성 정보 조회
	 * @param contentsVO
	 * @return
	 * @throws Exception
	 */
	public GpackContentsVO getGpackContentsView(GpackContentsVO contentsVO) throws Exception {
		GpackContentsVO result = (GpackContentsVO)(getSqlMapClientTemplate().queryForObject("gpack_contents.getGpackContentsView", contentsVO));
		return result;
	}
	
	/**
	 * 직접편성 저장
	 * @param contentsVO
	 * @throws Exception
	 */
	public void insertGpackContents(GpackContentsVO contentsVO) throws Exception {
		getSqlMapClientTemplate().insert("gpack_contents.insertGpackContents", contentsVO);
	}
	
	/**
	 * 직접편성 수정
	 * @param contentsVO
	 * @throws Exception
	public int updateGpackContents(GpackContentsVO contentsVO) throws Exception {
		int result = getSqlMapClientTemplate().update("gpack_contents.updateGpackContents", contentsVO);
		return result;
	}
	 */
	
	/**
	 * 직접편성 삭제
	 * @param contentsVO
	 * @throws Exception
	 */
	public int deleteGpackContents(GpackContentsVO contentsVO) throws Exception {
		int result = getSqlMapClientTemplate().delete("gpack_contents.deleteGpackContents", contentsVO);
		return result;
	}

	/**
	 * 자동편성(IMCS 카테고리 연동) 목록 조회
	 * @param contentsAutoVO
	 * @return
	 * @throws Exception
	 */
	public List<GpackContentsAutoVO> getGpackContentsAutoList(GpackContentsAutoVO contentsAutoVO) throws Exception {
		List<GpackContentsAutoVO> result = getSqlMapClientTemplate().queryForList("gpack_contents.getGpackContentsAutoList", contentsAutoVO);
		return result;
	}
	
	/**
	 * 자동편성(IMCS 카테고리 연동) 정보 조회
	 * @param contentsAutoVO
	 * @return
	 * @throws Exception
	 */
	public GpackContentsAutoVO getGpackContentsAutoView(GpackContentsAutoVO contentsAutoVO) throws Exception {
		GpackContentsAutoVO result = (GpackContentsAutoVO)(getSqlMapClientTemplate().queryForObject("gpack_contents.getGpackContentsAutoView", contentsAutoVO));
		return result;
	}
	
	/**
	 * 프로모션 자동편성(IMCS 카테고리 연동) 조회
	 * @param contentsAutoVO
	 * @return
	 * @throws Exception
	 */
	public GpackContentsAutoVO getGpackOneCategory(GpackContentsAutoVO contentsAutoVO) throws Exception {
		GpackContentsAutoVO result = (GpackContentsAutoVO)(getSqlMapClientTemplate().queryForObject("gpack_contents.getGpackOneCategory", contentsAutoVO));
		return result;
	}
	
	/**
	 * 자동편성(IMCS 카테고리 연동) 저장
	 * @param contentsAutoVO
	 * @throws Exception
	 */
	public void insertGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception {
		getSqlMapClientTemplate().insert("gpack_contents.insertGpackContentsAuto", contentsAutoVO);
	}
	
	/**
	 * 자동편성(IMCS 카테고리 연동) 수정
	 * @param contentsAutoVO
	 * @throws Exception
	public int updateGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception {
		int result = getSqlMapClientTemplate().update("gpack_contents.updateGpackContentsAuto", contentsAutoVO);
		return result;
	}
	 */
	
	/**
	 * 자동편성(IMCS 카테고리 연동) 삭제
	 * @param contentsAutoVO
	 * @throws Exception
	 */
	public int deleteGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception {
		int result = getSqlMapClientTemplate().delete("gpack_contents.deleteGpackContentsAuto", contentsAutoVO);
		return result;
	}

	/**
	 * 직접편성 순서 변경
	 * @param contentsVO
	 * @return
	 * @throws Exception
	 */
	public int updateGpackContentsOrderby(GpackContentsVO contentsVO) throws Exception {
		int result = getSqlMapClientTemplate().update("gpack_contents.updateGpackContentsOrderby", contentsVO);
		return result;
	}

	/**
	 * 자동편성(IMCS 카테고리 연동) 순서 변경 - 안쓰는데 우선 만들어놈
	 * @param contentsVO
	 * @return
	 * @throws Exception
	 */
	public int updateGpackContentsAutoOrderby(GpackContentsAutoVO contentsAutoVO) throws Exception {
		int result = getSqlMapClientTemplate().update("gpack_contents.updateGpackContentsAutoOrderby", contentsAutoVO);
		return result;
	}
	
	/**
	 * IMCS 카테고리 조회
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryVO> getImcsCategoryList(String category_id, String gubun, String callby ) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_id", category_id);
		param.put("gubun", gubun);
		
		/*해당 카테고리 무료 컨텐츠만 조회 2014.07.24 */
		if ( StringUtils.equals ( callby, "playlist" )) {
			param.put ( "product_id", "10000" );
		}
		
		List<GpackImcsCategoryVO> result = getSqlMapClientTemplate().queryForList("gpack_contents.getImcsCategoryList", param);
		return result;
	}
	
	/**
	 * 미리보기 - 자동편성 IMCS 컨텐츠 조회 
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryAlbumVO> getPrevCategoryAutoList(String pack_id, String category_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id",     pack_id);
		param.put("category_id", category_id);
		
		List<GpackImcsCategoryAlbumVO> result = getSqlMapClientTemplate().queryForList("gpack_contents.getPrevCategoryAutoList", param);
		return result;
	}
	
	/**
	 * 미리보기 - 자동편성 (상용) IMCS 컨텐츠 조회 
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryAlbumVO> getPrevCategoryAutoBizList(String pack_id, String category_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id",     pack_id);
		param.put("category_id", category_id);
		
		List<GpackImcsCategoryAlbumVO> result = getSqlMapClientTemplate().queryForList("gpack_contents.getPrevCategoryAutoBizList", param);
		return result;
	}
	
	/**
	 * 미리보기 - 수동편성 IMCS 컨텐츠 조회 
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryAlbumVO> getPrevCategoryContentsList(String pack_id, String category_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id",     pack_id);
		param.put("category_id", category_id);
		
		List<GpackImcsCategoryAlbumVO> result = getSqlMapClientTemplate().queryForList("gpack_contents.getPrevCategoryContentsList", param);
		return result;
	}
	
	/**
	 * 미리보기 - 수동편성 (상용) IMCS 컨텐츠 조회 
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryAlbumVO> getPrevCategoryContentsBizList(String pack_id, String category_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id",     pack_id);
		param.put("category_id", category_id);
		
		List<GpackImcsCategoryAlbumVO> result = getSqlMapClientTemplate().queryForList("gpack_contents.getPrevCategoryContentsBizList", param);
		return result;
	}
}
