package com.offcn.project.controller;


import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.dycommon.vo.BaseVO;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "发布项目，包含四个步骤")
@RestController
@RequestMapping("/project")
public class ProjectCreateController {

    @Autowired
    ProjectCreateService projectCreateService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;



    @ApiOperation("发布项目的第一步：阅读并同意协议，初始化项目")
    @PostMapping("/init")
    public AppResponse init(BaseVO baseVO){

        String accessToken = baseVO.getAccessToken();

        if(StringUtils.isEmpty(accessToken)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("请携带用户身份令牌！");
            return fail;
        }

        String memberId = stringRedisTemplate.boundValueOps(accessToken).get();//memberId
        if(StringUtils.isEmpty(memberId)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("用户令牌不合法！");
            return fail;
        }

        //校验用户身份，只有企业用户才可以发布项目

        String projectToken = projectCreateService.initCreateProject(Integer.parseInt(memberId));

        AppResponse<String> ok = AppResponse.ok(projectToken);

        return ok;
    }

    @ApiOperation("发布项目的第二步：保存项目基本信息")
    @PostMapping("/saveBaseInfo")
    public AppResponse saveBaseInfo(ProjectBaseInfoVo baseInfoVo){


        String accessToken = baseInfoVo.getAccessToken();
        String projectToken = baseInfoVo.getProjectToken();

        if(StringUtils.isEmpty(accessToken)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("请携带用户身份令牌！");
            return fail;
        }

        String memberId = stringRedisTemplate.boundValueOps(accessToken).get();//memberId
        if(StringUtils.isEmpty(memberId)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("用户令牌不合法！");
            return fail;
        }

        // 从redis中获取临时保存的redisVo对象
        String key = ProjectConstant.TEMP_PROJECT_PREFIX + projectToken;
        String jsonString = stringRedisTemplate.boundValueOps(key).get();
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(jsonString, ProjectRedisStorageVo.class);

        // baseInfoVo ---> redisStorageVo
        BeanUtils.copyProperties(baseInfoVo,redisStorageVo);

        jsonString = JSON.toJSONString(redisStorageVo);
        stringRedisTemplate.boundValueOps(key).set(jsonString);

        AppResponse<Object> ok = AppResponse.ok(null);
        ok.setMsg("发布项目第二步，保存基本信息成功");

        return ok;
    }

    @ApiOperation("发布项目的第三步：保存回报信息")
    @PostMapping("/saveReturnInfo")
    public AppResponse saveReturnInfo(@RequestBody List<ProjectReturnVo> returnVos){

        String accessToken = returnVos.get(0).getAccessToken();
        String projectToken = returnVos.get(0).getProjectToken();

        if(StringUtils.isEmpty(accessToken)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("请携带用户身份令牌！");
            return fail;
        }

        String memberId = stringRedisTemplate.boundValueOps(accessToken).get();//memberId
        if(StringUtils.isEmpty(memberId)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("用户令牌不合法！");
            return fail;
        }

        // 先取出redisVo
        String key = ProjectConstant.TEMP_PROJECT_PREFIX + projectToken;
        String jsonString = stringRedisTemplate.boundValueOps(key).get();
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(jsonString, ProjectRedisStorageVo.class);

        // returnVos-->redisVo

        List<TReturn> projectReturns = new ArrayList<>();

        for (ProjectReturnVo returnVo : returnVos) {//遍历页面上录入的每一个回报
            // returnVo --> TReturn
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(returnVo,tReturn);
            projectReturns.add(tReturn);
        }

        redisStorageVo.setProjectReturns(projectReturns);

        // redisVo -->存入redis
        jsonString = JSON.toJSONString(redisStorageVo);
        stringRedisTemplate.boundValueOps(key).set(jsonString);

        AppResponse<Object> ok = AppResponse.ok(null);
        ok.setMsg("发布项目第三步，保存回报信息成功");
        return ok;
    }

    @ApiOperation("发布项目的第四步：提交审核或保存草稿")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ops",value = "项目的状态，提交审核或保存草稿") ,
            @ApiImplicitParam(name = "accessToken",value = "用户身份令牌") ,
            @ApiImplicitParam(name = "projectToken",value = "项目临时token")
    })
    @PostMapping("/submit")
    public AppResponse submit(String ops ,String accessToken , String projectToken ){

        if(StringUtils.isEmpty(accessToken)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("请携带用户身份令牌！");
            return fail;
        }

        String memberId = stringRedisTemplate.boundValueOps(accessToken).get();//memberId
        if(StringUtils.isEmpty(memberId)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("用户令牌不合法！");
            return fail;
        }

        if(StringUtils.isEmpty(projectToken)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("请传递项目临时的token");
            return fail;
        }

        if(StringUtils.isEmpty(ops)){
            AppResponse<Object> fail = AppResponse.fail(null);
            fail.setMsg("请传递ops参数");
            return fail;
        }

        // 保存草稿或者提交审核
        ProjectStatusEnume statusEnume = ops.equals("0") ? ProjectStatusEnume.DRAFT : ProjectStatusEnume.SUBMIT_AUTH;


        String key = ProjectConstant.TEMP_PROJECT_PREFIX + projectToken;
        String jsonString = stringRedisTemplate.boundValueOps(key).get();
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(jsonString, ProjectRedisStorageVo.class);

        projectCreateService.saveProjectInfo(statusEnume,redisStorageVo);

        //删除redis中项目的临时vo
        stringRedisTemplate.delete(key);

        AppResponse<Object> ok = AppResponse.ok(null);
        ok.setMsg("项目发布成功");

        return ok;
    }



}
