package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbCarDao implements CarSharingDao<DatabaseElement> {

    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    private static final String JDBC_DRIVER = "org.h2.Driver";

    private final DbClient dbClient;

    public DbCarDao() {
        try {
            dbClient = new DbClient(DB_URL, JDBC_DRIVER);
//            dbClient.run(CarQueries.DROP_TABLE);
            dbClient.run(CarQueries.CREATE_CAR_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DatabaseElement> findAll() {
        return dbClient.selectForList(String.format(CarQueries.SELECT_ALL_CARS));
    }

    public List<DatabaseElement> findAllAvailableCars(int companyId) {
        return dbClient.selectForList(String.format(CarQueries.SELECT_COMPANIES_AVAILABLE_CARS, companyId));
    }
    @Override
    public DatabaseElement findById(int id) {
        DatabaseElement car = dbClient.select(String.format(CarQueries.SELECT_CAR_BY_ID, id));

        if (car != null) {
            return car;
        } else {
            return null;
        }
    }

    public List<DatabaseElement> findAllByCompanyId(int companyId) {
        return  dbClient.selectForList(String.format(CarQueries.SELECT_CAR_BY_COMPANY_ID, companyId));
    }

    @Override
    public boolean add(DatabaseElement car) {
        return dbClient.run(String.format(CarQueries.INSERT_CAR_NAME_AND_COMPANY_ID, car.getName(),car.getCompanyId()));
    }

    @Override
    public boolean update(DatabaseElement car) {
        return dbClient.run(String.format(CarQueries.UPDATE_CAR_NAME, car.getName(), car.getId()));
    }

    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(CarQueries.DELETE_CAR, id));
    }

    @Override
    public int getID(DatabaseElement car) {
        int id = 0;
        try(var ps = dbClient.getConnection().prepareStatement(String.format(CarQueries.SELECT_CAR_ID, car.getName()))) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public int getCompanyID(DatabaseElement car) {
        int id = 0;
        try(var ps = dbClient.getConnection().prepareStatement(String.format(CarQueries.SELECT_COMPANY_ID, car.getId()))) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                id = rs.getInt("COMPANY_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public void close() {
        dbClient.closeConnection();
    }
}
