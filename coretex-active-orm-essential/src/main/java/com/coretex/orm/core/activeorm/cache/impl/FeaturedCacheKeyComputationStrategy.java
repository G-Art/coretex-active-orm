package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.CacheKeyComputationStrategy;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FeaturedCacheKeyComputationStrategy implements CacheKeyComputationStrategy<MetaTypeItem, FeaturedStatementCacheContext> {

	private ConcurrentMap<Object, Set<MetaTypeItem>> keyMetaTypeRelation = Maps.newConcurrentMap();
	private Set<MetaTypeItem> metaTypeItemsAffected = Sets.newHashSet();

	@Override
	public <K> K createKey(FeaturedStatementCacheContext ctx) {
		var query = ctx.getResultQuery();
		var parameters = ctx.getParameters();
		var k = (K) new CacheKey(query, parameters);
		storeKeyWithMetaData(k, ctx.getItemsUsed());
		return k;
	}

	@Override
	public <K> Set<K> collectFeaturedKeys(Predicate<MetaTypeItem> predicate){
		return keyMetaTypeRelation.entrySet()
				.stream()
				.filter(objectSetEntry -> objectSetEntry.getValue().stream().anyMatch(predicate))
				.map(objectSetEntry -> (K)objectSetEntry.getKey())
				.collect(Collectors.toSet());
	}

	@Override
	public boolean containFeature(Predicate<MetaTypeItem> predicate){
		return metaTypeItemsAffected.stream().anyMatch(predicate);
	}

	public class CacheKey {

		private final String query;
		private final Map<String, Object> parameters;

		public CacheKey(String query, Map<String, Object> parameters) {
			this.query = query;
			this.parameters = Map.copyOf(parameters);
		}

		public String getQuery() {
			return query;
		}

		public Map<String, Object> getParameters() {
			return Map.copyOf(parameters);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			CacheKey cacheKey = (CacheKey) o;
			return query.equals(cacheKey.getQuery()) &&
					parameters.equals(cacheKey.getParameters());
		}

		@Override
		public int hashCode() {
			return Objects.hash(query, parameters);
		}

		@Override
		public String toString() {
			return "CacheKey { \n" +
					"query='" + query + '\'' +
					"\n, parameters=" + parameters +
					'}';
		}
	}

	private <K> void storeKeyWithMetaData(K key, Set<MetaTypeItem> itemsUsed) {
		if (CollectionUtils.isNotEmpty(itemsUsed)) {
			keyMetaTypeRelation.computeIfAbsent(key, k -> Sets.newHashSet(itemsUsed));
			keyMetaTypeRelation.computeIfPresent(key, (k, s) -> {
				s.addAll(itemsUsed);
				return s;
			});
		}
		metaTypeItemsAffected.addAll(itemsUsed);
	}
}
