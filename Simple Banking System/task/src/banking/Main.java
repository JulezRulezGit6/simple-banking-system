package banking;

import banking.controller.Controller;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Controller controller = new Controller();
        if (args[0].equals("-fileName") && args[1] != null) {
            String fileName = args[1];
            controller.start(fileName);

        } else {
            System.out.println("Please Enter A Valid DB Name. Exiting the program");
        }


    }

}