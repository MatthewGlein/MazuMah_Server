package resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;

import authentication.SigninPayload;
import models.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {
	public User user(SigninPayload payload) {
		return payload.getUser();
	}
}
