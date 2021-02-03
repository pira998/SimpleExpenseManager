package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 *
 Created by Piraveen.


 */




public class PersistentExpenseManager extends ExpenseManager{


    Context context;
    private static final String ACCOUNT_TABLE_NAME = "Account";
    private static final String ACCOUNT_NUMBER = "accountNo";
    private static final String ACCOUNT_BANK_NAME = "bankName";
    private static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    private static final String ACCOUNT_BALANCE = "balance";
    private static final String ACCOUNT_TRANSACTION_TABLE_NAME = "Account_Transaction";
    private static final String ACCOUNT_TRANSACTION_ID = "Transaction_id";
    private static final String ACCOUNT_TRANSACTION_EXPENSE_TYPE = "expenseType";
    private static final String ACCOUNT_TRANSACTION_AMOUNT = "amount";
    private static final String ACCOUNT_TRANSACTION_DATE = "date";





    public PersistentExpenseManager(Context context1)  {
        context=context1;
        try {

            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }
    @Override

    public void setup() throws ExpenseManagerException {

        SQLiteDatabase db = context.openOrCreateDatabase("180476L", context.MODE_PRIVATE, null);

//         create the databases.
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ACCOUNT_TABLE_NAME+" ( " +
                ACCOUNT_NUMBER + " VARCHAR PRIMARY KEY, " +
                ACCOUNT_BANK_NAME+" VARCHAR, " +
                ACCOUNT_HOLDER_NAME +" VARCHAR, " +
                ACCOUNT_BALANCE+ " REAL" +
                " );");



        db.execSQL("CREATE TABLE IF NOT EXISTS "+ ACCOUNT_TRANSACTION_TABLE_NAME +" (" +
                 ACCOUNT_TRANSACTION_ID + " INTEGER PRIMARY KEY, " +
                 ACCOUNT_NUMBER+ " VARCHAR, " +
                 ACCOUNT_TRANSACTION_EXPENSE_TYPE + " INT, " +
                 ACCOUNT_TRANSACTION_AMOUNT+ " REAL, " +
                ACCOUNT_TRANSACTION_DATE + " DATE, "  +
                "FOREIGN KEY (" + ACCOUNT_NUMBER +") REFERENCES "+ ACCOUNT_TABLE_NAME +"(" + ACCOUNT_NUMBER +")" +
                ");");






        PersistentMemoryAccountDAO accountDAO = new PersistentMemoryAccountDAO(db);
        PersistentMemoryTransactionDAO transactionDAO = new PersistentMemoryTransactionDAO(db);
        setAccountsDAO(accountDAO);

        setTransactionsDAO(transactionDAO);

        /*** End ***/
    }
}
