package br.com.heiderlopes.iotmqttapi.controller;

import br.com.heiderlopes.iotmqttapi.IotmqttapiApplication;
import br.com.heiderlopes.iotmqttapi.model.Coisa;
import br.com.heiderlopes.iotmqttapi.service.CoisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coisa")

public class CoisaController {

    @Autowired
    CoisaService coisaService;

    @Autowired
    IotmqttapiApplication.MyGateway gateway;

    @PostMapping
    public void ligar(@RequestBody Coisa coisa) {
        /*gateway.sendToMqtt(coisa.getValor());
        coisaService.salvar(coisa);*/
        coisaService.salvar(coisa);
    }

    @GetMapping("{deviceID}")
    public Coisa getValue(@PathVariable("deviceID") String deviceID){
        return coisaService.findBy(deviceID);
    }
}
