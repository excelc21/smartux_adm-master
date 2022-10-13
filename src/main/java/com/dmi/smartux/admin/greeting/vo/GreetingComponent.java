package com.dmi.smartux.admin.greeting.vo;

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
import java.util.*;

import static com.dmi.smartux.admin.greeting.vo.GreetingComponent.Category.*;
import static com.dmi.smartux.admin.greeting.vo.GreetingComponent.MapKey.*;

/**
 * GreetingComponent
 *
 * @author ckkim on 2018-08-29
 */
@Component(value = "greeting")
@Scope
public class GreetingComponent implements JsonFileator<GreetingInfo> {

    private final Log logger = LogFactory.getLog(this.getClass());
    //noinspection unchecked
    private JSONObject jsonData = new JSONObject();
    //data result
    private List<GreetingInfo> greetingInfoList = new ArrayList<GreetingInfo>();
    //order setter
    private Map<Integer, GreetingInfo> greetingInfoMap = new LinkedHashMap<Integer, GreetingInfo>();

    public enum Category {
        NORMAL_DAY("기본", 1), EVENT_DAY("특정일", 2), EMPTY("empty", 0);

        private String name;
        private int type;
        Category(String name, int type) {
            this.name = name;
            this.type = type;
        }
        public String getName() { return name; }
        public int getType() { return type; }

        public static boolean findType(int type, Category category) {
            return getCategory(type) == category;
        }
        public static Category getCategory(int type) {
            for(Category category : values()) {
                if(category.getType() == type)
                    return category;
            }
            return EMPTY;
        }
        public static Category getCategory(String name) {
            for(Category category : values()) {
                if(category.getName().equals( name.toLowerCase() ))
                    return category;
            }
            return EMPTY;
        }
        public static List<Category> getList() {
            List<Category> list = new ArrayList<Category>();
            for(Category category : values()){
                if(Category.EMPTY != category)
                    list.add(category);
            }
            return list;
        }
    }
    public enum MapKey {
        list("list"),
        order("order"),
        seq("seq"),
        greeting_id("greeting_id"),
        img_url("img_url"),
        bg_image("bg_image"),
        bg_image_original_name("bg_image_original_name"),
        start_point("start_point"),
        end_point("end_point"),
        greeting_txt("greeting_txt"),
        greeting_voice("greeting_voice"),
        greeting_voice_original_name("greeting_voice_original_name"),
        date_type("date_type"),
        event_day("event_day"),
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
        greeting_id("greeting_id"),
        greeting_txt("greeting_txt"),
        date_type("date_type"),
        event_day("event_day"),
        start_point("start_point"),
        end_point("end_point"),
        greeting_voice_original_name("greeting_voice_original_name"),
        bg_image_original_name("bg_image_original_name"),
        order_change("order_change"),
        empty("empty");
        private String key;
        public String key() {
            return key;
        }
        SearchType(String key) {
            this.key = key;
        }
        private static SearchType findSearchType(String searchType) {
            for(SearchType type : values()) {
                if(type.key().equals(searchType))
                    return type;
            }
            return empty;
        }
    }





