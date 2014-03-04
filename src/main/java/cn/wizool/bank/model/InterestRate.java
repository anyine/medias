package cn.wizool.bank.model;

/**
 * 利率牌
 */

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

//@Entity
public class InterestRate {

	@Id
	private String id;

	@Column
	private Date birth;

	@Column
	private String regularThreeMonth;// 定期整存整取 三月

	@Column
	private String regularSixMonth;// 定期整存整取 六月

	@Column
	private String regularOneYear;// 定期整存整取 一年

	@Column
	private String regularTwoYear;// 定期整存整取 两年

	@Column
	private String regularThreeYear;// 定期整存整取 三年

	@Column
	private String regularFiveYear;// 定期整存整取 六年

	@Column
	private String current;// 活期

	@Column
	private String agreementDeposit;// 协定存款

	@Column
	private String accessOneYear;// 零整存取 一年

	@Column
	private String accessThreeYear;// 零整存取 三年

	@Column
	private String accessFiveYear;// 零整存取 五年

	@Column
	private String educationOneYear;// 教育储蓄 一年

	@Column
	private String educationThreeYear;// 教育储蓄 三年

	@Column
	private String educationSixYear;// 教育储蓄 六年

	@Column
	private String noticeDepositOneDay;// 通知存款 一天

	@Column
	private String noticeDepositSevenDay;// 通知存款 七天

	@Column
	private String discount;// 折扣

	@Column
	private String shortLoansThreeMonth;// 短期贷款 三月以内

	@Column
	private String shorLoansSixMonth;// 短期贷款 六月以内

	@Column
	private String longLoansSixMonth;// 长期贷款 六月至一年

	@Column
	private String longLoansOneToThree;// 长期贷款 一年至三年

	@Column
	private String longLoansThreeToFive;// 长期贷款 三年至五年

	@Column
	private String longLoansFiveMore;// 长期贷款 五年以上

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegularThreeMonth() {
		return regularThreeMonth;
	}

	public void setRegularThreeMonth(String regularThreeMonth) {
		this.regularThreeMonth = regularThreeMonth;
	}

	public String getRegularSixMonth() {
		return regularSixMonth;
	}

	public void setRegularSixMonth(String regularSixMonth) {
		this.regularSixMonth = regularSixMonth;
	}

	public String getRegularOneYear() {
		return regularOneYear;
	}

	public void setRegularOneYear(String regularOneYear) {
		this.regularOneYear = regularOneYear;
	}

	public String getRegularTwoYear() {
		return regularTwoYear;
	}

	public void setRegularTwoYear(String regularTwoYear) {
		this.regularTwoYear = regularTwoYear;
	}

	public String getRegularThreeYear() {
		return regularThreeYear;
	}

	public void setRegularThreeYear(String regularThreeYear) {
		this.regularThreeYear = regularThreeYear;
	}

	public String getRegularFiveYear() {
		return regularFiveYear;
	}

	public void setRegularFiveYear(String regularFiveYear) {
		this.regularFiveYear = regularFiveYear;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getAgreementDeposit() {
		return agreementDeposit;
	}

	public void setAgreementDeposit(String agreementDeposit) {
		this.agreementDeposit = agreementDeposit;
	}

	public String getAccessOneYear() {
		return accessOneYear;
	}

	public void setAccessOneYear(String accessOneYear) {
		this.accessOneYear = accessOneYear;
	}

	public String getAccessThreeYear() {
		return accessThreeYear;
	}

	public void setAccessThreeYear(String accessThreeYear) {
		this.accessThreeYear = accessThreeYear;
	}

	public String getAccessFiveYear() {
		return accessFiveYear;
	}

	public void setAccessFiveYear(String accessFiveYear) {
		this.accessFiveYear = accessFiveYear;
	}

	public String getEducationOneYear() {
		return educationOneYear;
	}

	public void setEducationOneYear(String educationOneYear) {
		this.educationOneYear = educationOneYear;
	}

	public String getEducationThreeYear() {
		return educationThreeYear;
	}

	public void setEducationThreeYear(String educationThreeYear) {
		this.educationThreeYear = educationThreeYear;
	}

	public String getEducationSixYear() {
		return educationSixYear;
	}

	public void setEducationSixYear(String educationSixYear) {
		this.educationSixYear = educationSixYear;
	}

	public String getNoticeDepositOneDay() {
		return noticeDepositOneDay;
	}

	public void setNoticeDepositOneDay(String noticeDepositOneDay) {
		this.noticeDepositOneDay = noticeDepositOneDay;
	}

	public String getNoticeDepositSevenDay() {
		return noticeDepositSevenDay;
	}

	public void setNoticeDepositSevenDay(String noticeDepositSevenDay) {
		this.noticeDepositSevenDay = noticeDepositSevenDay;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getShortLoansThreeMonth() {
		return shortLoansThreeMonth;
	}

	public void setShortLoansThreeMonth(String shortLoansThreeMonth) {
		this.shortLoansThreeMonth = shortLoansThreeMonth;
	}

	public String getShorLoansSixMonth() {
		return shorLoansSixMonth;
	}

	public void setShorLoansSixMonth(String shorLoansSixMonth) {
		this.shorLoansSixMonth = shorLoansSixMonth;
	}

	public String getLongLoansSixMonth() {
		return longLoansSixMonth;
	}

	public void setLongLoansSixMonth(String longLoansSixMonth) {
		this.longLoansSixMonth = longLoansSixMonth;
	}

	public String getLongLoansOneToThree() {
		return longLoansOneToThree;
	}

	public void setLongLoansOneToThree(String longLoansOneToThree) {
		this.longLoansOneToThree = longLoansOneToThree;
	}

	public String getLongLoansThreeToFive() {
		return longLoansThreeToFive;
	}

	public void setLongLoansThreeToFive(String longLoansThreeToFive) {
		this.longLoansThreeToFive = longLoansThreeToFive;
	}

	public String getLongLoansFiveMore() {
		return longLoansFiveMore;
	}

	public void setLongLoansFiveMore(String longLoansFiveMore) {
		this.longLoansFiveMore = longLoansFiveMore;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

}
