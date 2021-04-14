package com.frubana.operations.logistics.yms.yard.domain.repository;

import com.amazonaws.services.dynamodbv2.model.Select;

import com.frubana.operations.logistics.yms.yard.domain.Yard;

import netscape.javascript.JSObject;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.Call;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.core.statement.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Jdbc;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
<<<<<<< HEAD

/**
 * Some repository using JDBI
=======
/** Some repository using JDBI
>>>>>>> 90b995c (Se recupera el color original al liberar el yard, debido al campo original_color de la bd)
 */
@Component
public class YardRepository {
	private final JdbcTemplate template;
	/** The JDBI instance to request data to the database, it's never null. */
	private final Jdbi dbi;

	/**
	 * Base constructor of the repository.
	 *
	 * @param jdbi the JDBI instance to use in the queries.
	 */
	@Autowired
	public YardRepository(Jdbi jdbi, JdbcTemplate template) {
		this.template = template;
		this.dbi = jdbi;
	}

<<<<<<< HEAD
	public Yard register(Yard yard, String warehouse) {
		String sql_query = "Insert into yard (color, warehouse)" + " values(:color, :warehouse)";
		try (Handle handler = dbi.open(); Update query_string = handler.createUpdate(sql_query)) {
			query_string.bind("color", yard.getColor()).bind("warehouse", warehouse);
			int yard_id = query_string.executeAndReturnGeneratedKeys("id").mapTo(int.class).first();
			return new Yard(yard_id, yard.getColor(), 1);
		}

	}
=======
    /**
     * register a yard for a specific warehouses.
     * @param yard the yard to be register.
     * @param warehouse the warehouse to be registered.
     * @return the {@link Yard}  registered.
     */
    public Yard register(Yard yard, String warehouse){
        int nextAssignation = this.getNextAssignationNumber(yard.getColor(), warehouse);
        System.out.println(nextAssignation);
        String sql_query="Insert into yard (color, warehouse, assignation_number, original_color)"+" values(:color, :warehouse, :assignation_number, :original_color)";
        try(Handle handler=dbi.open();
            Update query_string = handler.createUpdate(sql_query)){
            query_string
                    .bind("color",yard.getColor())
                    .bind("warehouse",warehouse)
                    .bind("assignation_number", nextAssignation)
            		.bind("original_color" , yard.getColor());
            int yard_id=query_string
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class).first();
            handler.close();
            Yard createdYard = new Yard(yard_id,yard.getColor(), nextAssignation);
            createdYard.AssignWarehouse(warehouse);
            return createdYard;
        }
    }

    /**
     * Obtiene el siguiente numero de la assignacion ejemplo
     * si para el muelle #ff0000 de la bodega ALQ existen el 1,2,3,4,5 en base de datos
     * debe retornar el 6.
     * OJO:
     * si para el muelle #0000ff de la bodega ARM existen el 1,3,4,5 en base de datos
     * debe retornar el 2.
     * @param color
     * @param warehouse
     * @return
     */
    private int getNextAssignationNumber(String color, String warehouse){
        int nextAssignationNumber = 0;
        String sql_query = "SELECT assignation_number from YARD WHERE color= :color and warehouse= :warehouse";
        try (Handle handler = dbi.open();
            Query query_string = handler.createQuery(sql_query)) {
            query_string
                    .bind("color", color)
                    .bind("warehouse", warehouse);
            List<Integer> listAssignationNumbers = query_string.mapTo(int.class).list();
            if(!listAssignationNumbers.isEmpty()) {
                nextAssignationNumber = listAssignationNumbers.get(listAssignationNumbers.size()-1);   
            	for(int i=0; i<listAssignationNumbers.size();i++){
                    if(i==0 && listAssignationNumbers.get(i)!=1){
                        nextAssignationNumber = 0;
                        break;
                    }
                    if((i+1)<listAssignationNumbers.size()){
                    	if(listAssignationNumbers.get(i+1) != listAssignationNumbers.get(i)+1) {
                    		nextAssignationNumber = listAssignationNumbers.get(i);
                            break;
                    	}
                    }
                }
            }
            handler.close();
            return nextAssignationNumber+1;
        }
    	
    }

    /**
     * Retrieve if an yard exists or not in the DB
     * @param id the id of the yard
     * @param warehouse the warehouse to be retrieved
     * @return the {@link Boolean} that checks if a yard exists
     */
    public boolean exist(int id, String warehouse) {
        String sql_query = "Select count(*) from YARD " +
                "where id= :id and warehouse=:warehouse";
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            query_string
                    .bind("id", id)
                    .bind("warehouse", warehouse);
            int yard_id = query_string.mapTo(int.class).first();
            handler.close();
            return yard_id > 0;
        }
    }

    /**
     * Retrieve a {@link Yard} by its id and warehouse.
     * @param id the id for yard
     * @param warehouse the warehouse that you are asking for.
     * @return the Yard if exist.
     */
    public Yard getByIdAndWarehouse(int id, String warehouse) {
        String sql_query = "Select id,color,warehouse,assignation_number "+
                "from YARD " +
                "where id= :id and warehouse=:warehouse";
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
>>>>>>> 58ceeee (cosa 1 funcionando, cambiando el estado del yard a ocupado)

<<<<<<< HEAD
	public List<Yard> listByWarehouse(String warehouse) {
		String sql_query = "select id,color from yard where warehouse=?";
		Object[] args = { warehouse };
		List<Yard> listaYards = template.query(sql_query, args,
				(result, rowNum) -> new Yard(result.getInt("id"), result.getString("color"), 1));
		System.out.println(listaYards.get(0).getColor() + " " + listaYards.size());
		return listaYards;
	}
=======
    public List<Yard> getByWarehouse(String warehouse) {
        String sql_query = "Select id,color,warehouse,assignation_number "+
                "from YARD " +
                "where warehouse=:warehouse order by assignation_number";
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            query_string.bind("warehouse", warehouse);
            List<Yard> yards = query_string.mapTo(Yard.class).list();
            handler.close();
            return yards;
        }
    }
