package com.transferwise.tasks.uuidtestapp.resource;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshingCacheResourceService {
	@Autowired
	private ResourceService resourceService;

	private LoadingCache<Long, String> cache;

	@PostConstruct
	public void init() {
		cache = Caffeine.newBuilder()
				.maximumSize(100)
				.expireAfterWrite(20, TimeUnit.SECONDS)
				.refreshAfterWrite(10, TimeUnit.SECONDS)
				.build(id -> resourceService.fetchResource(id));
	}

	public String getResource(Long id) {
		return cache.get(id);
	}

	// For Visuals only.
	public String getCurrentCachedResource(Long id){
		return cache.getIfPresent(id);
	}
}
