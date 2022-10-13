package com.dmi.smartux.common.util;

import java.util.List;
import java.util.Map;

/**
 * JsonFileator
 *
 * @author ckkim on 2018-08-22
 */
public interface JsonFileator<E> {

    void readList() throws Exception;
    List<E> getList() throws Exception;
    List<E> getList(String searchType, String searchText) throws Exception;
    void insert(E e) throws Exception;
    void update(E e) throws Exception;
    void delete(int[] orderList) throws Exception;
    void writeFile(List<? extends Map> dataList) throws Exception;
    Map addOrder(Map obj) throws Exception;
    void setOrder(int[] codeList) throws Exception;
}