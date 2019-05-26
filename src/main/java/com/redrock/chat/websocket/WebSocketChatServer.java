package com.redrock.chat.websocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //创建一个线程安全的map
    private static Map<String, WebSocketChatServer> users = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //放入map中的key,用来表示该连接对象
    private String username;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        this.username = username;
        //将session_id作为主键
        users.put(session.getId(), this);   //加入map中,这里使用session_id作为key
        addOnlineCount();           //在线数加1
        sendMessageToAll(Message.jsonStr(Message.ENTER, "", "", "", getOnlineCount()));
    }

    /**
     * 收到客户端消息后触发的方法
     * 这里规定传递的消息为JSON字符串 方便传递更多参数
     */

    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = JSON.parseObject(jsonStr, Message.class);

        if (message.getTo() == null || message.getTo().equals("") || message.getTo().toUpperCase().equals("ALL")) {
            sendMessageToAll(Message.jsonStr(Message.SPEAK, message.getUsername(), message.getTo(), message.getMsg(), getOnlineCount()));
        } else {
            String[] userlist = message.getTo().split(",");
            for (String user : userlist) {
                if(!user.equals(message.getUsername())) {
                    sendMessageToSomeBody(user, Message.jsonStr(Message.SPEAK, message.getUsername(), message.getTo(), message.getMsg(), getOnlineCount()));
                }
            }
         }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        users.remove(session.getId());  //从set中删除
        subOnlineCount();           //在线数减1
        sendMessageToAll(Message.jsonStr(Message.ENTER, "", "", "", getOnlineCount()));
    }

    /**
     * 打印错误日志
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误 session: " + session);
        error.printStackTrace();
    }

    /**
     * 发送信息给所有人
     */
    private static void sendMessageToAll(String msg) {
        for (Map.Entry<String, WebSocketChatServer> entry : users.entrySet()) {
            String username = entry.getKey();
            Session session = entry.getValue().session;
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息给指定用户
     */
    private void sendMessageToSomeBody(String username, String message) {
        try {
            List<String> key = getKey(users, username);
            for (int i = 0; i < key.size(); i++) {
                users.get(key).session.getBasicRemote().sendText(message);    //发送给指定用户
            }
            this.session.getBasicRemote().sendText("from:" + this.username + ",to:" + username + "," + "msg:" + message);   //发送给自己
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketChatServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketChatServer.onlineCount--;
    }

    /**
     * 根据用户名拿到对应的session_id
     *
     * @return keyList(存有session_id)
     */
    public static List<String> getKey(Map<String, WebSocketChatServer> map, String username) {
        List<String> keyList = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key).username.equals(username)) {
                keyList.add(key);
            }
        }
        return keyList;
    }


}