package com.example.magicmirror.bean;

import java.util.ArrayList;

public class FirmWare {
    private String version_number;
    private String version_name;
    private String pathAdd;

    public FirmWare(String version, String name) {
        this.version_number = version;
        this.version_name = name;

    }

    public String getVersion_number() {
        return version_number;
    }

    public void setVersion_number(String version_number) {
        this.version_number = version_number;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getPathAdd() {
        return pathAdd;
    }

    public void setPathAdd(String pathAdd) {
        this.pathAdd = pathAdd;
    }
}
