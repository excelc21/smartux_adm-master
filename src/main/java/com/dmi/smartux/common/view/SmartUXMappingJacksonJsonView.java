package com.dmi.smartux.common.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * 기존의 MappingJacksonJsonView를 사용할 경우 리턴되는 객체의 클래스명을 멤버변수로 시작해서 서브구조로 들어가기때문에
 * 바로 멤버변수 구조로 들어가도록 하기 위해 MappingJacksonJsonView를 상속받아 커스터마이징한 클래스임
 * (관련 블로그 주소 : http://lks21c.blogspot.com/2011/12/mappingjacksonjsonview-json-how-to.html)
 * @author Terry Chang
 *
 */
public class SmartUXMappingJacksonJsonView extends MappingJacksonJsonView {

//	@SuppressWarnings("unchecked")
//	@Override
//	protected Object filterModel(Map<String, Object> model) {
//		// TODO Auto-generated method stub
//		Object result = super.filterModel(model);
//		if (!(result instanceof Map)) {
//		    return result;
//		}
//		Map map = (Map) result;
//		if (map.size() == 1) {
//			return map.values().toArray()[0];
//		}
//		return map;
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object filterModel(Map<String, Object> model) {
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		
		Set<String> renderedAttributes = 	model.keySet();
				//!CollectionUtils.isEmpty(this.renderedAttributes) ? this.renderedAttributes : model.keySet();
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			System.out.println("key"+entry.getKey()+ "   value="+entry.getValue());
			if (!(entry.getValue() instanceof BindingResult) && renderedAttributes.contains(entry.getKey())) {
				result.put("result", entry.getValue());
			}
		}
		return result;
	}
}
