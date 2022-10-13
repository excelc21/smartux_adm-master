package com.dmi.smartux.admin.greeting.dao;

import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.common.dao.CommonDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GreetingDao extends CommonDao {

    /**
     * 이미지서버 URL 조회
     * @param gubun
     * @return
     */
    public String getImgServerUrl(String gubun) {
        return (String)getSqlMapClientTemplate().queryForObject("admin_news.getImageURL");
    }

//    public List<ViewVO> getPanelTitleTempList(String panelId) {
//        return getSqlMapClientTemplate().queryForList("admin_teleport.getPanelTitleTempList", panelId);
//    }
}