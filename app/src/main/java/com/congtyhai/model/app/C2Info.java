package com.congtyhai.model.app;

/**
 * Created by HAI on 10/17/2017.
 */

public class C2Info {

    private String code;

    private String store;

    private String deputy;

 //   private List<SubOwner> c1;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    /*

    public List<SubOwner> getC1() {
        return c1;
    }

    public void setC1(List<SubOwner> c1) {
        this.c1 = c1;
    }


    public SubOwner chooseC1() {
        for (SubOwner item: c1) {
            if (item.getPriority() == 1)
                return item;
        }
        if (c1.size() > 0)
            return c1.get(0);

        return new SubOwner();
    }

    public SubOwner findC1(int idx) {
        if(c1.size() > idx && idx >=0)
            return c1.get(idx);

        return new SubOwner();
    }
    */
}
