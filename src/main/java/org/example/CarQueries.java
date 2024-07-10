package org.example;

public class CarQueries {
    public static final String CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS CAR (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(1000) NOT NULL UNIQUE," +
            "COMPANY_ID INT NOT NULL," +
            "CONSTRAINT fk_companyId FOREIGN KEY (COMPANY_ID)" +
            "REFERENCES COMPANY(ID)" +
            "ON UPDATE CASCADE" +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS CAR";

    public static final String INSERT_CAR_NAME_AND_COMPANY_ID = "INSERT INTO CAR(NAME,COMPANY_ID) VALUES('%s',%d)";

    public static final String UPDATE_CAR_NAME = "UPDATE CAR SET NAME " +
            "= '%s' WHERE ID = %d";

    public static final String DELETE_CAR = "DELETE * FROM CAR WHERE ID = %d";

    public static final String SELECT_ALL_CARS = "SELECT * FROM CAR";

    public static final String SELECT_CAR_BY_ID = "SELECT * FROM CAR WHERE ID = %d";

    public static final String SELECT_CAR_BY_COMPANY_ID = "SELECT * FROM CAR WHERE COMPANY_ID = %d";

    public static final String SELECT_CAR_ID = "SELECT ID FROM CAR WHERE NAME = '%s'";

    public static final String SELECT_COMPANY_ID = "SELECT COMPANY_ID FROM CAR WHERE ID = %d";

    public static final String SELECT_COMPANIES_AVAILABLE_CARS = "SELECT * " +
            "FROM CAR " +
            "WHERE COMPANY_ID = %d " +
            "AND " +
            "ID NOT IN (" +
            "SELECT RENTED_CAR_ID " +
            "FROM CUSTOMER " +
            "WHERE RENTED_CAR_ID IS NOT NULL)";
}
