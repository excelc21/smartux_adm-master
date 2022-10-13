package com.dmi.smartux.admin.pvs.service.impl;

import com.dmi.smartux.admin.news.vo.TargetVO;
import com.dmi.smartux.admin.pvs.dao.PvsDao;
import com.dmi.smartux.admin.pvs.service.PvsService;
import com.dmi.smartux.admin.pvs.vo.PvsProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PVS 서비스 Impl
 *
 * @author dongho
 */
@Service
public class PvsServiceImpl implements PvsService {
    @Autowired
    PvsDao mDao;

    @Override
    public List<PvsProductVO> getPvsProductList() throws Exception {
        return mDao.getPvsProductList();
    }

    @Override
    public List<PvsProductVO> getOptionalServiceList() throws Exception {
        return mDao.getOptionalServiceList();
    }

    @Override
    public List<String> getAllSaIDList(TargetVO targetVO) throws Exception {
        return mDao.getAllSaIDList(targetVO);
    }

    @Override
    public int getAllSaIDCount(TargetVO targetVO) throws Exception {
        return mDao.getAllSaIDCount(targetVO);
    }

    @Override
    public List<String> getIncludeSaIDList(TargetVO targetVO) throws Exception {
        return mDao.getIncludeSaIDList(targetVO);
    }

    @Override
    public int getIncludeSaIDCount(TargetVO targetVO) throws Exception {
        return mDao.getIncludeSaIDCount(targetVO);
    }

    @Override
    public List<String> getExcludeSaIDList(TargetVO targetVO) throws Exception {
        return mDao.getExcludeSaIDList(targetVO);
    }

    @Override
    public int getExcludeSaIDCount(TargetVO targetVO) throws Exception {
        return mDao.getExcludeSaIDCount(targetVO);
    }
}
