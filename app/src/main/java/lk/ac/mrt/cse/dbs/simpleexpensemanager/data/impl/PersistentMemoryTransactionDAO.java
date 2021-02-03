package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;



/**
 *
 Created by Piraveen.


 */




public class PersistentMemoryTransactionDAO implements TransactionDAO {

    //transaction table


    private static final String ACCOUNT_TRANSACTION_EXPENSE_TYPE = "expenseType";
    private static final String ACCOUNT_TRANSACTION_AMOUNT = "amount";
    private static final String ACCOUNT_TRANSACTION_DATE = "date";
    private static final String ACCOUNT_NUMBER = "accountNo";


    SQLiteDatabase sqLiteDatabase;

    public PersistentMemoryTransactionDAO(SQLiteDatabase db){

      this.sqLiteDatabase =db;
    }


    @Override
    //insert values into transaction table
    public void logTransaction(Date d, String accNo, ExpenseType et, double amt){



            String insertQuery = "INSERT INTO Account_Transaction (accountNo,expenseType,amount,date) VALUES (?,?,?,?)";
            SQLiteStatement statement = sqLiteDatabase.compileStatement(insertQuery);

            statement.bindString(1,accNo);
            statement.bindLong(2,(et == ExpenseType.EXPENSE) ? 0 : 1);
            statement.bindDouble(3,amt);
            statement.bindLong(4,d.getTime());

            statement.executeInsert();



    }

    @Override
    //get all transactions
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();

        String transactionDetailSelectQuery = "SELECT * FROM Account_Transaction";
        Cursor rawQuery = sqLiteDatabase.rawQuery(transactionDetailSelectQuery, null);

        try {
            if (rawQuery.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(rawQuery.getLong(rawQuery.getColumnIndex(ACCOUNT_TRANSACTION_DATE))),
                            rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_NUMBER)),
                            (rawQuery.getInt(rawQuery.getColumnIndex(ACCOUNT_TRANSACTION_EXPENSE_TYPE)) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            rawQuery.getDouble(rawQuery.getColumnIndex(ACCOUNT_TRANSACTION_AMOUNT)));




                    transactions.add(trans);

                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        }


        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {



        List<Transaction> transactionArrayList = new ArrayList<>();

        String transDetailSelectQuery = "SELECT * FROM Account_Transaction LIMIT"+limit;


        Cursor rawQuery = sqLiteDatabase.rawQuery(transDetailSelectQuery, null);


            if (rawQuery.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(rawQuery.getLong(rawQuery.getColumnIndex(ACCOUNT_TRANSACTION_DATE))),
                            rawQuery.getString(rawQuery.getColumnIndex(ACCOUNT_NUMBER)),
                            (rawQuery.getInt(rawQuery.getColumnIndex(ACCOUNT_TRANSACTION_EXPENSE_TYPE)) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            rawQuery.getDouble(rawQuery.getColumnIndex(ACCOUNT_TRANSACTION_AMOUNT)));


                    transactionArrayList.add(trans);

                } while (rawQuery.moveToNext());
            }

        return  transactionArrayList;
    }





}

