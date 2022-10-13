package com.dmi.smartux.common.vo;

public enum CompareValue {
	High(1) 
	, Equal(0)
	, Low(-1)
	, NonEqual(1)
	;

	private int mValue;

	CompareValue(int value) {
		mValue = value;
	}

	public int getValue() {
		return mValue;
	}
}
