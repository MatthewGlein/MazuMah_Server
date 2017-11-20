package endpoint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coxautodev.graphql.tools.SchemaParser;

import authentication.AuthContext;
import errors.SanitizedError;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;
import models.User;
import repositories.UserRepository;
import resolvers.SigninResolver;
import schemas.Mutation;
import schemas.Query;

@WebServlet(urlPatterns="/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
	public static final long serialVersionUID = 1;
	private static final UserRepository userRepository;
	
	static {
		userRepository = new UserRepository();
	}
	
	@Override
	protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
		response.get().addHeader("Access-Control-Allow-Origin","*");
		response.get().addHeader("Access-Control-Allow-Methods","GET,POST");
		response.get().addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
		response.get().addHeader("Access-Control-Allow-Credentials","true");

		
//		Collection<String> c = response.get().getHeaders("Access-Control-Allow-Origin");
//		for (String s : c) {
//			System.out.println(s);
//		}
		System.out.println(response.get());
		User user = request
				.map(req -> req.getHeader("Authorization"))
				.filter(id -> !id.isEmpty())
				.map(id -> id.replace("Bearer ", ""))
				.map(userRepository::findById)
				.orElse(null);
		return new AuthContext(user, request, response);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		setAccessControlHeaders(resp);
	}
	
	private void setAccessControlHeaders(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
	}
	
	@Override
	protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
		return errors.stream()
				.filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e))
				.map(e -> e instanceof ExceptionWhileDataFetching ? new SanitizedError((ExceptionWhileDataFetching) e) : e)
				.collect(Collectors.toList());
	}
	
	public GraphQLEndpoint() {
		super(buildSchema());
	}
    
	
	
	private static GraphQLSchema buildSchema() {
		return SchemaParser.newParser()
				.file("schema.graphqls")
				.resolvers(
					new Query(userRepository),
					new Mutation(userRepository),
					new SigninResolver()
				)
				.build()
				.makeExecutableSchema();
	}
}
