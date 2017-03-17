package cn.hm.oil.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import cn.hm.oil.domain.Tips;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.core.utility.StringUtility;

public class AndroidPushMessageSample {
	//百度推送应用所对应的key值
    private static String apiKey = "Qx9rtbaTWxoOYlCwjwWMcePI";
    private static String secretKey = "zzIMeyxOEax2Ekz4EWBMp0jtIj9Wifqa";

    /**
     * 推送单播消息
     * @param userId
     * @param ChannelId
     * @param title
     * @param message
     */
    public static void pushMessage(String userId, Long ChannelId, String title, String message) {
    	// 1. 设置developer平台的ApiKey/SecretKey
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            // 4. 创建请求类对象
            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
                                      // 4:ios 5:wp
            //request.setChannelId(3870620768511225045L);
            //request.setUserId("995379719876257548");

            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
            request.setChannelId(ChannelId);
            request.setUserId(userId);

            //request.setMessage("Hello Channel");

            request.setMessageType(0);
            request.setMessage("{\"title\":\"" + title +"\",\"description\":\"" + message + "\",customContentString:\"" +"sfsaf" +"\"}");
            
            // 5. 调用pushMessage接口
            PushUnicastMessageResponse response = channelClient
                    .pushUnicastMessage(request);

            // 6. 认证推送成功
            System.out.println("push amount : " + response.getSuccessAmount());

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            System.out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }

    }
    
    /**
     * 推送广播消息 message_type = 0 (默认为0)
     * @param title
     * @param message
     */
    public static void pushMessageAll(String title, String message) {
    	// 1. 设置developer平台的ApiKey/SecretKey
        String apiKey = "Qx9rtbaTWxoOYlCwjwWMcePI";
        String secretKey = "zzIMeyxOEax2Ekz4EWBMp0jtIj9Wifqa";
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            // 4. 创建请求类对象
            PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
                                      // 4:ios 5:wp

            //request.setMessage("你好");
            // 若要通知，
            request.setMessageType(0);
            request.setMessage("{\"title\":\"" + title + "\",\"description\":\"" + message + "\"}");

            // 5. 调用pushMessage接口
            PushBroadcastMessageResponse response = channelClient
                    .pushBroadcastMessage(request);

            // 6. 认证推送成功
            System.out.println("push amount : " + response.getSuccessAmount());

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            System.out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }

    }
    
    /**
     * 推送单播通知
     * @param userId
     * @param channelId
     * @param title
     * @param message
     */
    public static void pushNotice(String userId, Long channelId, String title, Tips tip) {

        

        // 1. 设置developer平台的ApiKey/SecretKey
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            // 4. 创建请求类对象
            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
                                      // 4:ios 5:wp
            request.setChannelId(channelId);
            request.setUserId(userId);

            List<String> name = new ArrayList<String>();
            name.add("url");
            name.add("tips_id");
            //JSONObject json = new JSONObject(list);
            
            List<String> content = new ArrayList<String>();
            content.add(tip.getUrl());
            content.add(tip.getId() + "");
            
            request.setMessageType(1);
            request.setMessage("{\"title\":\"" + title + "\","
            		+ "\"description\":\"" + tip.getContent() + "\","
            		+ "\"custom_content\":\"" + toJson(name, content) + "\"}");
            
            // 5. 调用pushMessage接口
            PushUnicastMessageResponse response = channelClient
                    .pushUnicastMessage(request);

            // 6. 认证推送成功
            System.out.println("push amount : " + response.getSuccessAmount());

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            System.out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }

    }
    
    public static String toJson(List<String> name, List<String> content) {
    	StringBuffer str = new StringBuffer();
    	str.append("{");
    	int i = 0;
    	for(String s : name) {
    		if(i > 0) {
    			str.append(",");
    		}
    		str.append("'" + s + "':");
    		str.append("'" + content.get(i) + "'");
    		i++;
    	}
    	str.append("}");
    	return str.toString();
    }

}
