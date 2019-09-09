package ImageHoster.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.Tag;
import ImageHoster.model.User;
import ImageHoster.service.ImageService;
import ImageHoster.service.TagService;

@Controller
public class ImageController {

	@Autowired
	private ImageService imageService;

	@Autowired
	private TagService tagService;

	// This method displays all the images in the user home page after successful
	// login
	@RequestMapping("images")
	public String getUserImages(Model model) {
		List<Image> images = imageService.getAllImages();
		model.addAttribute("images", images);
		return "images";
	}

	// This method is called when the details of the specific image with
	// corresponding title are to be displayed
	@RequestMapping("/images/{imageId}/{title}")
	public String showImage(@PathVariable("imageId") Integer imageId, @PathVariable("title") String title,
			Model model) {
		// Image image = imageService.getImageByTitle(title);
		Image image = imageService.getImageByID(imageId);
		model.addAttribute("image", image);
		model.addAttribute("tags", image.getTags());
		model.addAttribute("comments", image.getComment());
		return "images/image";
	}

	// The method returns landing page of upload.
	@RequestMapping("/images/upload")
	public String newImage() {
		return "images/upload";
	}

	// The method receives all the details of the image to be stored in the
	// database, and now the image will be sent to the business logic to be
	// persisted in the database
	@RequestMapping(value = "/images/upload", method = RequestMethod.POST)
	public String createImage(@RequestParam("file") MultipartFile file, @RequestParam("tags") String tags,
			Image newImage, HttpSession session) throws IOException {

		User user = (User) session.getAttribute("loggeduser");
		newImage.setUser(user);
		String uploadedImageData = convertUploadedFileToBase64(file);
		newImage.setImageFile(uploadedImageData);

		List<Tag> imageTags = findOrCreateTags(tags);
		newImage.setTags(imageTags);
		newImage.setDate(new Date());
		imageService.uploadImage(newImage);
		return "redirect:/images";
	}

	// This method fetches the image with the corresponding id from the database and
	// adds it to the model with the key as 'image'
	// The method then returns 'images/edit.html' file wherein you fill all the
	// updated details of the image
	@RequestMapping(value = "/editImage")
	public String editImage(@RequestParam("imageId") Integer imageId, Model model, HttpSession session) {
		Image image = imageService.getImageByID(imageId);
		if (isOperationAllowedOnImage(image, session)) {
			String tags = convertTagsToString(image.getTags());
			model.addAttribute("image", image);
			model.addAttribute("tags", tags);
			return "images/edit";
		} else {
			String error = "Only the owner of the image can edit the image";
			model.addAttribute("editError", error);
			model.addAttribute("image", image);
			model.addAttribute("tags", image.getTags());
			model.addAttribute("comments", image.getComment());
			return "images/image";
		}

	}

	// The method adds the new imageFile to the updated image if user updates the
	// imageFile and adds the previous imageFile to the new updated image if user
	// does not choose to update the imageFile
	@RequestMapping(value = "/editImage", method = RequestMethod.PUT)
	public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId,
			@RequestParam("tags") String tags, Image updatedImage, HttpSession session) throws IOException {

		Image image = imageService.getImageByID(imageId);
		String updatedImageData = convertUploadedFileToBase64(file);
		List<Tag> imageTags = findOrCreateTags(tags);

		if (updatedImageData.isEmpty())
			updatedImage.setImageFile(image.getImageFile());
		else {
			updatedImage.setImageFile(updatedImageData);
		}

		updatedImage.setId(imageId);
		User user = (User) session.getAttribute("loggeduser");
		updatedImage.setUser(user);
		updatedImage.setTags(imageTags);
		updatedImage.setDate(new Date());

		imageService.updateImage(updatedImage);
		return "redirect:/images/" + updatedImage.getId() + "/" + updatedImage.getTitle();
	}

	// This method deleted image by its ID.
	@RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
	public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId, Model model, HttpSession session) {
		Image image = imageService.getImageByID(imageId);
		if (isOperationAllowedOnImage(image, session)) {
			imageService.deleteImage(imageId);
			return "redirect:/images";
		} else {
			String error = "Only the owner of the image can delete the image";
			model.addAttribute("deleteError", error);
			model.addAttribute("image", image);
			model.addAttribute("tags", image.getTags());
			model.addAttribute("comments", image.getComment());
			return "images/image";
		}
	}

	// This method converts the image to Base64 format
	private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
		return Base64.getEncoder().encodeToString(file.getBytes());
	}

	// After adding all tags to a list, the list is returned
	private List<Tag> findOrCreateTags(String tagNames) {
		StringTokenizer st = new StringTokenizer(tagNames, ",");
		List<Tag> tags = new ArrayList<Tag>();

		while (st.hasMoreTokens()) {
			String tagName = st.nextToken().trim();
			Tag tag = tagService.getTagByName(tagName);

			if (tag == null) {
				Tag newTag = new Tag(tagName);
				tag = tagService.createTag(newTag);
			}
			tags.add(tag);
		}
		return tags;
	}

	private String convertTagsToString(List<Tag> tags) {
		StringBuilder tagString = new StringBuilder();

		for (int i = 0; i <= tags.size() - 2; i++) {
			tagString.append(tags.get(i).getName()).append(",");
		}
		if (tags.size() > 0) {
			Tag lastTag = tags.get(tags.size() - 1);
			tagString.append(lastTag.getName());

		}

		return tagString.toString();
	}

	// This method check whether logged in user can do write operation on image
	private boolean isOperationAllowedOnImage(Image image, HttpSession session) {
		User user = (User) session.getAttribute("loggeduser");

		if (image.getUser().getId() == user.getId())// Logged in user is the owner of the image
			return true;
		else // Logged in user is not the owner of the image
			return false;
	}

}
