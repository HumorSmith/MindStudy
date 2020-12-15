package com.jhzl.mindstudy.factory;

import com.jhzl.mindstudy.model.MindNode;

public class MindNodeFactory {


    public static MindNode create(MindNode parentNode, String content) {
        MindNode mindNode = new MindNode(new MindNode.MindData(content));
        mindNode.relationShip.parent = parentNode;
        parentNode.relationShip.children.add(mindNode);
        return mindNode;
    }
}
