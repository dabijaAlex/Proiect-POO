package org.poo.commands.TransactionCommands.splitPayment;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.transactions.SplitPaymentEqualErrorTransaction;
import org.poo.transactions.SplitPaymentEqualTransaction;
import org.poo.transactions.SplitPaymentErrorTransaction;
import org.poo.transactions.SplitPaymentTransaction;

import java.util.ArrayList;

@Getter @Setter
public class SplitPaymentDB {
    @Getter @Setter
    private static ArrayList<SingleSplitPayment> splitPaymentsList = new ArrayList<>();

    /**
     * add a split payment instance to the database
     * @param payment
     */
    public static void addSplitPaymentToList(final SingleSplitPayment payment) {
        splitPaymentsList.add(payment);
    }

    /**
     * remove a split payment instance from database as it was processed
     * @param payment
     */
    public static void removeSplitPaymentFromList(final SingleSplitPayment payment) {
        splitPaymentsList.remove(payment);
    }


    /**
     * get the first split payment instance where all accepted payment and proceed
     *      with payment mechanism
     */
    public static void checkAllAccepted() {
        for (SingleSplitPayment payment : splitPaymentsList) {
            if (payment.checkAllAccepted()) {
                removeSplitPaymentFromList(payment);
                proceedWithPayment(payment);
                return;
            }
        }
    }

    /**
     * the mechanism that checks all accounts have the necessary amount of money
     * if all have money, a transaction of successful payment will be added
     *      else, a transaction which specifies the account that didn t have the amount
     *      will be added
     * @param payment
     */
    private static void proceedWithPayment(final SingleSplitPayment payment) {
        for (Pair pair: payment.getEachAccountAndPayment()) {
            Account account = pair.getAccount();
            User user = account.getUserRef();
            double commission = user.getServicePlan().getCommissionAmount(pair.getAmountToPay(),
                    account.getCurrency());
            if (account.getBalance() < pair.getAmountToPay() + commission) {
                //  an account didnt have the money so loop through all accounts to add transaction
                for (Pair pairFailed: payment.getEachAccountAndPayment()) {
                    Account accountFailed = pairFailed.getAccount();
                    if (payment.getType().equals("equal")) {
                        accountFailed.addTransaction(
                                new SplitPaymentEqualErrorTransaction(payment.getTimestamp(),
                                        payment.getDescription(), payment.getInvolvedAccounts(),
                                        payment.getCurrency(), account.getIban(),
                                        payment.getType(), payment.getAmountEachOriginal(),
                                        payment.getAmount() / payment.getNrAccounts()));
                    } else {
                        accountFailed.addTransaction(
                                new SplitPaymentErrorTransaction(payment.getTimestamp(),
                                        payment.getDescription(), payment.getInvolvedAccounts(),
                                        payment.getCurrency(), account.getIban(),
                                        payment.getType(), payment.getAmountEachOriginal()));
                    }
                }
                return;
            }
        }

        //  all accounts had money. Take specific amount from each acc and add transaction
        for (Pair pair: payment.getEachAccountAndPayment()) {
            Account account = pair.getAccount();
            account.setBalance(account.getBalance() - pair.getAmountToPay());
            if (payment.getType().equals("equal")) {
                account.addTransaction(new SplitPaymentEqualTransaction(payment.getTimestamp(),
                        payment.getDescription(), payment.getInvolvedAccounts(),
                        payment.getCurrency(), payment.getType(),
                        payment.getAmountEachOriginal(),
                        payment.getAmount() / payment.getNrAccounts()));
            } else {
                account.addTransaction(new SplitPaymentTransaction(payment.getTimestamp(),
                        payment.getDescription(), payment.getInvolvedAccounts(),
                        payment.getCurrency(), payment.getType(),
                        payment.getAmountEachOriginal()));
            }
        }
    }

}
