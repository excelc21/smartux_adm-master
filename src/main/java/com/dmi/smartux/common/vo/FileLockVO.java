package com.dmi.smartux.common.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * File Lock VO
 *
 * @author dongho
 */
public class FileLockVO implements Serializable {
	private boolean isLocked;
	private Date mCheckedDate;

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Date getCheckedDate() {
		return mCheckedDate;
	}

	public void setCheckedDate(Date checkedDate) {
		mCheckedDate = checkedDate;
	}
}
