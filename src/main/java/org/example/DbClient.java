package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbClient {
    private Connection connection;

    public DbClient(String DB_URL, String JDBC_DRIVER) throws SQLException {
        this.connection = createConnection(DB_URL, JDBC_DRIVER);
    }

    public Connection createConnection(String DB_URL, String JDBC_DRIVER) {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean run(String str) {
        try (var ps = connection.prepareStatement(str)) {
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public DatabaseElement select(String query) {
        List<DatabaseElement> elements = selectForList(query);

        if (elements.size() == 1) {
            return elements.get(0);
        } else if (elements.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }

    public List<DatabaseElement> selectForList(String query) {
        List<DatabaseElement> elements = new ArrayList<>();

        try(var ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                DatabaseElement element =new DatabaseElement.DatabaseElementBuilder()
                        .name(name)
                        .id(id)
                        .build();
                elements.add(element);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
