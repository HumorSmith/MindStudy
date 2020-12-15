package com.jhzl.mindstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jhzl.mindstudy.factory.MindNodeFactory;
import com.jhzl.mindstudy.manager.MindNodeManager;
import com.jhzl.mindstudy.model.MindNode;
import com.jhzl.mindstudy.ui.ArtBoard;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArtBoard artBoard = findViewById(R.id.artboard);
        Button testDrawBtn = findViewById(R.id.test_draw_btn);
        MindNode mindNode = new MindNode(new MindNode.MindData("root节点"));
        MindNodeManager.getInstance().setRootNode(mindNode);
        for (int i = 0; i < 10; i++) {
            MindNodeFactory.create( MindNodeManager.getInstance().getRootNode(),"哈哈"+i);
        }

        for (int i = 0; i < 10; i++) {
            MindNode firstChild = MindNodeFactory.create(MindNodeManager.getInstance().getRootNode().relationShip.children.get(0),"孙节点"+i);
        }


        testDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artBoard.drawAll();
                artBoard.requestLayout();
            }
        });
    }
}