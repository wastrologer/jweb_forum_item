package com.common.cache;

import net.rubyeye.xmemcached.KeyProvider;

/**
 * 缓存键值预处理
 * 
 * @author Admin
 */
public class CacheKeyProvider implements KeyProvider {

	private String keyPrefix;

	public String process(String key) {
		if (key.startsWith("@")) {
			return key.substring(1);
		}
		return keyPrefix + key;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
}
