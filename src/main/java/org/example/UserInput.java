package org.example;

import java.util.Scanner;

public class UserInput {

    private String userInput;
    private Scanner scanner = new Scanner(System.in);

    public void setUserInput() {
        userInput = scanner.nextLine();
    }

    public String getUserInput() {
        return userInput;
    }
}
