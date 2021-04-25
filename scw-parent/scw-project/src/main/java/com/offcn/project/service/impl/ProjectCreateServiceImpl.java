package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {
    @Autowired
    TProjectMapper projectMapper;

    @Autowired
    TProjectImagesMapper imagesMapper;

    @Autowired
    TProjectTagMapper tagMapper;

    @Autowired
    TProjectTypeMapper typeMapper;

    @Autowired
    TReturnMapper returnMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String initCreateProject(Integer memberId) {
        String projectToken = UUID.randomUUID().toString().replace("-", "");
        //项目的临时对象
        ProjectRedisStorageVo initVo = new ProjectRedisStorageVo();
        initVo.setMemberid(memberId);
        initVo.setProjectToken(projectToken);
        //initVo转为json字符串
        String jsonString = JSON.toJSONString(initVo);
        //TEMP_PROJECT_PREFIX = "project:create:temp:";
        stringRedisTemplate.opsForValue()
                .set(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken, jsonString);

        return projectToken;//项目的临时的token
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStorageVo redisStorageVo) {

        // 1、项目的基本信息
        TProject project = new TProject();
//        project.setId();
        project.setName(redisStorageVo.getName());//项目名称
        project.setRemark(redisStorageVo.getRemark());
        project.setDay(redisStorageVo.getDay());
        project.setMemberid(redisStorageVo.getMemberid());//发起人的id
        project.setMoney(Long.parseLong(redisStorageVo.getMoney()+""));// 虽然属性名一样，但是类型不一样

//        BeanUtils.copyProperties(redisStorageVo,project);

        project.setStatus(status.getCode()+"");//项目的状态
        project.setDeploydate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        project.setSupportmoney(0L);//已经筹集的金额
        project.setSupporter(0);//支持者的人数
        project.setCompletion(0);// 0%
        project.setCreatedate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        project.setFollower(0);//关注者数量默认0

        projectMapper.insert(project);

        //添加项目基本信息成功后,返回主键
        Integer projectId = project.getId();

        // 2、项目的图片信息
        String headerImage = redisStorageVo.getHeaderImage();//头图 url地址
        List<String> detailsImage = redisStorageVo.getDetailsImage();//详情图

        TProjectImages images = new TProjectImages(null,projectId,headerImage, ProjectImageTypeEnume.HEADER.getCode());
        imagesMapper.insert(images);

        for (String url : detailsImage) {//遍历项目的多个详情图地址
            TProjectImages detailImages = new TProjectImages(null,projectId,url,ProjectImageTypeEnume.DETAILS.getCode());
            imagesMapper.insert(detailImages);
        }


        // 3、标签信息
        List<Integer> tagids = redisStorageVo.getTagids();//当前项目所属的标签的id集合

        for (Integer tagid : tagids) {
            TProjectTag projectTag = new TProjectTag(null,projectId,tagid);
            tagMapper.insert(projectTag);
        }


        // 4、分类信息

        List<Integer> typeids = redisStorageVo.getTypeids();//分类id
        for (Integer typeid : typeids) {
            TProjectType projectType = new TProjectType(null,projectId,typeid);
            typeMapper.insert(projectType);
        }


        // 5、项目回报信息

        List<TReturn> returnList = redisStorageVo.getProjectReturns();

        for (TReturn tReturn : returnList) {

            tReturn.setCount(tReturn.getPurchase());//当前回报剩余数
            tReturn.setProjectid(projectId);

            returnMapper.insert(tReturn);
        }
    }

}
