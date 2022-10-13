package com.dmi.smartux.admin.backgroundimage.dao;

import com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageInfo;
import com.dmi.smartux.admin.backgroundimage.vo.CategoryAlbumVO;
import com.dmi.smartux.common.dao.CommonDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BackgroundImageDao extends CommonDao {
	private final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 카테고리 코드를 이용하여 입력받은 카테고리의 하위 카테고리와 앨범들을 조회
	 * @param category_id	카테고리 코드
	 * @return				하위 카테고리와 앨범 정보
	 * @throws Exception
	 */
	public List<CategoryAlbumVO> getCategoryAlbumList(String category_id, String type, String series_yn) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_id", category_id);
		param.put("type", type);
		param.put("series_yn", series_yn);
		
		List<CategoryAlbumVO> result = getSqlMapClientTemplate().queryForList("admin_backgroundimage.getCategoryAlbumList", param);
		
		return result;
	}
	
}
