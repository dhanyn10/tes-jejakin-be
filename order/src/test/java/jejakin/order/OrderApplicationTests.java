package jejakin.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderApplicationTests {

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
	
	@Test
	void getAllUsers() throws IOException {
		HttpUriRequest request = new HttpGet(this.configHost() + "/users/all");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		assertEquals(httpResponse.getCode(), HttpStatus.SC_OK);
	}
}
