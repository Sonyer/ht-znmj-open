package com.handturn.bole.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
@Data
public class CommonTree<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String href;
    private String title;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<CommonTree<T>> childs = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private T data;


    public static <T> CommonTree<T> buildTree(List<CommonTree<T>> nodes,boolean removeRoot,String rootResourceId) {
        if (nodes == null) {
            return null;
        }
        List<CommonTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if(removeRoot){
                if (rootResourceId.equals(pid)) {
                    topNodes.add(children);
                    return;
                }
            }else{
                if (pid == null || "0".equals(pid)) {
                    topNodes.add(children);
                    return;
                }
            }

            for (CommonTree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChilds().add(children);
                    children.setHasParent(true);
                    parent.setHasChild(true);
                    return;
                }
            }
        });

        CommonTree<T> root = new CommonTree<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setHasChild(true);
        root.setChecked(true);
        root.setChilds(topNodes);
        Map<String, Object> state = new HashMap<>(16);
        root.setState(state);
        return root;
    }

}