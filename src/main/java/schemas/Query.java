package schemas;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import models.User;
import repositories.UserRepository;

public class Query implements GraphQLRootResolver {
	private final UserRepository userRepository;
	
	public Query(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> allUsers() {
		return userRepository.getAllUsers();
	}
}
