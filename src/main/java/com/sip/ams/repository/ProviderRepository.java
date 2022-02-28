package com.sip.ams.repository;

import java.util.List;

//import java.util.List;

import com.sip.ams.entity.Article;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.sip.ams.entity.Provider;

@Repository
public interface ProviderRepository extends CrudRepository<Provider, Long> {
	//List<Provider> findByName(String name);
	
	@Query("FROM Article a WHERE a.provider.id = ?1")
	List<Article> findArticlesByProvider(long id);

}



