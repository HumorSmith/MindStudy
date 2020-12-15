package com.jhzl.mindstudy.model;

import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MindNode {
    public MindData data;
    public MindStyle style;
    public RelationShip relationShip;



    public MindNode(MindData data) {
        this.data = data;
        relationShip = new RelationShip();
        style = new MindStyle();
    }

    public static class MindData {
        public String title;
        public String content;

        public MindData(String title) {
            this.title = title;
        }
    }

    public static class MindStyle {
        public int color;
        public View view;
    }

    public static class RelationShip {
        public List<MindNode> children = new LinkedList<>();
        public MindNode parent;
    }
}
