package br.com.heiderlopes.iotmqttapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Coisa {

    @JsonIgnore
    private String id;
    private String deviceID;
    private String valor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
