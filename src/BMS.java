import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static java.util.Objects.isNull;


class Bank {
    public static String name = "ICICI";
//    public static List<Long> accNumDB =  new ArrayList<>();

    public static Map<Long, Customer> customerDB = new HashMap<>();

    public long addCustomerGenerateAccNum(Customer cust) {
        long accNum = (int)(Math.random()*1000000000)+1000000000;
        if(!isNull(customerDB.get(accNum))) {
            accNum = addCustomerGenerateAccNum(cust);
        }
        customerDB.put(accNum, cust);
        System.out.println("Your unique account number : "+accNum);
        System.out.println("Customer Details Added To Bank Successfully!");
//        accNumDB.add(accNum);
        cust.setAccNum(accNum);
        return accNum;
    }


}

class BankAccount extends Bank{
    private float balance;
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
        System.out.println("Welcome to Bank Management System: ");
        System.out.println("Press 1: to add a customer");
        System.out.println("Press 2: to Login a customer's information");
        System.out.println("Press 3: to remove a customer");

        Bank icici = new Bank();
        BankAccount customerAcc = new BankAccount();

        while(true) {
            Scanner inp = new Scanner(System.in);
            System.out.print("> ");
            int opt = inp.nextInt();
            switch (opt) {
                case 1:
                {
                    Customer newCust = new Customer();
                    System.out.println("Proceed to add data to bank?");
                    boolean proceed;
                    proceed = inp.nextBoolean();
                    if(proceed) {
                        icici.addCustomerGenerateAccNum(newCust);
                    }
                }
                break;
                case 2: //login : customer
                {
                    Scanner val = new Scanner(System.in);
                    long adhNum, accNum;
                    System.out.println("Enter your Aadhaar Number:");
                    adhNum = val.nextLong();
                    System.out.println("Enter your Unique Account Number:");
                    accNum = val.nextLong();
                    customerAcc.login(adhNum, accNum);
                    if(customerAcc.currActive != 0) {
                        boolean start = true;
                        while(start) {
                            System.out.println("Hello "+ customerAcc.getUserName());
                            System.out.println("Press 1: To deposit amount");
                            System.out.println("Press 2: To withdraw amount");
                            System.out.println("Press 3: To check balance");
                            System.out.println("Press 4: To logout");
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
                case 3: //l
            }
        }
    }
}
