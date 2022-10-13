package com.dmi.smartux.admin.imcs.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.imcs.vo.goodsVO;
import com.dmi.smartux.admin.imcs.vo.paramVO;

@Repository
public class goodsDao extends CommonDao {
	private final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 
	 * @param paramVO
	 * @return List<goodsVO>
	 * @throws DataAccessException
	 */
	public List<goodsVO> getGoodsList(paramVO paramVO) throws DataAccessException{
		
		//paramVO.setStart_rnum(paramVO.getPageNum()*paramVO.getPageSize()-paramVO.getPageSize()+1);
		//paramVO.setEnd_rnum(paramVO.getStart_rnum()+paramVO.getPageSize()-1);
		
		@SuppressWarnings("unchecked")
		List<goodsVO> result = getSqlMapClientTemplate().queryForList("goodsList.getGoodsList",paramVO);
		logger.debug(result);
		return result;
	}
	
	/**
	 * 페이징처리를 위한 토탈카운트
	 * @param paramVO
	 * @return Integer
	 * @throws DataAccessException
	 */
	public int getListCnt(paramVO paramVO) throws DataAccessException{
		
		int result=0;
		result=(Integer)getSqlMapClientTemplate().queryForObject("goodsList.getGoodsListCnt",paramVO);
		
		return result;
	}

}