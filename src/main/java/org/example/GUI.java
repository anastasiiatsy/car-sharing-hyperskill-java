package org.example;

public class GUI {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void menuWasSelected() {
        command.execute();
    }

    public void exitWasSelected() {
        command.undo();
    }


}
