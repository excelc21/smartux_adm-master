package com.dmi.smartux.admin.teleport.vo;

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

import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.Category.*;
import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.MapKey.*;
import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.TeleportMaster.korea;

/**
 * TeleportComponent
 *
 * 파일저장 직렬화대신 JSON 사용사유
 * 1. 저장 후 DB처럼 확인이 어렵다.
 * 2. 데이터타입 저장으로 인한 파일용량이 커진다.
 * 3. java 버전변경시, 값수정시 등 오류가 생길 가능성등이 있으며 편의성에 비해 고려해야 할 것이 많다.
 *
 * @author ckkim on 2018-08-17
 */
@Component(value = "teleport")
@Scope
public class TeleportComponent implements JsonFileator<TeleportInfo> {

    private final Log logger = LogFactory.getLog(this.getClass());
    //noinspection unchecked
    private JSONObject jsonData = new JSONObject();
    //data result
    private List<TeleportInfo> teleportInfoList = new ArrayList<TeleportInfo>();
    //order setter
    private Map<Integer, TeleportInfo> teleportInfoMap = new LinkedHashMap<Integer, TeleportInfo>();

    public enum Category {
        NATION("상위", 1), CITY("하위", 2), EMPTY("empty", 0);

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
        anchor_type("anchor_type"),
        anchor_type_txt("anchor_type_txt"),
        order("order"),
        anchor_txt("anchor_txt"),
        parent_id("parent_id"),
        parent_txt("parent_txt"),
        anchor_id("anchor_id"),
        seq("seq"),
        insert_date("insert_date");
        private String key;
        public String key() {
            return key;
        }
        MapKey(String key) {
            this.key = key;
        }
    }
    public enum TeleportMaster {
        korea("대한민국", 1, 1);
        private String name;
        private int order;
        private int anchor_id;
        TeleportMaster(String name, int order, int anchor_id) {
            this.name = name;
            this.order = order;
            this.anchor_id = anchor_id;
        }
        public String getName() { return name; }
        public int getOrder() { return order; }
        public int getAnchor_id() { return anchor_id; }
    }
    public enum SearchType {
        anchor_type("anchor_type"),
        anchor_txt("anchor_txt"),
        panel_id("panel_id"),
        paper_code("paper_code"),
        order_change("order_change"),
        anchor_id("anchor_id"),
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
        final String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("teleport.file.nas.dir"));
        final String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("teleport.file.dir"));
        final String fileName = "teleport.json";
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
        this.teleportInfoList =  om.readValue(jsonStr, new TypeReference<List<TeleportInfo>>(){});
        Collections.reverse(this.teleportInfoList);

        Map<Integer, TeleportInfo> map = new LinkedHashMap<Integer, TeleportInfo>();
        for(TeleportInfo teleportInfo : this.teleportInfoList) {
            map.put(teleportInfo.getAnchor_id(), teleportInfo);
        }
        this.teleportInfoMap = map;
    }
    @Override
    public List<TeleportInfo> getList() throws Exception {
        this.readList();
        return this.teleportInfoList;
    }
    @Override
    public List<TeleportInfo> getList(String searchType, String searchText) throws IOException, ParseException {
        this.readList();
        if(this.teleportInfoList.size() == 0) return Collections.emptyList();

        List<TeleportInfo> list = new ArrayList<TeleportInfo>();
        for(TeleportInfo find : this.teleportInfoList) {
            switch (SearchType.findSearchType(searchType)) {
                case anchor_type:
                    if(getCategory(find.getAnchor_type()) == getCategory(Integer.valueOf(searchText)))
                        list.add(find);
                    break;
                case anchor_txt:
                    if(find.getAnchor_txt().equals(searchText))
                        list.add(find);
                    break;
                case panel_id:
                    if(find.getPanel_id().equals(searchText))
                        list.add(find);
                    break;
                case paper_code:
                    if(find.getPaper_code().equals(searchText))
                        list.add(find);
                    break;
                case order_change:
                    if(!find.getAnchor_txt().equals(searchText))
                        list.add(find);
                    break;
                case anchor_id:
                    if(find.getAnchor_id() == Integer.valueOf(searchText)) {
                        list.add(find);
                        return list;
                    }
                    break;
            }
        }
        return list;
    }
    @Override
    public void insert(TeleportInfo teleportInfo) throws Exception {
        logger.info("insert in");
        this.readList();

        Map obj = new HashMap<String, Object>();
        for(Field field : teleportInfo.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            //noinspection unchecked
            obj.put(field.getName(), field.get(teleportInfo));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        //noinspection unchecked
        obj.put(insert_date.key(), format.format(Calendar.getInstance().getTime()));

        int numType = (Integer) obj.get(anchor_type.key());
        //noinspection unchecked
        obj.put(anchor_type_txt.key(), getCategory(numType).getName());
        if( findType(numType, NATION) ) {
            obj.remove(parent_id.key());
            obj.remove(parent_txt.key());
        }


        List<Map> dataList = valueToMapArray(this.jsonData, list);
        obj = setFirstCheck(obj, dataList.size());

        dataList.add( addOrder(obj) );
        this.writeFile(dataList);
    }

    @Override
    public void update(TeleportInfo teleportInfo) throws Exception {
        teleportInfo.setAnchor_type_txt(getCategory(teleportInfo.getAnchor_type()).getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        teleportInfo.setInsert_date(format.format(Calendar.getInstance().getTime()));
        if( findType(teleportInfo.getAnchor_type(), NATION) ) {
            teleportInfo.setParent_id(0);
            teleportInfo.setParent_txt("");
        }

        this.teleportInfoMap.put(teleportInfo.getAnchor_id(), teleportInfo);

        final ObjectMapper om = new ObjectMapper();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for(Map.Entry<Integer, TeleportInfo> entry : this.teleportInfoMap.entrySet()) {
            listMap.add(om.convertValue(entry.getValue(), Map.class));
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i = listMap.size()-1; i >= 0; i--) {
            list.add(listMap.get(i));
        }
        writeFile(list);
    }

    //국가 삭제시 하위 도시도 삭제되야 하면 type, 상위코드(상위코드명) 등도 같이 가져와서 삭제
    @Override
    public void delete(int[] orderList) throws Exception {
        this.readList();

        for(int i = orderList.length-1; i >= 0; i--) {
            //code 1 대한민국은 삭제불가
            if(orderList[i] == 1) return;
            this.teleportInfoList.remove(teleportInfoMap.get(orderList[i]));
        }

        updateOrder();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        final ObjectMapper om = new ObjectMapper();
        for(TeleportInfo teleportInfo : this.teleportInfoList) {
            //noinspection unchecked
            list.add(om.convertValue(teleportInfo, Map.class));
        }


        this.writeFile(list);
    }
    @Override
    public void writeFile(List<? extends Map> dataList) throws Exception {
        //noinspection unchecked
        this.jsonData.put(list.key(), dataList);

        final String nasDir = GlobalCom.isNull(SmartUXProperties.getPathProperty("teleport.file.nas.dir"));
        final String dir    = GlobalCom.isNull(SmartUXProperties.getPathProperty("teleport.file.dir"));
        final String fileName = "teleport.json";

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
                ? (Long)this.jsonData.get(seq.key())+1 : korea.getAnchor_id();

        //noinspection unchecked
        this.jsonData.put(seq.key(), sequence);
        //noinspection unchecked
        obj.put(anchor_id.key(), sequence);
        return obj;
    }

    @Override
    public void setOrder(int[] codeList) throws Exception {
        this.readList();
        List<Map> list = new ArrayList<Map>();
        final ObjectMapper om = new ObjectMapper();
        int i = 0;
        for(int j = codeList.length; j > 0; j--) {
            TeleportInfo teleportInfo = this.teleportInfoMap.get(codeList[j-1]);
            teleportInfo.setOrder(++i);
            list.add(om.convertValue(teleportInfo, Map.class));
        }
        writeFile(list);
    }


    @SuppressWarnings("unchecked")
    private Map setFirstCheck(Map obj, int size) {
        if(size == 0) {
            obj.put(anchor_type.key(), NATION.getType());
            obj.put(anchor_type_txt.key(), NATION.getName());
            obj.put(anchor_txt.key(), korea.getName());
        }
        return obj;
    }
    private void updateOrder() {
        Collections.sort(this.teleportInfoList);
        for(int i = 0, size = this.teleportInfoList.size(); i < size; i++) {
            TeleportInfo teleportInfo = this.teleportInfoList.get(i);
            teleportInfo.setOrder(i+1);
            this.teleportInfoList.set(i, teleportInfo);
        }
    }



    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private List<Map> valueToMapArray(Map map, MapKey key) {
        Object value = map.get(key.key());
        return value == null ? new ArrayList<Map>() : (List<Map>) value;
    }
}