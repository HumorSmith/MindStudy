package com.jhzl.mindstudy.manager;

import android.util.Log;

import com.jhzl.mindstudy.model.MindNode;

import java.util.LinkedList;
import java.util.List;

public class MindNodeManager {
    public static final String TAG = MindNodeManager.class.getSimpleName();
    private static final MindNodeManager mMindNodeManager = new MindNodeManager();

    private MindNodeManager() {
    }

    public static MindNodeManager getInstance() {
        return mMindNodeManager;
    }

    private List<MindNode> mAllMindNode = new LinkedList<>();
    public MindNode mRootNode = new MindNode(new MindNode.MindData("无主题" +
            ""));

    public void addMindNode(MindNode node) {
        if (node == null) {
            Log.e(TAG, "addMindNode failed cause node is null");
            return;
        }
        mAllMindNode.add(node);
    }

    public void removeMindNode(MindNode node) {
        if (node == null) {
            Log.e(TAG, "deleteMindNode failed cause node is null");
            return;
        }
        mAllMindNode.remove(node);
    }


    public MindNode getRootNode(){
        return mRootNode;
    }


    public MindNode getRootMindNode() {
        return mRootNode;
    }

    public void setRootNode(MindNode mindNode) {
        mRootNode = mindNode;
    }
}
