package br.com.heiderlopes.iotmqttapi.service;

import br.com.heiderlopes.iotmqttapi.model.Coisa;
import br.com.heiderlopes.iotmqttapi.repository.CoisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoisaService {


    @Autowired
    public CoisaRepository coisaRepository;

    public void salvar(Coisa coisa) {
        Coisa aux = coisaRepository.findByDeviceID(coisa.getDeviceID());
        if (aux != null) {
            coisa.setId(aux.getId());
        }
        coisaRepository.save(coisa);
    }

    public Coisa findBy(String deviceID) {
        return coisaRepository.findByDeviceID(deviceID);
    }
}
