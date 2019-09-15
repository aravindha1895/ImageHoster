package ImageHoster.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;

@Controller
public class CommentController {

	@Autowired
	CommentService commentService;

	@Autowired
	private ImageService imageService;

	@RequestMapping(value = "/image/{imageId}/{imageTitle}/comments", method = RequestMethod.POST)
	public String postComments(@PathVariable("imageId") Integer imageId, @PathVariable("imageTitle") String imageTitle,
			@RequestParam("comment") String userComment, Model model, HttpSession session) {
		Image image = imageService.getImageByID(imageId);
		User user = (User) session.getAttribute("loggeduser");
		Comment comment = new Comment();
		comment.setImage(image);
		comment.setText(userComment);
		comment.setUser(user);
		comment.setDate(LocalDate.now());

		commentService.postComment(comment);

		model.addAttribute("image", image);
		model.addAttribute("tags", image.getTags());
		model.addAttribute("comments", commentService.getAllComments(imageId));
		return "redirect:/images/" + image.getId() + "/" + image.getTitle();
	}
}
