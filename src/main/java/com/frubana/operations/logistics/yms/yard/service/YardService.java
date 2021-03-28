package com.frubana.operations.logistics.yms.yard.service;

import com.frubana.operations.logistics.yms.health.service.HealthCheck;
import com.frubana.operations.logistics.yms.yard.domain.Yard;
import com.frubana.operations.logistics.yms.yard.domain.repository.YardRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Service of the some objects Logic.
 */
@Component
public class YardService implements HealthCheck {
    /** The name that represents this service. MUST NOT BE CHANGED. */
    public static final String SERVICE_NAME = "yms";


    /** The repository to ask for data, it's never null. */
    private final YardRepository repository;


    /** Constructor.
     *
     * @param repository        Repository to persists or extract the needed
     *                          data of the tasks.
     */
    @Autowired
    public YardService(YardRepository repository) {
        this.repository = repository;
    }


    /** Returns the some object of the given id.
     *
     * @param id        The id of the wanted tasks.
     * @param warehouse The warehouse where the task belongs.
     * @return True if the tasks exists, false otherwise.
     */
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public boolean exists(String id, String warehouse) {
        return false;
    }

    /** Returns the some object of the given id.
     *
     * @param id        The id of the wanted tasks.
     * @param warehouse The warehouse where the task belongs.
     * @return The some object of the given id.
     */
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public Yard obtainSomeObject(String id, String warehouse) {
        return null;
    }
    /** Creates the some object in the labels repository to generate its
     * some object when required.
     * @param yard The task to register, cannot be null.
     * @param warehouse
     */
    @Transactional
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public void registerSomeObject(Yard yard,String warehouse) {

    }

    @Override
    public boolean isServiceHealthy() {
        return true;
    }

    /**
     * Save a Yard in the repository
     * @param yard
     * @param warehouse
     */
    public Yard registerYard(Yard yard, String warehouse) {
       return this.repository.register(yard,warehouse);
    }
    public List<Yard> searchByWarehouse(String warehouse){
    	return this.repository.listByWarehouse(warehouse);
    }
}
