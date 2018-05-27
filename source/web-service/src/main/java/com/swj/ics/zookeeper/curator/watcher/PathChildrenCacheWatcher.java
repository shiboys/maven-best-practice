package com.swj.ics.zookeeper.curator.watcher;

import java.util.concurrent.TimeUnit;
import com.swj.ics.zookeeper.curator.CuratorHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/26.
 */
public class PathChildrenCacheWatcher {
    private static final Logger logger = LoggerFactory.getLogger(PathChildrenCacheWatcher.class);
    private static CuratorFramework cf = null;
    
    public static void main(String[] args) throws Exception {
         cf = CuratorHelper.init();
         //建立一个cache缓存

        //pathChildrenCache,第三个参数为是否接收节点的数据内容，false为不接收
        PathChildrenCache pathChildrenCache = new PathChildrenCache(cf,"/super",true);
        
        //初始化的时候就要进行缓存监听
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            /**
             * 概要说明，监听子节点的添加修改删除
             * @param cf curatorFramework
             * @param e event
             * @throws Exception
             */
            @Override
            public void childEvent(CuratorFramework cf, PathChildrenCacheEvent e) throws Exception {
                ChildData childData = e.getData();
                switch (e.getType()) {
                    case CHILD_ADDED:
                        logger.info("child added:{}",childData.getPath());
                        break;
                    case CHILD_UPDATED:
                        logger.info("child updated:{}",childData.getPath());
                        break;
                    case CHILD_REMOVED:
                        logger.info("child removed:{}",childData.getPath());
                        break;
                    default:
                        break;
                }
            }
        });
        
        TimeUnit.SECONDS.sleep(1);
        String nodePath = "/super";
        Stat stat = cf.checkExists().forPath(nodePath);
        if (stat == null) {
            cf.create().forPath(nodePath,"init".getBytes());
        }
        //添加子节点
        TimeUnit.SECONDS.sleep(1);
        cf.create().forPath(nodePath + "/c1","c1内容".getBytes());
        TimeUnit.SECONDS.sleep(1);
        cf.create().forPath(nodePath + "/c2","c2内容".getBytes());
       
        //修改子节点
        TimeUnit.SECONDS.sleep(1);
        cf.setData().forPath(nodePath + "/c1","c1内容更新".getBytes());
        
        //删除子节点
        TimeUnit.SECONDS.sleep(1);
        cf.delete().forPath(nodePath + "/c2");
        
        //删除节点
        TimeUnit.SECONDS.sleep(1);
        cf.delete().deletingChildrenIfNeeded().forPath(nodePath );

        TimeUnit.SECONDS.sleep(10);
        System.out.println("done!");
        
        /*
        * 最终的打印结果如下
        * 2018-02-26 22:15:58,984 - INFO  - [PathChildrenCache-0:PathChildrenCacheWatcher$1@45] - child added:/super/c1
2018-02-26 22:15:59,981 - INFO  - [PathChildrenCache-0:PathChildrenCacheWatcher$1@45] - child added:/super/c2
2018-02-26 22:16:00,993 - INFO  - [PathChildrenCache-0:PathChildrenCacheWatcher$1@48] - child updated:/super/c1
2018-02-26 22:16:02,003 - INFO  - [PathChildrenCache-0:PathChildrenCacheWatcher$1@51] - child removed:/super/c2
2018-02-26 22:16:03,047 - INFO  - [PathChildrenCache-0:PathChildrenCacheWatcher$1@51] - child removed:/super/c1
done!
        * */
        
    }
}
