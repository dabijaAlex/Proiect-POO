package org.poo.app.splitPayment;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;
import org.poo.transactions.SplitPaymentEqualErrorTransaction;
import org.poo.transactions.SplitPaymentEqualTransaction;
import org.poo.transactions.SplitPaymentErrorTransaction;
import org.poo.transactions.SplitPaymentTransaction;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class SplitPaymentDB {
    @Getter @Setter
    private static ArrayList<SingleSplitPayment> SplitPaymentsList = new ArrayList<>();

    public static void addSplitPaymentToList(SingleSplitPayment payment) {
        SplitPaymentsList.add(payment);
    }

    public static void removeSplitPaymentFromList(SingleSplitPayment payment) {
        SplitPaymentsList.remove(payment);
    }

    public static void SetAcceptedPayment(Account account, final String type) {
        for (SingleSplitPayment payment : SplitPaymentsList) {
            Pair pair = null;
            if(payment.getType().equals(type)) {
                pair = payment.findAccount(account);

                if(pair != null && !pair.isStatus()) {
                    pair.setStatus(true);
                    return;
                }
            }
        }
    }

    public static SingleSplitPayment checkAllAccepted() {
        for (SingleSplitPayment payment : SplitPaymentsList) {
            if(payment.checkAllAccepted()) {
                removeSplitPaymentFromList(payment);
                proceedWithPayment(payment);
                return payment;
            }
        }
        return null;
    }

    private static void proceedWithPayment(SingleSplitPayment payment) {
        for(Pair pair: payment.getEachAccountAndPayment()) {
            Account account = pair.getAccount();
            double commission = account.getServicePlan().getCommissionAmount(pair.getAmountToPay(), account.getCurrency());
            if(account.getBalance() < pair.getAmountToPay() + commission) {
                for(Pair pairFailed: payment.getEachAccountAndPayment()) {
                    Account accountFailed = pairFailed.getAccount();
                    if(payment.getType().equals("equal")) {
                        accountFailed.addTransaction(new SplitPaymentEqualErrorTransaction(payment.getTimestamp(), payment.getDescription()
                                , payment.getInvolvedAccounts(), payment.getCurrency(), account.getIBAN(), payment.getType(), payment.getAmountEachOriginal(), payment.getAmount() / payment.getNrAccounts()));
                    } else {
                        accountFailed.addTransaction(new SplitPaymentErrorTransaction(payment.getTimestamp(), payment.getDescription()
                                , payment.getInvolvedAccounts(), payment.getCurrency(), account.getIBAN(), payment.getType(), payment.getAmountEachOriginal()));
                    }
                }
                payment.remove();
                return;
            }

        }

        for(Pair pair: payment.getEachAccountAndPayment()) {
            Account account = pair.getAccount();
            double commission = account.getServicePlan().getCommissionAmount(pair.getAmountToPay(), account.getCurrency());
            account.setBalance(account.getBalance() - pair.getAmountToPay() - commission);
            if(payment.getType().equals("equal")) {
                account.addTransaction(new SplitPaymentEqualTransaction(payment.getTimestamp(), payment.getDescription(),
                        payment.getInvolvedAccounts(), payment.getCurrency(), payment.getType(), payment.getAmountEachOriginal(), payment.getAmount() / payment.getNrAccounts()));
            } else {
                account.addTransaction(new SplitPaymentTransaction(payment.getTimestamp(), payment.getDescription(),
                        payment.getInvolvedAccounts(), payment.getCurrency(), payment.getType(), payment.getAmountEachOriginal()));
            }
        }
    }

}
