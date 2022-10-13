package com.dmi.smartux.common.util;

import java.util.Enumeration;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

public class Json{
 private String objName;
 private String[] keys;
 private String[] data;
 
 private String head;
 private StringBuffer body = new StringBuffer();
 private String tail;
 
 public Json(String obj){
  setObjName(obj);
 }
 
 public void setKeys(String[] keys){
  this.keys = keys;
 }
 
 public void addObject(String[] data){
  
  if(bodyFirstYN())
   body.append("{");
  else
   body.append(",{");
  for(int i=0 ; i<data.length ; i++){
   if(i != data.length-1)
    body.append("'"+keys[i]+"':'"+data[i]+"',");
   else
    body.append("'"+keys[i]+"':'"+data[i]+"'");
  }
  body.append("}");
 }
 
 public void setKeys(Hashtable data){
  this.keys = new String[data.size()];
  
  Enumeration enu = data.keys();
  
  for(int i=0 ; i<keys.length ; i++){
   this.keys[i] = (String)enu.nextElement();
  }
 }
 
 public void addObject(Hashtable data){
  this.data = new String[data.size()];
  
  Enumeration enu = data.keys();
  
  for(int i=0 ; i<this.data.length ; i++){
   this.data[i] = (String) data.get((String)enu.nextElement());
  }
  
  addObject(this.data);
 }
 
 public String toString(){
  //head  = "{'"+objName+"':[";
  //tail = "]}";
  head  = "{'"+objName+"':";
  tail = "}";
  
  //System.out.println((head+body+tail).replaceAll("'", "\""));
  return (head+body+tail).replaceAll("'", "\"");
 }
 
 private boolean bodyFirstYN(){
  return body.toString().equals("");
 }
 
 public void clear(){
  body.setLength(0);
 }
 
// public void toJson(DataTable data){
//  setKeys(data);
//  addObject(data);
// }
 
 public String getObjName() {
  return objName;
 }
 
 public void setObjName(String objName) {
  this.objName = objName;
 }

public static void main(String args[]){
  Json json = new Json("request");
  
  
  Hashtable table1 = new Hashtable();
//  Hashtable table2 = new Hashtable();
//  Hashtable table3 = new Hashtable();
//  
//  table1.put("key01", "data01");
//  table1.put("key02", "data02");
//  table1.put("key03", "data03");
//  
//  table2.put("key01", "data04");
//  table2.put("key02", "data05");
//  table2.put("key03", "data06");
//  
//  table3.put("key01", "data07");
//  table3.put("key02", "data08");
//  table3.put("key03", "data09");
  
  table1.put("msg_id", "PUSH_NOTI");
  table1.put("push_id", "201111111");
  
  
  json.setKeys(table1);
  
  json.addObject(table1);
//  json.addObject(table2);
//  json.addObject(table3);
  
  //System.out.println(json);
  
  //System.out.println(json.toString());
  String jsonStr = json.toString();
  System.out.println(jsonStr);
  
  try {
	JSONObject jsonObject = new JSONObject(jsonStr);
	JSONObject results = jsonObject.getJSONObject("request");
	String push_id = results.getString("push_id");
	System.out.println(push_id);
} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  
  
  //JSONObject obj = null;
//  try {
//  	  //obj = new JSONObject(jsonStr);
//	  
//  	  //HashMap<String, String> arr = (HashMap) obj.
//  	  //JSONArray arr = (JSONArray) obj.
//  } catch (JSONException e) {	 
//	  System.out.println("ERROR");
//  }
  //JSONObject jobject = (JSONObject)obj;
  //JSONArray arr = (JSONArray) jobject.get("request");
  //JSONObject j = (JSONObject)arr.get(0);
  
  System.out.println(jsonStr);
  
//  try {
//	//System.out.println(obj.get("push_id"));
//	  //obj.getString("msg_id");
//  } catch (JSONException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
  
 }
}
