package com.example.magicmirror.bean;

import java.util.ArrayList;

public class FirmWareInfo {
    private int status;//状态
    private ArrayList<FirmWare> data;//获取得客户信息

    public FirmWareInfo(int status, ArrayList<FirmWare> data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<FirmWare> getData() {
        return data;
    }

    public void setData(ArrayList<FirmWare> data) {
        this.data = data;
    }
}
