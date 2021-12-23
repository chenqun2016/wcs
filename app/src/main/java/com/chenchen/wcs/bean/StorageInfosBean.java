package com.chenchen.wcs.bean;

import java.util.List;

/**
 * 创建时间：2021/12/21
 *
 * @Author： 陈陈陈
 * 功能描述：
 */
public class StorageInfosBean {

    public String current;
    public Boolean hitCount;
    public List<?> orders;
    public String pages;
    public List<RecordsBean> records;
    public Boolean searchCount;
    public String size;
    public String total;

    public static class RecordsBean {
        public Integer cellStatus;
        public String id;
        public String prodCode;
        public Integer prodCount;
        public String prodCreateDate;
        public String prodId;
        public String prodMarkId;
        public String prodName;
        public String prodNum;
        public String prodScalars;
        public Integer prodType;
        public String prodTypeName;
        public String prodUnit;
        public String custName;
    }
}
