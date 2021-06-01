package com.frubana.operations.logistics.yms.yard.domain.repository;

import com.frubana.operations.logistics.yms.yard.domain.Yard;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.core.statement.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/** Some repository using JDBI
 */
@Component
public class YardRepository {

    /**
     * Select * from YARD
     */
    public static final String SELECT_YARD_SQL_QUERY = "Select * from YARD ";
    public static final String SELECT_FULL_YARD_SQL_QUERY =SELECT_YARD_SQL_QUERY+
            "INNER JOIN vehicle_type vtp on vtp.id = yard.vehicle_type ";


    /**
     * The JDBI instance to request data to the database, it's never null.
     */
    private final Jdbi dbi;

    /**
     * Base constructor of the repository.
     *
     * @param jdbi the JDBI instance to use in the queries.
     */
    @Autowired
    public YardRepository(Jdbi jdbi) {
        this.dbi = jdbi;
    }

    /**
     * register a yard for a specific warehouses.
     *
     * @param yard      the yard to be register.
     * @param warehouse the warehouse to be registered.
     * @return the {@link Yard}  registered.
     */
    public Yard register(Yard yard, String warehouse) {
        int nextAssignation = this.getNextAssignationNumber(yard.getColor(),
                warehouse);
        String sql_query = "Insert into yard (color, warehouse)" +
                " values(:color, :warehouse)";
        try (Handle handler = dbi.open();
             Update query_string = handler.createUpdate(sql_query)) {
            query_string
                    .bind("color", yard.getColor())
                    .bind("warehouse", warehouse);
            int yard_id = query_string
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class).first();
            handler.close();
            Yard createdYard = new Yard(yard_id,yard.getColor(),
                    nextAssignation,false);
            createdYard.assignWarehouse(warehouse);
            return createdYard ;

        }
    }

    /**
     * Obtiene el siguiente numero de la assignacion ejemplo
     * si para el muelle #ff0000 de la bodega ALQ existen el 1,2,3,4,5 en base de datos
     * debe retornar el 6.
     * OJO:
     * si para el muelle #0000ff de la bodega ARM existen el 1,3,4,5 en base de datos
     * debe retornar el 2.
     *
     * @param color
     * @param warehouse
     * @return
     */
    private int getNextAssignationNumber(String color, String warehouse){
        String sql_query=   "SELECT * FROM yard " +
                "WHERE color=:color and warehouse=:warehouse " +
                "ORDER BY assignation_number ASC";

        List<Yard> yards;
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            query_string
                    .bind("color",color)
                    .bind("warehouse",warehouse);
            yards = query_string.mapTo(Yard.class).list();
            handler.close();
        }

        for(int i = 0; i <yards.size(); ++i) {
            Yard yard = yards.get(i);
            if(i+1 != yard.getAssignationNumber()){
                return i+1;
            }
        }
        return yards.size()+1;
    }


    /**
     * Retrieve if an yard exists or not in the DB
     *
     * @param id        the id of the yard
     * @return the {@link Boolean} that checks if a yard exists
     */
    public boolean exist(int id) {
        String sql_query = "Select count(*) from YARD " +
                "where id= :id ";
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            query_string
                    .bind("id", id);
            int yard_id = query_string.mapTo(int.class).first();
            handler.close();
            return yard_id > 0;
        }
    }

    /**
     * Retrieve a {@link Yard} by its id and warehouse.
     *
     * @param id        the id for yard
     * @param warehouse the warehouse that you are asking for.
     * @return the Yard if exist.
     */
    public Yard getByIdAndWarehouse(int id, String warehouse) {

        String sql_query = SELECT_YARD_SQL_QUERY +
                " where id= :id and warehouse=:warehouse";
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            query_string
                    .bind("id", id)
                    .bind("warehouse", warehouse);
            Yard yard = query_string.mapTo(Yard.class).first();
            handler.close();
            return yard;
        }
    }

        /**
     * Retrieve a {@link Yard} by its id and warehouse.
     *
     * @param id        the id for yard
     * @return the Yard if exist.
     */
    public Yard get(int id) {
        String sql_query = SELECT_YARD_SQL_QUERY +
                " where id= :id ";
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            query_string
                    .bind("id", id);
            Yard yard = query_string.mapTo(Yard.class).first();
            handler.close();
            return yard;
        }
    }

    public List<Yard> getByWarehouse(String warehouse) {

        String sql_query = SELECT_FULL_YARD_SQL_QUERY +
                " where warehouse=:warehouse order by assignation_number";
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            query_string
                    .bind("warehouse", warehouse);
            List<Yard> yards = query_string.mapTo(Yard.class).list();
            handler.close();
            return yards;
        }
    }

    public List<Yard> getAll() {

        String sqlQuery = SELECT_YARD_SQL_QUERY;
        try (Handle handler = dbi.open();
             Query queryString = handler.createQuery(sqlQuery)) {
            List<Yard> yards = queryString.mapTo(Yard.class).list();
            handler.close();
            return yards;
        }
    }


    public Yard liberarMuelle(Yard yard){

       //  int nextAssignation = this.getNextAssignationNumber(yard.getColor(),
         //   yard.getWarehouse());
        //System.out.println("este es el siguiente numero asignado  "+nextAssignation);


        String sql_query=" UPDATE yard SET " +
                "occupied='false'"+
                " WHERE assignation_number =:assignation_number"+
                " and warehouse= :warehouse and color=:color;";

        try(Handle handler=dbi.open();
            Update query_string = handler.createUpdate(sql_query)){
            query_string.bind("assignation_number",yard.getAssignationNumber())
                        .bind("warehouse",yard.getWarehouse())
                        .bind("color",yard.getColor());
            query_string.execute();
            handler.close();
        }


        String sql_queryGetYard= SELECT_YARD_SQL_QUERY +
                " WHERE assignation_number =:assignation_number"+
                " and warehouse= :warehouse and color=:color;";

        try(Handle handler=dbi.open();
            Query query_string = handler.createQuery(sql_queryGetYard)){
            query_string.bind("assignation_number",yard.getAssignationNumber())
                        .bind("warehouse",yard.getWarehouse())
                        .bind("color",yard.getColor());
            Yard yards = query_string.mapTo(Yard.class).first();
            return yards;
        }
    }


    public Yard update(Yard updatedYard) {
        String sqlQuery = "UPDATE yard SET color = :color," +
                "warehouse = :warehouse," +
                "assignation_number = :assignation_number," +
                "vehicle_type = :vehicle_type" +
                " WHERE id=:id";

        try (Handle handler = dbi.open();
             Update update = handler.createUpdate(sqlQuery)) {
            update.bind("color", updatedYard.getColor())
                    .bind("warehouse", updatedYard.getWarehouse())
                    .bind("assignation_number",
                            updatedYard.getAssignationNumber())
                    .bind("vehicle_type", updatedYard.getVehicleType())
                    .bind("id",updatedYard.getId());
            int numberOfRows = update.execute();
            return updatedYard;
        }
    }

    public  Yard  getNextFreeYard(String warehouse, String vehicleTypeName) {




        String sql_queryGetYard= SELECT_FULL_YARD_SQL_QUERY +
                " WHERE warehouse= :warehouse and occupied= :occupied and vtp.name= :name";

        try(Handle handler=dbi.open();
            Query query_string = handler.createQuery(sql_queryGetYard)){
            query_string.bind("warehouse",warehouse)
                    .bind("name",vehicleTypeName)
                    .bind("occupied",false);
            List<Yard> yard = query_string.mapTo(Yard.class).list();
            if(yard.isEmpty()){
                return null;
            }
            return yard.get(0);
        }

    }
    
    public Yard ocuparMuelle(Yard yard){

        String sql_query=" UPDATE yard SET " +
                "occupied='true'"+
                " WHERE assignation_number =:assignation_number"+
                " and warehouse= :warehouse and color=:color;";

        try(Handle handler=dbi.open();
            Update query_string = handler.createUpdate(sql_query)){
            query_string.bind("assignation_number",yard.getAssignationNumber())
                        .bind("warehouse",yard.getWarehouse())
                        .bind("color",yard.getColor());
            query_string.execute();
            handler.close();
        }

        String sql_queryGetYard= SELECT_YARD_SQL_QUERY +
                " WHERE assignation_number =:assignation_number"+
                " and warehouse= :warehouse and color=:color;";

        try(Handle handler=dbi.open();
            Query query_string = handler.createQuery(sql_queryGetYard)){
            query_string.bind("assignation_number",yard.getAssignationNumber())
                        .bind("warehouse",yard.getWarehouse())
                        .bind("color",yard.getColor());
            Yard  yards = query_string.mapTo(Yard.class).first();
            return yards;
        }
    }


    /** Mapper of the {@link Yard} for the JDBI implementation.
     */
    @Component
    public static class YardMapper implements RowMapper<Yard> {

        /** Override of the map method to set the fields in the SomeObject
         * object when extracted from the repository.
         *
         * @param rs  result set with the fields of the extracted some object.
         * @param ctx the context of the request that extracted the some
         *            object.
         * @return the {@link Yard} instance with the extracted fields.
         * @throws SQLException if the result set throws an error when
         *                      extracting some field.
         */
        @Override
        public Yard map(ResultSet rs, StatementContext ctx)
                throws SQLException {
            Yard yard = new Yard(
                    rs.getInt("id"),
                    rs.getString("color"),
                    rs.getInt("assignation_number"),
                    rs.getBoolean("occupied")
            );
            yard.assignWarehouse(rs.getString("warehouse"));
            yard.setVehicleType(rs.getInt("vehicle_type"));
            yard.setSize(rs.getInt("size"));
            yard.setExclusive(rs.getBoolean("is_exclusive"));
            return yard;
        }
    }
    
    
}
