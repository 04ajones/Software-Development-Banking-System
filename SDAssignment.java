/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sdassignment;

/**
 *
 * @author ashleigh
 */
//importing java packages
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * 
 */
//declaring the interface
interface Account 
{ 
    //calling the deposit method
    void deposit(double amount); 
    //calling the get_balance method
    double get_balance(); 
 
} 

//declaring the class 
class BaseAccount implements Account { 
    //declaring variables 
    public double balance; 
    ArrayList<String> holders = new ArrayList<>();   
    ArrayList<Transaction> transactions = new ArrayList<>();  
    ArrayList<Loan> loans = new ArrayList<>();
    protected double interestRate;     
    protected int acc_number; 
    public String account_type; 
    int id;
    int overdraft;
    
    //constructor chaining - adds another argument to contructor 
    //http://stackoverflow.com/questions/285177/how-do-i-call-one-constructor-from-another-in-java
    public BaseAccount(String acc_owner, int acc_num, String acc_type, int _id)
    {
        this(acc_owner,acc_num,acc_type,_id,0);
    }
    //Set up a new account - assign them their variables 
    public BaseAccount(String acc_owner, int acc_num, String acc_type, int _id, double intrate) 
    { 
        holders.add(acc_owner);         
        acc_number = acc_num; 
        account_type = acc_type; 
        interestRate = intrate;
    } 
 
    //Add account holder 
    public void AddAccHolder(String acc_owner, int acc_num) 
    { 
        holders.add(acc_owner); 
        acc_number = acc_num; 
    } 
    //return the holders name
    public String getHolderName()
    {
        return holders.get(0);
    }
    
    //return the ID
    public int getID()
    {
        return id;
    }
    
    //return the account number
    public int getAccountNum() 
    { 
        return acc_number; 
    } 
 
    //calculate and return the balance after an amount has been withdrawn
    public void withdraw(double amount) 
    { 
        balance -= amount; 
    } 
 
    //calculate and return the balance after the amount has been deposited
    @Override
    public void deposit(double amount) 
    { 
        balance += amount; 
    } 
 
    //return the balance
    @Override
    public double get_balance() 
    { 
        return balance; 
    } 
   
    //return the overdraft
    public int get_overdraft()
    {
        return overdraft;
    }
    
    //boolean to find out if an overdraft is allowed
    public boolean is_allowed_overdraft()
    {
        //return true if the account type is not LCR 
        return(!account_type.equals("LCR"));
    }
    
    //set a overdraft limit to a new overdraft
    public void grant_overdraft(int limit)
    {
        set_overdraft(limit);
    }
    
    //change an overdraft limit
    public void change_overdraft(int limit)
    {
        set_overdraft(limit);
    }
    
    //set an overdraft limit of 0 on a revoked overdraft
    public void revoke_overdraft()
    {
        set_overdraft(0);
    }
    
    //grant/change/revoke are all doing the same thing 
    //i.e. setting overdraft to a value so having one method called setOverdraft that they all call
    public void set_overdraft(int limit)
    {
        overdraft = limit;
    }
    
    //boolean to find out if they already have an overdraft
    public boolean has_overdraft()
    {
        return (overdraft != 0);
    }
 
    //returning the account type
    public String get_acc_type() 
    { 
        return account_type; 
    } 
    
    //method used to rollback transfers
    public void rollback()
    {
        double amount;
        //retrieving the last amount that was transfered 
        amount = transactions.get(transactions.size()-1).getAmount();
        transactions.remove(transactions.size()-1);
        //depositing this amount back into their account
        deposit(amount);
        //adding a record to the transaction history
        addTransaction(new Date(),"Rollback",amount);
        
    }
    
    //method to work out the interest rate
    public void interest_rate() 
    { 
        //debugging code - System.out.println(new Date());
        //debugging code - System.out.println(balance);
        balance += get_interest();
        //debugging code - System.out.println(balance);
    } 
    
    //returning the interest to be added on
    public double get_interest()
    {
        return interestRate * balance;
    }
    
    //method used to issue a loan and adding it to the balance
    public void issue_loan(double loan)
    {
         
        balance += loan;
    }
    
    public void addLoan(Date d, double amount)
    {
        //adding a new loan the saving the history in the transactions
        loans.add(new Loan(d, amount));
        this.addTransaction(d,"Deposit Loan", amount);
    }
    
