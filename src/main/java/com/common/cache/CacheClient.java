package com.common.cache;

import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;
@Component
public class CacheClient {

	private static Logger logger = LoggerFactory.getLogger(CacheClient.class);

	// 默认操作超时时间 30000ms
	private long defaultTimeout = 30000;

	// 过期时间一分钟
	public static final int CACHE_EXPIRE_TIME_1_MIN = 60;

	// memcached 对象
	private MemcachedClient memcachedClient;

	/**
	 * 向缓存设置数据，默认操作超时时间为500ms
	 * 
	 * @param key
	 * @param data
	 * @param expireTime
	 *            缓存数据有效期,以秒为单位
	 * @return
	 * @throws Exception
	 */
	public boolean set(String key, int expireTime, Object data) {
		return set(key, expireTime, data, defaultTimeout);
	}

	/**
	 * 向缓存设置数据，默认操作超时时间为500ms
	 * 
	 * @param key
	 *            缓存key
	 * @param data
	 *            缓存对象
	 * @param expireTime
	 *            缓存数据有效期,以秒为单位
	 * @param timeout
	 *            操作缓存超时时间，单位为ms
	 * @return
	 * @throws Exception
	 */
	public boolean set(String key, int expireTime, Object data, long timeout) {
		if (data == null) {
			return false;
		}
		try {
			boolean ret = memcachedClient.set(key, expireTime, data, timeout);
			return ret;
		} catch (TimeoutException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 从缓存取数据，默认操作缓存超时时间500ms
	 * 
	 * @param key
	 *            缓存数据key
	 * @return
	 * @throws Exception
	 */
	public Object get(String key) {
		return get(key, defaultTimeout);
	}

	/**
	 * 从缓存取数据
	 * 
	 * @param key
	 *            缓存数据key
	 * @param timeout
	 *            操作缓存超时时间，单位为ms
	 * @return
	 * @throws Exception
	 */
	public Object get(String key, long timeout) {
		try {
			Object ret = memcachedClient.get(key, timeout);
			return ret;
		} catch (TimeoutException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从缓存取数据
	 * 
	 * @param key
	 * @return
	 */
	public GetsResponse<Object> gets(String key) {
		try {
			return memcachedClient.gets(key);
		} catch (TimeoutException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从缓存取数据
	 *
	 * @param key
	 * @return
	 */
	public GetsResponse<Object> getAndTouch(String key,Integer newExpire) {
		try {
			return memcachedClient.getAndTouch(key, newExpire);
		} catch (TimeoutException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从缓存中删除数据
	 * 
	 * @param key
	 *            缓存数据key
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String key) {
		return delete(key, defaultTimeout);
	}

	/**
	 * 从缓存中删除数据
	 * 
	 * @param key
	 *            缓存数据key
	 * @param timeout
	 *            操作缓存超时时间，单位为ms
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String key, long timeout) {
		try {
			boolean ret = memcachedClient.delete(key, timeout);
			return ret;
		} catch (TimeoutException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 向缓存设置数据
	 * 
	 * @param key
	 * @param operation
	 * @return
	 */
	public boolean cas(String key, CASOperation operation) {
		try {
			return (boolean) memcachedClient.cas(key, operation);
		} catch (TimeoutException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			logger.error(e.getMessage(), e);
		}

		return false;
	}

	/**
	 * 向缓存设置数据
	 * 
	 * @param key
	 * @param expireTime
	 * @param data
	 * @param cas
	 * @return
	 */
	public boolean cas(String key, int expireTime, Object data, long cas) {
		if (data == null) {
			return false;
		}
		try {
			return (boolean) memcachedClient.cas(key, expireTime, data, cas);
		} catch (TimeoutException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			logger.error(e.getMessage(), e);
		}

		return false;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public MemcachedClient getMemcachedClient() {
		return this.memcachedClient;
	}

	public long getDefaultTimeout() {
		return defaultTimeout;
	}

	public void setDefaultTimeout(long defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

}