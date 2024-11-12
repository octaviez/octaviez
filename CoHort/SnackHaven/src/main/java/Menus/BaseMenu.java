package Menus;

import java.io.IOException;
import java.util.Scanner;

public abstract class BaseMenu implements MainMenu.Menu {
    protected Scanner scanner = new Scanner(System.in);
    protected static String banner = "*********************************************";

    @Override
    public void displayOptions() throws IOException {
        System.out.println(banner);

        displayMenuTitle();

        displayMenuOptions();

        System.out.println(banner);


        boolean validInput = false;
        while (!validInput) {
            String userInput = scanner.nextLine();

            validInput = handleUserInput(userInput);
        }
    }


    protected abstract void displayMenuTitle();
    protected abstract void displayMenuOptions();
    protected abstract boolean handleUserInput(String input) throws IOException;
    protected void banner() {
        System.out.println("*********************************************");
    }
}
