package com.haoocai.jscheduler.web.controller;

import com.haoocai.jscheduler.core.JschedulerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author Michael Jiang on 16/5/24.
 */
@Controller
public class HomeController {
    @Autowired
    private JschedulerConfig jschedulerConfig;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dashboard(Model model) {
        model.addAttribute("now", new Date());
        return "index";
    }

}
