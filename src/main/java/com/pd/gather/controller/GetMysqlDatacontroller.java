package com.pd.gather.controller;

import com.pd.gather.service.GatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */

@Controller
@RequestMapping("/mysql")
public class GetMysqlDatacontroller {
    @Autowired
    GatherDataService gatherDataService;


    @RequestMapping(value = "/gather-data", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap getGatherDataEntity() {
        ModelMap model = new ModelMap();
        try {
            model.addAttribute("success", true);
            model.addAttribute("products", gatherDataService.getGatherData());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success", false);
            model.addAttribute("msg", e.getMessage());
        }
        return model;
    }


    @RequestMapping(value = "/show-table", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap showCreateTable(@RequestParam("tableName") String tableName) {
        ModelMap model = new ModelMap();
        try {
            model.addAttribute("success", true);
            // model.addAttribute("products", getMysqlDataService.showCreateTable(tableName));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success", false);
            model.addAttribute("msg", e.getMessage());
        }
        return model;
    }


    @RequestMapping(value = "/all_jobs", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap createALlJobs(@RequestParam("ids") List ids) {
        ModelMap model = new ModelMap();
        try {
            model.addAttribute("success", true);
            model.addAttribute("products", gatherDataService.createAllJobs(ids));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success", false);
            model.addAttribute("msg", e.getMessage());
        }
        return model;
    }

}
