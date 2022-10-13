package com.dmi.smartux.admin.hotvod.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.dmi.smartux.admin.hotvod.vo.HotvodConst;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

public class HotvodBadgeTag extends SimpleTagSupport {

	private String hotvodBadgeList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.badge.list"), HotvodConst.DEFAULT_HOTVOD_BADGE_LIST);
	
	public void setHotvodBadgeList(String hotvodBadgeList) {
		this.hotvodBadgeList = hotvodBadgeList;
	}
	
	protected String format(int index, String badgeName) {
		StringBuffer tag = new StringBuffer();
		tag.append("<input type='checkbox' id='badge_check_%d' class='badge' name='badge_check' size='50' style='font-size: 12px;'>");
		tag.append("<label for='badge_check_%d'>%s</label>");
		if(index%5 == 0) {
			tag.append("<br/>");
		}
		return String.format(tag.toString(), index, index, badgeName);
	}	

	@Override
	public void doTag() throws JspException, IOException {
		try {
			StringBuffer badgeTag = new StringBuffer();
			JspWriter out = getJspContext().getOut();
			
			String[] badgeList = hotvodBadgeList.split("\\|");
			int j=0;
			for (int i = 0; i < badgeList.length; i++) {
				j=i+1;
				badgeTag.append(format(j, badgeList[i]));
			}
			out.write(badgeTag.toString());		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
