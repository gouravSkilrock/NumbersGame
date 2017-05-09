package com.skilrock.lms.api.common;

import java.util.Comparator;

import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperOrgRegShiftBean;

public class LmsWrapperOrgRegShiftBeanComp implements Comparator<LmsWrapperOrgRegShiftBean>{

	@Override
	public int compare(LmsWrapperOrgRegShiftBean o1 , LmsWrapperOrgRegShiftBean o2) {
		return -(o1.getOrgType().compareTo(o2.getOrgType()));
		
	}

}
