package com.TransactionService.TransactionService.utils;

import java.util.Random;

public class ResponseUtils {
    public static final String USER_EXISTS_CODE = "001";
    public static final String USER_EXISTS_MESSAGE = "User with provided email already exists";

    public static final String SUCCESS = "002";
    public static final String USER_REGISTERED_SUCCESS = "User successfully registered";
    public static final String SUCCESS_MESSAGE = "Successfully done";
    public static final String USER_NOT_FOUND_CODE = " 003";
    public static final  String USER_NOT_FOUND_MESSAGE = "User does not exist";
    public static final String SUCCESSFUL_TRANSACTION_CODE = "004";
    public static final String ACCOUNT_CREDITED = "Account has been credited";
    public static final String ACCOUNT_DEBITED = "Account has been debited";
    public static final String UNSUCCESSFUL_TRANSACTION_CODE = "005";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds";

    public static final String USER_DELETED_CODE = "006";
    public static final String USER_DELETED_MESSAGE = "User successfully deleted";






    public static final int LENGTH_OF_ACCOUNT_NUMBER = 10;

    public static String generateAccountNumber(int len){
        String accountNumber = "";
        int x;
        char[] stringchars = new char[len];
        for(int i =0; i < len; i++)
        {

            Random random = new Random();
            x = random.nextInt(9);

            stringchars[i] = Integer.toString(x).toCharArray()[0];

        }
        accountNumber = new String(stringchars);
        return accountNumber.trim();
    }

    public static String truncatedAccountNumber(String accountNumber){
        String firstThreeDigits = accountNumber.substring(0,3);
        String lastThreeDigits = accountNumber.substring(accountNumber.length()-3);
        StringBuilder truncatedAccountNumber = new StringBuilder(firstThreeDigits);

        for(int i = 3; i <accountNumber.length()-3; i++){
            truncatedAccountNumber.append('*');
        }

        truncatedAccountNumber.append(lastThreeDigits);
        return truncatedAccountNumber.toString();
    }


//    public static void main(String[] args) {
//        System.out.println(generateAccountNumber(LENGTH_OF_ACCOUNT_NUMBER));
//        System.out.println(truncatedAccountNumber(generateAccountNumber(LENGTH_OF_ACCOUNT_NUMBER)));
//    }
    //write a method that adds star symbol in between the accountNumber. this would serve when you are returning acctNumber after any transaction
    //something like this 2115899773 would be shrunk to 211**773
}
