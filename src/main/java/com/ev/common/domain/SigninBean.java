package com.ev.common.domain;

import java.util.Date;

/**
 * 统计用bean类
 * 
 * 
 */
public class SigninBean {
	private String lessonsName;
	private String memberName;
	private String cellphone;
	private String company;
	private String cardNumber;
	private String cardType;
	private String serviceName;
	private String orgName;
	private String orgLinkMan;
	private Date signintime;
	private Long typeId;
	private Long serviceId;
	private Long orgId;

	private Long cardId;
	private int status;
	private Date zctime;
	private int number;

	private Date lessonsTime;

	public String getLessonsName() {
		return lessonsName;
	}

	public void setLessonsName(String lessonsName) {
		this.lessonsName = lessonsName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgLinkMan() {
		return orgLinkMan;
	}

	public void setOrgLinkMan(String orgLinkMan) {
		this.orgLinkMan = orgLinkMan;
	}

	public Date getSignintime() {
		return signintime;
	}

	public void setSignintime(Date signintime) {
		this.signintime = signintime;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getZctime() {
		return zctime;
	}

	public void setZctime(Date zctime) {
		this.zctime = zctime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public Date getLessonsTime() {
		return lessonsTime;
	}

	public void setLessonsTime(Date lessonsTime) {
		this.lessonsTime = lessonsTime;
	}
}
