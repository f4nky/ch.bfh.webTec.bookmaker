package beans;

import dao.UserDao;
import helpers.LanguageHelper;
import model.User;
import validators.AccountValidator;
import validators.ValidationFault;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This bean is used for actions concerning the account of a gambler.
 * Every gambler has an account where he can charge money with a credit card transaction. When a gambler bets money, the
 * bet amount is withdraw from the account (saldo).
 *
 * <b>History:</b>
 * <pre>
 * 1.0	01.01.2016	Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 01.01.2016
 */
@ManagedBean
@RequestScoped
public class AccountBean {

    private String amount;
    private String cardHolderName;
    private String cardNumber;
    private String cvv;
    private byte expireMonth;
    private int expireYear;
    private Map<Integer,Integer> expireYearChoices;
    private String chargeErrorMessage = null;

    private static final String CHARGE_FORM_NAME = "chargeaccount";

    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public byte getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(byte expireMonth) {
        this.expireMonth = expireMonth;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getChargeErrorMessage() {
        return chargeErrorMessage;
    }

    public void setChargeErrorMessage(String chargeErrorMessage) {
        this.chargeErrorMessage = chargeErrorMessage;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    /**
     * Prepares a Map which contains the year figures of this and the next 5 years (2016, 2017, 2018, 2019, 2020).
     * The key and the value of every map item are the year figure (2016, usw).
     *
     * @return Map which contains the year figure of this and the next 5 years. Key and value are the year figure,
     *         (e.g 2016).
     * @since 01.01.2016
     */
    public Map<Integer,Integer> getExpireYearChoices() {
        if (expireYearChoices == null) {
            expireYearChoices = new LinkedHashMap<Integer,Integer>();
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = currentYear; i <= currentYear + 5; i++) {
                expireYearChoices.put(i, i);
            }
        }
        return expireYearChoices;
    }

    /**
     * Charges the account of the logged in user with the amount of this bean ({@link #amount}).
     * Before the account is charged, the amount is validated (see {@link AccountValidator}). If validation errors occurred,
     * the account is not charged and the validation faults are displayed.
     *
     * @throws IOException
     * @since 01.01.2016
     */
    public void chargeAccount() throws IOException {
        //Validate input data
        AccountValidator accountValidator = new AccountValidator();
        List<ValidationFault> validationFaults = accountValidator.validateChargeAccount(amount, cardHolderName, cardNumber, cvv, expireMonth, expireYear);

        if (validationFaults.size() == 0) {
            //No validation faults -> update saldo
            User userData = SessionBean.getUser();
            userData.setSaldo(userData.getSaldo() + Double.parseDouble(getAmount()));
            SessionBean.setUser(userData);
            UserDao.getInstance().updateSaldo(userData);
            chargeErrorMessage = null;
            navigationBean.redirectToUserAccount();
        } else {
            //Validation faults -> display validation faults
            chargeErrorMessage = LanguageHelper.createValidationFaultOutput(CHARGE_FORM_NAME, validationFaults);
        }
    }
}
