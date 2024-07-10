package org.example;

import java.util.*;

public class Application implements AutoCloseable {

    private Map<String, String> mainMenuOptions = new LinkedHashMap<>() {{
        put("1", "Log in as a manager");
        put("2", "Log in as a customer");
        put("3", "Create a customer");
        put("0", "Exit");
    }};

    private Map<String, String> managerMenuOptions = new LinkedHashMap<>() {{
        put("1", "Company list");
        put("2", "Create a company");
    }};
    private Map<String, String> carMenuOptions = new LinkedHashMap<>() {{
        put("1", "Car list");
        put("2", "Create a car");
    }};

    private Map<String, String> customerMenuOptions = new LinkedHashMap<>() {{
        put("1", "Rent a car");
        put("2", "Return a rented car");
        put("3", "My rented car");
    }};

    private ArrayDeque<Command> savedCommands = new ArrayDeque<>();

    private DbCompanyDao companyDb = new DbCompanyDao();

    private DbCarDao carDb = new DbCarDao();

    private DbCustomerDao customerDb = new DbCustomerDao();

    public void start() {
        startMainMenu();
    }

    private void startMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.setMenu(mainMenuOptions);
        MenuCommand menuCommand = new MenuCommand(mainMenu);
        savedCommands.push(menuCommand);

