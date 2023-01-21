package jejakin.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import jejakin.order.dao.UserRepository;
import jejakin.order.model.User;
import net.datafaker.Faker;

@SpringBootTest
@DisplayName("users testing")
class OrderApplicationUsersTests {
	
	@Autowired
	private UserRepository userRepo;
	
	@Value("${webhost}") // ambil data webhost dari application.properties
	private String alamatHost;
	
	@Test
	@DisplayName("tambah admin")
	void isertAdmin() throws IOException {
		userRepo.deleteAll();
		URL url = new URL("http://"+ alamatHost +":8080/users/admin");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		MatcherAssert.assertThat(body, CoreMatchers.containsString("admin generated"));
	}
	
	@Test
	@DisplayName("tambah admin gagal")
	void isertAdminTwice() throws IOException {
		URL url = new URL("http://"+ alamatHost +":8080/users/admin");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		MatcherAssert.assertThat(body, CoreMatchers.containsString("admin only generated once"));
	}

	@Test
	@DisplayName("tambah user biasa")
	void AddRegularkUsers() throws IOException, JSONException {
		User reguler = new User();
		Faker faker = new Faker();
		reguler.setUsername(faker.football().teams());
		reguler.setFirstname(faker.football().players());
		reguler.setLastname(faker.football().positions());
		reguler.setRole("user");
		reguler.setEmail(faker.football().coaches());
		userRepo.save(reguler);
		URL url = new URL("http://"+ alamatHost +":8080/users/all");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		JSONArray resBody = new JSONArray(body);
		assertEquals(resBody.length(), 2); // ada 2 user, admin dan reguler
	}
	
}
