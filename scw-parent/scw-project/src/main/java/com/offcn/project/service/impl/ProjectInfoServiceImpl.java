package com.offcn.project.service.impl;

import com.offcn.project.mapper.TReturnMapper;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.pojo.TReturnExample;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {
    private TReturnMapper returnMapper;
    @Override
    public List<TReturn> getProjectReturns(Integer projectId) {

        TReturnExample example = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(example);

    }

    @Override
    public TReturn findReturnInfo(Integer returnid) {
        return returnMapper.selectByPrimaryKey(returnid);
    }

}
