package org.hjan.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author HJan
 * @Date 2024/9/1 18:21
 * @Description
 */
@Data
@Accessors(chain = true)
public class PodPO {

    private String namespace;
    private String podName;
    private String containerName;
}
