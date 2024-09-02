package org.hjan.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

/**
 * @Author HJan
 * @Date 2024/9/1 13:56
 * @Description
 */
@Configuration
public class KubernetesConfig {
    final static String kubeConfigPath = "D:\\projects\\train-system\\container-service\\src\\main\\resources\\config";  //.kube/config文件存放路径
    @Bean
    public ApiClient apiClient() throws IOException {
        KubeConfig kubeConfig = KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath));
        ApiClient client = ClientBuilder.kubeconfig(kubeConfig).build();
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
        return client;
    }
}
