package com.dmi.smartux.admin.lifemessage.vo;

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
import static com.dmi.smartux.admin.lifemessage.vo.LifeMessageComponent.MapKey.*;

/**
 * LifeMessageComponent
 */
@Component(value = "lifemessage")
@Scope
public class LifeMessageComponent implements JsonFileator<LifeMessageInfo> {

    private final Log logger = LogFactory.getLog(this.getClass());
    //noinspection unchecked
    private JSONObject jsonData = new JSONObject();
    private List<LifeMessageInfo> lifeMessageInfoList;
  //order setter
    private Map<Integer, LifeMessageInfo> LifeMessageInfoMap = new LinkedHashMap<Integer, LifeMessageInfo>();

    public enum MapKey {
    	list("list"),
    	order("order"),
    	cont_msg("cont_msg"),
        cont_type("cont_type"),
        cont_type_name("cont_type_name"),
        display_time("display_time"),
        start_point("start_point"),
        end_point("end_point"),
        writeId("writeId"),
        insertDate("insertDate");
    	
        private String key;
        public String key() {
            return key;
        }
        MapKey(String key) {
            this.key = key;
        }
    }
    
    public enum SearchType {
        order,
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
        final String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("lifemessage.file.nas.dir"));
        final String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("lifemessage.file.dir"));
        final String fileName = "lifemessage.json";
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

        final ObjectMapper om = new ObjectMapper();
        String jsonStr = om.writeValueAsString(this.jsonData.get(list.name()));
        this.lifeMessageInfoList =  om.readValue(jsonStr, new TypeReference<List<LifeMessageInfo>>(){});
    }
    @Override
    public List<LifeMessageInfo> getList() throws Exception {
        this.readList();
        return this.lifeMessageInfoList;
    }
    
    @Override
    public List<LifeMessageInfo> getList(String searchType, String searchText) throws IOException, ParseException {
        this.readList();
        if(this.lifeMessageInfoList.size() == 0) return Collections.emptyList();

        List<LifeMessageInfo> list = new ArrayList<LifeMessageInfo>();
        for(LifeMessageInfo find : this.lifeMessageInfoList) {
            switch (SearchType.findSearchType(searchType)) {
                case order:
                	if(find.getOrder() == Integer.valueOf(searchText))
                        list.add(find);
                    break;    
            }
        }
        return list;
    }
    @Override
    public void insert(LifeMessageInfo lifeMessageInfo) throws Exception {
        this.readList();

        Map obj = new HashMap();
        for(Field field : lifeMessageInfo.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            //noinspection unchecked
            obj.put(field.getName(), field.get(lifeMessageInfo));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        obj.put(insertDate.key(), format.format(Calendar.getInstance().getTime()));
        
        List<Map> dataList = valueToMapArray(this.jsonData, list);

        dataList.add( addOrder(obj) );
        this.writeFile(dataList);
    }
    
    @Override
    public void update(LifeMessageInfo lifeMessageInfo) throws Exception {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    	lifeMessageInfo.setInsertDate(format.format(Calendar.getInstance().getTime()));
    	
    	this.lifeMessageInfoList.set(lifeMessageInfo.getOrder()-1, lifeMessageInfo);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        final ObjectMapper om = new ObjectMapper();
        for(LifeMessageInfo info : this.lifeMessageInfoList) {
            //noinspection unchecked
            list.add(om.convertValue(info, Map.class));
        }
        writeFile(list);
    }
    
    @Override
    public void delete(int[] orderList) throws Exception {
        this.readList();

        for(int i = orderList.length-1; i >= 0; i--) {
            this.lifeMessageInfoList.remove(orderList[i]-1);
        }

        updateOrder();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        final ObjectMapper om = new ObjectMapper();
        for(LifeMessageInfo teleportInfo : this.lifeMessageInfoList) {
            //noinspection unchecked
            list.add(om.convertValue(teleportInfo, Map.class));
        }


        this.writeFile(list);
    }
    @Override
    public void writeFile(List dataList) throws Exception {
        //noinspection unchecked
        this.jsonData.put(list.name(), dataList);

        final String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("lifemessage.file.nas.dir"));
        final String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("lifemessage.file.dir"));
        final String fileName = "lifemessage.json";

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
    @Override
    public Map addOrder(Map obj) {
        //noinspection unchecked
        List dataList = valueToMapArray(this.jsonData, list);
        //noinspection unchecked
        obj.put(order.key(), dataList.size()+1);

        /*long sequence = (this.jsonData.get(seq.key()) instanceof Long)
                ? (Long)this.jsonData.get(seq.key())+1 : korea.code;*/
        //noinspection unchecked
        //this.jsonData.put(seq.key(), sequence);
        //noinspection unchecked
        //obj.put(code.key(), sequence);
        return obj;
    }

    private void updateOrder() {
        Collections.sort(this.lifeMessageInfoList);
        for(int i = 0, size = this.lifeMessageInfoList.size(); i < size; i++) {
            LifeMessageInfo teleportInfo = this.lifeMessageInfoList.get(i);
            teleportInfo.setOrder(i+1);
            this.lifeMessageInfoList.set(i, teleportInfo);
        }
    }

    @Override
    public void setOrder(int[] codeList) throws Exception {
        this.readList();
        List<Map> list = new ArrayList<Map>();
        final ObjectMapper om = new ObjectMapper();
        int i = 0;
        for(int key : codeList) {
            LifeMessageInfo teleportInfo = this.LifeMessageInfoMap.get(key);
            teleportInfo.setOrder(++i);
            list.add(om.convertValue(teleportInfo, Map.class));
        }
        writeFile(list);
    }


    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private List<Map> valueToMapArray(Map map, MapKey key) {
        Object value = map.get(key.key());
        return value == null ? new ArrayList<Map>() : (List<Map>) value;
    }
}