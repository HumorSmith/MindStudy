package com.jhzl.mindstudy.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jhzl.mindstudy.R;
import com.jhzl.mindstudy.manager.MindNodeManager;
import com.jhzl.mindstudy.model.MindNode;
import com.jhzl.mindstudy.touch.MoveAndScaleHandler;

import java.util.List;

import static com.jhzl.mindstudy.util.DensityUtils.dp2px;

public class ArtBoard extends ViewGroup {
    private MoveAndScaleHandler mMoveAndScaleHandler;

    enum ArtBoardStyle {
        AUTO_BALANCE,
        ALL_LEFT
    }

    private ArtBoardStyle mArtBoardStyle = ArtBoardStyle.AUTO_BALANCE;

    public int COL_SPACE = 100;
    private int HEIGHT_MARGIN = 20;
    private Context mContext;
    public static final String TAG = ArtBoard.class.getSimpleName();

    public ArtBoard(@NonNull Context context) {
        super(context);
    }

    public ArtBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 获取屏幕的高度
        sreenH = getScreenSize(((Activity) mContext))[1];
        mMoveAndScaleHandler = new MoveAndScaleHandler(context, this);
    }

    private int sreenH;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mMoveAndScaleHandler.onTouchEvent(event);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        // 获得子View个数

        MindNode rootMindNode = MindNodeManager.getInstance().getRootMindNode();
        View rootNodeView = rootMindNode.style.view;
        if (rootNodeView == null) {
            return;
        }
        rootNodeView.layout(getMeasuredWidth() / 2 , getMeasuredHeight() / 2 , getMeasuredWidth() / 2 + rootNodeView.getMeasuredWidth(), getMeasuredHeight() / 2 + rootNodeView.getMeasuredHeight());


        switch (mArtBoardStyle) {
            case AUTO_BALANCE:
                layoutLeft(rootMindNode, (int) rootNodeView.getX(), (int) rootNodeView.getY());
                layoutRight(rootMindNode, (int) rootNodeView.getX(), (int) rootNodeView.getY());
                break;
            case ALL_LEFT:
                break;
        }


//        int childCount = getChildCount();
//        // 设置一个变量保存到父View左侧的距离
//        int mLeft = 0;
//        // 遍历子View
//        for (int i = 0; i < childCount; i++) {
//
//            View childView = getChildAt(i);
//            // 获得子View的高度
//            int childViewHeight = childView.getMeasuredHeight();
//            // 获得子View的宽度
//            int childViewWidth = childView.getMeasuredWidth();
//            // 让子View在竖直方向上显示在屏幕的中间位置
//            int height = sreenH / 2 - childViewHeight / 2;
//            // 调用layout给每一个子View设定位置mLeft,mTop,mRight,mBottom.左上右下
//            childView.layout(mLeft, height, mLeft + childViewWidth, height
//                    + childViewHeight);
//            // 改变下一个子View到父View左侧的距离
//            mLeft += childViewWidth;
//        }
    }

    private void layoutRight(MindNode mindNode, int parentX, int parentY) {
        if (mindNode == null || mindNode.relationShip == null || mindNode.relationShip.children.size() == 0) {
            return;
        }
        List<MindNode> rightMindList = getPartOrChildren(mindNode, getRightNodeList(mindNode));
        int leftSize = rightMindList.size();

        //ParentY在列表中间
        int halfSize = (int) (leftSize / 2f);
        int baseLineY = (int) (parentY + halfSize * (mindNode.style.view.getMeasuredHeight() + 20));
        for (int i = 0; i < rightMindList.size(); i++) {
            View view = rightMindList.get(i).style.view;
            int left = parentX + COL_SPACE + view.getMeasuredWidth();
            int top = baseLineY - i * (view.getMeasuredHeight() + HEIGHT_MARGIN);
            view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
            layoutRight(rightMindList.get(i), (int) view.getX(), (int) view.getY());
        }
    }

    private List<MindNode> getRightNodeList(MindNode rootMindNode) {
        int size = rootMindNode.relationShip.children.size();
        return rootMindNode.relationShip.children.subList(size / 2, size - 1);
    }

    private void layoutLeft(MindNode mindNode, int parentX, int parentY) {
        if (mindNode == null || mindNode.relationShip == null || mindNode.relationShip.children.size() == 0) {
            return;
        }
        List<MindNode> leftMindList = getPartOrChildren(mindNode, getLeftNodeList(mindNode));

        int leftSize = leftMindList.size();
        //ParentY在列表中间
        int halfSize = (int) (leftSize / 2f);


        int baseLineY = (int) (parentY + halfSize * (mindNode.style.view.getMeasuredHeight() + 20));

        for (int i = 0; i < leftMindList.size(); i++) {
            View view = leftMindList.get(i).style.view;
            int left = parentX - COL_SPACE - view.getMeasuredWidth();
            int top = baseLineY - i * (view.getMeasuredHeight() + HEIGHT_MARGIN);
            view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
            layoutLeft(leftMindList.get(i), (int) view.getX(), (int) view.getY());
        }
    }

    private List<MindNode> getLeftNodeList(MindNode rootMindNode) {
        int size = rootMindNode.relationShip.children.size();
        return rootMindNode.relationShip.children.subList(0, size / 2);
    }

