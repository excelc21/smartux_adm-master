package com.dmi.smartux.admin.hotvod.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.dmi.smartux.admin.hotvod.vo.HotvodConst;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

public class HotvodServiceTag extends SimpleTagSupport {
	
	private String hotvodServiceList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.service.list"), HotvodConst.DEFAULT_HOTVOD_SERVICE_LIST);
	
	private Boolean isLock = false;
	
	private String serviceType;

	public void setHotvodServiceList(String hotvodServiceList) {
		this.hotvodServiceList = hotvodServiceList;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	protected String format(String serviceCode, String serviceName) {
		StringBuffer tag = new StringBuffer();
		tag.append("<input type='checkbox' id='%s' value='%s' name='service_check' class='service_check' size='50' style='font-size: 12px;' ");
		if(isLock && StringUtils.equals(serviceType, serviceCode)) {
			tag.append("disabled='disabled'");
		}
		tag.append("><label for='%s'>%s</label>");
		String serviceId = "service_check_"+serviceCode;
		return String.format(tag.toString(), serviceId, serviceCode, serviceId, serviceName);
	}

	@Override
	public void doTag() throws JspException, IOException {
		try {
			StringBuffer serviceTag = new StringBuffer();
			JspWriter out = getJspContext().getOut();
			
			String[] serviceList = hotvodServiceList.split("\\|");
			String[] serviceCodeArray = null;
			for (int i = 0; i < serviceList.length; i++) {
				serviceCodeArray = serviceList[i].split("\\^");
				serviceTag.append(format(serviceCodeArray[0], serviceCodeArray[1]));
			}
			out.write(serviceTag.toString());		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
