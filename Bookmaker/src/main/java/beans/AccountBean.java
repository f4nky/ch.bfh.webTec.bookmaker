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
 * TODO
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

    /**
     *
     * @return
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

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    /**
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
