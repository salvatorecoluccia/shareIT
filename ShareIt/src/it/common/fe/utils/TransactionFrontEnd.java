package it.common.fe.utils;

import java.math.BigDecimal;
import java.util.Date;

import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.dto.transactions.shareitdb.Transactions;

public class TransactionFrontEnd {
	Integer transactionId;
	Integer idBuyer;
	Integer idItem;
	String message;
	String itemName;
	String itemDescription;
	String imageUri;
	BigDecimal priceCredit;
	Integer categoryCode;
	BigDecimal longitude;
	BigDecimal latitude;
	Date sellDate;
	
	public TransactionFrontEnd(){
		
	}

	public TransactionFrontEnd(Items item, Transactions transaction) {
		this.transactionId = transaction.getId();
		this.idBuyer = transaction.getIdUser();
		this.idItem = transaction.getIdItem();
		this.message = transaction.getMessage();
		this.itemName = item.getName();
		this.itemDescription = item.getDescription();
		this.imageUri = item.getImageUri();
		this.priceCredit = item.getPriceCredit();
		this.categoryCode = item.getCategoryCode();
		this.longitude = item.getLongitude();
		this.latitude = item.getLatitude();
		this.sellDate = item.getSellDate();
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getIdBuyer() {
		return idBuyer;
	}

	public void setIdBuyer(Integer idBuyer) {
		this.idBuyer = idBuyer;
	}

	public Integer getIdItem() {
		return idItem;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public BigDecimal getPriceCredit() {
		return priceCredit;
	}

	public void setPriceCredit(BigDecimal priceCredit) {
		this.priceCredit = priceCredit;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public Date getSellDate() {
		return sellDate;
	}

	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}

}
