package com.handturn.bole.common.entity;

import com.handturn.bole.system.entity.SysOrganizationDep;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
@Data
public class CommonTreeSelect<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String href;
    private String name;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<CommonTreeSelect<T>> children = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private Object data;


    public static <T> List<CommonTreeSelect<T>> buildTreeSelect(List<CommonTreeSelect<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<CommonTreeSelect<T>> result = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                result.add(children);
                return;
            }
            for (CommonTreeSelect<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    n.getChildren().add(children);
                    children.setHasParent(true);
                    n.setHasChild(true);
                    return;
                }
            }
        });

        return result;
    }

}