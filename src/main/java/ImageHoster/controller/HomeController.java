package ImageHoster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ImageHoster.model.Image;
import ImageHoster.service.ImageService;


@Controller
public class HomeController {

	@Autowired
	ImageService imageservice;
	
    public HomeController() {
        System.out.println("*** HomeController ***");
    }

    @RequestMapping("/")
    public String getAllImages(Model model) {
        //Call getAllImages() method in ImageService class to get the list of all images
        //Add the list of images in the model with the key as "images"
    	List<Image> images =imageservice.getAllImages();
    	model.addAttribute("images", images);
        return "index";
    }

}