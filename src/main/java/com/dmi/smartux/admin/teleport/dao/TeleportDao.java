package com.dmi.smartux.admin.teleport.dao;

import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.common.dao.CommonDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TeleportDao extends CommonDao {

    /**
     * 이미지서버 URL 조회
     * @return
     */
    public String getImgServerUrl() {
        return (String)getSqlMapClientTemplate().queryForObject("admin_news.getImageURL");
    }

//    public List<ViewVO> getPanelTitleTempList(String panelId) {
//        return getSqlMapClientTemplate().queryForList("admin_teleport.getPanelTitleTempList", panelId);
//    }
}