package com.dmi.smartux.admin.mainpanel.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.mainpanel.vo.BubbleInsert;
import com.dmi.smartux.admin.mainpanel.vo.BubbleList;
import com.dmi.smartux.admin.mainpanel.vo.BubbleSearch;
import com.dmi.smartux.common.dao.CommonIptvDao;

@Repository
public class BubbleIptvDao extends CommonIptvDao {
	
	/**
	 * 말풍선 목록조회
	 * @return
	 * @throws Exception
	 */
	public List<BubbleList> getBubbleList(BubbleSearch vo) {
		
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		
		return getSqlMapClientTemplate().queryForList("admin_bubble_iptv.getBubbleList", vo);
	}

	/**
	 * 말풍선 목록 카운트 조회
	 * @return
	 * @throws Exception
	 */
	public int getBubbleListCnt(BubbleSearch vo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_bubble_iptv.getBubbleListCnt", vo);
	}

	/**
	 * 말풍선 삭제
	 * @return
	 * @throws Exception
	 */
	public void deleteBubble(String string) {
		getSqlMapClientTemplate().delete("admin_bubble_iptv.deleteBubble", string);
		
	}

	/**
	 * 말풍선 단말삭제
	 * @return
	 * @throws Exception
	 */
	public void deleteBubbleTerminal(BubbleInsert param) {
		getSqlMapClientTemplate().delete("admin_bubble_iptv.deleteBubbleTerminal", param);
		
	}

	/**
	 * 말풍선 채번
	 * @return
	 * @throws Exception
	 */
	public String getRegNo() {
		return (String) getSqlMapClientTemplate().queryForObject("admin_bubble_iptv.getRegNo");
	}

	/**
	 * 말풍선 등록
	 * @return
	 * @throws Exception
	 */
	public void insertBubble(BubbleInsert bubbleInsert) {
		getSqlMapClientTemplate().insert("admin_bubble_iptv.insertBubble", bubbleInsert);
	}

	/**
	 * 말풍선 단말등록
	 * @return
	 * @throws Exception
	 */
	public void insertBubbleTerm(BubbleInsert bubbleInsert) {
		getSqlMapClientTemplate().insert("admin_bubble_iptv.insertBubbleTerm", bubbleInsert);
		
	}

	/**
	 * 지면상세
	 * @return
	 * @throws Exception
	 */
	public BubbleInsert getBubbleDetail(String reg_no) {
		return (BubbleInsert) getSqlMapClientTemplate().queryForObject("admin_bubble_iptv.getBubbleDetail", reg_no);
	}

	/**
	 * 지면단말조회
	 * @return
	 * @throws Exception
	 */
	public List<String> getBubbleTerminal(String reg_no) {
		return (List<String>) getSqlMapClientTemplate().queryForList("admin_bubble_iptv.getBubbleTerminal", reg_no);
	}

	/**
	 * 말풍선 수정
	 * @return
	 * @throws Exception
	 */
	public void updateBubble(BubbleInsert bubbleInsert) {
		getSqlMapClientTemplate().update("admin_bubble_iptv.updateBubble", bubbleInsert);
		
	}
}