        while (!savedCommands.isEmpty()) {
            String mainMenuOption = displayMenu(menuCommand);

            switch (mainMenuOption) {
                case "0" -> exitMainMenu();
                case "1" -> {
                    startManagerMenu();
                }
                case "2" -> {
                    startAllCustomersMenu();
                }
                case "3" -> {
                    createCustomer();
                }
            }
        }
    }

    private void startManagerMenu() {
        Menu managerMenu = new Menu();
        managerMenu.setMenu(managerMenuOptions);
        managerMenu.addReturnOption();
        MenuCommand menuCommand = new MenuCommand(managerMenu);
        savedCommands.push(menuCommand);

        while (!savedCommands.isEmpty()) {
            String managerMenuOption = displayMenu(menuCommand);

            switch (managerMenuOption) {
                case "0" -> {
                    savedCommands.pop();
                    startMainMenu();
                }
                case "1" -> startCompanyMenu();
                case "2" -> createCompany();
            }
        }
    }

    private void startCompanyMenu() {
        Menu companyMenu = new Menu();
        MenuCommand menuCommand = new MenuCommand(companyMenu);
        List<DatabaseElement> companyList = companyDb.findAll();

        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!\n");
        } else {
            companyMenu.populateMenuOptions(companyList);
            companyMenu.addReturnOption();
            System.out.println("Choose the company:");
            String userInput = displayMenu(menuCommand);

            if ("0".equals(userInput)) {
                savedCommands.pop();
                startManagerMenu();
            }

            if (null != userInput) {
                DatabaseElement company = companyList.get(Integer.parseInt(userInput) - 1);
                System.out.println("'" + company.getName() + "'" + " company");
                startCarMenu(company);
            }
        }
    }

    private void startCarMenu(DatabaseElement company) {
        Menu carMenu = new Menu();
        MenuCommand menuCommand = new MenuCommand(carMenu);
        carMenu.setMenu(carMenuOptions);
        carMenu.addReturnOption();

        while (!savedCommands.isEmpty()) {
            String selectedOption = displayMenu(menuCommand);

            switch (selectedOption) {
                case "0" -> {
                    savedCommands.pop();
                    startManagerMenu();
                }
                case "1" -> {
                    savedCommands.push(menuCommand);
                    startCarCompanyMenu(company);
                }
                case "2" -> createCar(company);
            }
        }
    }

    private void startCarCompanyMenu(DatabaseElement company) {
        Menu carCompanyMenu = new Menu();
        MenuCommand menuCommand = new MenuCommand(carCompanyMenu);
        int company_id = companyDb.getID(company);
        List<DatabaseElement> carList = carDb.findAllByCompanyId(company_id);
        carCompanyMenu.populateMenuOptions(carList);
        savedCommands.push(menuCommand);

        if (carList.isEmpty()) {
            System.out.println("The car list is empty!\n");
        } else {
            System.out.println("Car list:");
            carCompanyMenu.displayMenu();
            System.out.println();
            savedCommands.push(menuCommand);
        }
    }

    private void startAllCustomersMenu() {
        Menu allCustomersMenu = new Menu();
        MenuCommand menuCommand = new MenuCommand(allCustomersMenu);
        List<DatabaseElement> customerList = customerDb.findAll();
        allCustomersMenu.populateMenuOptions(customerList);
        allCustomersMenu.addReturnOption();
        savedCommands.push(menuCommand);

        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!\n");
        } else {
            System.out.println("Choose a customer:");
            String userInput = displayMenu(menuCommand);
            DatabaseElement customer = customerList.get(Integer.parseInt(userInput) - 1);

            if ("0".equals(userInput)) {
                savedCommands.pop();
            } else {
                startCustomerMenu(customer);
            }
        }
    }

    private void startCustomerMenu(DatabaseElement customer) {
        Menu customerMenu = new Menu();
        MenuCommand menuCommand = new MenuCommand(customerMenu);
        customerMenu.setMenu(customerMenuOptions);
        customerMenu.addReturnOption();
        savedCommands.push(menuCommand);

        while (!savedCommands.isEmpty()) {
            String userInput = displayMenu(menuCommand);

            switch (userInput) {
                case "0" -> {
                    savedCommands.pop();
                    startMainMenu();
                }
                case "1" -> rentCar(customer);
                case "2" -> returnRentedCar(customer);
                case "3" -> printRentedCar(customer);
            }
        }

    }

    private void rentCar(DatabaseElement customer) {
        if (customerDb.getID(customer) != 0) {
            System.out.println("You've already rented a car!\n");
        } else {
            List<DatabaseElement> companiesList = companyDb.findAll();
            if (companiesList.isEmpty()) {
                System.out.println("The company list is empty!\n");
            } else {
                selectCompany(companiesList, customer);
            }
        }
    }

    private void selectCompany(List<DatabaseElement> companiesList, DatabaseElement customer) {
        Menu menu = new Menu();
        menu.populateMenuOptions(companiesList);
        menu.addReturnOption();
        MenuCommand menuCommand = new MenuCommand(menu);
        savedCommands.push(menuCommand);
        while (!savedCommands.isEmpty()) {
            System.out.println("Choose a company:");
            String companyIndex = displayMenu(menuCommand);

            if ("0".equals(companyIndex)) {
                savedCommands.pop();
                startCustomerMenu(customer);
            } else {
                DatabaseElement company = companiesList.get(Integer.parseInt(companyIndex) - 1);
                int companyId = companyDb.getID(company);
                selectCar(customer,company, companyId, companyIndex);
            }
        }
    }

    private void selectCar(DatabaseElement customer, DatabaseElement company, int companyId, String companyIndex) {
        List<DatabaseElement> availableCars = carDb.findAllAvailableCars(companyId);

        if (!hasCars(availableCars, companyIndex)) {
            System.out.printf("No available cars in the '%s' company", company.getName());
        } else {
            Menu carMenu = new Menu();
            carMenu.populateMenuOptions(availableCars);
            carMenu.addReturnOption();
            MenuCommand carMenuCommand = new MenuCommand(carMenu);
            System.out.println("Choose a car:");
            String selectedCar = displayMenu(carMenuCommand);
            if ("0".equals(selectedCar)) {
                return;
            }

            customer.setRentedCarId(availableCars.get(Integer.parseInt(selectedCar) - 1).getId());
            if (customerDb.update(customer)) {
                System.out.println("You rented " + "'" + availableCars.get(Integer.parseInt(selectedCar) - 1).getName() + "'\n");
                startCustomerMenu(customer);
            }
        }
    }

    private boolean hasCars(List<DatabaseElement> availableCars, String userInput) {

        if (availableCars.size() == 1) {
            DatabaseElement car = availableCars.get(Integer.parseInt(userInput) - 1);
            if (customerDb.hasRentedCarId(car)) {
                return false;
            }
        }

        if (availableCars.isEmpty()) {
            return false;
        }
        return true;
    }

    private void printRentedCar(DatabaseElement customer) {
        int carId = customerDb.getID(customer);

        if (carId == 0) {
            System.out.println("You didn't rent a car!\n");
        } else {
            DatabaseElement car = carDb.findById(carId);
            int companyId = carDb.getCompanyID(car);
            DatabaseElement company = companyDb.findById(companyId);
            System.out.println("Your rented car:\n" +
                    car.toString() + "\n" +
                    "Company:" + "\n" +
                    company.toString() + "\n");
        }
    }

    public void returnRentedCar(DatabaseElement customer) {
        if (customerDb.getID(customer) == 0) {
            System.out.println("You didn't rent a car!\n");
        } else {
            if (customerDb.setRentedCarIdToNull(customer)) {
                System.out.println("You've returned a rented car!\n");
            }
        }
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        UserInput userInput = new UserInput();
        userInput.setUserInput();
        String name = userInput.getUserInput();
        DatabaseElement company = new DatabaseElement.DatabaseElementBuilder()
                .name(name)
                .build();
        if (companyDb.add(company)) {
            System.out.println("The company was created!");
            System.out.println();
        }
        company.setCompanyId(companyDb.getID(company));
    }

    private void createCar(DatabaseElement company) {
        System.out.println("Enter the car name:");
        UserInput userInput = new UserInput();
        userInput.setUserInput();
        int companyId = companyDb.getID(company);
        String name = userInput.getUserInput();

        DatabaseElement car = new DatabaseElement.DatabaseElementBuilder()
                .name(name)
                .companyId(companyId)
                .build();

        if (carDb.add(car)) {
            System.out.println("The car was added!");
            System.out.println();
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        UserInput userInput = new UserInput();
        userInput.setUserInput();
        String name = userInput.getUserInput();
        DatabaseElement customer = new DatabaseElement.DatabaseElementBuilder()
                .name(name).build();
        if (customerDb.add(customer)) {
            System.out.println("The customer was added!");
            System.out.println();
        }
    }


    private String displayMenu(Command command) {
        UserInput userInput = new UserInput();
        GUI gui = new GUI();
        gui.setCommand(command);
        gui.menuWasSelected();
        userInput.setUserInput();
        System.out.println();
        return userInput.getUserInput();
    }

    private void exitMainMenu() {
        Menu exit = new Menu();
        GUI gui = new GUI();
        MenuCommand menuCommand = new MenuCommand(exit);
        gui.setCommand(menuCommand);
        gui.exitWasSelected();
    }

    @Override
    public void close() throws Exception {
        companyDb.close();
        carDb.close();
        customerDb.close();
    }
}
