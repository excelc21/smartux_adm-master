package com.dmi.smartux.admin.teleport.service.impl;

import com.dmi.smartux.admin.teleport.dao.TeleportDao;
import com.dmi.smartux.admin.teleport.service.TeleportService;
import com.dmi.smartux.admin.teleport.vo.TeleportInfo;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.JsonFileator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.SearchType.anchor_txt;

@Service
public class TeleportServiceImpl implements TeleportService {

	private final JsonFileator teleport;
	private final TeleportDao dao;
	@Autowired
	public TeleportServiceImpl(@Qualifier("teleport") JsonFileator teleport, TeleportDao dao) {
		this.teleport = teleport;
        this.dao = dao;
    }

	@Override
	public List getTeleportList(String searchType, String searchText) throws Exception {
        if(StringUtils.isEmpty(searchText)) {
            return this.teleport.getList();
        }
		return this.teleport.getList(searchType, searchText);
	}

	@Override
	public String insertTeleport(TeleportInfo teleportInfo) throws Exception {
		List<TeleportInfo> list = this.teleport.getList(anchor_txt.key(), teleportInfo.getAnchor_txt());
		if(CollectionUtils.isNotEmpty(list)) {
			return SmartUXProperties.getProperty("message.key1");
		}
		this.teleport.insert(teleportInfo);
		return null;
	}


	@Override
	public void deleteList(int[] orderList) throws Exception {
		this.teleport.delete(orderList);
	}

    @Override
    public String setTeleport(TeleportInfo teleportInfo) throws Exception {
		List<TeleportInfo> list = this.teleport.getList(anchor_txt.key(), teleportInfo.getAnchor_txt());
		if(CollectionUtils.isNotEmpty(list)
			&& teleportInfo.getAnchor_id() != list.get(0).getAnchor_id()) {
			return SmartUXProperties.getProperty("message.key1");
		}
        this.teleport.update(teleportInfo);
		return null;
    }
    @Override
    public void setTeleportOrder(int[] codeList) throws Exception {
        this.teleport.setOrder(codeList);
    }


}
