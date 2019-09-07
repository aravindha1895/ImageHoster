package ImageHoster.repository;

import ImageHoster.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

//Write the annotation which is a special type of @Component annotation and describes that the class defines a data repository
@Repository
public class UserRepository {
    //Get an instance of EntityManagerFactory from persistence unit with name as 'imageHoster'
    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    //The method receives the User object to be persisted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void registerUser(User newUser) {
        EntityManager em = emf.createEntityManager();
        //Complete the method
        EntityTransaction transaction=em.getTransaction();
        try {
        	transaction.begin();
        	em.persist(newUser);
        	transaction.commit();
        } catch(Exception e) {
        	transaction.rollback();
        }
    }
    
  //The method receives the entered username and password
  //Creates an instance of EntityManager
  //Executes JPQL query to fetch the user from User class where username is equal to received username and password is equal to received password
  //Returns the fetched user
  //Returns null in case of NoResultException
  public User checkUser(String username, String password) {
      //Complete the method
  	EntityManager em = emf.createEntityManager();
  	TypedQuery<User> query=em.createQuery("SELECT u from User u where username=:username AND password=:password", User.class);
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

