package com.controller;

import com.common.cache.CacheClient;
import com.common.entity.CommonEnum;
import com.common.entity.Constants;
import com.common.entity.ReturnValueConstants;
import com.common.entity.SMSVo;
import com.common.utils.HelperUtil;
import com.common.utils.ImageValidationUtil;
import com.common.utils.SendSMS;
import com.common.utils.StringUtil;
import com.constant.ErrorCode;
import com.pojo.ImageCode;
import com.pojo.ImageCodeCount;
import com.pojo.ImageValidateCode;
import com.pojo.User;
import com.service.IUserSvc;
import net.rubyeye.xmemcached.GetsResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/reg")
public class RegisterController extends BaseController {

    @Value("${baseLoginUrl}")
    private String baseLoginUrl = "http://localhost:8080";

    @Autowired
    private IUserSvc userSvcImpl;

    @Autowired
    private CacheClient cacheClient;

    private String isNeedAuthCode = "1"; // 登录是否需要验证码 1否(0000可做为万能验证码) 2是

    @Value("${company.account.number}")
    private String companyAccountNumber = "";


    @RequestMapping(value = "/getValidateImg", method =  { RequestMethod.GET, RequestMethod.POST })

    public String getValidateImg(HttpServletRequest request, HttpServletResponse response) {
        try {

            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");

            // 设置响应的类型格式为图片格式
            response.setContentType("image/jpeg");
//			response.setHeader("content-type", "image/jpeg;charset=UTF-8");
            //禁止图像缓存。
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);


            String ip = getIpAddr(request);
            GetsResponse<Object> smsCountVoGetsResponse = cacheClient.gets(Constants.IMAGE_CODE_Count_KEY_STR + ip);
            ImageCodeCount ImageCodeCount;

            ImageValidateCode vCode = null;
            if (smsCountVoGetsResponse == null) {
                vCode = new ImageValidateCode(135,38,5,200);

                ImageCode imc = new ImageCode();
                imc.setContent(vCode.getImageCode());
                imc.setTimestamp(HelperUtil.getSecondStamp());

                ImageCodeCount = new ImageCodeCount();
                ImageCodeCount.setCount(1);
                ImageCodeCount.setTimestamp(HelperUtil.getSecondStamp());
                cacheClient.set(Constants.IMAGE_CODE_DETAIL_KEY_STR + ip, Constants.IMAGE_CODE_EXPIRE_SECOND, imc);
                cacheClient.set(Constants.IMAGE_CODE_Count_KEY_STR + ip, Constants.IMAGE_CODE_COUNT_EXPIRE_SECOND, ImageCodeCount);
            }else{
                ImageCodeCount = (ImageCodeCount) smsCountVoGetsResponse.getValue();
                if (ImageCodeCount.getCount() < Constants.DEFAULT_DAY_MAX_IMAGE_CODE) {
                    ImageCode imc = (ImageCode) cacheClient.get(Constants.IMAGE_CODE_DETAIL_KEY_STR + ip);
                    vCode = new ImageValidateCode(135,38,5,200);
                    if (imc == null) {
                        imc = new ImageCode();
                        imc.setTimestamp(HelperUtil.getSecondStamp());
                        imc.setContent(vCode.getImageCode());
                    } else {
                        imc.setContent(vCode.getImageCode());
                        imc.setTimestamp(HelperUtil.getSecondStamp());
                    }
                    ImageCodeCount.setCount(ImageCodeCount.getCount() + 1);
                    cacheClient.set(Constants.IMAGE_CODE_DETAIL_KEY_STR + ip, Constants.SMS_SEND_EXPIRE_SECOND, imc);
                    cacheClient.cas(Constants.IMAGE_CODE_Count_KEY_STR + ip,
                            (int) ((24 * 3600) - (HelperUtil.getSecondStamp() - ImageCodeCount.getTimestamp())),
                            ImageCodeCount, smsCountVoGetsResponse.getCas());
                }else{
                    String info="访问次数超过最大数";
                    logger.info(info);
                    ImageValidationUtil.writeOutputInfo(info,response);
                    return null;
                }
            }
            vCode.write(response.getOutputStream());
            vCode = null;
        } catch (Exception e) {
            String info="获取图片验证码异常";
            logger.info(  info+e.getMessage());
            ImageValidationUtil.writeOutputInfo(info,response);
        }
        return "";
    }

    /**
     * 发送登录短信验证码
     *
     * @param telephone
     * @param smsUseType 验证码使用类型：1注册使用  2短信登陆使用  3忘记密码使用
     * @return
     */
    @RequestMapping(value = "/smsValidation", method =  { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> smsValidation(String telephone,String imageCode,
                                             @RequestParam(required=false)String signType,
                                             @RequestParam(required=false)Byte smsUseType) {
        Map<String, Object> map = getSuccessMap();
        try {
            if (StringUtil.isEmpty(telephone) ||  StringUtil.isEmpty(imageCode)) {
                map.put("errorCode", ErrorCode.PARAM_IS_NULL);
                map.put("msg", "参数为空");
                return map;
            }
/*            if (signType.equalsIgnoreCase("APP")) {
                if (compareVersion("1.0.3", "1.0.2") > 0) {
                    if (smsUseType == null) {
                        map.put("errorCode", ErrorCode.PARAM_IS_NULL);
                        map.put("msg", "参数为空");
                        return map;
                    }
                }
            }else{
                if (smsUseType == null) {
                    map.put("errorCode", ErrorCode.PARAM_IS_NULL);
                    map.put("msg", "参数为空");
                    return map;
                }
            }*/


            //===================2018-01-24 添加图片验证码=========================//
            String ip = getIpAddr(request);//相同ip？
            String imageKey = Constants.IMAGE_CODE_DETAIL_KEY_STR + ip;
            ImageCode imc = (ImageCode) cacheClient.get(imageKey);
            if (imc == null) {
                map.put("errorCode", ErrorCode.IMAGE_CODE_NOT_EXIST_ERROR);
                map.put("msg", "图片证码已过期，请重新获取");
                return map;
            } else if (!StringUtils.equalsIgnoreCase(imageCode, imc.getContent())) {
                map.put("errorCode", ErrorCode.IMAGE_CODE_ERROR);
                map.put("msg", "图片验证码错误");
                logger.info("图片验证码错误:" + imc.getContent() + "--" + imageCode);
                return map;
            }
            //===================2018-01-24 添加图片验证码=========================//


            boolean flag = Pattern.matches(Constants.REG_MOBILE, telephone);
            if (!flag) {
                map.put("errorCode", ErrorCode.USER_TELPHONE_FORMAT_ERROR);
                map.put("msg", "手机号码格式不正确");
                return map;
            }

//			boolean isRegister = false;
//			if (signType.equalsIgnoreCase("APP")) {
//				if (compareVersion("1.0.3", "1.0.2") < 0) {
//					isRegister = true;
//				}
//			}else if(signType.equalsIgnoreCase("WEB")){
//				isRegister = true;
//			}
//			if (isRegister) {
//				User user = userSvc.getUserByTelephone(telephone);
//				//帮用户注册用户账号
//				if (user == null) {
//					user = new User();
//					user.setTelephone(telephone);
//					user.setDeviceDesc(deviceDesc);
//					user.setDeviceIdentifier(deviceIdentifier);
//					user.setLongitude(longitude);
//					user.setLatitude(latitude);
//					user.setIp(appVersion.getAddressIp());
//					user.setVersion(appVersion.getAppVersion());
//					user.setCreateTime(new Date());
//					if (null != latitude && null != longitude) {
//						user.setCity(TxDiTuUtil.getCityByPosition(longitude, latitude));
//					}
//					Byte registerType = 0;
//					if (signType.equals("APP")) {
//						registerType = 1;
//					}else{
//						registerType = 2;
//					}
//					int resultVaule = userSvc.insertUser(user);
//					if (resultVaule <=0 ) {
//						map.put("errorCode", ErrorCode.CREATE_FAIL);
//						map.put("msg", "创建用户失败");
//						return map;
//					}
//				}
//			}

            String smsCountKey = Constants.SMS_LOGIN_PASSWD_COUNT_KEY_STR + telephone;
            String smsKey = Constants.SMS_LOGIN_PASSWD_KEY_STR + telephone;
            String code = SendSMS.generateValiateCode();
            SendSMS.sendUCP(telephone, map, smsCountKey, smsKey, new String[] { code,
                    Constants.SMS_VALIDATE_CODE_LIMIT_TIME }, cacheClient, CommonEnum.SMSTemplateId.BINDINGSMS_CODE.value);
            logger.info("登陆验证码 code:" + code);

//			cacheClient.set(imageKey, 0, imc);
            cacheClient.delete(imageKey);
        } catch (Exception e) {
            logger.error("exception:", e);
            map.put("errorCode", -1);
            map.put("msg", "异常:" + e.getMessage());
        }
        return map;
    }

 /*   *//**
     * 认证通过后，生成一个token返回客户端
     *
     * @param userName
     * @param platform
     * @return
     *//*
    @RequestMapping(value = "/getToken", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> getToken(@RequestParam("userName") String userName,
                                             @RequestParam(value = "platform", required = false) String platform,
                                             @RequestParam(value = "signType", required = false) String signType) {
        Map<String, Object> dataMap = getSuccessMap();
        if (platform == null || platform.equals("")) {
            platform = "kinder";
        }
        if (signType == null || signType.equals("")) {
            signType = "WEB";
        } else {
            signType = signType.toUpperCase();
        }
        String u_token = null;
        try {
            u_token = userSvcImpl.generalToken(userName, "kinder", signType);
            dataMap.put("errorCode", 0);
            dataMap.put("userName", userName);
            dataMap.put("token", u_token);
        } catch (Exception e) {
            dataMap.put("errorCode", -1);
            logger.error(e.getMessage(), e);
        }
        return dataMap;
    }*/


/*    *//**
     * 接受认证后的回调，将用户和utoken存储在当前用户cookie中，跳转至首页
     * 此处不做认证是否有效的校验，统一由LoginInterceptor去做拦截校验
     *//*
    @RequestMapping(value = "/writeCookie", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> writeCookie(String userName, String utoken, HttpServletRequest request,
                                        HttpServletResponse response) {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            User user = userSvcImpl.getUserByUserName(userName);
            if (user != null ) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("userId", user.getUserId());
                dataMap.put("errorCode", 0);
                dataMap.put("msg", "成功");
                dataMap.put("data", data);

                //写入cookie
                TokenUtil.writeCookie(userName, request, response, utoken);
            }else{
                dataMap.put("errorCode", -1);
                dataMap.put("msg", "用户账号不正确");
            }
        } catch (Exception e) {
            logger.error("exception:", e);
            dataMap.put("errorCode", -1);
            dataMap.put("msg", "登录异常:" + e.getMessage());
        }
        return dataMap;
    }*/

    /**
     * 判断手机号是否符合要求
     * @param telephone
     * @param smsUseType 验证码使用类型：1注册使用  2短信登陆使用  3忘记密码使用
     * @return
     */
    @RequestMapping(value="/validatePhoneNumber",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> validatePhoneNumber(String telephone,
                                                   @RequestParam(required=true)Integer smsUseType){
        Map<String, Object> map = getSuccessMap();
        map.put("msg", "手机号可用");
        try {
            if(smsUseType==null||StringUtil.isEmpty(telephone)){
                map.put("errorCode", ErrorCode.PARAM_IS_NULL);
                map.put("msg", "参数为空");
                return map;
            }
            if(smsUseType!=1&&smsUseType!=2&&smsUseType!=3){
                map.put("errorCode", ErrorCode.PARAM_ERROR);
                map.put("msg", "参数错误");
                return map;
            }
            boolean flag = Pattern.matches(Constants.REG_MOBILE, telephone);
            if (!flag) {
                map.put("errorCode", ErrorCode.USER_TELPHONE_FORMAT_ERROR);
                map.put("msg", "手机号码格式不正确");
                return map;
            }
            User user = userSvcImpl.getUserByPhoneNumber(telephone);
            if(user!=null&&smsUseType==1){
                map.put("errorCode", ErrorCode.USER_TELEPHONE_EXIST_ERROR);
                map.put("msg", "手机号已注册");
                return map;
            }else if(user==null&&(smsUseType==2||smsUseType==3)){
                map.put("errorCode", ErrorCode.USER_TELEPHONE_NOT_EXIST_ERROR);
                map.put("msg", "手机号未注册");
                return map;
            }
        } catch (Exception e) {
            logger.error("exception:", e);
            map.put("errorCode", -1);
            map.put("msg", "异常:" + e.getMessage());
        }
        return map;
    }

    /**
     * 判断用户名是否符合要求
     * @param userName
     * @return
     */
    @RequestMapping(value="/validateUserName",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> validateUserName(String userName){
        Map<String, Object> map = getSuccessMap();
        try {
            if (StringUtils.isEmpty(userName)) {
                map.put("msg", "默认使用手机号码");
                return map;
            }
            boolean flag = Pattern.matches(Constants.USERNAME_MOBILE, userName);
            if (!flag) {
                map.put("errorCode", ErrorCode.USER_USERNAME_FORMAT_ERROR);
                map.put("msg", "用户名格式不正确");
                return map;
            }
            User user = userSvcImpl.getUserByUserName(userName);
            if(user==null){
                map.put("msg", "用户名可用");
                return map;
            }else {
                map.put("errorCode", ErrorCode.USER_USERNAME_EXIST_ERROR);
                map.put("msg", "用户名已存在");
                return map;
            }
        } catch (Exception e) {
            logger.error("exception:", e);
            map.put("errorCode", -1);
            map.put("msg", "操作异常:" + e.getMessage());
        }
        return map;
    }


    /**
     * 判断密码是否符合要求
     * @param password
     * @return
     */
    @RequestMapping(value="/validatePassword",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> validatePassword(String password){
        Map<String, Object> map = getSuccessMap();
        try {
            if (StringUtils.isEmpty(password)) {
                map.put("errorCode", ErrorCode.PARAM_IS_NULL);
                map.put("msg", "参数为空");
                return map;
            }
            boolean flag = Pattern.matches(Constants.PASSWORD_MOBILE, password);
            if (!flag) {
                map.put("errorCode", ErrorCode.USER_PASSWORD_FORMATE_ERROR);
                map.put("msg", "密码格式不正确");
                return map;
            }
            map.put("msg", "密码可用");
        } catch (Exception e) {
            logger.error("exception:", e);
            map.put("errorCode", -1);
            map.put("msg", "操作异常:" + e.getMessage());
        }
        return map;
    }

    /**
     * 判断smsCode是否符合要求
     * @param smsCode
     * @return
     */
    public Map<String, Object> validateSmsCode(String smsCode, String telephone){
        Map<String, Object> map = getSuccessMap();
        try {
            if (StringUtils.isEmpty(smsCode)) {
                map.put("errorCode", ErrorCode.PARAM_IS_NULL);
                map.put("msg", "参数为空");
                return map;
            }
            String smsKey = Constants.SMS_LOGIN_PASSWD_KEY_STR + telephone;
            SMSVo smsVo = (SMSVo) cacheClient.get(smsKey);
            Boolean a=isNeedAuthCode.equals("1");
            System.out.println(isNeedAuthCode);
            Boolean aa= smsCode.equals("0000");
            if (isNeedAuthCode.equals("1") && smsCode.equals("0000") || (companyAccountNumber.equals("1") )) {

            } else if (smsVo == null) {
                map.put("errorCode", ErrorCode.USER_ERROR_SMS_CODE);
                map.put("msg", "短信验证码已过期，请重新发送并验证");
                return map;
            } else if (!smsVo.getContent().contains(smsCode)) {
                map.put("errorCode", ErrorCode.USER_ERROR_SMS_CODE);
                map.put("msg", "短信验证码错误");
                logger.info("验证码错误:" + smsVo.getContent() + "--" + smsCode);
                return map;
            }
        } catch (Exception e) {
            logger.error("exception:", e);
            map.put("errorCode", -1);
            map.put("msg", "操作异常:" + e.getMessage());
        }
        return map;
    }

    /**
     * 判断inviteCode是否符合要求
     * @param inviteCode
     * @return
     */
    @RequestMapping(value="/validateInviteCode",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> validateInviteCode(String inviteCode){
        Map<String, Object> map = getSuccessMap();
        try {
            if (StringUtils.isEmpty(inviteCode)) {
                map.put("msg", "inviteCode为空");
                return map;
            }
            boolean flag = Pattern.matches(Constants.INVITECODE_MOBILE, inviteCode);
            if (!flag) {
                map.put("errorCode", ErrorCode.USER_USERNAME_FORMAT_ERROR);
                map.put("msg", "邀请码格式不正确");
                return map;
            }
            User user = userSvcImpl.getUserByInviteCode(inviteCode);
            if(user!=null){
                map.put("msg", "邀请码可用");
                return map;
            }else {
                map.put("errorCode", ErrorCode.USER_USERNAME_EXIST_ERROR);
                map.put("msg", "邀请码不存在");
                return map;
            }
        } catch (Exception e) {
            logger.error("exception:", e);
            map.put("errorCode", -1);
            map.put("msg", "操作异常:" + e.getClass());
        }
        return map;
    }

    /**
     * 创建用户
     *
     * @return
     */
    @RequestMapping(value = "/createUser", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> createUser(User user,String smsCode,
                                          String inviteCode, String deviceIdentifier, Double longitude, Double latitude, String deviceDesc) {
        logger.info("注册用户, createUser, telephone=" + user.getPhoneNumber() + ", password=" + user.getPassword() + ", smsCode=" + smsCode
                + ", inviteCode=" + inviteCode);
        Map<String, Object> map = getSuccessMap();
        Map<String, Object> result = null;

        try {
            //判断smsCode是否符合要求
            result= validateSmsCode(smsCode,user.getPhoneNumber());
            if(result.get("errorCode")==null||(Integer)result.get("errorCode")!=1){
                return result;
            }
/*            //判断用户名是否符合要求
            result= validateUserName(user.getPhoneNumber());
            if(result.get("errorCode")==null||(Integer)result.get("errorCode")!=1){
                Integer i=(Integer)result.get("errorCode");
                if(i!=1)
                return result;
            }*/
            //判断telephone是否符合要求
            result= validatePhoneNumber(user.getPhoneNumber(),1);
            user.setUserName(user.getPhoneNumber());
            //int errorCode=(int)result.get("errorCode");
            if(result.get("errorCode")==null||(Integer)result.get("errorCode")!=1){
                return result;
            }
            //判断密码是否符合要求
            result= validatePassword(user.getPassword());
            if(result.get("errorCode")==null||(Integer)result.get("errorCode")!=1){
                return result;
            }
            //邀请码判定
            User inviteMan=null;
            if(!StringUtil.isEmpty(user.getInviteCode())){
                result= validateInviteCode(user.getInviteCode());
                Integer i=(Integer)result.get("errorCode");
                if(i!=1)
                    return result;
                else {
                    inviteMan=userSvcImpl.getUserByInviteCode(user.getInviteCode());
                    user.setInviteUserId(inviteMan.getUserId());
                }
            }

            int iValue = userSvcImpl.addUser(user);
            if (iValue <= ReturnValueConstants.OPERATE_INSERT_ZERO) {
                map.put("errorCode", -1);
                map.put("msg", "注册用户数据库异常");
                return map;
            }
            //使用邀请码注册
            if(!StringUtil.isEmpty(user.getInviteCode())){
            User pu=new User();
            pu.setUserId(inviteMan.getUserId());
            pu.setInviteNum(1);//add coin in it
            //pu.setUserCoin(Constants.INVITE_COIN_REWARD);
            userSvcImpl.updateUserWithCoinAndGold(pu);
            }
/*            //如果使用邀请码注册：通知mq进行邀请相关操作
            if (code != null) {
                ReturnMessage returnMessage = new ReturnMessage();
                returnMessage.setInviteUsreId(user.getUid());//被邀请人id
                returnMessage.setUserId(code.getUserId());	//邀请人id
                msgProducer.send(MsgCommandId.DIDI_INVITE_AWARD_ID, returnMessage);
            }*/
        } catch (Exception e) {
            logger.error("exception:", e);
            map.put("errorCode", -1);
            map.put("msg", "异常:" + e.getMessage());
        }
        return map;
    }


}
