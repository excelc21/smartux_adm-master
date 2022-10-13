package com.dmi.smartux.common.property;

/**
 * Created by medialog on 2017-03-24.
 */
public interface MimsConsts {
	
    /**
     * 파일 동기화시 파일 Read 시 필터 정책
     */
	public enum SyncNasFilter {
		NONE("0"), REPLACE_FILTER("1");
		private String value;

		private SyncNasFilter(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		public static SyncNasFilter findValue(String value) {
			if(value.equals("1")) {
				return SyncNasFilter.REPLACE_FILTER;
			} else {
				return SyncNasFilter.NONE;
			}
		}
	}


}
