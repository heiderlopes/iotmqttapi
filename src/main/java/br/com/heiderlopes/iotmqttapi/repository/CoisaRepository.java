package br.com.heiderlopes.iotmqttapi.repository;

import br.com.heiderlopes.iotmqttapi.model.Coisa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoisaRepository extends MongoRepository<Coisa, String> {

    Coisa findByDeviceID(String deviceID);

}
