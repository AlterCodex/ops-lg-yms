package com.frubana.operations.logistics.yms.common.clients;

public class VehicleDTO{
    private String id;
    private String type;

    public VehicleDTO(){};

    public void setId(String id){
        this.id=id;
    }

    public void setType(String type){
        this.type=type;
    }

    public String getId(){
        return this.id;
    }
    public String getType(){
        return this.type;
    }
}