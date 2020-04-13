package com.oneiron.config;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.query.Consistency;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

import com.couchbase.client.core.logging.CouchbaseLogLevel;
import com.couchbase.client.core.retry.FailFastRetryStrategy;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.oneiron.doc.TravelDoc;

@Configuration
@ComponentScan("com.oneiron.repo")
@EnableCouchbaseRepositories(basePackages = {"com.oneiron.repo"})
public class Couchbase extends AbstractCouchbaseConfiguration {
	
	protected static final Logger logger = LoggerFactory.getLogger(Couchbase.class);
	
	@Autowired
	private CouchbaseSetting couchbaseSetting;
	
	@Override
	protected List<String> getBootstrapHosts() {
		logger.info("Registering host '{}' for couchbase cluster", couchbaseSetting.getHostName());
		return Collections.singletonList(couchbaseSetting.getHostName());
	}

	@Override
	protected String getBucketName() {
		logger.info("Opening bucket '{}'", couchbaseSetting.getBucketName());
		return couchbaseSetting.getBucketName();
	}

	@Override
	protected String getBucketPassword() {
		logger.info("Get bucket password '{}'", couchbaseSetting.getPassword());
		return couchbaseSetting.getPassword();
	}
	
	@Override
	protected CouchbaseEnvironment getEnvironment() {
		final CouchbaseEnvironment env = DefaultCouchbaseEnvironment
				.builder()
				.connectTimeout(10000)
				.retryStrategy(FailFastRetryStrategy.INSTANCE)
				.defaultMetricsLoggingConsumer(true, CouchbaseLogLevel.INFO)
				.build();
		return env;
	}
	
	

	@Override
	protected Consistency getDefaultConsistency() {
		return Consistency.READ_YOUR_OWN_WRITES;
	}

	@Override
	protected void configureRepositoryOperationsMapping(RepositoryOperationsMapping mapping) {
		try {
			mapping.mapEntity(TravelDoc.class, couchbaseTemplate());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		super.configureRepositoryOperationsMapping(mapping);
	}
	
	@Bean
	public Bucket masterBucket() throws Exception {
		return couchbaseCluster().openBucket(couchbaseSetting.getBucketName(), couchbaseSetting.getPassword());
	}
	
	@Bean
	public CouchbaseTemplate couchbaseTemplate() throws Exception {
		CouchbaseTemplate template = new CouchbaseTemplate(
			couchbaseClusterInfo(), 
			masterBucket(), 
			mappingCouchbaseConverter(), 
			translationService()
		);
		
		template.setDefaultConsistency(getDefaultConsistency());
		return template;
	}
	
	
}
