package com.swj.ics.zookeeper.curator.watcher;

import java.util.concurrent.TimeUnit;
import com.swj.ics.zookeeper.curator.CuratorHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/26.
 */
public class NodeCacheWatcher {
    private static final Logger logger = LoggerFactory.getLogger(NodeCacheWatcher.class);
    private static CuratorFramework cf = null;
    
    public static void main(String[] args) throws Exception {
         cf = CuratorHelper.init();
         //建立一个cache缓存

        NodeCache nodeCache = new NodeCache(cf,"/super",false);
        //todo:这里刚开始省略了，因此监听不到程序的变化。因此调用start很重要
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            /**
             * 触发事件为节点新增和修改时，删除也触发，但是childData 为null
             * @throws Exception
             */
            @Override
            public void nodeChanged() throws Exception {
                ChildData childData = nodeCache.getCurrentData();
                if (childData != null) {
                    logger.info("路径为：{}",childData.getPath());
                    logger.info("数据为：{}",new String(childData.getData()));
                    logger.info("stat为：{}",childData.getStat());
                }             
            }
        });
        TimeUnit.SECONDS.sleep(1);
        String nodePath = "/super";
        cf.create().forPath(nodePath,"123".getBytes());
        
        TimeUnit.SECONDS.sleep(1);
        cf.setData().forPath(nodePath,"456".getBytes());
        
        
        TimeUnit.SECONDS.sleep(15);
        cf.delete().forPath(nodePath);
        
        TimeUnit.SECONDS.sleep(1);
        System.out.println("done !");
    }
}
