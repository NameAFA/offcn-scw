package com.offcn.project.mapper;

import com.offcn.project.pojo.TProject;
import com.offcn.project.pojo.TProjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TProjectMapper {
    int countByExample(TProjectExample example);

    int deleteByExample(TProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TProject record);

    int insertSelective(TProject record);

    List<TProject> selectByExample(TProjectExample example);

    TProject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TProject record, @Param("example") TProjectExample example);

    int updateByExample(@Param("record") TProject record, @Param("example") TProjectExample example);

    int updateByPrimaryKeySelective(TProject record);

    int updateByPrimaryKey(TProject record);
}