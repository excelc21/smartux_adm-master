package com.dmi.smartux.admin.news.vo;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Target Rule VO
 */
public class TargetVO {
    private String mTargetNetType;          // 망구분(ALL:00,HFC:01,광랜:02)
    private String mModelType;              // 셋탑구분(ALL:GA,G1,G2)
    private String mLocationCode;           // 지역코드(11:서울시,26:부산시,27:대구시,28:인천시,29:광주시,30:대전시,31:울산시,41:경기도,42:강원도,43:충북,44:충남,45:전북,46:전남,47:경북,48:경남,50:제주도)
    private String mProductionCode;         // 요금제
    private String mOptionalServiceCode;    // 부가 서비스
    private String mSendType;               // 발송타입(특정대상자O, 특정대상자X)
    private String mGender;                 // 성별(A:전체,M:남성,F:여성)
    private String mMinAge;                 // 연령시작(이상)
    private String mMaxAge;                 // 연령끝(미만)
    private int mSendCount;                 // 발송건수
    private String mSignUpType;             // 월정액 가입 기간(0:1주일이내 / 1:1개월이내 / 2:3개월이내 / C:특정날짜 / A:전체), 현재 HDTV에서만 사용(Default A)

    public String getTargetNetType() {
        return mTargetNetType;
    }

    public void setTargetNetType(String targetNetType) {
        mTargetNetType = targetNetType;
    }

    public String getModelType() {
        return mModelType;
    }

    public void setModelType(String modelType) {
        mModelType = modelType;
    }

    public String getLocationCode() {
        return mLocationCode;
    }

    public List<String> getLocationCodeAry() {
        if (StringUtils.isNotBlank(mLocationCode)) {
            String[] ary = mLocationCode.split(",");
            return Arrays.asList(ary);
        } else {
            return new ArrayList<String>();
        }
    }

    public void setLocationCode(String locationCode) {
        mLocationCode = locationCode;
    }

    public String getProductionCode() {
        return mProductionCode;
    }

    public void setProductionCode(String productionCode) {
        mProductionCode = productionCode;
    }

    public String getOptionalServiceCode() {
        return mOptionalServiceCode;
    }

    public List<String> getOptionalServiceCodeAry() {
        if (StringUtils.isNotBlank(mOptionalServiceCode)) {
            String[] ary = mOptionalServiceCode.split(",");
            return Arrays.asList(ary);
        } else {
            return new ArrayList<String>();
        }
    }

    public void setOptionalServiceCode(String optionalServiceCode) {
        mOptionalServiceCode = optionalServiceCode;
    }

    public String getSendType() {
        return mSendType;
    }

    public void setSendType(String sendType) {
        mSendType = sendType;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getMinAge() {
        return mMinAge;
    }

    public void setMinAge(String minAge) {
        mMinAge = minAge;
    }

    public String getMaxAge() {
        return mMaxAge;
    }

    public void setMaxAge(String maxAge) {
        mMaxAge = maxAge;
    }

    public int getSendCount() {
        return mSendCount;
    }

    public void setSendCount(int sendCount) {
        mSendCount = sendCount;
    }

    public String getSignUpType() {
        return mSignUpType;
    }

    public void setSignUpType(String signUpType) {
        mSignUpType = signUpType;
    }
}
