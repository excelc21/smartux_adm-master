package com.dmi.smartux.admin.mainpanel.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.mainpanel.vo.PanelTitleTreamVO;
import com.dmi.smartux.common.dao.CommonIptvDao;

@Repository
public class PanelViewIptvDao extends CommonIptvDao {
	
	/**
	 * 지면 노출단말 업데이트
	 * @return
	 * @throws Exception
	 */
	public void insertPaperTerminal(Map<String, String> param) {
		
		getSqlMapClientTemplate().update("paper_iptv.insertPaperTerminal", param);
	}

	public List<String> getPaperTerminal(String paper_id) {
		return getSqlMapClientTemplate().queryForList("paper_iptv.getPaperTerminal", paper_id);
	}

	public void deletePaperTerminal(String paper_id) {
		getSqlMapClientTemplate().delete("paper_iptv.deletePaperTerminal", paper_id);
	}
	
	public List<PanelTitleTreamVO> getLikePaperTerminal(String paper_id) {
		return getSqlMapClientTemplate().queryForList("paper_iptv.getLikePaperTerminal", paper_id);
	}
	
	public void deleteLikePaperTerminal(String paper_id) {
		getSqlMapClientTemplate().delete("paper_iptv.deleteLikePaperTerminal", paper_id);
	}
}
