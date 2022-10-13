package com.dmi.smartux.common.util;

import java.util.HashMap;
import java.util.Map;

public class GroupByCount {
    private Map<String,Integer> count = new HashMap<String,Integer>();

    public void add(String element) { 
        if( !count.containsKey( element ) ){
            count.put( element, 1 );
        } else { 
            count.put( element, count.get( element ) + 1 );
        }
    }

    public int getCount(String element ) { 
        if( ! count.containsKey( element ) ) {
            return 0;
        }
        return count.get( element );
    }
}