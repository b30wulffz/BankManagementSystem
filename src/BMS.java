import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

import static java.util.Objects.isNull;


class Bank {
    public static String name = "ICICI";

    public static Map<Long, Customer> customerDB = new HashMap<>();

    public long addCustomerGenerateAccNum(Customer cust) {
        long accNum = (int)(Math.random()*1000000000)+1000000000;
        if(!isNull(customerDB.get(accNum))) {
            accNum = addCustomerGenerateAccNum(cust);
        }
        customerDB.put(accNum, cust);
        System.out.println("Your unique account number : "+accNum);
        System.out.println("Customer Details Added To Bank Successfully!");
        cust.setAccNum(accNum);
        return accNum;
    }

    public void removeCustomer(){
        System.out.println("Enter the account number of the customer: ");
        Scanner inp = new Scanner(System.in);
        long accNum = inp.nextLong();
        if(isNull(customerDB.get(accNum))){
            System.out.println("Customer Not Found!");
        }
        else {
            System.out.println("Are you sure: (Enter true/false)");
            boolean confirm = inp.nextBoolean();
            if(confirm){
                customerDB.remove(accNum);
                System.out.println("Account: "+accNum+" successfully deleted!");
            }
            else{
                System.out.println("Deletion Aborted!");
            }
        }
    }

    public void printDetails(){
        System.out.println("The users and account details are as follows: ");
        customerDB.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " : " + entry.getValue().getName());
        });

    }

}

class BankAccount extends Bank{
//    private float balance;
    public long currActive;
    public static Map<Long, Double> balanceDB = new HashMap<>();

    public void login(long adhNum, long accNum){
        if(currActive == 0) {
            if (isNull(customerDB.get(accNum))) {
                System.out.println("Sorry! Account number does not exists.");
            } else if (customerDB.get(accNum).getAdhNum() == adhNum) {
                currActive = accNum;
                if(isNull(balanceDB.get(accNum))) {
                    balanceDB.put(accNum, 0.0);
                }
                System.out.println("Successfully Logged In!");
            } else {
                System.out.println("Login Details are incorrect.");
            }
        }
        else {
            System.out.println("User is currently logged in. First logout to login again!");
        }
    }

    public void logout() {
        if(currActive!= 0) {
            currActive = 0;
            System.out.println("Successfully Logged Out!");
        }
        else {
            System.out.println("First Login to Logout!");
        }
    }

    public String getUserName() {
        if(currActive!= 0) {
            return customerDB.get(currActive).getName();
        }
        else {
            return "<Error> User Not Logged In";
        }
    }

    public void deposit(double amount){
        if(currActive!=0) {
            if (amount > 0.0) {
                balanceDB.put(currActive, balanceDB.get(currActive) + amount);
                System.out.println("Transaction Successful.");
            } else {
                System.out.println("Sorry! Amount cannot be null or negative.");
            }
        }
        else {
            System.out.println("User is not logged in!");
        }
    }

    public void withdraw(double amount){
        if(currActive!=0) {
            double balance = balanceDB.get(currActive);
            if (amount <= 0.0) {
                System.out.println("Sorry! Amount cannot be null or negative.");
            } else if ((balance - amount) <= 500.0) {
                System.out.println("Sorry! The balance after deduction will be less than Rs 500.");
                System.out.println("Transaction cannot be carried out.");
            } else {
                balance = balance - amount;
                balanceDB.put(currActive, balance);
                System.out.println("Transaction successful!");
            }
        }
        else {
            System.out.println("User is not logged in!");
        }
    }

    public void checkBalance(){
        if(currActive!=0) {
            System.out.println("Your balance is : " + balanceDB.get(currActive));
        }
        else {
            System.out.println("User is not logged in!");
        }
    }

    public void fundsTransfer(){
        if(currActive!=0) {
            System.out.print("Enter the account number of the recipient: ");
            Scanner input = new Scanner(System.in);
            long recipientAccNum = input.nextLong();
            if(recipientAccNum == currActive) {
                System.out.println("Recipient should not be same as sender!");
                return;
            }
            if(isNull(customerDB.get(recipientAccNum))){
                System.out.println("Recipient does not exist. Please try again later.");
            }
            else {
                System.out.println("Enter the amount to be transferred: ");
                double amount = input.nextDouble();
                double balance = balanceDB.get(currActive);
                if((balance-amount) <= 500.0) {
                    System.out.println("Sorry! Transaction could be completed due to insufficient funds.");
                }
                else {
                    if(isNull(balanceDB.get(recipientAccNum))){
                        balanceDB.put(recipientAccNum, 0.0);
                    }
                    double netBalance = amount+balanceDB.get(recipientAccNum);
                    balanceDB.put(recipientAccNum, netBalance);
                    balanceDB.put(currActive, (balance-amount));
                    System.out.println("Transaction is successfully completed!");
                }
            }
        }
        else {
            System.out.println("User is not logged in!");
        }
    }
}

