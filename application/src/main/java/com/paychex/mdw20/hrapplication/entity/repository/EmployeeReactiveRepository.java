package com.paychex.mdw20.hrapplication.entity.repository;

import com.paychex.mdw20.hrapplication.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeReactiveRepository extends ReactiveMongoRepository<Employee, String> {
    Mono<Employee> findByEmployeeId(String id);
    Flux<Employee> getAllByClientId(String id);
}
