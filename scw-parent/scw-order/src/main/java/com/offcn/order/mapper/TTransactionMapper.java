package com.offcn.order.mapper;

import com.offcn.order.pojo.TTransaction;
import com.offcn.order.pojo.TTransactionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TTransactionMapper {
    int countByExample(TTransactionExample example);

    int deleteByExample(TTransactionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TTransaction record);

    int insertSelective(TTransaction record);

    List<TTransaction> selectByExample(TTransactionExample example);

    TTransaction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TTransaction record, @Param("example") TTransactionExample example);

    int updateByExample(@Param("record") TTransaction record, @Param("example") TTransactionExample example);

    int updateByPrimaryKeySelective(TTransaction record);

    int updateByPrimaryKey(TTransaction record);
}