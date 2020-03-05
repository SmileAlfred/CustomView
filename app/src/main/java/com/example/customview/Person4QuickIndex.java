package com.example.customview;

import com.example.customview.util.PinYinUtils;

/**
 * @author LiuSaiSai
 * @description:    通过 设置构造器  当 new person的时候，获取得到 拼音
 * @date :2020/03/04 15:33
 */
public class Person4QuickIndex {
    public String name;
    public String pinyin;

    public Person4QuickIndex(String name) {
        this.name = name;
        this.pinyin = PinYinUtils.getPinYin(name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "Person4QuickIndex{" +
                "name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
