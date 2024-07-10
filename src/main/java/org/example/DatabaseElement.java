package org.example;

public class DatabaseElement {
    private String name;
    private int id;

    private int companyId;

    private int rentedCarId;

    public DatabaseElement(DatabaseElementBuilder builder) {
        this.name = builder.name;
        this.id = builder.id;
        this.companyId = builder.companyId;
        this.rentedCarId = builder.rentedCarId;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    public int getCompanyId() {
        return companyId;
    }

    public int getRentedCarId() {
        return rentedCarId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setRentedCarId(int rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }


    public static class DatabaseElementBuilder {

        private String name;

        private int id;

        private int companyId;

        private int rentedCarId;

        public DatabaseElementBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DatabaseElementBuilder id(int id) {
            this.id = id;
            return this;
        }

        public DatabaseElementBuilder companyId(int companyId) {
            this.companyId = companyId;
            return this;
        }

        public DatabaseElementBuilder rentedCarId(int rentedCarId) {
            this.rentedCarId = rentedCarId;
            return this;
        }

        public DatabaseElement build() {
            DatabaseElement element = new DatabaseElement(this);
            return element;
        }

    }
}