    @Override
    public void readList() throws IOException, ParseException {
        //NAS서버에 파일 있는지 확인, 없으면 로컬파일 read
        final String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("greeting.file.nas.dir"));
        final String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("greeting.file.dir"));
        final String fileName = "greeting.json";
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
        String jsonStr = om.writeValueAsString(this.jsonData.get(list.key()));
        this.greetingInfoList =  om.readValue(jsonStr, new TypeReference<List<GreetingInfo>>(){});
        Collections.reverse(this.greetingInfoList);

        Map<Integer, GreetingInfo> map = new LinkedHashMap<Integer, GreetingInfo>();
        for(GreetingInfo greetingInfo : this.greetingInfoList) {
            map.put(greetingInfo.getGreeting_id(), greetingInfo);
        }
        this.greetingInfoMap = map;

        //파일 읽을때 오류가 생기면 로컬파일을 읽어서
        //기존 Nas에 있던파일은 백업파일로 복사하고, 로컬파일을 Nas에 저장
    }
    @Override
    public List<GreetingInfo> getList() throws Exception {
        this.readList();
        return this.greetingInfoList;
    }
    @Override
    public List<GreetingInfo> getList(String searchType, String searchText) throws IOException, ParseException {
        this.readList();
        if(this.greetingInfoList.size() == 0) return Collections.emptyList();

        List<GreetingInfo> list = new ArrayList<GreetingInfo>();
        for(GreetingInfo find : this.greetingInfoList) {
            switch (SearchType.findSearchType(searchType)) {
                case date_type:
                    if(getCategory(find.getDate_type()) == getCategory(Integer.valueOf(searchText)))
                        list.add(find);
                    break;
                case event_day:
                    if(find.getEvent_day().equals(searchText))
                        list.add(find);
                    break;
                case start_point:
                    if(find.getStart_point().equals(searchText))
                        list.add(find);
                    break;
                case end_point:
                    if(find.getEnd_point().equals(searchText))
                        list.add(find);
                    break;
                case greeting_txt:
                    if(find.getGreeting_txt().contains(searchText))
                        list.add(find);
                    break;
                case greeting_voice_original_name:
                    if(find.getGreeting_voice_original_name().contains(searchText))
                        list.add(find);
                    break;
                case bg_image_original_name:
                    if(find.getBg_image_original_name().contains(searchText))
                        list.add(find);
                    break;
                case order_change:
                    if(!find.getGreeting_txt().equals(searchText))
                        list.add(find);
                    break;
                case greeting_id:
                    if(find.getGreeting_id() == Integer.valueOf(searchText)) {
                        list.add(find);
                        return list;
                    }
                    break;
            }
        }
        return list;
    }
    @Override
    public void insert(GreetingInfo greetingInfo) throws Exception {
        this.readList();

        Map<String, Object> obj = new HashMap<String, Object>();
        for(Field field : greetingInfo.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            obj.put(field.getName(), field.get(greetingInfo));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        obj.put(insert_date.key(), format.format(Calendar.getInstance().getTime()));

        int numType = (Integer) obj.get(date_type.key());
        obj.put(greeting_id.key(), getCategory(numType).getName());
        if( findType(numType, NORMAL_DAY) ) {
            obj.put(event_day.key(), "ALL");
        }


        List<Map> dataList = valueToMapArray(this.jsonData, list);

        dataList.add( addOrder(obj) );
        this.writeFile(dataList);
    }

    @Override
    public void update(GreetingInfo greetingInfo) throws Exception {
        if( findType(greetingInfo.getDate_type(), NORMAL_DAY) ) {
            greetingInfo.setEvent_day("ALL");
        }

        this.greetingInfoMap.put(greetingInfo.getGreeting_id(), greetingInfo);

        final ObjectMapper om = new ObjectMapper();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for(Map.Entry<Integer, GreetingInfo> entry : this.greetingInfoMap.entrySet()) {
            listMap.add(om.convertValue(entry.getValue(), Map.class));
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i = listMap.size()-1; i >= 0; i--) {
            list.add(listMap.get(i));
        }
        writeFile(list);
    }

    @Override
    public void delete(int[] orderList) throws Exception {
        this.readList();

        for(int i = orderList.length-1; i >= 0; i--) {
            this.greetingInfoList.remove(greetingInfoMap.get(orderList[i]));
        }

        updateOrder();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        final ObjectMapper om = new ObjectMapper();
        for(GreetingInfo greetingInfo : this.greetingInfoList) {
            //noinspection unchecked
            list.add(om.convertValue(greetingInfo, Map.class));
        }


        this.writeFile(list);
    }
    @Override
    public void writeFile(List<? extends Map> dataList) throws Exception {
        //noinspection unchecked
        this.jsonData.put(list.key(), dataList);

        final String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("greeting.file.nas.dir"));
        final String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("greeting.file.dir"));
        final String fileName = "greeting.json";

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
    public Map addOrder(Map obj) throws Exception {
        //noinspection unchecked
        List dataList = valueToMapArray(this.jsonData, list);
        //noinspection unchecked
        obj.put(order.key(), dataList.size()+1);

        long sequence = (this.jsonData.get(seq.key()) instanceof Long)
                ? (Long)this.jsonData.get(seq.key())+1 : 1;

        //noinspection unchecked
        this.jsonData.put(seq.key(), sequence);
        //noinspection unchecked
        obj.put(greeting_id.key(), sequence);
        return obj;
    }

    @Override
    public void setOrder(int[] codeList) throws Exception {
        this.readList();
        List<Map> list = new ArrayList<Map>();
        final ObjectMapper om = new ObjectMapper();
        int i = 0;
        for(int j = codeList.length; j > 0; j--) {
            GreetingInfo greetingInfo = this.greetingInfoMap.get(codeList[j-1]);
            greetingInfo.setOrder(++i);
            list.add(om.convertValue(greetingInfo, Map.class));
        }
        writeFile(list);
    }


    private void updateOrder() {
        Collections.sort(this.greetingInfoList);
        for(int i = 0, size = this.greetingInfoList.size(); i < size; i++) {
            GreetingInfo greetingInfo = this.greetingInfoList.get(i);
            greetingInfo.setOrder(i+1);
            this.greetingInfoList.set(i, greetingInfo);
        }
    }


    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private List<Map> valueToMapArray(Map map, MapKey key) {
        Object value = map.get(key.key());
        return value == null ? new ArrayList<Map>() : (List<Map>) value;
    }
}