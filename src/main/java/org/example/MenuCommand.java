package org.example;

public class MenuCommand implements Command {
    private Menu menu;

    public MenuCommand(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void execute() {
        menu.displayMenu();
    }

    public void undo() {
        menu.exit();
    }
}
