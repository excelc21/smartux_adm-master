package com.dmi.smartux.admin.starrating.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.starrating.dao.StarRatingDao;
import com.dmi.smartux.admin.starrating.service.StarRatingService;
import com.dmi.smartux.admin.starrating.vo.HistoryVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingSearchVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingVO;

@Service
public class StarRatingServiceImpl implements StarRatingService {
	@Autowired
	StarRatingDao dao;
	
	/**
	 * 별점 목록 조회
	 */
	@Override
	public List<StarRatingVO> getStarRatingList(StarRatingSearchVO vo) throws Exception {
		return dao.getStarRatingList(vo);
	}
	
	/**
	 * 별점 목록 개수 조회
	 */
	@Override
	public int getStarRatingListCnt(StarRatingSearchVO vo) throws Exception {
		return dao.getStarRatingListCnt(vo);
	}
	
	/**
	 * 별점 등록
	 */
	@Override
	public void insertProc(StarRatingVO vo) throws Exception {
		StarRatingVO p_srVo = new StarRatingVO();
		
		p_srVo.setSr_id(dao.getStarRatingId());
		p_srVo.setSr_title(vo.getSr_title());
		p_srVo.setUse_yn("N");
		p_srVo.setSr_type("0");
		p_srVo.setSr_order("0");
		p_srVo.setReg_id(vo.getReg_id());
		p_srVo.setMod_id(vo.getMod_id());
		p_srVo.setSystem_gb(vo.getSystem_gb());
		
		dao.insertProc(p_srVo);
		
		for(String sr : vo.getSrList()){
			String arr[] = sr.split("\\|");
			
			if(6 < arr.length){
				StarRatingVO srVo = new StarRatingVO();
				srVo.setSr_id(dao.getStarRatingId());
				srVo.setSr_type("1");
				srVo.setSr_title(arr[1]);
				srVo.setSr_order(arr[2]);
				srVo.setSr_point(arr[3]);
				srVo.setImg_file(arr[4]);
				srVo.setUse_yn("N");
				srVo.setSr_pid(p_srVo.getSr_id());
				srVo.setSystem_gb(vo.getSystem_gb());
				srVo.setReg_id(vo.getReg_id());
				srVo.setMod_id(vo.getMod_id());
				
				dao.insertProc(srVo);
			}
		}
	}
	
	/**
	 * 별점 수정
	 */
	@Override
	public void updateProc(StarRatingVO vo) throws Exception {
		StarRatingVO p_srVo = new StarRatingVO();
		
		p_srVo.setSr_id(vo.getSr_pid());
		p_srVo.setSr_order("0");
		p_srVo.setSr_title(vo.getSr_title());
		p_srVo.setMod_id(vo.getMod_id());
		dao.updateProc(p_srVo);
		
		for(String sr : vo.getSrList()){
			String arr[] = sr.split("\\|");
			if(6 < arr.length){
				StarRatingVO srVo = new StarRatingVO();
				
				if(arr[0].equals("Y")){
					if(arr[6].equals("N")){
						srVo.setSr_id(dao.getStarRatingId());
						srVo.setSr_type("1");
						srVo.setSr_title(arr[1]);
						srVo.setSr_order(arr[2]);
						srVo.setSr_point(arr[3]);
						srVo.setImg_file(arr[4]);
						srVo.setSr_pid(arr[5]);
						srVo.setUse_yn("N");
						srVo.setSystem_gb(vo.getSystem_gb());
						srVo.setReg_id(vo.getReg_id());
						srVo.setMod_id(vo.getMod_id());
						dao.insertProc(srVo);
					}
				}else{
					if(arr[6].equals("N")){
						srVo.setSr_id(arr[7]);
						srVo.setSr_title(arr[1]);
						srVo.setSr_order(arr[2]);
						srVo.setSr_point(arr[3]);
						srVo.setImg_file(arr[4]);
						srVo.setMod_id(vo.getMod_id());
						
						dao.updateProc(srVo);
					}else{
						dao.deleteProc(arr[7]);
					}
				}
			}
		}
	}
	
	/**
	 * 이미지 서버 URL 조회
	 */
	@Override
	public String getImgServer() throws Exception {
		return dao.getImgServer(); 
	}
	
	/**
	 * 별점 제목 조회
	 */
	@Override
	public String getTitle(String sr_id) throws Exception {
		return dao.getTitle(sr_id);
	}
	
	/**
	 * 별점 상세 목록 조회
	 */
	@Override
	public String getSrList(String sr_pid) throws Exception {
		List<StarRatingVO> srList = dao.getSrList(sr_pid);
		String resultSrList = "";
		int i = 1;
		
		for(StarRatingVO vo : srList){
			resultSrList += vo.getSr_order() + "\\|" + vo.getSr_point() + "\\|" + vo.getSr_title() + "\\|" + vo.getImg_file() + "\\|" + vo.getSr_id();
			if(i < srList.size()){
				resultSrList += "\\@\\^";
			}
			i++;
		}
		
		return resultSrList;
	}
	
	/**
	 * 활성화상태 조회
	 */
	@Override
	public int getUseYnCnt(String system_gb) throws Exception {
		return dao.getUseYnCnt(system_gb);
	}
	
	/**
	 * 활성화상태 수정
	 */
	@Override
	public void updateUseYn(StarRatingVO vo) throws Exception {
		dao.updateUseYn(vo);
	}
	
	/**
	 * 별점 내역 조회
	 */
	@Override
	public List<HistoryVO> getAlbumHistoryList(StarRatingSearchVO vo) throws Exception {
		return dao.getAlbumHistoryList(vo);
	}
	
	/**
	 * 별점 내역 개수 조회
	 */
	@Override
	public int getAlbumHistoryListCnt(StarRatingSearchVO vo) throws Exception {
		return dao.getAlbumHistoryListCnt(vo);
	}
	
	/**
	 * 별점 주기 내역 조회
	 */
	@Override
	public List<HistoryVO> getSrHistoryList(StarRatingSearchVO vo) throws Exception {
		return dao.getSrHistoryList(vo);
	}
	
	/**
	 * 별점 주기 내역 개수 조회
	 */
	@Override
	public int getSrHistoryListCnt(StarRatingSearchVO vo) throws Exception {
		return dao.getSrHistoryListCnt(vo);
	}

	/**
	 * 별점 주기 내역 평균별점 조회(검색조건)
	 */
	@Override
	public String getSrHistoryListAvg(StarRatingSearchVO vo) throws Exception {
		return dao.getSrHistoryListAvg(vo);
	}
	
	/**
	 * 앨범정보 조회
	 */
	@Override
	public HistoryVO getAlbum(String sr_id, String album_id) throws Exception {
		StarRatingSearchVO vo = new StarRatingSearchVO();
		vo.setFindName("ALBUM_ID");
		vo.setFindValue(album_id);
		vo.setSr_pid(sr_id);
		vo.setPageNum(1);
		vo.setPageSize(1);
		vo.setBlockSize(1);
		
		List<HistoryVO> list = dao.getAlbumHistoryList(vo);
		
		HistoryVO resultVo = new HistoryVO();
		resultVo.setAlbum_id(list.get(0).getAlbum_id());
		resultVo.setAlbum_name(list.get(0).getAlbum_name());
		
		return resultVo;
	}
}
