package com.model;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private static List<String> lists;

    static {
        lists=new ArrayList<>();
    }
    public static List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        UserInfo.lists = lists;
    }
}
