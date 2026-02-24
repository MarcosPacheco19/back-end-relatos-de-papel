package com.relatospapel.ms_books_catalogue.config;

import java.net.URI;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

  @Bean
  public OpenSearchClient openSearchClient(@Value("${opensearch.url}") String url) {
    // CAMBIO: Cliente OpenSearch para Bonsai usando credenciales embebidas en la URL
    URI uri = URI.create(url);

    HttpHost host = new HttpHost(
        uri.getScheme(),
        uri.getHost(),
        uri.getPort() == -1 ? 443 : uri.getPort()
    );

    BasicCredentialsProvider creds = new BasicCredentialsProvider();
    String userInfo = uri.getUserInfo(); // user:pass

    if (userInfo != null && userInfo.contains(":")) {
      String username = userInfo.substring(0, userInfo.indexOf(':'));
      String password = userInfo.substring(userInfo.indexOf(':') + 1);

      creds.setCredentials(
          new AuthScope(host),
          new UsernamePasswordCredentials(username, password.toCharArray())
      );
    }

    RestClient restClient = RestClient.builder(host)
        .setHttpClientConfigCallback(httpClientBuilder ->
            httpClientBuilder.setDefaultCredentialsProvider(creds))
        .build();

    OpenSearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
    return new OpenSearchClient(transport);
  }
}