class ATM extends BankAccount{
    private static Map<Long, Integer> atmPinDB = new HashMap<>();
    public static long currActiveATM = 0;
    public void loginATM(){
        if(currActiveATM == 0) {
            Scanner val = new Scanner(System.in);
            long adhNum, accNum;
            int atmPin;
            System.out.println("Enter your Unique Account Number:");
            accNum = val.nextLong();

            if (isNull(customerDB.get(accNum))) {
                System.out.println("Sorry! Account number does not exists.");
            } else {
                if(isNull(atmPinDB.get(accNum))){
                    System.out.println("First time login!");

                    System.out.println("Enter your Aadhaar Number:");
                    adhNum = val.nextLong();
                    if(customerDB.get(accNum).getAdhNum() == adhNum) {
                        atmPin = ((int)(Math.random()*10000)+1000)%10000;
                        atmPinDB.put(accNum, atmPin);
                        System.out.println("Generated ATM pin: "+atmPin);
                        if(isNull(balanceDB.get(accNum))) {
                            balanceDB.put(accNum, 0.0);
                        }
                    }
                    else {
                        System.out.println("Login Details are Incorrect!");
                    }
                }

                System.out.println("Please enter your ATM pin to continue: ");
                atmPin = val.nextInt();
                if(atmPinDB.get(accNum) == atmPin){
                    currActiveATM = accNum;
                    System.out.println("Login Successful!");
                }
                else {
                    System.out.println("Login Details are Incorrect!");
                }

            }
        }
        else {
            System.out.println("User is currently logged in. First logout to login again!");
        }
    }

    public void depositATM(double amount){
        if(currActiveATM!=0) {
            if (amount > 0.0) {
                balanceDB.put(currActiveATM, balanceDB.get(currActiveATM) + amount);
                System.out.println("Transaction through ATM is Successful.");
            } else {
                System.out.println("Sorry! Amount cannot be null or negative.");
            }
        }
        else {
            System.out.println("User is not logged in!");
        }
    }

    public void withdrawATM(double amount){
        if(currActiveATM!=0) {
            double balance = balanceDB.get(currActiveATM);
            if (amount <= 0.0) {
                System.out.println("Sorry! Amount cannot be null or negative.");
            } else if ((balance - amount) <= 500.0) {
                System.out.println("Sorry! The balance after deduction will be less than Rs 500.");
                System.out.println("Transaction cannot be carried out.");
            } else {
                balance = balance - amount;
                balanceDB.put(currActiveATM, balance);
                System.out.println("Transaction successful!");
                System.out.println("Please collect the cash from tray!");
            }
        }
        else {
            System.out.println("User is not logged in!");
        }
    }

    public void checkBalanceATM(){
        if(currActiveATM!=0) {
            System.out.println("Your balance is : " + balanceDB.get(currActiveATM));
        }
        else {
            System.out.println("User is not logged in!");
        }
    }

    public void logoutATM(){
        if(currActiveATM!= 0) {
            currActiveATM = 0;
            System.out.println("Successfully Logged Out!");
            System.out.println("Thank you for using "+name+" bank ATM.");
            System.out.println("Do visit us again!");
        }
        else {
            System.out.println("First Login to Logout!");
        }
    }
    public String getUserName() {
        if(currActiveATM!= 0) {
            return customerDB.get(currActiveATM).getName();
        }
        else {
            return "<Error> User Not Logged In";
        }
    }

}

class Customer {
    private String name;
    private long accNum;
    private String address;
    private long mobNum;
    private long adhNum;

    Customer(){
        Scanner obj = new Scanner(System.in);
        System.out.print("Name: ");
        name = obj.nextLine();
        System.out.print("Address: ");
        address = obj.nextLine();
        System.out.print("Mobile Number: ");
        mobNum = obj.nextLong();
        System.out.print("Aadhaar Number: ");
        adhNum = obj.nextLong();
        //verify once
//        accNum = new Bank().generateAccNum(custData);
    }

    public void setAccNum(long accNum){
        this.accNum = accNum;
    }

    public void getAccNum() {
        System.out.println("Hello "+name+"!");
        System.out.println("Your account number is generated successfully. Please note it down for further use.");
        System.out.println("> "+accNum);
    }

    public long getAdhNum(){
        return adhNum;
    }

    public String getName(){
        return name;
    }
}

