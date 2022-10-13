package com.dmi.smartux.admin.hdtv.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.hdtv.service.HDTVService;
import com.dmi.smartux.admin.hdtv.vo.HDTVVO;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

@Service
public class HDTVServiceImpl implements HDTVService {

	@Override
	public HDTVVO getHDTVStartupXml() throws Exception {
		HDTVVO data = new HDTVVO();
		
		String filePath = SmartUXProperties.getProperty("hdtv_info.dir");
		String fileName = SmartUXProperties.getProperty("hdtv_info.xml.fileName");
//		BufferedReader br = new BufferedReader(new FileReader(new File(filePath+fileName)));
//		String line;
//		StringBuilder sb = new StringBuilder();
//
//		while((line=br.readLine())!= null){
//		    sb.append(line.trim());
//		}
//		System.out.println(sb.toString());
		
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(filePath+fileName);
		FileInputStream in = new FileInputStream(xmlFile);
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		
		List<Element> elements = root.getChildren();
		
		for(Element element : elements){
			if(element.getName().equals("emgency")){
				List<Element> items = element.getChildren();
				for(Element item : items){
					if(item.getName().equals("status")){
						data.setStatus(item.getText());
					}else if(item.getName().equals("message_yn")){
						data.setMessage_yn(item.getText());
					}else if(item.getName().equals("message")){
						data.setMessage(item.getText());
					}else if(item.getName().equals("net_type")){
						data.setNet_type(item.getText());
					}
					//System.out.println(item.getText());
				}
			}
		}
		return data;
	}

	@Override
	public void setHDTVStartupXml(HDTVVO vo) throws Exception {
		// TODO Auto-generated method stub
		String filePath = SmartUXProperties.getProperty("hdtv_info.dir");
		String fileName = SmartUXProperties.getProperty("hdtv_info.xml.fileName");
		
		Document doc = new Document();  
		Element root = new Element("result");
		Element emgency = new Element("emgency");
		root.addContent(emgency);//root element 의 하위 element 를 만들기
		
		Element status = new Element("status");
		Element message_yn = new Element("message_yn");
		Element message = new Element("message");
		Element net_type = new Element("net_type");
		  
		emgency.addContent(status);
		emgency.addContent(message_yn);
		emgency.addContent(message);
		emgency.addContent(net_type);
		  
		status.setText(vo.getStatus());
		message_yn.setText(vo.getMessage_yn());
		message.setText(vo.getMessage());
		net_type.setText(vo.getNet_type());
		
		doc.setRootElement(root);
		
		
		FileOutputStream out = new FileOutputStream(filePath+fileName); 
        //xml 파일을 떨구기 위한 경로와 파일 이름 지정해 주기
	    XMLOutputter serializer = new XMLOutputter();                 
	                                                                    
	    Format f = serializer.getFormat();                            
	    f.setEncoding("UTF-8");
	    //encoding 타입을 UTF-8 로 설정
	    f.setIndent(" ");                                             
	    f.setLineSeparator("\r\n");                                   
	    f.setTextMode(Format.TextMode.TRIM);                          
	    serializer.setFormat(f);                                      
	                                                                    
	    serializer.output(doc, out);                                  
	    out.flush();                                                  
	    out.close();    
	      
	    //String 으로 xml 출력
	    // XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8")) ;
	    // System.out.println(outputter.outputString(doc));
		
	}
	
	
}
