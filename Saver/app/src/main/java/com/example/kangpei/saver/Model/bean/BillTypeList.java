package com.example.kangpei.saver.Model.bean;

import java.util.List;

/**
 * Created by kangpei on 14/11/16.
 */

public class BillTypeList {

    public List<DataEntity> getDataEntities() {
        return dataEntities;
    }

    public void setDataEntities(List<DataEntity> dataEntities) {
        this.dataEntities = dataEntities;
    }

    private List<DataEntity> dataEntities;


    public static class DataEntity{
        private int type_id;//类型对应的id
        private int choose;//选择的次数
        private long time;//时间戳

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public void setChoose(int choose) {
            this.choose = choose;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getType_id() {
            return type_id;
        }

        public int getChoose() {
            return choose;
        }

        public long getTime() {
            return time;
        }
    }
}