public class BMS {
    public static void main(String[] args) {

        Bank icici = new Bank();
        BankAccount customerAcc = new BankAccount();
        ATM customerATM = new ATM();
        boolean outerLoop = true;
        while(outerLoop) {
            System.out.println("\n------------------------------------------------------\n");
            System.out.println("Welcome to Bank Management System: ");
            System.out.println("Press 1: For Bank Control Panel");
            System.out.println("Press 2: For Customer Bank Account Login");
            System.out.println("Press 3: For ATM Login");
            System.out.println("Press 4: To Exit");

            Scanner inp = new Scanner(System.in);
            System.out.print("> ");
            int opt = inp.nextInt();
            switch (opt) {
                case 1:
                {
                    boolean start = true;
                    while (start) {
                        System.out.println("\n------------------------------------------------------\n");
                        System.out.println("Welcome to " + icici.name + " Bank.");
                        System.out.println("Press 1: To add a customer");
                        System.out.println("Press 2: To remove a customer");
                        System.out.println("Press 3: To print list of customers");
                        System.out.println("Press 4: To exit");
                        System.out.print("-> ");
                        int userOpt = inp.nextInt();
                        switch (userOpt) {
                            case 1: {
                                Customer newCust = new Customer();
                                System.out.println("Proceed to add data to bank? (Enter true/false)");
                                boolean proceed;
                                proceed = inp.nextBoolean();
                                if (proceed) {
                                    icici.addCustomerGenerateAccNum(newCust);
                                }
                            }
                            break;
                            case 2: {
                                icici.removeCustomer();
                            }
                            break;
                            case 3:{
                                icici.printDetails();
                            }
                            break;
                            case 4: {
                                System.out.println("Exiting " + icici.name + " Bank Control Panel");
                                start = false;
                            }
                            default: {
                                System.out.println("Sorry! Wrong Input Detected. Please Try Again.");
                            }
                        }
                    }
                }
                break;
                case 2: //login : customer
                {
                    Scanner val = new Scanner(System.in);
                    System.out.println("\n------------------------------------------------------\n");
                    System.out.println("Welcome to "+new Bank().name+" bank account.");
                    long adhNum, accNum;
                    System.out.println("Enter your Aadhaar Number:");
                    adhNum = val.nextLong();
                    System.out.println("Enter your Unique Account Number:");
                    accNum = val.nextLong();
                    customerAcc.login(adhNum, accNum);
                    if(customerAcc.currActive != 0) {
                        boolean start = true;
                        while(start) {
                            System.out.println("------------------------------------------------------");
                            System.out.println("Hello "+ customerAcc.getUserName());
                            System.out.println("Press 1: To deposit amount");
                            System.out.println("Press 2: To withdraw amount");
                            System.out.println("Press 3: To check balance");
                            System.out.println("Press 4: To transfer funds");
                            System.out.println("Press 5: To logout");
                            System.out.print("-> ");
                            int userOpt = inp.nextInt();
                            switch (userOpt) {
                                case 1:
                                {
                                    double amount;
                                    System.out.println("Enter the amount to deposit");
                                    System.out.print("--> ");
                                    amount = inp.nextDouble();
                                    customerAcc.deposit(amount);
                                }
                                break;
                                case 2:
                                {
                                    double amount;
                                    System.out.println("Enter the amount to withdraw");
                                    System.out.print("--> ");
                                    amount = inp.nextDouble();
                                    customerAcc.withdraw(amount);
                                }
                                break;
                                case 3:
                                {
                                    customerAcc.checkBalance();
                                }
                                break;
                                case 4:
                                {
                                    customerAcc.fundsTransfer();
                                }
                                break;
                                case 5:
                                {
                                    customerAcc.logout();
                                    start = false;
                                }
                                break;
                                default:
                                {
                                    System.out.println("Sorry! Wrong Input Detected. Please Try Again.");
                                }
                            }
                        }
                    }
                }
                break;
                case 3:
                {
                    System.out.println("\n------------------------------------------------------\n");
                    System.out.println("Welcome to "+new Bank().name+" bank ATM.");
                    customerATM.loginATM();

                    if(customerATM.currActiveATM != 0) {
                        boolean start = true;
                        while(start) {
                            System.out.println("------------------------------------------------------");
                            System.out.println("Hello "+ customerATM.getUserName());
                            System.out.println("Welcome to "+new Bank().name+" bank ATM.");
                            System.out.println("Press 1: To check balance");
                            System.out.println("Press 2: To deposit amount");
                            System.out.println("Press 3: To withdraw amount");
                            System.out.println("Press 4: To logout");
                            System.out.print("-> ");
                            int userOpt = inp.nextInt();
                            switch (userOpt) {
                                case 1:
                                {
                                    customerATM.checkBalanceATM();
                                }
                                break;
                                case 2:
                                {
                                    double amount;
                                    System.out.println("Enter the amount to deposit");
                                    System.out.print("--> ");
                                    amount = inp.nextDouble();
                                    customerATM.depositATM(amount);
                                }
                                break;
                                case 3:
                                {
                                    double amount;
                                    System.out.println("Enter the amount to withdraw");
                                    System.out.print("--> ");
                                    amount = inp.nextDouble();
                                    customerATM.withdrawATM(amount);
                                }
                                break;
                                case 4:
                                {
                                    customerATM.logoutATM();
                                    start = false;
                                }
                                break;
                                default:
                                {
                                    System.out.println("Sorry! Wrong Input Detected. Please Try Again.");
                                }
                            }
                        }
                    }
                }
                break;
                case 4:
                {
                    outerLoop = false;
                    System.out.println("Exiting!");
                    System.out.println("\n------------------------------------------------------\n");
                }
                break;
                default:
                {
                    System.out.println("Sorry! Wrong Input Detected. Please Try Again.");
                }
            }
        }
    }
}