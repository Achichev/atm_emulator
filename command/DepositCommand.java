package atm_emulator.command;

import atm_emulator.CashMachine;
import atm_emulator.ConsoleHelper;
import atm_emulator.CurrencyManipulator;
import atm_emulator.CurrencyManipulatorFactory;
import atm_emulator.exception.InterruptOperationException;

import java.util.ResourceBundle;

class DepositCommand implements Command {

    //resource bundle to deposit_en.properties
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "deposit");

    //requests the currency code, denomination and number of bills which the user wants to deposit into the ATM
    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));
        String currencyCode = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);
        
        while (true) {
            String[] money = ConsoleHelper.getValidTwoDigits(currencyCode);
            try {
                int denomination = Integer.parseInt(money[0]);
                int amount = Integer.parseInt(money[1]);
                manipulator.addAmount(denomination, amount);
                ConsoleHelper.writeMessage(String.format(res.getString("success.format"), (denomination * amount), currencyCode));
                break;
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage(res.getString("invalid.data"));
            }
        }
    }
}
