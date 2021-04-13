package banking.model;

public class Card {

    private static final int PIN_LENGTH = 4;
    private static final int CUSTOMER_ACCOUNT_NUMBER_LENGTH = 9;

    private String cardNumber;
    private String pin;
    private double balance;


    public Card() {
        this.pin = generatePin();
        this.cardNumber = generateCardNumber();
        this.balance = 0.0;
    }

    public Card(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    private int getCheckSum(StringBuilder stringBuilder) {
        int[] fifteenArray = new int[15];
        for (int i = 0; i < stringBuilder.length(); i++) {
            fifteenArray[i] = Integer.parseInt(String.valueOf(stringBuilder.charAt(i)));
        }
        int theSumOfFifteen = 0;

        for (int i = 0; i < fifteenArray.length; i++) {
            if (i % 2 == 0) {
                fifteenArray[i] *= 2;
            }
            if (fifteenArray[i] > 9) {
                fifteenArray[i] -= 9;
            }
            theSumOfFifteen += fifteenArray[i];
        }
        int checkSum = (theSumOfFifteen / 10) * 10 + 10 - theSumOfFifteen;
        if (checkSum == 10) {
            checkSum = 0;
        }
        return checkSum;
    }

    private String generateCardNumber() {

        StringBuilder stringBuilder = new StringBuilder("400000");
        StringBuilder customerAccountNumber = generateCustomerAccountNumber();
        stringBuilder.append(customerAccountNumber);
        stringBuilder.append(getCheckSum(stringBuilder));
        return stringBuilder.toString();
    }

    private StringBuilder generateRandomNumber(int numberOfDigits) {
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < numberOfDigits; i++) {
            int random1 = (int) (Math.random() * 10);
            randomNumber.append(random1);
        }
        return randomNumber;
    }

    private String generatePin() {
        return generateRandomNumber(PIN_LENGTH).toString();

    }

    private StringBuilder generateCustomerAccountNumber() {
        return generateRandomNumber(CUSTOMER_ACCOUNT_NUMBER_LENGTH);

    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


}
