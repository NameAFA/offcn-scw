package com.offcn.project.controller;


import com.offcn.dycommon.response.AppResponse;
import com.offcn.dycommon.utils.OssTemplate;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="项目基本功能模块（文件上传、项目信息获取等）")
@Slf4j
@RequestMapping("/project")
@RestController
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;


    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("多文件上传")
    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> uploadFile(@RequestParam("file") MultipartFile[] files) throws IOException {
        Map<String,Object> map = new HashMap<>();
        List<String> urls = new ArrayList<>();

        if (files!=null && files.length>0)
        {
            for (MultipartFile ff:files) {
                if (!ff.isEmpty()){
                    String url = ossTemplate.upload(ff.getInputStream(),ff.getOriginalFilename());
                    urls.add(url);
                }
            }
            map.put("urls",urls);
            if (log.isDebugEnabled()){
                log.debug("ossTemplate信息：{},文件上传成功访问路径{}"+ossTemplate+urls);
            }
        }
        return AppResponse.ok(map);

    }



    @ApiOperation("获取项目回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable("projectId")Integer projectId) {

        List<TReturn> returns = projectInfoService.getProjectReturns(projectId);
        return AppResponse.ok(returns);
    }


    @ApiOperation("根据回报id查询回报对象")
    @GetMapping("/findReturnInfo/{returnid}")
    public AppResponse<TReturn> findReturnInfo(@PathVariable("returnid") Integer returnid) {
        TReturn tReturn = projectInfoService.findReturnInfo(returnid);
        AppResponse<TReturn> ok = AppResponse.ok(tReturn);
        return ok;
    }


}
