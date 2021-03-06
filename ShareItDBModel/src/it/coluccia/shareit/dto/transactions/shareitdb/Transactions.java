package it.coluccia.shareit.dto.transactions.shareitdb;

import java.io.Serializable;

public class Transactions implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transactions.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transactions.id_user
     *
     * @mbggenerated
     */
    private Integer idUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transactions.id_item
     *
     * @mbggenerated
     */
    private Integer idItem;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transactions.message
     *
     * @mbggenerated
     */
    private String message;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table transactions
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transactions.id
     *
     * @return the value of transactions.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transactions.id
     *
     * @param id the value for transactions.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transactions.id_user
     *
     * @return the value of transactions.id_user
     *
     * @mbggenerated
     */
    public Integer getIdUser() {
        return idUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transactions.id_user
     *
     * @param idUser the value for transactions.id_user
     *
     * @mbggenerated
     */
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transactions.id_item
     *
     * @return the value of transactions.id_item
     *
     * @mbggenerated
     */
    public Integer getIdItem() {
        return idItem;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transactions.id_item
     *
     * @param idItem the value for transactions.id_item
     *
     * @mbggenerated
     */
    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transactions.message
     *
     * @return the value of transactions.message
     *
     * @mbggenerated
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transactions.message
     *
     * @param message the value for transactions.message
     *
     * @mbggenerated
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}