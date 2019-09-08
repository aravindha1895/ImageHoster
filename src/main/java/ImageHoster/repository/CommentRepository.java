package ImageHoster.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ImageHoster.model.Comment;

@Repository
public class CommentRepository {

	@PersistenceUnit(unitName = "imageHoster")
	private EntityManagerFactory emf;

	@Autowired
	ImageRepository imageRepository;
	
	public void submitComment(Comment comment) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.persist(comment);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}

	public List<Comment> getCommentsList(Integer imageId) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Comment> query = em.createQuery("SELECT c from Comment c where c.image=:image", Comment.class);
		query.setParameter("image", imageRepository.getImageByID(imageId));
		return query.getResultList();
	}
}
