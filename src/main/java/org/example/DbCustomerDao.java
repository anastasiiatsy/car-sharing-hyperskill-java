package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbCustomerDao implements CarSharingDao<DatabaseElement>{

    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing;DB_CLOSE_DELAY=-1;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE";

    private static final String JDBC_DRIVER = "org.h2.Driver";

    private final DbClient dbClient;

    public DbCustomerDao() {
        try {
            dbClient = new DbClient(DB_URL, JDBC_DRIVER);
//            dbClient.run(DROP_TABLE);
            dbClient.run(CustomerQueries.CREATE_CUSTOMER_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DatabaseElement> findAll() {
        return dbClient.selectForList(String.format(CustomerQueries.SELECT_ALL));
    }

    @Override
    public DatabaseElement findById(int id) {
        DatabaseElement customer = dbClient.select(String.format(CustomerQueries.SELECT_BY_CUSTOMER_ID, id));

        if (customer != null) {
            return customer;
        } else {
            return null;
        }
    }

    public boolean hasRentedCarId(DatabaseElement car) {
        return dbClient.run(String.format(CustomerQueries.SELECT_BY_RENTED_CAR_ID, car.getId()));
    }

    @Override
    public boolean add(DatabaseElement customer) {
        return dbClient.run(String.format(CustomerQueries.INSERT_CUSTOMER, customer.getName(), customer.getRentedCarId()));
    }

    @Override
    public boolean update(DatabaseElement customer) {
        return dbClient.run(String.format(CustomerQueries.UPDATE_RENTED_CAR_ID, customer.getRentedCarId(), customer.getName()));
    }

    public boolean setRentedCarIdToNull(DatabaseElement customer) {
        return dbClient.run(String.format(CustomerQueries.SET_RENTED_CAR_ID_TO_NULL, customer.getName()));
    }

    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(CustomerQueries.DELETE_CUSTOMER, id));
    }

    @Override
    public int getID(DatabaseElement customer) {
            int rentedCarId = 0;
        try(var ps = dbClient.getConnection().prepareStatement(String.format(CustomerQueries.SELECT_RENTED_CAR_ID, customer.getName()))) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rentedCarId = rs.getInt("RENTED_CAR_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rentedCarId;
    }

    public void close() {
        dbClient.closeConnection();
    }
}
