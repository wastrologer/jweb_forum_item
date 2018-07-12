package com.common.utils;

/**
 * User: gravity
 * Date: 15-8-12
 * Time: 上午9:51
 */
public class EnvManager {

    /**
     * 环境配置变量名称，一般需要在JVM参数中设置-Dcfg.env=local/dev；
     */
    public static final String ENV_VARIALE_NAME = "cfg.env";

    public static final String ENV_NAME_LOCAL = "local";
    public static final String ENV_NAME_DEV = "dev";
    public static final String ENV_NAME_IDC = "idc";
    
    private static String CURRENT_ENV_NAME = null;

    /**
     * 获取当前环境名称，默认为local
     * @return
     */
    public static String getCurrentEnvName() {
    	
    	if (CURRENT_ENV_NAME == null) {
	        String str = System.getProperty(ENV_VARIALE_NAME);
	        if (str == null) {
	            str = System.getenv(ENV_VARIALE_NAME);
	        }
	        CURRENT_ENV_NAME = str;
    	}
        return CURRENT_ENV_NAME;
    }

    /**
     * 判断当前环境是否为IDC环境
     * @return
     */
    public static boolean isIDCEnvCurrent() {
        if (ENV_NAME_IDC.equals(getCurrentEnvName())) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断当前环境是否为Local环境
     * @return
     */
	public static boolean isLocalEnvCurrent() {
		if (ENV_NAME_LOCAL.equals(System.getProperty(ENV_VARIALE_NAME, ""))) {
			return true;
		}
		return false;
	}
    
    /**
     * 判断当前环境是否为Local环境
     * @return
     */
	public static boolean isDevEnvCurrent() {
		if (ENV_NAME_DEV.equals(System.getProperty(ENV_VARIALE_NAME, ""))) {
			return true;
		}
		return false;
	}
	
	public static String getStrValueByEnv(String localValue, String devValue, String idcValue) {
		String currentEnvName = getCurrentEnvName();
		if (ENV_NAME_LOCAL.equals(currentEnvName)) {
			return localValue;
		} else if (ENV_NAME_DEV.equals(currentEnvName)) {
			return devValue;
		} else if (ENV_NAME_IDC.equals(currentEnvName)) {
			return idcValue;
		}
		return localValue;
	} 

}
