package net.jadefisher.monkeystatus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class AppConfig {

	@Value("${monkeystatus.http.connections.maxTotal}")
	private int maxConnections;

	@Value("${monkeystatus.http.connections.defaultMaxPerRoute}")
	private int defaultMaxPerRoute;

	@Value("${monkeystatus.scheduleThreadPoolSize}")
	private int scheduleThreadPoolSize;

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public PoolingHttpClientConnectionManager connectionManager() {
		PoolingHttpClientConnectionManager cmgr = new PoolingHttpClientConnectionManager();
		cmgr.setMaxTotal(maxConnections);
		cmgr.setDefaultMaxPerRoute(defaultMaxPerRoute);
		return cmgr;
	}

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newScheduledThreadPool(scheduleThreadPoolSize);
	}

	@Bean
	public MongoClient mongoClient(
			@Value("${monkeystatus.mongodb.host}") String mongodbHost)
			throws Exception {
		return new MongoClient(mongodbHost);
	}

	@Bean
	@Autowired
	public MongoTemplate mongoTemplate(MongoClient mongoClient,
			@Value("${monkeystatus.mongodb.dbName}") String dbName)
			throws Exception {
		return new MongoTemplate(mongoClient, dbName);
	}
}
