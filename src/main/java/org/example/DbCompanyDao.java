package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbCompanyDao implements CarSharingDao <DatabaseElement> {
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing;DB_CLOSE_DELAY=-1;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE";

    private static final String JDBC_DRIVER = "org.h2.Driver";

    private final DbClient dbClient;

    public DbCompanyDao() {
        try {
            dbClient = new DbClient(DB_URL, JDBC_DRIVER);
//            dbClient.run(CompanyQueries.DROP_TABLE);
            dbClient.run(CompanyQueries.CREATE_COMPANY_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DatabaseElement> findAll() {
        return dbClient.selectForList(String.format(CompanyQueries.SELECT_ALL_COMPANIES));
    }

    @Override
    public DatabaseElement findById(int id) {
        DatabaseElement company = dbClient.select(String.format(CompanyQueries.SELECT_COMPANY_BY_ID, id));

        if (company != null) {
            return company;
        } else {
            return null;
        }
    }

    @Override
    public boolean add(DatabaseElement company) {
        String name = company.getName();
        return dbClient.run(String.format(CompanyQueries.INSERT_COMPANY, name));
    }

    @Override
    public boolean update(DatabaseElement company) {
        return dbClient.run(String.format(CompanyQueries.UPDATE_COMPANY_NAME, company.getName(), company.getId()));
    }

    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(CompanyQueries.DELETE_COMPANY, id));
    }

    @Override
    public int getID(DatabaseElement company) {
        int id = 0;
        try(var ps = dbClient.getConnection().prepareStatement(String.format(CompanyQueries.SELECT_COMPANY_ID, company.getName()))) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                id = rs.getInt("ID");
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
