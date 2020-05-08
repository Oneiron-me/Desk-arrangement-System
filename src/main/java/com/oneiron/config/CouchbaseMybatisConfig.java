package com.oneiron.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "couchbase")
@MapperScan(basePackages = "com.oneiron.admin.dao", sqlSessionFactoryRef = "sqlSessionFactoryBean")
/**
 * 
 * @author Interface
 * 걍 저거만 마이바티스로 잡고싶어서 베이스패키지 좁게 잡았음
 *
 */
public class CouchbaseMybatisConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String hostName;

	private String bucketName;

	private String password;

	private String driver;
	
	private String url;
	
	private String server;
	
	private String n1qlport;

	public CouchbaseMybatisConfig() {
		logger.info("Loading Couchbase Mybatis properties");
	}

	@PostConstruct
	public void postConstruct() {
		logger.info(toString());
	}
	
	@Bean
	public DataSource dataSource() throws Exception {
		
//		Properties prop = new Properties();
//		prop.put("-u", bucketName);
//		prop.put("-p", password);
//		
//		SimpleDriverDataSource ds = new SimpleDriverDataSource();
//		ds.setDriver( new com.couchbase.jdbc.CBDriver());
//		ds.setUsername(bucketName);
//		ds.setPassword(password);
//		ds.setUrl(url+"//hancheol.dlinkddns.com:8093");
//		ds.setConnectionProperties(prop);
		
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url+"//hancheol.dlinkddns.com:8093");
		ds.setUsername(bucketName);
		ds.setPassword(password);
		ds.addConnectionProperty("-u", bucketName);
		ds.addConnectionProperty("-p", password);
		ds.addConnectionProperty("Server", server);
		ds.addConnectionProperty("N1QLPort", n1qlport);
		return ds;
	}
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception{
		//jdbc config
		org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
		conf.setJdbcTypeForNull(JdbcType.NULL);
		conf.setMapUnderscoreToCamelCase(true);
		conf.setCallSettersOnNulls(false);
		
		ClassLoader cl = this.getClass().getClassLoader(); 
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = resolver.getResources("classpath*:/query/*/*.xml") ;
		for (Resource resource: resources){
		    logger.info("바보 : {}",resource.getFilename());
		}
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setFailFast(true);
		factory.setDataSource(dataSource());
		factory.setMapperLocations(resources);
		factory.setConfiguration(conf);
		return factory;
	}
}