    //returning the loans from the Array
    public ArrayList<Loan> getLoans()
    {
        return loans;
    }
    
    //adding a transaction to the transaction history
    public void addTransaction(Date d, String trans_Type, double amount)
    {
        transactions.add(new Transaction(d,trans_Type, amount));
    }
    
    //returning the transactions from the Array
    public ArrayList<Transaction> getTransactions()
    {
        return transactions;
    }
} 

//declaring a new class 
class Transaction
{

    //declaring variables
    protected Date time;
    String transactionType;
    protected double amount;
    
    //retrieving variables for a transaction
    public Transaction(Date _time, String _transactionType, double _amount)
    {
        time = _time;
        transactionType = _transactionType;
        amount = _amount;

    }
    
    //returning the date
    public Date getDate()
    {
        return time;
    }
    
    //returning the transaction type
    public String getType()
    {
        return transactionType;
    }
    
    //returning the amount 
    public double getAmount()
    {
        return amount;
    }

}

//declaring a class
class Loan
{
    //declaring variables
    protected Date time;
    protected double paybackAmount;
    protected double amount;
    //setting the interest rate
    protected double interestRate = 1.05;
    
    //recieving variables for a loan
    public Loan(Date _time, double _amount)
    {
        time = _time;
        amount = _amount;
        //calculating how much should be paid back
        paybackAmount = amount * interestRate;
    }

    //taking any money paid back of the pay back amount
    public void doPayback(double payment)
    {
        paybackAmount -= payment;
    }

    //returning the date
    public Date getDate()
    {
        return time;
    }
    
    //returning the amount
    public double getAmount()
    {
        return amount;
    }
    
    //returning the pay back amount
    public double getPaybackAmount()
    {
        return paybackAmount;
    }

}

//declare master account for the bank 
class MasterAccount implements Account{ 

    //setting a balance    
    public double balance=10000;
        
    //working out the deposit amount
    @Override
    public void deposit(double amount) {
        
        balance+=amount;
    }
    
    //working out the balance minus the amount withdrawn
    public void withdraw(double amount) {
        
        balance-=amount;
    }

    //returning the balance
    @Override
    public double get_balance() {
        return balance;
    }

} 

//Business Account class inherting from the BaseAccount
class BusinessAccount extends BaseAccount{ 
 
    //variable types are declared
    public BusinessAccount(String business_name, int acc_num, int _id) 
    { 
        super(business_name, acc_num, "Business",_id); 
    } 
} 

//Current Account class inherting from the BaseAccount
class CurrentAccount extends BaseAccount{ 
    
    //variable types are declared
    public CurrentAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "Current",_id); 
    } 
} 

//IRA Account class inherting from the BaseAccount
class IRAccount extends BaseAccount{ 
    
    //variable types are declared
    public IRAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "IRA", _id); 
    } 
} 

//Student Account class inherting from the BaseAccount
class StudentAccount extends BaseAccount{ 
    
    //variable types are declared
    public StudentAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "Student", _id); 
    } 
 
} 

//SBMA Account class inherting from the BaseAccount
class SMBAccount extends BaseAccount{ 
 
    //variable types are declared
    public SMBAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "SMB",_id); 
    } 
 
} 

//Savings Account class inherting from the BaseAccount
class SavingsAccount extends BaseAccount{ 
    
    //variable types are declared
    public SavingsAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "Savings", _id); 
    } 
    
}
   
//High Interest Account class inherting from the BaseAccount
class HighInterestAccount extends BaseAccount{ 
    
    //variable types are declared
    public HighInterestAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "High", _id,0.1); 
    } 
    
}
  
//Islamic Account class inherting from the BaseAccount
class IslamicAccount extends BaseAccount{
    
    //variable types are declared
    public IslamicAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "Islamic", _id); 
    } 
}
    
//Private Account class inherting from the BaseAccount
class PrivateAccount extends BaseAccount{ 
        
    //variable types are declared    
    public PrivateAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "Private", _id, 0.01); 
    } 
}
    
//LCR Account class inherting from the BaseAccount
class LCRAccount extends BaseAccount{ 
 
    //variable types are declared
    public LCRAccount(String owner, int acc_num, int _id) 
    { 
        super(owner, acc_num, "LCR", _id); 
    } 
 
} 
 
//menu class
class Menu { 
 
