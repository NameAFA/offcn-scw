package com.offcn.user.mapper;

import com.offcn.user.pojo.TMemberCert;
import com.offcn.user.pojo.TMemberCertExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TMemberCertMapper {
    int countByExample(TMemberCertExample example);

    int deleteByExample(TMemberCertExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TMemberCert record);

    int insertSelective(TMemberCert record);

    List<TMemberCert> selectByExample(TMemberCertExample example);

    TMemberCert selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TMemberCert record, @Param("example") TMemberCertExample example);

    int updateByExample(@Param("record") TMemberCert record, @Param("example") TMemberCertExample example);

    int updateByPrimaryKeySelective(TMemberCert record);

    int updateByPrimaryKey(TMemberCert record);
}