package org.example;

public class CompanyQueries {

    public static final String CREATE_COMPANY_TABLE = "CREATE TABLE IF NOT EXISTS COMPANY (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(1000) NOT NULL UNIQUE" +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS COMPANY";

    public static final String INSERT_COMPANY = "INSERT INTO COMPANY(NAME) VALUES('%s')";

    public static final String UPDATE_COMPANY_NAME = "UPDATE COMPANY SET NAME " +
            "= '%s' WHERE ID = %d";

    public static final String DELETE_COMPANY = "DELETE * FROM COMPANY WHERE ID = %d";

    public static final String SELECT_ALL_COMPANIES = "SELECT * FROM COMPANY";

    public static final String SELECT_COMPANY_BY_ID = "SELECT * FROM COMPANY WHERE ID = %d";

    public static final String SELECT_COMPANY_ID = "SELECT ID FROM COMPANY WHERE NAME = '%s'";
}