    //allows you to take user input
    private static final Scanner input = new Scanner(System.in);
    //declaring variables
    private  ArrayList<BaseAccount> accounts = new ArrayList<>();     
    private int acc_num;     
    private int acc_number;     
    private double amount; 
    private String name;
    //creating variable referring to the Master Account class
    MasterAccount master;
    
    //menu method
    public Menu() { 
        //creates a new master account
        master = new MasterAccount();
        //uses the Interest class to do the payInterest method at certain times
        Interest payInterest = new Interest(this);
        //uses the LoanTimer class to do the payLoan method at certain times
        LoanTimer payLoan = new LoanTimer(this);
        //calls the main_menu
        main_menu(); 
    } 
    
    //class which uses the timer task to set times to do certain things
    class Interest extends TimerTask
    {
        Menu _m;

        //interest method
        public Interest (Menu m)
        {
            _m=m;
            //setting when the timer should go off
            (new Timer ()).scheduleAtFixedRate(this, new Date(), 30000);
        }

        //run method to determine what should be done 
        @Override
        public void run() {
            //for loop to go through each of the accounts
            for (int i = 0; i < accounts.size(); i++) 
            {   
            //debugging code - System.out.println("calculating interest for account " + accounts.get(i).getAccountNum() + " at " + new Date());
                double interest = accounts.get(i).get_interest();
                //if interest should be given it is then withdrawn from the master account using the withdraw method
                master.withdraw(interest);
                //retrieving the interest rate
                accounts.get(i).interest_rate(); 
            }   
        }
    }
    
        //class which uses the timer task to set times to do certain things
        class LoanTimer extends TimerTask
    {
        Menu _m;

        public LoanTimer (Menu m)
        {
            _m=m;
            //setting how often timer should go off
            (new Timer ()).scheduleAtFixedRate(this, new Date(), 30000);
        }

        //run method to determine what should be done each time the timer is due to run
        @Override
        public void run() {
            
            //for loop to go through the accounts
            for (int i = 0; i < accounts.size(); i++) 
            {   
                //retrieving the account 
                BaseAccount account = accounts.get(i); 
                
                //debugging System.out.println("calculating interest for account " + accounts.get(i).getAccountNum() + " at " + new Date());
                //retrieving any accounts that have loans
                ArrayList<Loan> loans = account.getLoans();
                //for loop to go through all of the loans
                for (int j = 0; j < loans.size(); j++ )
                {
                    //setting the repayment amount
                    double repayment = 10;
                    //calling the getPaybackAmount method and setting the value to the payback_amount variable 
                    double payback_amount = loans.get(j).getPaybackAmount();
                    //if the amount that is due to be paid back it more then 0 carry onto the next if
                    if( payback_amount > 0)
                    {
                        //if the repayment is more then the payback amount carry onto the next if
                        if (repayment > payback_amount) 
                        {
                            //the repayment is equal to the payback amount
                            repayment = payback_amount;
                        }
                        //if the payment is less then the payback amount 
                        //call the doPayback method
                        loans.get(j).doPayback(repayment);
                        //call the withdraw method
                        account.withdraw(repayment);
                        //call the deposit method from the master account
                        master.deposit(repayment);
                        //add this transaction to the transaction history
                        account.addTransaction(new Date(), "Loan Repayment", repayment);
                    }
                }
                
            }   
        }
    }

    //declare a method of the BaseAccount class
    private BaseAccount getAccount(int acc_num)
    {
        //for loop to go through the accounts
        for (int i = 0; i < accounts.size(); i++) 
        { 
            //if the account number from the getAccountNum method is equal to the account number passed through thid method
            if (accounts.get(i).getAccountNum() == acc_num) 
            {
                //return the account number
                return accounts.get(i);
            } 
        }
        
        // if we get here then we did not find the account so output and return null
        System.out.println("Account not found");
        return null;
    }
    
