package raf.rs.reservations.configuration;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ReservationServiceConfiguration {

    @Bean
    public RestTemplate movieServiceRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8084/user-service/api"));
        restTemplate.setInterceptors(List.of(new TokenInterceptor()));
        return restTemplate;
    }

    private static class TokenInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                            ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            final HttpHeaders headers = httpRequest.getHeaders();
            headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwidXNlcklkIjozMDIsInJvbGVzIjpbIlJPTEVfQURNSU4iXX0.JMA9SVJk_rJKjAtxr7eop2K16Jm7j17DKmeJhQ0vMeA");
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        }
    }


}
