package com.dmi.smartux.admin.secondtv_push.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.common.dao.CommHmimsDao;
import com.dmi.smartux.admin.common.dao.CommMimsDao;
import com.dmi.smartux.admin.secondtv_push.dao.LatestScheduleDao;
import com.dmi.smartux.admin.secondtv_push.service.LatestScheduleService;
import com.dmi.smartux.admin.secondtv_push.vo.LatestContentVO;
import com.dmi.smartux.common.util.GlobalCom;

/**
 * 최신회 관리자 Service Impl
 *
 * @author dongho
 */
@Service
public class LatestScheduleServiceImpl implements LatestScheduleService {
	@Autowired
	LatestScheduleDao mDao;
	
	@Autowired
	CommHmimsDao hmimsDao;
	
	@Autowired
	CommMimsDao mimsDao;

	@Override
	public List getCategoryList() throws Exception {
		return mimsDao.getCategoryList();
	}

	@Override
	public List getLatestContentList(String startDate, String endDate) throws Exception {
		List<LatestContentVO> result = new ArrayList<LatestContentVO>();

		List list = getCategoryList();

		if (!GlobalCom.isEmpty(list)) {
			for (Object o : list) {
				//noinspection unchecked
				Map<String, String> cateMap = (Map<String, String>) o;
				String catID = cateMap.get("CAT_ID");
				String catGB = cateMap.get("CATEGORY_GB");

				if (!GlobalCom.isEmpty(catID)) {
					LatestContentVO vo = new LatestContentVO();
					vo.setCategoryID(catID);
					vo.setStartDate(startDate);
					vo.setEndDate(endDate);
					vo.setCategory_gb(catGB);

					LatestContentVO resultVO = mDao.getContent(vo);

					if (null != resultVO) {
						result.add(resultVO);
					}
				}
			}
		}

		return result;
	}

	@Override
	public int getUserListCount(String catID) throws Exception {
		return mimsDao.getUserListCount(catID);
	}

	@Override
	public List getUserList(LatestContentVO latestContentVO) throws Exception {
		return mimsDao.getUserList(latestContentVO);
	}

	@Override
	public int updatePushComplete(LatestContentVO completeVO) throws Exception {
		return hmimsDao.updatePushComplete(completeVO);
	}
}
