package com.frubana.operations.logistics.yms.yard.service;

import com.frubana.operations.logistics.yms.health.service.HealthCheck;
import com.frubana.operations.logistics.yms.yard.domain.Yard;
import com.frubana.operations.logistics.yms.yard.domain.repository.YardRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Service of the some objects Logic.
 */
@Component
public class YardService implements HealthCheck {
    /**
     * The name that represents this service. MUST NOT BE CHANGED.
     */
    public static final String SERVICE_NAME = "yms";


    /**
     * The repository to ask for data, it's never null.
     */
    private final YardRepository repository;


    /**
     * Constructor.
     *
     * @param repository Repository to persists or extract the needed
     *                   data of the tasks.
     */
    @Autowired
    public YardService(YardRepository repository) {
        this.repository = repository;
    }


    /**
     * Returns the some object of the given id.
     *
     * @param id        The id of the wanted tasks.
     * @return True if the tasks exists, false otherwise.
     */
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)

    public boolean exists(String id) {
        return this.repository.exist(Integer.parseInt(id));
    }

    /**
     * Returns the some object of the given id.
     *
     * @param id        The id of the wanted tasks.
     * @param warehouse The warehouse where the task belongs.
     * @return The some object of the given id.
     */
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public Yard getYard(String id, String warehouse) {
        return repository.getByIdAndWarehouse(Integer.parseInt(id), warehouse);
    }


    @Override
    public boolean isServiceHealthy() {
        return true;
    }

    /**
     * Save a Yard in the repository
     *
     * @param yard
     * @param warehouse
     */
    @Transactional
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public Yard registerYard(Yard yard, String warehouse) {
        return this.repository.register(yard, warehouse);
    }

    /**
     * get a list of yards by warehouse
     *
     * @param warehouse the warehouse that contains the yards
     */
    @Transactional
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public List<Yard> getYards(String warehouse) {
        return repository.getByWarehouse(warehouse);
    }

    /**
     * get a list of yards.
     */
    @Transactional
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public List<Yard> getYards() {
        return repository.getAll();
    }


    public Yard ocuparMuelle(Yard yard) {
        return this.repository.ocuparMuelle(yard);
    }


    public Yard liberarMuelle(Yard yard){
        return this.repository.liberarMuelle(yard);
    }
    /**
     * Update a Yard.
     * @param yard the yard with the new information.
     * @param yardId the id to be updated
     * @return {@link Yard} the updated object.
     */
    @Transactional
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public Yard update(Yard yard, String yardId) {
        Yard oldYard = repository.get(Integer.parseInt(yardId));
        yard.copyTo(oldYard);
        Yard newYard = repository.update(oldYard);

        return newYard;
    }

    /**
     *
     * @param warehouse
     * @param vehicleTypeName
     * @return
     */
    @Transactional
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public Yard nextFreeYard(String warehouse, String vehicleTypeName) {
        return repository.getNextFreeYard(warehouse,vehicleTypeName);
    }


}

