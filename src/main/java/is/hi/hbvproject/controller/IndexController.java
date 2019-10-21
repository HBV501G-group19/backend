package is.hi.hbvproject.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class IndexController {
	@Autowired
	public IndexController() {}
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public String getIndex() {
		// TODO: make pretty json
		return "{\n"
				+ "\"Users\": {"
				+ "\"users\": \"/users\",\n"
				+ "\"user\": \"/users/{id}\",\n"
				+ "\"register\": \"/users/register\""
				+ "},\n"
				+ "\"Rides\": {},\n"
				+ "\"Messages\": {}\n"
				+ "}";
	}
}
