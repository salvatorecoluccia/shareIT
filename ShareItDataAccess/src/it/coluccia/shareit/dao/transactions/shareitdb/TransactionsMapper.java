package it.coluccia.shareit.dao.transactions.shareitdb;

import it.coluccia.shareit.dto.transactions.shareitdb.Transactions;
import it.coluccia.shareit.dto.transactions.shareitdb.TransactionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransactionsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int countByExample(TransactionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int deleteByExample(TransactionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int insert(Transactions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int insertSelective(Transactions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    List<Transactions> selectByExample(TransactionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    Transactions selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Transactions record, @Param("example") TransactionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Transactions record, @Param("example") TransactionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Transactions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transactions
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Transactions record);
}