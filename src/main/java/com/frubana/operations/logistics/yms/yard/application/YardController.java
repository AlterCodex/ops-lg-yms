package com.frubana.operations.logistics.yms.yard.application;

import com.frubana.operations.logistics.yms.common.configuration.FormattedLogger;
import com.frubana.operations.logistics.yms.common.utils.JsonUtils;
import com.frubana.operations.logistics.yms.yard.domain.Yard;
import com.frubana.operations.logistics.yms.yard.service.YardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.HashMap;

import static org.springframework.http.ResponseEntity.status;

/** Entry point for Some Controller.
 */
@RestController
@RequestMapping("/yms/yard")
public class YardController {

    /** Logger. */
    private final Logger logger =
            LoggerFactory.getLogger(YardController.class);

    /** Formatter to set the log in a specific format and add the body as part
     * of the same log. */
    private final FormattedLogger logFormatter;


    /** The jackson's object mapper, it's never null. */
    private final YardService yardService;



    /** Creates a new instance of the controller.
     *
     * @param yardService   The service used to process the requests,
     *                         required.
     * @param logFormatter     The formatter utility to log errors, required.
     */
    @Autowired
    public YardController(YardService yardService,
                          FormattedLogger logFormatter) {
        this.yardService = yardService;
        this.logFormatter = logFormatter;

    }

    /**
     * Return true if the service is healthy, otherwise false.
     * @return A 200 Status Code if the service is healthy
     */

    @GetMapping(
            value = "/healthz",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean healthCheck(){
        return yardService.isServiceHealthy();
    }

    /** Returns the task of the given id.
     *
     * @param id        The id of the task to request.
     * @param warehouse The warehouse where the task belongs.
     * @return A JSON representing a some object:
     * <code>
     * {@link Yard}
     * </code>
     */
    @GetMapping(
            value =  "/{warehouse}/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getTask(
            @PathVariable(value = "warehouse") String warehouse,
            @PathVariable(value = "id") String id) {
        //Logging the given info
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("warehouse", warehouse);
        logFormatter.logInfo(logger, "getTask", "Received request", params);

        if (id == null || id.isBlank()) {
            return status(HttpStatus.BAD_REQUEST).body(
                    JsonUtils.jsonResponse(HttpStatus.BAD_REQUEST,
                            "The task cannot be null or empty"));
        }
        if (warehouse == null || warehouse.isBlank()) {
            return status(HttpStatus.BAD_REQUEST).body(
                    JsonUtils.jsonResponse(HttpStatus.BAD_REQUEST,
                            "The warehouse cannot be null or empty"));
        }

        if (yardService.exists(id, warehouse)) {
            // Register the task throws an error if something fails.
            Yard task = yardService.obtainSomeObject(id, warehouse);
            params.put("task", task);
            logFormatter.logInfo(logger, "obtainSomeObject", "found the task",
                    params);
            return status(HttpStatus.OK).body(task);
        }

        return status(HttpStatus.NO_CONTENT).body(null);
    }
    
    
    
    /** Generates the yard.
     *
     * @param yard the yard object to be persisted in the repository, cannot be
     *             null.
     * @return A JSON response with a message and status:
     * <code>
     * {
     * "message": "created",
     * "status": 201
     * }
     * </code>
     */
    @PostMapping(
            value = "/{warehouse}/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> register(
            @PathVariable(value = "warehouse") String warehouse,
            @RequestBody final Yard yard) {
        //Logging the given info

        HashMap<String, Object> params = new HashMap<>();
        params.put("yard", yard);
        params.put("warehouse", warehouse);
        logFormatter.logInfo(logger, "registerYard",
                "Received request", params);
        if (yard == null) {
            return status(HttpStatus.BAD_REQUEST).body(
                    JsonUtils.jsonResponse(HttpStatus.BAD_REQUEST,
                            "The Yard cannot be null"));
        }
        return status(HttpStatus.CREATED).body(
                yardService.registerYard(yard,warehouse)
        );
    }
    
    @GetMapping(
            value =  "/get/{warehouse}/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> searchByWarehouse(
            @PathVariable(value = "warehouse") String warehouse) {
        //Logging the given info
        HashMap<String, Object> params = new HashMap<>();
        params.put("warehouse", warehouse);
        logFormatter.logInfo(logger, "searchByWarehouse", "Received request", params);

        if (warehouse == null || warehouse.isBlank()) {
            return status(HttpStatus.BAD_REQUEST).body(
                    JsonUtils.jsonResponse(HttpStatus.BAD_REQUEST,
                            "The warehouse cannot be null or empty"));
        }

        return status(HttpStatus.ACCEPTED).body(yardService.searchByWarehouse(warehouse));
    }
}
