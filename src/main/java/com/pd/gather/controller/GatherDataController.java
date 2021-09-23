package com.pd.gather.controller;

import com.pd.gather.entity.GatherDataEntity;
import com.pd.gather.service.GatherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/12
 */

@Controller
public class GatherDataController {
    private Logger logger = LoggerFactory.getLogger(GatherDataController.class);

    @Autowired
    private GatherDataService gatherDataService;


    @RequestMapping("insertGather")
    public ModelAndView insertGather(GatherDataEntity gatherDataEntity) {
        logger.info("insert gather");
        int n = 0;
        ModelAndView mv = new ModelAndView();
        try {

            n = gatherDataService.insertGatherData(gatherDataEntity);
        } catch (Exception e) {
            mv.setViewName("redirect:error.jsp");
            e.printStackTrace();
        }
        if (n > 0) {
            mv.setViewName("redirect:findGatherByPage?pageIndex=1");
        }
        return mv;
    }

    @RequestMapping("findGatherByPage")
    public ModelAndView findGatherByPage(Integer pageIndex) {
        logger.info("find gather by page");
        int pageSize = 10;
        int totalCount = gatherDataService.findGatherCount();
        int totalPage = (totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1);
        ModelAndView mv = new ModelAndView();
        try {
            List<GatherDataEntity> gathers = gatherDataService.findGatherByPage((pageIndex - 1) * pageSize, pageSize);
            mv.addObject("totalCount", totalCount);
            mv.addObject("pageSize", pageSize);
            mv.addObject("totalPage", totalPage);
            mv.addObject("pageIndex", pageIndex);
            mv.addObject("gathers", gathers);
            mv.setViewName("forward:show.jsp");
        } catch (Exception e) {
            mv.setViewName("redirect:error.jsp");
            e.printStackTrace();
        }
        return mv;
    }


    @RequestMapping("updateGatherShow")
    public ModelAndView updateGathershow(Integer id) {
        logger.info("redirect to update ui  by id");

        ModelAndView mv = new ModelAndView();
        try {
            GatherDataEntity gatherDataEntity = gatherDataService.findGatherById(id);
            mv.addObject("gatherDataEntity", gatherDataEntity);
            mv.setViewName("forward:update.jsp");
        } catch (Exception e) {
            mv.setViewName("redirect:error.jsp");
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping("updateGather")
    public ModelAndView updateGather(GatherDataEntity gatherDataEntity) {
        logger.info("update gather by id");
        int n = 0;
        ModelAndView mv = new ModelAndView();
        try {

            n = gatherDataService.updateGather(gatherDataEntity);
        } catch (Exception e) {
            mv.setViewName("redirect:error.jsp");
            e.printStackTrace();
        }
        if (n > 0) {
            mv.setViewName("redirect:findGatherByPage?pageIndex=1");
        }
        return mv;
    }

    @RequestMapping("deleteGather")
    public ModelAndView deleteGather(Integer id) {
        logger.info("delete gather by id");
        int n = 0;
        ModelAndView mv = new ModelAndView();
        try {
            n = gatherDataService.deleteGatherData(id);
        } catch (Exception e) {
            mv.setViewName("redirect:error.jsp");
            e.printStackTrace();
        }

        if (n > 0) {
            mv.setViewName("redirect:findGatherByPage?pageIndex=1");
        }
        return mv;
    }


    @RequestMapping("deleteGatherBatch")
    public @ResponseBody
    int deleteGatherBatch(@RequestParam(value = "list[]") String[] gatherJobIdList) {
        logger.info(" batch delete gather datas: {}", gatherJobIdList.toString());
        int n = 0;
        for (String GatherJobId : gatherJobIdList) {
            n = gatherDataService.deleteGatherData(Integer.valueOf(GatherJobId));
        }
        return n;
    }


    @RequestMapping("onLineGather")
    public @ResponseBody
    int onLineGather(@RequestParam(value = "int") Integer id) {
        logger.info("online job by id: {}", id);
        return gatherDataService.onLineGather(id);
    }

    @RequestMapping("offLineGather")
    public @ResponseBody
    int offLineGather(@RequestParam(value = "int") Integer id) {
        logger.info("offline job by id: {}", id);
        return gatherDataService.offLineGather(id);
    }

    @RequestMapping("onLineBatch")
    public @ResponseBody
    int onLineBatch(@RequestParam(value = "list[]") String[] onLineList) {
        logger.info(" batch online jobs: {}", onLineList.toString());
        int n = 0;
        for (String onLineId : onLineList) {
            n = gatherDataService.onLineGather(Integer.valueOf(onLineId));
        }
        return n;
    }

    @RequestMapping("dolphinUrl")
    public ModelAndView dolphinUrl() {
        logger.info("redirect to dolphin url");
        String dolphinUrl = gatherDataService.getDolphinUrl();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:" + dolphinUrl);
        return mv;
    }


}
