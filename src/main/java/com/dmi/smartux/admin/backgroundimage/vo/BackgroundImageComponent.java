package com.dmi.smartux.admin.backgroundimage.vo;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.JsonFileator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import static com.dmi.smartux.admin.lifemessage.vo.LifeMessageComponent.Category.*;
import static com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageComponent.MapKey.*;
import static com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageComponent.MapKey.list;
import static com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageComponent.MapKey.order_no;
import static com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageComponent.MapKey.seq;

/**
 * LifeMessageComponent
 */
@Component(value = "backgroundimage")
@Scope
public class BackgroundImageComponent implements JsonFileator<BackgroundImageInfo> {

    private final Log logger = LogFactory.getLog(this.getClass());
    //noinspection unchecked
    private JSONObject jsonData = new JSONObject();
    private List<BackgroundImageInfo> backgroundImageInfoList;
  //order setter
    private Map<Integer, BackgroundImageInfo> BackgroundImageInfoMap = new LinkedHashMap<Integer, BackgroundImageInfo>();

  
    public enum MapKey {
    	list("list"),
    	order_no("order_no"),
    	seq("seq"),
    	position_fix("position_fix"),
    	album_id("album_id"),
    	category_id("category_id"),
    	album_title("album_title"),
    	cat_id("cat_id"),
    	write_id("write_id"),
    	insert_date("insert_date");
    	
        private String key;
        public String key() {
            return key;
        }
        MapKey(String key) {
            this.key = key;
        }
    }
    
    public enum SearchType {
        order_no,
        empty;

        private static SearchType findSearchType(String searchType) {
            for(SearchType type : values()) {
                if(type.name().equals(searchType))
                    return type;
            }
            return empty;
        }
    }