    //method declared
    public void main_menu() { 

        //declaring the account variable from the BaseAccount
        //asking for a user input
        System.out.println("Press Enter to enter menu"); 
        //when enter is pressed it carries on
         try { 
            System.in.read(); 
        } catch (Exception e) { 
            System.out.println(e); 
        } 
 
        //list of options appear to the user
        System.out.println("1. Create Account\r\n");         
        System.out.println("2. Deposit\r\n"); 
        System.out.println("3. Display Balance\r\n"); 
        System.out.println("4. Withdraw\r\n"); 
        System.out.println("5. Transfer Money\r\n"); 
        System.out.println("6. Overdraft\r\n"); 
        System.out.println("7. Add Account Holder\r\n"); 
        System.out.println("8. Show all accounts Held by a customer\r\n"); 
        System.out.println("9. View Transactions\r\n"); 
        System.out.println("10. Create loan\r\n"); 
        System.out.println("Select option: "); 
 
        //taking in and storing a user input as a variable called option
        int option = Integer.parseInt(input.nextLine()); 
 
        //a switch relates to the user input so whatever number is put in it will go to that numbered case
        switch (option) { 
            //when 1 is input the below method is called
            case 1: 
                CreateAccount();
                break; 
               
            //when 2 is input the below method is called
            case 2: 
                DoDeposit();
                break; 
                 
            //when 3 is input the below method is called
            case 3: 
                ShowBalance();
                break; 
            
            //when 4 is input the below method is called
            case 4: 
                DoWithdraw();
                break;
            
            //when 5 is input the below method is called
            case 5:                 
               DoTransfer();                  
               break;     
            
            //when 6 is input the below method is called
            case 6:  
                DoOverdraft();
                break;
            
            //when 7 is input the below method is called
            case 7: 
                AddAccountHolder();
                break; 
            
            //when 8 is input the below method is called
            case 8: 
                ShowAccounts();
                break;
            
            //when 9 is input the below method is called
            case 9:
                ViewTransaction();
                 break;
            
            //when 10 is input the below method is called
            case 10:
                DoLoan();
                break;
        } 
        
        //main menu method is called once the swich method is complete
        main_menu(); 
 
    }
    
    //method created
    public void CreateAccount()
    {
        //list of options appear to the user
        System.out.println("1. Current Account\r\n");         
        System.out.println("2. Savings Account\r\n"); 
        System.out.println("3. Student Account\r\n"); 
        System.out.println("4. Business Account\r\n"); 
        System.out.println("5. SMB Account\r\n");
        System.out.println("6. IR Account\r\n"); 
        System.out.println("7. High Interest Account\r\n"); 
        System.out.println("8. Islamic Account\r\n"); 
        System.out.println("9. Private Account\r\n"); 
        System.out.println("10. LCR Account\r\n");
        //user input is stored as an int
        int option = Integer.parseInt(input.nextLine());
        //user is asked for another input
        System.out.println("Enter Customer first and Last Name");
        //input stored
        name = input.nextLine();
        //user is asked for another input
        System.out.println("Enter Customer ID");
        //input it stored as an int
        int id = Integer.parseInt(input.nextLine());
        //1 is added to the current account number
        acc_num++;
        //switch is created to select an account depending on the number input by the user
        switch(option){
            //when 1 is input new Current Account is created with the input values
            case 1:
                 accounts.add(new CurrentAccount(name, acc_num, id));  
                break;
            //when 2 is input new Savings Account is created with the input values
            case 2:
                accounts.add(new SavingsAccount(name, acc_num, id));
                break;
            //when 3 is input new Student Account is created with the input values
            case 3:
                accounts.add(new StudentAccount(name, acc_num, id));
                break;
            //when 4 is input new Business Account is created with the input values
            case 4:
                accounts.add(new BusinessAccount(name, acc_num, id));
                break;
            //when 5 is input new SMB Account is created with the input values
            case 5:
                accounts.add(new SMBAccount(name, acc_num, id));
                break;
            //when 6 is input new IR Account is created with the input values
            case 6:
                 accounts.add(new IRAccount(name, acc_num, id));
                break;
            //when 1 is input new High Interest Account is created with the input values
            case 7:
                 accounts.add(new HighInterestAccount(name, acc_num, id));
                break;
            //when 1 is input new Islamic Account is created with the input values
            case 8:
                 accounts.add(new IslamicAccount(name, acc_num, id));
                break;
            //when 1 is input new Private Account is created with the input values
            case 9:
                 accounts.add(new PrivateAccount(name, acc_num, id));
                break;
            //when 1 is input new LCR Account is created with the input values
            case 10:
                 accounts.add(new LCRAccount(name, acc_num, id));
                break;
        }                      
    }
    
