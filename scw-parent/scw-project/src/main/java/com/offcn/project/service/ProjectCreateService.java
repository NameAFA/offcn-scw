package com.offcn.project.service;

import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {
    /**
     * 初始化项目（阅读并同意协议）
     * @param memberId （企业用户的id）
     * @return  项目的临时token
     */
    public String initCreateProject(Integer memberId);
    public void saveProjectInfo(ProjectStatusEnume status , ProjectRedisStorageVo redisStorageVo);

}