    @Override
    public void readList() throws IOException, ParseException {
        //NAS서버에 파일 있는지 확인, 없으면 로컬파일 read
        String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("backgroundimage.file.nas.dir"));
        String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("backgroundimage.file.dir"));
        String fileName = "backgroundimage.json";
        File file = new File(nasDir + fileName);
        if(!file.exists() && new File(dir + fileName).exists()) {
            file = new File(dir + fileName);
            FileUtils.copyFile(file, new File(nasDir + fileName));
            logger.info("Nas 서버에 파일이 없어서 내부파일을 읽어들입니다.");
        } else if(!file.exists() && !new File(dir + fileName).exists()) {
            return;
        }

        this.jsonData = (JSONObject) new JSONParser()
                .parse(new FileReader(file));

        ObjectMapper om = new ObjectMapper();
        String jsonStr = om.writeValueAsString(this.jsonData.get(list.name()));
        this.backgroundImageInfoList =  om.readValue(jsonStr, new TypeReference<List<BackgroundImageInfo>>(){});
        
        Map<Integer, BackgroundImageInfo> map = new LinkedHashMap<Integer, BackgroundImageInfo>();        
        for(BackgroundImageInfo backgroundImageInfo : this.backgroundImageInfoList) {
            map.put(backgroundImageInfo.getSeq(), backgroundImageInfo);
        }
        this.BackgroundImageInfoMap = map;
    }
    @Override
    public List<BackgroundImageInfo> getList() throws Exception {
        this.readList();
        return this.backgroundImageInfoList;
    }
    
    @Override
    public List<BackgroundImageInfo> getList(String searchType, String searchText) throws IOException, ParseException {
        this.readList();
        if(this.backgroundImageInfoList.size() == 0) return Collections.emptyList();

        List<BackgroundImageInfo> list = new ArrayList<BackgroundImageInfo>();
        for(BackgroundImageInfo find : this.backgroundImageInfoList) {
            switch (SearchType.findSearchType(searchType)) {
                case order_no:
                	if(find.getOrder_no() == Integer.valueOf(searchText))
                        list.add(find);
                    break;    
            }
        }
        return list;
    }
    @Override
    //public void insert(BackgroundImageInfo lifeMessageInfo) throws Exception {
    public void insert(BackgroundImageInfo backgroundImageInfo) throws Exception {    
    	this.readList();
    	
        Map obj = new HashMap();
        for(Field field : backgroundImageInfo.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            //noinspection unchecked
            obj.put(field.getName(), field.get(backgroundImageInfo));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        obj.put(insert_date.key(), format.format(Calendar.getInstance().getTime()));
        
        //obj.put(position_fix.key(), "false");
        
        List<Map> dataList = valueToMapArray(this.jsonData, list);        
        //obj = setFirstCheck(obj, dataList.size());

        dataList.add( addOrder(obj) );
        this.writeFile(dataList);
    }
    
    @Override
    public void update(BackgroundImageInfo backgroundImageInfo) throws Exception {
    	//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    	//backgroundImageInfo.setInsert_date(format.format(Calendar.getInstance().getTime()).toString());
    	this.readList();
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ObjectMapper om = new ObjectMapper();
        for(BackgroundImageInfo info : this.backgroundImageInfoList) {
            //noinspection unchecked
        	if(backgroundImageInfo.getSeq() == info.getSeq()){
        		/*if(backgroundImageInfo.getPosition_fix().equals("true")){
        			info.setPosition_fix("Y");
        		}else{
        			info.setPosition_fix("X");
        		}*/
        		info.setPosition_fix(backgroundImageInfo.getPosition_fix());
        	}
        	
        	list.add(om.convertValue(info, Map.class));
        }
        writeFile(list);
    }
    
    @Override
    public void delete(int[] orderList) throws Exception {
        this.readList();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        if(orderList.length == 0){
        	if(this.backgroundImageInfoList != null){
        		this.backgroundImageInfoList.clear();
            	this.jsonData.put(seq.key(), 0);
        	}
        }else{
	        for(int i = orderList.length-1; i >= 0; i--) {
	        	for(int j = 0; j < this.backgroundImageInfoList.size(); j++){
	        		if(this.backgroundImageInfoList.get(j).getSeq() == orderList[i]){
	        			this.backgroundImageInfoList.remove(j);
	        		}
	        	}
	        }
	
	        updateOrder();
	        
	        ObjectMapper om = new ObjectMapper();
	        for(BackgroundImageInfo backgroundImageInfo : this.backgroundImageInfoList) {
	            //noinspection unchecked
	            list.add(om.convertValue(backgroundImageInfo, Map.class));
	        }
	    }

        this.writeFile(list);
    }
    @Override
    public void writeFile(List dataList) throws Exception {
        //noinspection unchecked
        this.jsonData.put(list.name(), dataList);

        String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("backgroundimage.file.nas.dir"));
        String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("backgroundimage.file.dir"));
        String fileName = "backgroundimage.json";

        FileWriter fw = null;
        try {
            File file = new File(dir);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
            file = new File(dir + fileName);
            fw = new FileWriter(file);
            fw.write(this.jsonData.toString());
            fw.flush();
            fw.close();

            File copyFile = new File(nasDir);
            //noinspection ResultOfMethodCallIgnored
            copyFile.mkdirs();
            FileUtils.copyFile(file, new File(nasDir + fileName));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }
   /* @Override
    public Map addOrder(Map obj) {
        //noinspection unchecked
        List dataList = valueToMapArray(this.jsonData, list);
        //noinspection unchecked
        obj.put(order_no.key(), dataList.size()+1);

        long sequence = (this.jsonData.get(seq.key()) instanceof Long)
                ? (Long)this.jsonData.get(seq.key())+1 : 0;
        //noinspection unchecked
        this.jsonData.put(seq.key(), sequence);
        //noinspection unchecked
        //obj.put(code.key(), sequence);
        obj.put(seq.key(), sequence);
        return obj;
    }
*/
    @Override
    public Map addOrder(Map obj) throws Exception {
        //noinspection unchecked
        List dataList = valueToMapArray(this.jsonData, list);
        //noinspection unchecked
        obj.put(order_no.key(), dataList.size()+1);

        long sequence = (this.jsonData.get(seq.key()) instanceof Long)
                ? (Long)this.jsonData.get(seq.key())+1 : 1;
        if(sequence >= Integer.MAX_VALUE) {
            throw new Exception("Max sequence code limit!");
        }

        //noinspection unchecked
        this.jsonData.put(seq.key(), sequence);
        //noinspection unchecked
        obj.put(seq.key(), sequence);
        return obj;
    }


    @SuppressWarnings("unchecked")
    private JSONObject setFirstCheck(JSONObject obj, int size) {
        if(size == 0) {
            /*obj.put(type.name(), NATION.getType());
            obj.put(typeName.name(), NATION.getName());
            obj.put(displayName.name(), "대한민국");*/
        }
        return obj;
    }
    private void updateOrder() {
        Collections.sort(this.backgroundImageInfoList);
        for(int i = 0, size = this.backgroundImageInfoList.size(); i < size; i++) {
        	BackgroundImageInfo backgroundImageInfo = this.backgroundImageInfoList.get(i);
        	backgroundImageInfo.setOrder_no(i+1);
            this.backgroundImageInfoList.set(i, backgroundImageInfo);
        }
    }

    @Override
    public void setOrder(int[] codeList) throws Exception {
        this.readList();
        List<Map> list = new ArrayList<Map>();
        ObjectMapper om = new ObjectMapper();
        int i = 0;
        for(int key : codeList) {
        	BackgroundImageInfo backgroundImageInfo = this.BackgroundImageInfoMap.get(key);
        	backgroundImageInfo.setOrder_no(++i);
            list.add(om.convertValue(backgroundImageInfo, Map.class));
        }
        writeFile(list);
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private List<Map> valueToMapArray(Map map, MapKey key) {
        Object value = map.get(key.key());
        return value == null ? new ArrayList<Map>() : (List<Map>) value;
    }
}