    //method declared
    public void DoDeposit()
    {
        //running the AskForAccount method
        BaseAccount account = AskForAccount(); 
        //if the account does not equal null
        if(account != null)
        { 
        //ask for user input
        System.out.println("Enter deposit amount: "); 
        //store the input
        amount = Double.parseDouble(input.nextLine());
        //call the deposit method 
        account.deposit(amount);
        //add this to the transaction history by calling the addTransaction method
        account.addTransaction(new Date(), "Deposit", amount);
        }
    }
    
    //method declared
    public void ShowBalance()
    {
        //running the AskForAccount method
        BaseAccount account = AskForAccount(); 
        //if the account does not equal null
        if(account != null)
        {
                //output the balance of the account
                System.out.println((account.get_balance()));
                //add this to the transaction history
                account.addTransaction(new Date(), "View Balance", amount);
        }
     
    }
    
    //method declared
    public void DoWithdraw()
    {
        //running the AskForAccount method
        BaseAccount account = AskForAccount();
        //if the account does not equal null
        if(account != null)
        { 
            //ask for the user input
            System.out.println("How much would you like to withdraw: "); 
            //store the user input in a variable
            amount = Double.parseDouble(input.nextLine());
            //switch that alls the method to get the account type for the input account
            switch (account.get_acc_type()) 
            {
                //when the account type is Current
                case "Current":
                    //if the amount is less than or equal to 500
                    if (amount <= 500)
                    { 
                        //call the witchdraw method
                        account.withdraw(amount);
                        //add this to the transaction history
                        account.addTransaction(new Date(), "Withdraw", amount);
                    }
                    //if the amount is more then 500
                    else {
                        //output a message to the user
                        System.out.println("The maximum daily withdrawal for a Current account is ?500. This transaction has been cancelled");
                    }
                    break;
                    //when the account type is Savings
                case "Savings":
                    //if the amount is less thanof equal to 300
                    if (amount <= 300)
                    {
                        //call the withdraw amount
                        account.withdraw(amount);  
                        //add this to the transaction history
                        account.addTransaction(new Date(), "Withdraw", amount);
                    }
                    //if the amount it more than 300
                    else
                    { 
                        //output the below message
                        System.out.println("The maximum daily withdrawal for a Savings account is ?300. This transaction has been cancelled");
                    }
                    break;
                    //when the account type is Business
                case "Business":
                    //if the amount is less than or equal to 500
                    if (amount <= 500)
                    { 
                        //call the withdraw method
                        account.withdraw(amount);
                        //add this to the transaction history
                        account.addTransaction(new Date(), "Withdraw", amount);
                    }
                    //if the amount is more than 500
                    else
                    {
                        //output the below message
                        System.out.println("The maximum daily withdrawal for a Business account is ?500. This transaction has been cancelled");
                    }
                    break;                          
            }
        }       
    }
    
    //method declared
    public void DoTransfer()
    {
        //ask the user for input
        System.out.println("Enter account Number to transfer money FROM:"); 
        //takes the user input and stores it as an int                
        int from_acc_number = Integer.parseInt(input.nextLine());
        //retrieving the account number
        BaseAccount fromAccount = getAccount(from_acc_number);
        //if the from account is equal to null
        if(fromAccount==null)
            {
                //output the below message
                System.out.println("Account does not exist");
                return;
            }
        else
            {
                //Write instruction to the user 
                System.out.println("Enter transfer amount: ");                 
                //take the user input and store it as a double               
                amount = Double.parseDouble(input.nextLine());                 
                //if the balance is more than or equal to the amount
                if(fromAccount.get_balance() >= amount) 
                {             
                    //call the withdraw method
                    fromAccount.withdraw(amount); 
                    //add the transaction to the history
                    fromAccount.addTransaction(new Date(), "Transfer", amount);
                } 
                //if the balance is less than the amount
                else 
                { 
                    //output the below message
                    System.out.println("There are insufficient funds to make this transfer"); 
                    return; 
            } 
    } 

        //output the below message
        System.out.println("Enter account Number to transfer money TO: "); 
        //takes the user input and stores it as an int                 
        int to_acc_number = Integer.parseInt(input.nextLine());  
        //retrieves the account number
        BaseAccount toAccount = getAccount(to_acc_number);
        //if the account is equal to null
        if(toAccount == null)
        {
            //call the rollback method
            fromAccount.rollback();
            //output the below message
            System.out.println("Account doesnt exist, rolling back");
        }
        //if the account is equal to true
        else
        {           
                //call the deposit method
                toAccount.deposit(amount);
                //add this to the transaction history
                toAccount.addTransaction(new Date(), "Transfer", amount);
                //output the below message
                System.out.println("Payment has been successfully transferred"); 
        }
                                 
    }
    
