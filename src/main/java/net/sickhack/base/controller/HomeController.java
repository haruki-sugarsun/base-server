package net.sickhack.base.controller;

import net.sickhack.base.pagemodel.CommonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    ModelAndView home() {
        CommonModel common = new CommonModel();
        common.title = "Base-Server Index";
        ModelAndView mav = new ModelAndView("index", "common", common);
        return mav;
    }
}
