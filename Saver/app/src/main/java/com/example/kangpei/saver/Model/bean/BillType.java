package com.example.kangpei.saver.Model.bean;

/**
 * Created by kangpei on 14/11/16.
 */

public class BillType {

    /*
    * 花费时记账的种类的实体类，比如说交通类型这一类
    * */

    private  int typeId; //这个typeid相对应于全局中的数组，里面保存的花费的种类
    private  int time;//次数
    private String typeName;

    public BillType(){

    }
    public BillType(int typeId, int time, String typeName){
        this.typeId=typeId;
        this.typeName=typeName;
        this.time=time;
    }
    @Override
    public String toString() {
        return "BillType{" +
                "typeId=" + typeId +
                ", time=" + time +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
