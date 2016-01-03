package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Model for user bets. A user bet is a bet which the gambler sets for a match. He choose a match bet from a match and
 * set an amount to this bet. For example, a user bet is: gambler x sets 10 CHF to the bet that "France wins against Switzerland".
 * Is a jpa entity (jpa annotations) to get, store and update userBet in the database.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	27.12.2015	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 27.12.2015
 */
@Entity
public class UserBet {

    public static final String TABLE_NAME = "UserBet";
    public static final String COLUMN_NAME_USER_ID = "userId";
    public static final String COLUMN_NAME_MATCH_BET_ID = "matchBetId";
    public static final String COLUMN_NAME_AMOUNT = "amount";
    public static final String COLUMN_NAME_ENTRY_DATE_TIME = "entryDateTime";

    @GeneratedValue
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = COLUMN_NAME_USER_ID)
    private User userId;
    @ManyToOne
    @JoinColumn(name = COLUMN_NAME_MATCH_BET_ID)
    private MatchBet matchBetId;
    @Basic
    @Column(name = COLUMN_NAME_AMOUNT)
    private Double amount;
    @Basic
    @Column(name = COLUMN_NAME_ENTRY_DATE_TIME)
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public MatchBet getMatchBetId() {
        return matchBetId;
    }

    public void setMatchBetId(MatchBet matchBetId) {
        this.matchBetId = matchBetId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(Date entryDateTime) {
        this.entryDateTime = entryDateTime;
    }
}
