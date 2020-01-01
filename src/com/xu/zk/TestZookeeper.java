package com.xu.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestZookeeper {

    //特别提醒，以下连接的是客户端的ip+端口，不要写成与leader通信的接口。
    //链连接zkServer
    private String  connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    //超时时间
    private int sessionTimeout = 2000;
    ZooKeeper zooKeeper;
    @Before
    public void init() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            //回调方法
            @Override
            public void process(WatchedEvent event) {
//                try {
//                    //持续监听节点是否存在
//                    Stat exists = zooKeeper.exists("/hello", true);//true 表示执行回调
//                    System.out.println(exists == null? "not exist":"exist");
//                } catch (KeeperException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println("------------------执行回调------------------------");
                System.out.println(event.getType() + "\t" + event.getPath());
                List<String> children = null;
                try {
                    children = zooKeeper.getChildren("/", true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String child: children) {
                    System.out.println(child);
                }
                System.out.println("------------------回调完成--------------------");
            }
        });
    }
    //创建子节点
    @Test
    public void create() throws KeeperException, InterruptedException {
        String path = zooKeeper.create("/idea", "idea_test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println("path = " + path);
    }
    //获取子节点
    @Test
    public void getChild() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/", true);
        for (String child: children) {
            System.out.println(child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }
    //判断znode是否存在
    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat exists = zooKeeper.exists("/hello", true);//true 表示执行回调
        System.out.println(exists == null? "not exist":"exist");
        Thread.sleep(Long.MAX_VALUE);
    }

}
