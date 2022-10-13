package com.dmi.smartux.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;

public class XmlUtil {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * <root><nodes><node></node><node></node>..</nodes><msg></msg></root> 구조
	 * @param xml xml형식의 String 데이터
	 * @param type 데이터가 value형식이면 value text형식이면 text
	 * @param level 최상위 루트가 파싱 대상이면 2 최상위 루트가 파싱 대상이 아니면 1
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> pushXmlParser(String xml, String type, int level) throws Exception{
		HashMap<String,String> resultMap = new HashMap<String,String>();
		
		try{
			String xmlRecords = xml;
			StringBuffer sb = new StringBuffer();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			if(xmlRecords!=null && !xmlRecords.equals("")){
				is.setCharacterStream(new StringReader(xmlRecords));
				Document doc = db.parse(is);
			     
				NodeList nlist = null;
				if(level == 1){
					nlist = doc.getChildNodes();
				}else if(level == 2){
					nlist = doc.getChildNodes().item(0).getChildNodes();
				}else{
					nlist = doc.getChildNodes().item(0).getChildNodes();
				}
				
				for (int i = 0, t_len = nlist.getLength();i < t_len; i ++)
				{
					String parseTxt = "";
					String parseNm = "";
					int nodeCnt = nlist.item(i).getChildNodes().getLength();
					
					if(nodeCnt <= 1){
						parseNm = nlist.item(i).getNodeName();
						if("value".equals(type)){
							parseTxt = parseTxt + nlist.item(i).getNodeValue();
						}else{//text
							parseTxt = parseTxt + nlist.item(i).getTextContent();
						}
						resultMap.put(parseNm, parseTxt);
					}else{
						for (int x = 0, sub_len = nodeCnt;x < sub_len; x ++)
						{
							parseNm = nlist.item(i).getChildNodes().item(x).getNodeName();
							parseTxt = "";
							if("value".equals(type)){
								parseTxt = parseTxt + nlist.item(i).getChildNodes().item(x).getNodeValue();
							}else{//text
								parseTxt = parseTxt + nlist.item(i).getChildNodes().item(x).getTextContent();
							}
							resultMap.put(parseNm+"_"+Integer.toString(x), parseTxt);
						}
					}
				}
			}
		}catch(Exception e){
			logger.info("[pushXmlParser][Error]- ["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
		}
		return resultMap; 
	}

}
