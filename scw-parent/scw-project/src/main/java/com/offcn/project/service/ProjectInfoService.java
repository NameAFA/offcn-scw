package com.offcn.project.service;

import com.offcn.project.pojo.TReturn;

import java.util.List;

public interface ProjectInfoService {
    List<TReturn> getProjectReturns(Integer projectId);
    public TReturn findReturnInfo(Integer returnid);
}