    //declare the method
    public void DoOverdraft()
    {
        //call the AskForAccount method to get the account        
        BaseAccount account = AskForAccount();
        //if the returned account is equal to null
        if(account == null)
        {
            //output the below message
            System.out.println("Account not found");  
        }
        //if the returned account is equal to true
        else 
        {
            //if the account already has an overdraft
            if(account.has_overdraft())
            {
                //output the below message
                System.out.println("To change press 1 or to revoke press 2");
                //store the input of the user in the below variable
                int overdraftAction = Integer.parseInt(input.nextLine());
                //switch to action according to the number input
                switch(overdraftAction)
                {
                    //if 1 was input
                    case 1: 
                        //output the below message
                        System.out.println("How much would you like to change your overdraft to?");
                        //store the input as the below variable
                        int limit = Integer.parseInt(input.nextLine());
                        //call the below method to change the overdraft
                        account.change_overdraft(limit);
                        break;
                    //if 2 was input
                    case 2:
                        //call the below method to revoke the overdraft
                        account.revoke_overdraft();
                        break;
                }
            }
            //if the account doesn't have an overdraft
            else
            {
                //if the account is allowed an overdraft
                if(account.is_allowed_overdraft())
                {
                    //ask for the user input
                    System.out.println("How much would you like your overdraft to be?");
                    //user input stored in the below variable
                    int limit = Integer.parseInt(input.nextLine());
                    //call the method to grant the overdraft
                    account.grant_overdraft(limit);
                }
            }
        }
    }
    
    //declare the method
    public void AddAccountHolder()
    {
        //call the below method to get the account number
        BaseAccount account = AskForAccount();
        //output the below message
        System.out.println("Enter Customer first and Last Name");
        //store the input as the below variable 
        name = input.nextLine(); 
        //if the account is not equal to null (true)
        if(account != null)
        {       
            //call the below method
            account.AddAccHolder(name, acc_num);                         
            return;
        }  
        //if the account is equal to null output the below
        System.out.println("Account does not exist");
    }
    
    //declare the method
    private void ShowAccounts()
    {
        //call the below method to get the account number
        BaseAccount account = AskForAccount();
        //if the returned account is not equal to null
        if(account != null)
        {
            //call the below methods and output the results
            System.out.println(account.get_acc_type()+" "+account.getHolderName()+" "+account.get_balance());                         
        }         
    }
    
    //declare the method
    private void ViewTransaction()
    {
        //call the below method to get the account number
        BaseAccount account = AskForAccount();
        //declare the below variable with the value of 0
        int j=0;
        //if the account is not equal to null
        if(account != null)
        {    
            //while the the account Transaction size is less than the value of the j variable
            while(j<account.getTransactions().size())
            {
                //call the below methods and output the below message with the variables in 
                System.out.println(account.getTransactions().get(j).getType()+" "+account.getTransactions().get(j).getDate()+" "+account.getTransactions().get(j).getAmount());
                //add 1 to the below variable
                j++;
            }
        }        
    }
    
    //declare the method 
    private void DoLoan() 
    {
        //call the below method to get the account number
        BaseAccount account = AskForAccount();
        //if the account returned is not equal to null
        if(account != null)
        {
            //output the below message
            System.out.println("How much loan do you require?");
            //store the user input as the below variable
            double loan_amount = Integer.parseInt(input.nextLine());
            //call the below method from the master account
            master.withdraw(loan_amount);
            //call the below method to issue the loan
            account.issue_loan(loan_amount);
            //call the below method to add the loan
            account.addLoan(new Date(), loan_amount); 
        }
    }
    
    //declare the method
    private BaseAccount AskForAccount()
    {
        //output the below input message
        System.out.println("Enter Account Number");   
        //store the input as the below variable
        acc_num = Integer.parseInt(input.nextLine());
        //call the below method
        return getAccount(acc_num);
    }
}

//declare the class
public class SDAssignment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
                    new Menu();
    } 
}