>>>>>>> 571515b (n)

<<<<<<< HEAD
	/**
	 * Mapper of the {@link Yard} for the JDBI implementation.
	 */
	@Component
	public static class YardMapper implements RowMapper<Yard> {
=======
    public List<Yard> getAll() {
        String sql_query = "Select id,color,warehouse,assignation_number "+
                "from YARD ";
        try (Handle handler = dbi.open();
             Query query_string = handler.createQuery(sql_query)) {
            List<Yard> yards = query_string.mapTo(Yard.class).list();
            handler.close();
            return yards;
        }
    }
    
    public Yard changeColorOccupy(Yard yard) {
        String sql_query="update yard set color = '#E0E0E0' where  color= :color and  "
        		+ "warehouse= :warehouse and assignation_number= :assignation_number";
        try(Handle handler=dbi.open();
            Update query_string = handler.createUpdate(sql_query)){
            query_string
                    .bind("color",yard.getColor())
                    .bind("warehouse",yard.getWarehouse())
                    .bind("assignation_number", yard.getAssignationNumber());
            int yard_id=query_string.executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class).first();
            handler.close();
            yard.setColor("#E0E0E0");
            yard.setId(yard_id);
            return yard;
      }
   }
<<<<<<< HEAD
>>>>>>> 58ceeee (cosa 1 funcionando, cambiando el estado del yard a ocupado)
=======
    
    public Yard modifyColor(Yard yard) {
        String sql_query="update yard set color = original_color where  color= '#E0E0E0' and  "
        		+ "warehouse= :warehouse and assignation_number= :assignation_number";
        try(Handle handler=dbi.open();
            Update query_string = handler.createUpdate(sql_query)){
            query_string	
                    .bind("warehouse",yard.getWarehouse())
                    .bind("assignation_number", yard.getAssignationNumber());
            int yard_id=query_string.executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class).first();
            handler.close();
            yard.setId(yard_id);
            return yard;
      }
   }
>>>>>>> 0611fec (Punto 2 terminado, cambiando el estado de un yard)

		/**
		 * Override of the map method to set the fields in the SomeObject object when
		 * extracted from the repository.
		 *
		 * @param rs  result set with the fields of the extracted some object.
		 * @param ctx the context of the request that extracted the some object.
		 * @return the {@link Yard} instance with the extracted fields.
		 * @throws SQLException if the result set throws an error when extracting some
		 *                      field.
		 */
		@Override
		public Yard map(ResultSet rs, StatementContext ctx) throws SQLException {

			Yard yard = new Yard(rs.getInt("id"), rs.getString("color"), 1);

<<<<<<< HEAD
			return yard;
		}
	}
=======
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
        public Yard map(ResultSet rs, StatementContext ctx) throws SQLException {
            Yard yard = new Yard( rs.getInt("id"), rs.getString("color"), rs.getInt("assignation_number"));
            yard.AssignWarehouse(rs.getString("warehouse"));
            return yard;
        }
    }
>>>>>>> 571515b (n)
}
