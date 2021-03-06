package it.coluccia.shareit.dto.items.shareitdb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Items implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.name
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.description
     *
     * @mbggenerated
     */
    private String description;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.image_uri
     *
     * @mbggenerated
     */
    private String imageUri;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.price_credit
     *
     * @mbggenerated
     */
    private BigDecimal priceCredit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.category_code
     *
     * @mbggenerated
     */
    private Integer categoryCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.active
     *
     * @mbggenerated
     */
    private Short active;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.owner_id
     *
     * @mbggenerated
     */
    private Integer ownerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.creation_date
     *
     * @mbggenerated
     */
    private Date creationDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.publish_date
     *
     * @mbggenerated
     */
    private Date publishDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.sell_date
     *
     * @mbggenerated
     */
    private Date sellDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.longitude
     *
     * @mbggenerated
     */
    private BigDecimal longitude;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column items.latitude
     *
     * @mbggenerated
     */
    private BigDecimal latitude;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table items
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.id
     *
     * @return the value of items.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.id
     *
     * @param id the value for items.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.name
     *
     * @return the value of items.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.name
     *
     * @param name the value for items.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.description
     *
     * @return the value of items.description
     *
     * @mbggenerated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.description
     *
     * @param description the value for items.description
     *
     * @mbggenerated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.image_uri
     *
     * @return the value of items.image_uri
     *
     * @mbggenerated
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.image_uri
     *
     * @param imageUri the value for items.image_uri
     *
     * @mbggenerated
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri == null ? null : imageUri.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.price_credit
     *
     * @return the value of items.price_credit
     *
     * @mbggenerated
     */
    public BigDecimal getPriceCredit() {
        return priceCredit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.price_credit
     *
     * @param priceCredit the value for items.price_credit
     *
     * @mbggenerated
     */
    public void setPriceCredit(BigDecimal priceCredit) {
        this.priceCredit = priceCredit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.category_code
     *
     * @return the value of items.category_code
     *
     * @mbggenerated
     */
    public Integer getCategoryCode() {
        return categoryCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.category_code
     *
     * @param categoryCode the value for items.category_code
     *
     * @mbggenerated
     */
    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.active
     *
     * @return the value of items.active
     *
     * @mbggenerated
     */
    public Short getActive() {
        return active;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.active
     *
     * @param active the value for items.active
     *
     * @mbggenerated
     */
    public void setActive(Short active) {
        this.active = active;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.owner_id
     *
     * @return the value of items.owner_id
     *
     * @mbggenerated
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.owner_id
     *
     * @param ownerId the value for items.owner_id
     *
     * @mbggenerated
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.creation_date
     *
     * @return the value of items.creation_date
     *
     * @mbggenerated
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.creation_date
     *
     * @param creationDate the value for items.creation_date
     *
     * @mbggenerated
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.publish_date
     *
     * @return the value of items.publish_date
     *
     * @mbggenerated
     */
    public Date getPublishDate() {
        return publishDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.publish_date
     *
     * @param publishDate the value for items.publish_date
     *
     * @mbggenerated
     */
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.sell_date
     *
     * @return the value of items.sell_date
     *
     * @mbggenerated
     */
    public Date getSellDate() {
        return sellDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.sell_date
     *
     * @param sellDate the value for items.sell_date
     *
     * @mbggenerated
     */
    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.longitude
     *
     * @return the value of items.longitude
     *
     * @mbggenerated
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.longitude
     *
     * @param longitude the value for items.longitude
     *
     * @mbggenerated
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column items.latitude
     *
     * @return the value of items.latitude
     *
     * @mbggenerated
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column items.latitude
     *
     * @param latitude the value for items.latitude
     *
     * @mbggenerated
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}