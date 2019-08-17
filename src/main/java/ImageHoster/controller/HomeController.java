package ImageHoster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    public HomeController() {
        System.out.println("*** HomeController ***");
    }

    @RequestMapping("/")
    public String homePage(Model model) {

        return "index";

    }

}