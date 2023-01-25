package jejakin.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import org.testcontainers.shaded.org.hamcrest.CoreMatchers;
import org.testcontainers.shaded.org.hamcrest.MatcherAssert;

import jejakin.order.dao.UserRepository;
import jejakin.order.model.User;
import net.datafaker.Faker;

@SpringBootTest
class OrderApplicationTests {
	
	@Autowired
	private UserRepository userRepo;

	private static Boolean isRunningInsideDocker() {
        try (Stream < String > stream =
            Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }
	
	private String configHost () {
		if(isRunningInsideDocker()) {
			return "http://web:8080";
		} else {
			return "http://localhost:8080";
		}
	}
	
	private void delaytest (int sec) {
		try {
			TimeUnit.SECONDS.sleep(sec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	void getAllUsers() throws IOException {
		HttpUriRequest request = new HttpGet(this.configHost() + "/users/all");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		assertEquals(httpResponse.getCode(), HttpStatus.SC_OK);
	}
	@Test
	void isertAdmin() throws IOException {
		userRepo.deleteAll();
		URL url = new URL(this.configHost() + "/users/admin");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		MatcherAssert.assertThat(body, CoreMatchers.containsString("admin generated"));
	}

	@Test
	void isertAdminTwice() throws IOException {
		URL url = new URL(this.configHost() + "/users/admin");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		MatcherAssert.assertThat(body, CoreMatchers.containsString("admin only generated once"));
	}

	@Test
	void dataNotEmpty () {
		List<User> dataRepo = userRepo.findAll();
		assertEquals(1, dataRepo.size());
	}
	
	@Test
	void AddRegularkUsers() throws IOException, JSONException {
		User reguler = new User();
		Faker faker = new Faker();
		reguler.setUsername(faker.football().teams());
		reguler.setFirstname(faker.football().players());
		reguler.setLastname(faker.football().positions());
		reguler.setRole("user");
		reguler.setEmail(faker.football().coaches());
		userRepo.save(reguler);
		URL url = new URL(this.configHost() + "/users/all");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		JSONArray resBody = new JSONArray(body);
		assertEquals(resBody.length(), 2); // ada 2 user, admin dan reguler
	}
}
