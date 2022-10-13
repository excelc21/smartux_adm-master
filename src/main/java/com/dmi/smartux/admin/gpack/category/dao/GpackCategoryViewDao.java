package com.dmi.smartux.admin.gpack.category.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.gpack.category.vo.GpackCategoryVO;
import com.dmi.smartux.common.dao.CommonMimsDao;

@Repository
public class GpackCategoryViewDao extends CommonMimsDao {

	/**
	 * 카테고리의 콘텐츠 수 조회 (직접편성, IMCS카테고리 연동)
	 * @param categoryVO
	 * @return
	 * @throws Exception
	 */
	public int countGpackCategoryContents(GpackCategoryVO categoryVO) throws Exception {
		int count = (Integer) getSqlMapClientTemplate().queryForObject("gpack_category.countGpackCategoryContents", categoryVO);
		return count;
	}
	
	/**
	 * 새로운 카테고리 ID 생성
	 * @return
	 * @throws Exception
	 */
	public String getNewGpackCategoryId() throws Exception{
		String newId = (String) getSqlMapClientTemplate().queryForObject("gpack_category.getNewGpackCategoryId");
		return newId;
	}
	
	/**
	 * 카테고리 목록 조회
	 * @param categoryVO
	 * @return
	 * @throws Exception
	 */
	public List<GpackCategoryVO> getGpackCategoryList(GpackCategoryVO categoryVO) throws Exception {
		List<GpackCategoryVO> result = getSqlMapClientTemplate().queryForList("gpack_category.getGpackCategoryList", categoryVO);
		return result;
	}
	
	/**
	 * 카테고리 정보 조회
	 * @param categoryVO
	 * @return
	 * @throws Exception
	 */
	public GpackCategoryVO getGpackCategoryView(GpackCategoryVO categoryVO) throws Exception {
		GpackCategoryVO result = (GpackCategoryVO)(getSqlMapClientTemplate().queryForObject("gpack_category.getGpackCategoryView", categoryVO));
		return result;
	}
	
	/**
	 * 카테고리 정보 조회 (상용)
	 * @param categoryVO
	 * @return
	 * @throws Exception
	 */
	public GpackCategoryVO getGpackCategoryBizView(GpackCategoryVO categoryVO) throws Exception {
		GpackCategoryVO result = (GpackCategoryVO)(getSqlMapClientTemplate().queryForObject("gpack_category.getGpackCategoryBizView", categoryVO));
		return result;
	}
	
	/**
	 * 카테고리 저장
	 * @param categoryVO
	 * @throws Exception
	 */
	public void insertGpackCategory(GpackCategoryVO categoryVO) throws Exception {
		getSqlMapClientTemplate().insert("gpack_category.insertGpackCategory", categoryVO);
	}
	
	/**
	 * 카테고리 수정
	 * @param categoryVO
	 * @throws Exception
	public int updateGpackCategory(GpackCategoryVO categoryVO) throws Exception {
		int result = getSqlMapClientTemplate().update("gpack_category.updateGpackCategory", categoryVO);
		return result;
	}
	 */
	
	/**
	 * 카테고리 순서 수정
	 * @param categoryVO
	 * @throws Exception
	 */
	public int updateGpackCategoryOrderby(GpackCategoryVO categoryVO) throws Exception {
		int result = getSqlMapClientTemplate().update("gpack_category.updateGpackCategoryOrderby", categoryVO);
		return result;
	}
	
	/**
	 * 카테고리 삭제
	 * @param categoryVO
	 * @throws Exception
	 */
	public int deleteGpackCategory(GpackCategoryVO categoryVO) throws Exception {
		int result = getSqlMapClientTemplate().delete("gpack_category.deleteGpackCategory", categoryVO);
		return result;
	}
	
	/**
	 * 미리보기 카테고리 목록 조회
	 * @param categoryVO
	 * @return
	 * @throws Exception
	 */
	public List<GpackCategoryVO> previewGpackCategory(GpackCategoryVO categoryVO, String preview_gb) throws Exception {
		List<GpackCategoryVO> result = getSqlMapClientTemplate().queryForList("gpack_category.getGpackCategoryBizList", categoryVO);
		return result;
	}
}