//    public void layoutNode(MindNode mindNode,int left,int top,int right,int bottom){
//        MindNode.RelationShip relationShip = mindNode.relationShip;
//        mindNode.style.view.layout(left,top,);
//    }

    /**
     * 获取屏幕尺寸
     */
    public static int[] getScreenSize(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, heightMeasureSpec);
//        }

//        MindNode rootMindNode = MindNodeManager.getInstance().getRootMindNode();
//        switch (artBoardStyle) {
//            case AUTO_BALANCE:
//                measureAutoBalance();
//                break;
//            case ALL_LEFT:
//                break;
//        }


    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        MindNode rootNode = MindNodeManager.getInstance().getRootNode();
        if (rootNode != null) {
            switch (mArtBoardStyle) {
                case AUTO_BALANCE:
                    List<MindNode> rightNodeList = getRightNodeList(rootNode);
                    List<MindNode> leftNodeList = getLeftNodeList(rootNode);
                    drawTreeLineLeft(canvas, rootNode);
                    drawTreeLineRight(canvas, rootNode);

                    break;
                case ALL_LEFT:
                    break;
            }

        }

    }


    /**
     * 绘制树形的连线
     *
     * @param canvas
     * @param mindNode
     */
    private void drawTreeLineLeft(Canvas canvas, MindNode mindNode) {
        MindNode.RelationShip relationShip = mindNode.relationShip;
        if (relationShip == null || relationShip.children == null || relationShip.children.size() == 0) {
            return;
        }
        if (mindNode.style.view == null) {
            return;
        }

        List<MindNode> nodeList = getPartOrChildren(mindNode, getLeftNodeList(mindNode));
        if (nodeList == null) {
            return;
        }

        View rootView = mindNode.style.view;
        for (int i = 0; i < nodeList.size(); i++) {
            MindNode node = relationShip.children.get(i);
            View view = node.style.view;
            drawLineLeftImpl(canvas, rootView, view);
            drawTreeLineLeft(canvas, node);
        }
    }



    /**
     * 绘制树形的连线
     *
     * @param canvas
     * @param mindNode
     */
    private void drawTreeLineRight(Canvas canvas, MindNode mindNode) {
        MindNode.RelationShip relationShip = mindNode.relationShip;
        if (relationShip == null || relationShip.children == null || relationShip.children.size() == 0) {
            return;
        }
        if (mindNode.style.view == null) {
            return;
        }

        List<MindNode> nodeList = getPartOrChildren(mindNode, getRightNodeList(mindNode));
        if (nodeList == null) {
            return;
        }

//        View rootView = mindNode.style.view;

//        drawLineRightImpl(canvas, rootView, nodeList.get(0).style.view);
//        drawLineRightImpl(canvas, rootView, nodeList.get(1).style.view);


        View rootView = mindNode.style.view;
        for (int i = 0; i < nodeList.size(); i++) {
            MindNode node = nodeList.get(i);
            View view = node.style.view;
            drawLineRightImpl(canvas, rootView, view);
            drawTreeLineRight(canvas, node);
        }
    }

    private List<MindNode> getPartOrChildren(MindNode mindNode, List<MindNode> part) {
        MindNode rootMindNode = MindNodeManager.getInstance().getRootMindNode();
        List<MindNode> rightMindList = null;
        if (mindNode == rootMindNode) {
            rightMindList = part;
        } else {
            rightMindList = mindNode.relationShip.children;
        }
        return rightMindList;
    }

    /**
     * 绘制两个View直接的连线
     *
     * @param canvas
     * @param from
     * @param to
     */
    Paint mPaint = new Paint();
    Path mPath = new Path();

    private void drawLineLeftImpl(Canvas canvas, View from, View to) {
        if (to.getVisibility() == GONE) {
            return;
        }

        mPaint.setStyle(Paint.Style.STROKE);

        float width = 2f;

        mPaint.setStrokeWidth(dp2px(mContext, width));
        mPaint.setColor(mContext.getResources().getColor(R.color.chelsea_cucumber));

        int formY = (int) (from.getTop()+from.getMeasuredHeight()/2);
        int formX = (int) (from.getLeft());


        int toY = (int) (to.getTop()+to.getMeasuredHeight()/2);
        int toX = (int) (to.getRight());

        mPath.reset();
        mPath.moveTo(formX, formY);
        mPath.lineTo(toX,toY);
//        mPath.quadTo(toX - dp2px(mContext, 15), toY, toX, toY);
        canvas.drawPath(mPath, mPaint);
    }



    private void drawLineRightImpl(Canvas canvas, View from, View to) {
        if (to.getVisibility() == GONE) {
            return;
        }

        mPaint.setStyle(Paint.Style.STROKE);

        float width = 2f;

        mPaint.setStrokeWidth(dp2px(mContext, width));
        mPaint.setColor(mContext.getResources().getColor(R.color.chelsea_cucumber));

        int formY = (int) (from.getTop()+from.getMeasuredHeight()/2);
        int formX = (int) (from.getRight());


        int toY = (int) (to.getTop()+to.getMeasuredHeight()/2);
        int toX = (int) (to.getLeft());

        mPath.reset();
        mPath.moveTo(formX, formY);
        mPath.lineTo(toX,toY);
//        mPath.quadTo(toX - dp2px(mContext, 15), toY, toX, toY);
        canvas.drawPath(mPath, mPaint);
    }


    private void drawLineToViewLeft(Canvas canvas, View from, View to) {
        if (to.getVisibility() == GONE) {
            return;
        }

        mPaint.setStyle(Paint.Style.STROKE);

        float width = 2f;

        mPaint.setStrokeWidth(dp2px(mContext, width));
        mPaint.setColor(mContext.getResources().getColor(R.color.chelsea_cucumber));

        int top = from.getTop();
        int formY = top + from.getMeasuredHeight() / 2;
        int formX = from.getRight();

        int top1 = to.getTop();
        int toY = top1 + to.getMeasuredHeight() / 2;
        int toX = to.getLeft();

        mPath.reset();
        mPath.moveTo(formX, formY);
        mPath.quadTo(toX - dp2px(mContext, 15), toY, toX, toY);
        canvas.drawPath(mPath, mPaint);
    }


    private void measureAutoBalance() {
//        MindNode rootMindNode = MindNodeManager.getInstance().getRootMindNode();
//        if (rootMindNode.relationShip != null && rootMindNode.relationShip.children != null) {
//            List<MindNode> children = rootMindNode.relationShip.children;
//            int size = children.size();
//            List<MindNode> leftMindList = rootMindNode.relationShip.children.subList(0, size / 2 - 1);
//            List<MindNode> rightMindList = rootMindNode.relationShip.children.subList(size / 2, size - 1);
//            Size leftSize = getLeftSize(leftMindList);
//            Size rightSize = getRightSize(rightMindList);
//
//        }
    }

    private Size getRightSize(List<MindNode> rightMindList) {


        return null;
    }

    private Size getLeftSize(List<MindNode> leftMindList) {
        return null;
    }


    public void drawAll() {
        MindNode rootMindNode = MindNodeManager.getInstance().getRootMindNode();
        addMindNodeView(rootMindNode);

//        if (rootMindNode == null) {
//            Log.e(TAG, "draw All failed cause root node is null");
//            return;
//        }
//
//
//        View rootView = View.inflate(getContext(), R.layout.layout_mind_node, null);
//        this.addView(rootView);
//
//        EditText contentEt = rootView.findViewById(R.id.content_et);
//        contentEt.setText(rootMindNode.data.title);
//        rootMindNode.style.view = rootView;
//        MindNode.RelationShip relationShip = rootMindNode.relationShip;
//        if (relationShip == null) {
//            if (relationShip.children == null) {
//                Log.d(TAG, "drawAll failed cause children is empty");
//                return;
//            }
//
//        }
//        for (int i = 0; i < relationShip.children.size(); i++) {
//            View nodeView = View.inflate(getContext(), R.layout.layout_mind_node, null);
//            EditText contentEtChild = nodeView.findViewById(R.id.content_et);
//            contentEtChild.setText("不错" + i);
//            relationShip.children.get(i).style.view = nodeView;
//            addView(nodeView);
//        }
    }

    public void addMindNodeView(MindNode mindNode) {
        if (mindNode == null) {
            return;
        }
        MindNode.RelationShip relationShip = mindNode.relationShip;
        View rootView = View.inflate(getContext(), R.layout.layout_mind_node, null);
        addView(rootView);
        EditText contentEt = rootView.findViewById(R.id.content_et);
        contentEt.setText(mindNode.data.title);
        mindNode.style.view = rootView;

        if (relationShip == null) {
            if (relationShip.children == null) {
                Log.d(TAG, "drawAll failed cause children is empty");
                return;
            }
        }
        for (int i = 0; i < relationShip.children.size(); i++) {
            View nodeView = View.inflate(getContext(), R.layout.layout_mind_node, null);
            EditText contentEtChild = nodeView.findViewById(R.id.content_et);
            contentEtChild.setText("不错" + i);
            relationShip.children.get(i).style.view = nodeView;
            addView(nodeView);
            addMindNodeView(relationShip.children.get(i));
        }

    }


}
