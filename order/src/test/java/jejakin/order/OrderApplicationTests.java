package jejakin.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderApplicationTests {

	@Test
	void contextLoads() throws IOException {
		HttpUriRequest request = new HttpGet("http://localhost:8080/users/all");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		assertEquals(httpResponse.getCode(), HttpStatus.SC_OK);
	}
}
