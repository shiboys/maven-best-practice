package com.swj.ics.zookeeper.book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swj on 2018/2/24.
 * Auxiliary(助动词；辅助者，辅助物；附属机构) cache to handle changes to the lists of tasks and of workers.
 */
public class ChildrenCache {
    
    private List<String> children;

    public ChildrenCache(List<String> children) {
        this.children = children;
    }

    public ChildrenCache() {
        children = null;
    }
    
    List<String> getList() {
        return children;
    }
    
    List<String> addedAndSet(List<String> newChildren) {
        List<String> diff = null;
        if (children == null) {
            diff = new ArrayList<>(newChildren);
        } else if (newChildren != null) {
            for (String child : newChildren) {
                if (!children.contains(child)) {
                    if (diff == null) {
                        diff = new ArrayList<>();
                    }
                    
                    diff.add(child);
                }
            }
        }
        //缓存替换为新的集合
        this.children = newChildren;
        return diff;
    }
    
    List<String> removedAndSet(List<String> newChildren) {
        List<String> diff = null;
        if (children != null) {
            for (String child : newChildren) {
                if (!children.contains(child)) {
                    if (diff == null) {
                        diff = new ArrayList<>();
                    }
                    diff.add(child);
                }
            }
        }
        this.children = newChildren;
        return diff;
    }
}
