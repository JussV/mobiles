package com.isb.mobiles.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.isb.mobiles.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.isb.mobiles.repository.search")
public class DatabaseConfiguration {

}
