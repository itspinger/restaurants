package raf.rs.notification.configuration;

import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate movieServiceRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8084/user-service/api"));
        restTemplate.setInterceptors(List.of(new TokenInterceptor()));
        return restTemplate;
    }

    private static class TokenInterceptor implements ClientHttpRequestInterceptor {

        @Override
        @NonNull
        public ClientHttpResponse intercept(HttpRequest httpRequest, @NonNull final byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            final HttpHeaders headers = httpRequest.getHeaders();
            headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwidXNlcklkIjoyLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XX0.pMvZIsPzR0ZCfxfLGYi-ds_HIq1vznO7RxhcFOw56SA");
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        }
    }
}
