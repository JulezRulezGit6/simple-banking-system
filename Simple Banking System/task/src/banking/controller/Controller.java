package banking.controller;

import banking.dao.JDBCCardDAO;
import banking.model.Card;
import org.sqlite.SQLiteDataSource;

import java.util.Scanner;

public class Controller {

    JDBCCardDAO cardDAO;
    Card card;
    Scanner scanner = new Scanner(System.in);


    public void userDisplay() {

        boolean loggedIn = false;
        boolean loopCondition = true;

        while (loopCondition) {
            if (!loggedIn) {
                System.out.println("1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit");

                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        loggedIn = login();
                        break;
                    case 0:
                        loopCondition = false;
                        System.out.println("\nBye!");
                        break;
                    default:
                }
            } else {
                System.out.println("1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit");
                int input = scanner.nextInt();

                switch (input) {
                    case 1:
                        printBalance();
                        break;
                    case 2:
                        addIncome();
                        break;
                    case 3:
                        doTransfer();
                        break;
                    case 4:
                        loggedIn = false;
                        closeAccount();
                        break;
                    case 5:
                        loggedIn = false;
                        System.out.println("\nYou have successfully logged out!\n");
                        break;
                    case 0:
                        loopCondition = false;
                        System.out.println("\nBye!");
                        break;
                    default:
                }
            }
        }
    }

    private void createAccount() {

        Card card = new Card();

        cardDAO.createAccount(card);

        System.out.printf("Your card has been created\n" +
                "Your card number:\n" +
                "%s\n" +
                "Your card PIN:\n" +
                "%s\n", card.getCardNumber(), card.getPin());

    }

    public void start(String databaseName) {

        String url = "jdbc:sqlite:" + databaseName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        cardDAO = new JDBCCardDAO(dataSource);
        cardDAO.init();
        userDisplay();

    }

    private void addIncome() {

        System.out.println("Enter income:\n");
        int deposit = scanner.nextInt();

        cardDAO.addIncome(card, deposit);

        System.out.println("Income was added!\n");

    }

    private void closeAccount() {
        cardDAO.deleteAccount(card);
        System.out.println("The account has been closed!\n");
    }

    private void doTransfer() {
        System.out.println("Transfer\n" +
                "Enter card number:");
        String receivingCardNumber = scanner.next();

        Card byNumber = cardDAO.findByNumber(receivingCardNumber);
        if (!lunhChecker(receivingCardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }

        if (byNumber == null) {
            System.out.println("Such a card does not exist.");
            return;
        }


        if (card.getCardNumber().equals(receivingCardNumber)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }

        System.out.println("Enter how much money you want to transfer:\n");
        int amountToTransfer = scanner.nextInt();

        if (cardDAO.insufficientFunds(card, amountToTransfer)) {
            System.out.println("Not enough money!");
            return;
        }

        cardDAO.transferAmount(card, receivingCardNumber, amountToTransfer);
        System.out.println("Success!");

    }

    private boolean lunhChecker(String cardNumber) {
        int sum = 0;
        char[] chars = cardNumber.toCharArray();
        int[] integers = new int[16];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = Integer.parseInt(String.valueOf(chars[i]));
            if (i % 2 == 0) {
                integers[i] *= 2;
                if (integers[i] > 9) {
                    integers[i] -= 9;
                }
            }
            sum += integers[i];

        }

        return sum % 10 == 0;

    }

    private void printBalance() {
        System.out.println("\nBalance: " + cardDAO.getBalance(card));
    }

    private boolean login() {
        System.out.println("\nEnter your card number:");
        String number = scanner.next();

        System.out.println("Your card PIN:");
        String pin = scanner.next();

        card = cardDAO.findByNumber(number);

        if (card != null && card.getCardNumber().equals(number) && card.getPin().equals(pin)) {
            System.out.println("\nYou have successfully logged in!\n");
            return true;
        } else {
            System.out.println("\nWrong card number or PIN!\n");
            return false;
        }
    }
}

