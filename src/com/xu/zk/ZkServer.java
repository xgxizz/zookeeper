package com.xu.zk;

import org.apache.zookeeper.*;

import java.io.IOException;

public class ZkServer {

    private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;
    private String parentNode = "/servers";
    public void getConnection() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });
    }
    //注册服务器节点
    private void regist(String hostname) throws KeeperException, InterruptedException {
        zkClient.create(parentNode + "/server", hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    //业务逻辑处理
    private void business(String hostname) {
        System.out.println(hostname + "is online!");
    }
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZkServer zkServer = new ZkServer();
        // 1 获取连接zkServer
        zkServer.getConnection();
        // 2 注册节点信息
        zkServer.regist(args[0]);
        // 3 业务逻辑处理
        zkServer.business(args[0]);
        Thread.sleep(Long.MAX_VALUE);
    }
}
