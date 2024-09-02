package org.hjan.controller;

import io.kubernetes.client.Exec;
import lombok.extern.slf4j.Slf4j;
import org.hjan.domain.PodPO;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author HJan
 * @Date 2024/9/1 12:37
 * @Description
 */
@Slf4j
public class CommandWebSocketHandler extends TextWebSocketHandler {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Map<WebSocketSession, PodPO> podMap = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String[] command =  message.getPayload().split(" ");
        System.out.println("Received command: " + Arrays.toString(command));
        //Thread pool calls to prevent blocking
        executorService.submit(() -> {
            try {
                //从podMap中获取pod信息
                PodPO podPO = podMap.get(session);
                Process proc = new Exec()
                        .exec(podPO.getNamespace(), podPO.getPodName(), command, podPO.getContainerName(),true, true);
                String output = new String(proc.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                log.info("print:{}",output);
                session.sendMessage(new TextMessage(output));
                proc.destroy();
            } catch (Exception e) {
                try {
                    session.sendMessage(new TextMessage("Error executing command: " + e.getMessage()));
                } catch (IOException ex) {
                   ex.printStackTrace();
                }
                e.printStackTrace();
            }
        });
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //解析namespace podName containerName
        try {
            Map<String, String> queryParams = parseQueryString(Objects.requireNonNull(session.getUri()).getQuery());
            String namespace = queryParams.get("namespace");
            String podName = queryParams.get("podName");
            String containerName = queryParams.get("containerName");
            //构造podPO对象存入podMap
            PodPO podPO = new PodPO().setNamespace(namespace).setPodName(podName).setContainerName(containerName);
            podMap.put(session, podPO);
            System.out.println(namespace+"-"+podName);
        }catch (NullPointerException e){
            session.sendMessage(new TextMessage("no params"));
            throw new NullPointerException();
        }
    }
    //解析出参数存入map
    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryPairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                    URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return queryPairs;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        podMap.remove(session);
    }
}
