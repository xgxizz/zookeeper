package com.xu.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZkClient {

    private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;
    private String parentNode = "/servers";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZkClient client = new ZkClient();
        // 1 获取连接
        client.getConnection();
        // 2 监听服务器节点路径
        client.getServerList();
        // 3 业务处理
        client.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    //获取服务器列表
    private void getServerList() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/servers", true);

        List<String> serverList = new ArrayList<>();
        //获取每个节点中的数据
        for (String child:children) {
            byte[] data = zkClient.getData("/servers/" + child, false, null);
            serverList.add(new String(data));
        }
        System.out.println(serverList);
    }

    private void getConnection() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    getServerList();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
