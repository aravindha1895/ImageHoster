package ImageHoster.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.model.UserProfile;
import ImageHoster.service.ImageService;
import ImageHoster.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	// This method is responsible for registration landing page.
	@RequestMapping("users/registration")
	public String registration(Model model) {
		User user = new User();
		UserProfile profile = new UserProfile();
		user.setProfile(profile);
		model.addAttribute("User", user);
		return "users/registration";
	}

	// This method calls the business logic and after the user record is persisted
	// in the database, directs to login page
	@RequestMapping(value = "users/registration", method = RequestMethod.POST)
	public String registerUser(User user, Model model) {
		Pattern pattern = Pattern.compile("^(?=\\D*\\d)(?=.*?[a-zA-Z]).*[\\W_].*$");
		Matcher m = pattern.matcher(user.getPassword());
		if (m.find()) {
			userService.registerUser(user);
			return "redirect:/users/login";
		} else {
			user = new User();
			UserProfile profile = new UserProfile();
			user.setProfile(profile);
			model.addAttribute("User", user);
			model.addAttribute("passwordTypeError",
					"Password must contain atleast 1 alphabet, 1 number & 1 special character");
			return "users/registration";
		}
	}

	// This controller method is called when the request pattern is of type
	// 'users/login'
	@RequestMapping("users/login")
	public String login() {
		return "users/login";
	}

	// This controller method is called when the request pattern is of type
	// 'users/login' and also the incoming request is of POST type
	@RequestMapping(value = "users/login", method = RequestMethod.POST)
	public String loginUser(User user, HttpSession session) {
		User loggedUser = userService.login(user);
		if (loggedUser != null) {
			session.setAttribute("loggeduser", loggedUser);
			return "redirect:/images";
		} else {
			return "users/login";
		}
	}

	// The method receives the Http Session and the Model type object
	// session is invalidated and logged out
	@RequestMapping(value = "users/logout", method = RequestMethod.POST)
	public String logout(Model model, HttpSession session) {
		session.invalidate();

		List<Image> images = imageService.getAllImages();
		model.addAttribute("images", images);
		return "index";
	}
}
