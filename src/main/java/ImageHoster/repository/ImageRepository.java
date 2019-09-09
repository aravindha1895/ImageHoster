package ImageHoster.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class ImageRepository {

	@PersistenceUnit(unitName = "imageHoster")
	private EntityManagerFactory emf;

	// The method receives the Image object to be persisted in the database
	public Image uploadImage(Image newImage) {
		// Complete the method
		EntityManager em = emf.createEntityManager();
		EntityTransaction tran = em.getTransaction();
		try {
			tran.begin();
			em.persist(newImage);
			tran.commit();
			return newImage;
		} catch (NoResultException e) {
			return null;
		}
	}

	// This method returns the list of all the images fetched from the database
	public List<Image> getAllImages() {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Image> query = em.createQuery("SELECT i from Image i", Image.class);
		return query.getResultList();

	}

	// Returns the image in case the image is found in the database
	// Returns null if no image is found in the database
	public Image getImageByTitle(String title) {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Image> query = em.createQuery("SELECT i from Image i where i.title =:title", Image.class);
			query.setParameter("title", title);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Bug fix while retrieving image by title when title is same. Retrieving by
	// unique ID
	// getImageByTitle() method shall be retired in future release.
	public Image getImageByID(Integer id) {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Image> query = em.createQuery("SELECT i from Image i where i.id =:id", Image.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// The method receives the Image object to be updated in the database
	public void updateImage(Image updatedImage) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tran = em.getTransaction();

		try {
			tran.begin();
			em.merge(updatedImage);
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		}
	}

	// The method receives the Image id of the image to be deleted in the database
	public void deleteImage(Integer imageId) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			Image image = em.find(Image.class, imageId);
			em.remove(image);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
	}

}
