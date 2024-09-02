package org.hjan.controller;

import com.google.gson.Gson;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author HJan
 * @Date 2024/8/24 15:05
 * @Description ！！！You need to change the serverIP address in the .kube/config file to a public IP address
 */
@RestController
@RequestMapping("/container")
@Slf4j
public class WebShellController {

    @Autowired
    private Gson gson;

    /**
     * list all pods
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getPods")
    public List<String> hello() throws Exception {
        CoreV1Api api = new CoreV1Api();
        // Call the client API to get all pod information
        V1PodList v1PodList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        // Use gson to serialize the collection object into JSON and print it out in the log
        log.info("pod info \n{}", gson.toJson(v1PodList));
        for (V1Pod pod : v1PodList.getItems()) {
            System.out.println("Pod name: " + pod.getMetadata().getName());
        }
        return v1PodList
                .getItems()
                .stream()
                .map(value ->
                        value.getMetadata().getNamespace()
                                + ":"
                                + value.getMetadata().getName())
                .collect(Collectors.toList());
    }

}
