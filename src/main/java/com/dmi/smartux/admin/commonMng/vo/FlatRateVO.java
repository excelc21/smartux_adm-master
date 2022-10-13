package com.dmi.smartux.admin.commonMng.vo;

/**
 * 월정액 VO
 */
public class FlatRateVO {
	private String mProductID;
	private String mProductName;
	private boolean mProductCodeUse;

	public String getProductID() {
		return mProductID;
	}

	public void setProductID(String productID) {
		mProductID = productID;
	}

	public String getProductName() {
		return mProductName;
	}

	public void setProductName(String productName) {
		mProductName = productName;
	}

	public boolean getProductCodeUse() {
		return mProductCodeUse;
	}

	public void setProductCodeUse(boolean mProductCodeUse) {
		this.mProductCodeUse = mProductCodeUse;
	}
	
	
}
