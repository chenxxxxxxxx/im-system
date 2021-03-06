package com.github.im.tcp.dispatcher;

import lombok.Data;

/**
 * @author wangsz
 * @create 2020-03-29
 **/
@Data
public class DispatcherAddress {

    private String hostName;
    private String ip;
    private int port;

    public DispatcherAddress(String hostName, String ip, int port) {
        this.hostName = hostName;
        this.ip = ip;
        this.port = port;
    }
}
