package com.example.kangpei.saver.Model.bean;

/**
 * Created by kangpei on 14/11/16.
 */

public class Bill  {

    private String money;//每一个种类的花费所对应的钱
    //private String name; //
    private String typeId;//花费的钱的id，对应于种类
    private String time;

    public Bill(){

    }
    public Bill(String money, String typeId, String time){
        this.money=money;
        this.typeId=typeId;
        this.time=time;
    }
    @Override
    public String toString() {
        return "Bill{" +
                "money='" + money + '\'' +
                ", typeId='" + typeId + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
