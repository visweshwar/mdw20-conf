package com.paychex.mdw20.hrapplication.entity.repository;

import com.paychex.mdw20.hrapplication.entity.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientReactiveRepository extends ReactiveMongoRepository<Client, String> {
    Mono<Client> findByClientId(String id);
}
