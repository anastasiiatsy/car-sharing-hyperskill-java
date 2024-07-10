package org.example;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private Map<String, String> menu = new LinkedHashMap<>();

    public void displayMenu() {
        for (String item : menu.keySet()) {
            System.out.println(item + ". " + menu.get(item));
        }
    }

    public void populateMenuOptions(List<DatabaseElement> entities) {
        for (int i = 0; i < entities.size(); i++) {
            menu.put(String.valueOf(i + 1), entities.get(i).getName());
        }
    }

    public void addReturnOption() {
        menu.put(String.valueOf(0), "Back");
    }

    public void exit() {
        System.exit(0);
    }

    public void setMenu(Map<String, String> menu) {
        this.menu = menu;
    }
}
