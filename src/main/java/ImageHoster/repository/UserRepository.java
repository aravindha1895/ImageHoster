package ImageHoster.repository;

import ImageHoster.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Repository
public class UserRepository {

	@PersistenceUnit(unitName = "imageHoster")
	private EntityManagerFactory emf;

	// The method receives the User object to be persisted in the database
	public void registerUser(User newUser) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.persist(newUser);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}

	// The method receives the entered username and password and checks if
	// registered user in database.
	public User checkUser(String username, String password) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<User> query = em.createQuery("SELECT u from User u where username=:username AND password=:password",
				User.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		try {
			User user = query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}
}
