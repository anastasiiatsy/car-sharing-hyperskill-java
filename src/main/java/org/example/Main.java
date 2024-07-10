package org.example;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try(Application application = new Application()) {
            application.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}