package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


/**
 *
 Created by Piraveen.


 */




import android.annotation.TargetApi;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentMemoryAccountDAO implements AccountDAO {


    private static final String ACCOUNT_NUMBER = "accountNo";
    private static final String ACCOUNT_BANK_NAME = "bankName";
    private static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    private static final String ACCOUNT_BALANCE = "balance";

    SQLiteDatabase sqLiteDatabase;

   public PersistentMemoryAccountDAO(SQLiteDatabase db)
   {
       this.sqLiteDatabase = db;

   }

    //add account
    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (accountNo,bankName,accountHolderName,balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);



        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());


        statement.executeInsert();
    }


    //get account numbers
    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();

        String accountNumbersSelectQuery = "SELECT accountNo FROM Account";


        Cursor rawQuery = sqLiteDatabase.rawQuery(accountNumbersSelectQuery, null);
        try {
            if (rawQuery.moveToFirst()) {
                do {

                    String test=rawQuery.getString(rawQuery
                            .getColumnIndex(ACCOUNT_NUMBER));

                    accountNumbers.add(test);

                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        }

        return accountNumbers;
    }

    @Override

    //fetch all account details
    public List<Account> getAccountsList() {
        List<Account> accountDetail = new ArrayList<>();

        String accountDetailSelectQuery = "SELECT * FROM Account";


        Cursor rawQuery = sqLiteDatabase.rawQuery(accountDetailSelectQuery, null);

        try {
            if (rawQuery.moveToFirst()) {
                do {
                    Account acc=new Account(
                            rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_NUMBER)),
                            rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_BANK_NAME)),
                            rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_HOLDER_NAME)),
                            rawQuery.getDouble(rawQuery.getColumnIndex(ACCOUNT_BALANCE)));




                    accountDetail.add(acc);

                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        }

        return accountDetail;

    }

    //get account details for a particular account number
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String accountSelectQuery = "SELECT * FROM Account where accountNo = "+accountNo;

        Cursor rawQuery = sqLiteDatabase.rawQuery(accountSelectQuery, null);
        if (rawQuery != null)
            rawQuery.moveToFirst();

        Account td = new Account(
                rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_NUMBER)),
                rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_BANK_NAME)),
                rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_HOLDER_NAME)),
                rawQuery.getDouble(rawQuery.getColumnIndex(ACCOUNT_BALANCE)));
        return td;

    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        String sql = "DELETE FROM Account WHERE accountNo = ?";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void updateBalance(String accountNo, ExpenseType expense_Type, double _amount) throws InvalidAccountException {

        String sql = "UPDATE Account SET balance = balance + ?";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        if(expense_Type == ExpenseType.EXPENSE){
            statement.bindDouble(1,-_amount);
        }else{
            statement.bindDouble(1,_amount);
        }

        statement.executeUpdateDelete();
    }
}

