package ImageHoster.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.repository.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;

	public ImageService() {
	}

	// The method returns the list of two harc-coded images
	public List<Image> getAllImages() {
		return imageRepository.getAllImages();
	}

	// The method does not store the image in the database
	public void uploadImage(Image image) {
		imageRepository.uploadImage(image);
	}

	// The method calls the getImageByTitle() method in the Repository and passes
	// the title of the image to be fetched.
	// Will be retired in future release or changed to getImageListByTitle()
	@Deprecated
	public Image getImageByTitle(String title) {
		return imageRepository.getImageByTitle(title);
	}

	// The method calls the getImageByTitle() method in the Repository and passes
	// the title of the image to be fetched
	public Image getImageByID(Integer id) {
		return imageRepository.getImageByID(id);
	}


	// The method calls the updateImage() method in the Repository and passes the
	// Image to be updated in the database
	public void updateImage(Image updatedImage) {
		imageRepository.updateImage(updatedImage);
	}

	// The method calls the deleteImage() method in the Repository and passes the
	// Image id of the image to be deleted in the database
	public void deleteImage(Integer imageId) {
		// Complete the method
		imageRepository.deleteImage(imageId);
	}

}
