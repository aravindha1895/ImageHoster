package ImageHoster.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ImageHoster.model.Comment;
import ImageHoster.repository.CommentRepository;
import ImageHoster.repository.ImageRepository;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;

	public void postComment(Comment comment) {
		commentRepository.submitComment(comment);
	}

	public List<Comment> getAllComments(Integer imageId) {
		return commentRepository.getCommentsList(imageId);
	}
}
