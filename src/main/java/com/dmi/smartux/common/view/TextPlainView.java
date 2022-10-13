package com.dmi.smartux.common.view;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

public class TextPlainView extends AbstractView {

	public static final String DEFAULT_CONTENT_TYPE = "text/plain";
	public static final String encoding = "UTF-8";

	private Set<String> renderedAttributes;
	
	private boolean disableCaching = true;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	public TextPlainView(){
		setContentType(DEFAULT_CONTENT_TYPE);
	}
	
	
	public Set<String> getRenderedAttributes() {
		return renderedAttributes;
	}


	public void setRenderedAttributes(Set<String> renderedAttributes) {
		this.renderedAttributes = renderedAttributes;
	}


	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(getContentType());
		response.setCharacterEncoding(encoding);
		
		if (disableCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
	}
	
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>)(filterModel(model));
		logger.debug("map size : " + map.size());
		String result = "";
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			String key = (String)iterator.next();
			result = map.get(key).toString();
		}
		
		logger.debug("result : " + result);
		
		
		Writer out = response.getWriter();
		out.append(result);
	}
	
	protected Object filterModel(Map<String, Object> model) {
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		Set<String> renderedAttributes =
				!CollectionUtils.isEmpty(this.renderedAttributes) ? this.renderedAttributes : model.keySet();
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			if (!(entry.getValue() instanceof BindingResult) && renderedAttributes.contains(entry.getKey())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

}
