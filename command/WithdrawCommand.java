package atm_emulator.command;

import atm_emulator.CashMachine;
import atm_emulator.ConsoleHelper;
import atm_emulator.CurrencyManipulator;
import atm_emulator.CurrencyManipulatorFactory;
import atm_emulator.exception.InterruptOperationException;
import atm_emulator.exception.NotEnoughMoneyException;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WithdrawCommand implements Command {
    //resource bundle to withdraw_en.properties
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "withdraw");

    //requests the amount of money to withdraw and displays it if withdrawn or information about the lack of funds
    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));

        String currencyCode = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);

        while (true) {
            try {
                ConsoleHelper.writeMessage(res.getString("specify.amount"));
                String cash = ConsoleHelper.readString();
                if (cash == null) continue;
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(cash);
                if (m.matches()) {
                    int expectedAmount = Integer.parseInt(cash);
                    if (manipulator.isAmountAvailable(expectedAmount)) {
                        Map<Integer, Integer> withdrawAmount = manipulator.withdrawAmount(expectedAmount);
                        for (Map.Entry<Integer, Integer> entry: withdrawAmount.entrySet()) {
                            ConsoleHelper.writeMessage(String.format(res.getString("success.format"),
                                    entry.getKey() * entry.getValue(), currencyCode));
                        }
                        break;
                    } else {
                        ConsoleHelper.writeMessage(res.getString("not.enough.money"));
                    }
                } else {
                    ConsoleHelper.writeMessage(res.getString("specify.not.empty.amount"));
                }
            } catch (NotEnoughMoneyException e) {
                ConsoleHelper.writeMessage(res.getString("exact.amount.not.available"));
            }
        }

    }
}
