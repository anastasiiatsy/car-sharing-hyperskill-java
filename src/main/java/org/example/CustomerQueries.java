package org.example;

public class CustomerQueries {
    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(1000) NOT NULL UNIQUE," +
            "RENTED_CAR_ID INT DEFAULT NULL," +
            "CONSTRAINT fk_rentedCarId FOREIGN KEY (RENTED_CAR_ID)" +
            "REFERENCES CAR(ID)" +
            "ON UPDATE CASCADE" +
            ")";

    public static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS CUSTOMER";

    public static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(NAME) VALUES('%s')";

    public static final String SET_RENTED_CAR_ID_TO_NULL = "UPDATE CUSTOMER SET RENTED_CAR_ID " +
            "= NULL WHERE NAME = '%s'";

    public static final String UPDATE_RENTED_CAR_ID = "UPDATE CUSTOMER SET RENTED_CAR_ID " +
            "= %d WHERE NAME = '%s'";

    public static final String DELETE_CUSTOMER = "DELETE * FROM CUSTOMER WHERE ID = %d";

    public static final String SELECT_BY_CUSTOMER_ID = "SELECT * FROM CUSTOMER WHERE ID = %d";

    public static final String SELECT_BY_RENTED_CAR_ID = "SELECT * FROM CUSTOMER WHERE RENTED_CAR_ID = %d";

    public static final String SELECT_RENTED_CAR_ID = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE NAME = '%s'";
}
