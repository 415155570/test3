package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
    MyListView.ILoadListener {

  private MyListView myListView;
  private List<MyData> mListViewDatas = new ArrayList<MyData>();
  private MyAdapter mAdapter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initData();//该方法初始化数据
    initView();

  }

  private void initView() {
    myListView = (MyListView) findViewById(R.id.list_view);
    myListView.setOnILoadListener(this);
    mAdapter = new MyAdapter(this, mListViewDatas);
    myListView.setAdapter(mAdapter);
    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("MainActivity", "点击了第" + position + "项");
        MyData md = new MyData("修改后的数据：position = " + position);
        mListViewDatas.set(position, md);
        notifyDataSetChanged(position, myListView);
      }
    });
  }

  private void notifyDataSetChanged(int position, ListView listView) {
    int firstVisiblePosition = listView.getFirstVisiblePosition();//获得可见的第一个item的position
    int lastVisiblePosition = listView.getLastVisiblePosition();//获得可见的最后一个item的position
    if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
      View view = listView.getChildAt(position - firstVisiblePosition);
      mAdapter.getView(position, view, listView);
    }
  }

  /**
   * 该方法初始化数据，即提供初始的素材
   */
  private void initData() {
    for (int i = 0; i < 12; i++) {
      MyData md = new MyData("你好，我是提前设定的");
      mListViewDatas.add(md);
    }

  }

  /**
   * 该方法提供模拟的加载数据
   */
  private void getLoadData() {
    for (int i = 0; i < 3; i++) {
      MyData md = new MyData("你好，我是加载进来的");
      mListViewDatas.add(md);
    }

  }

  //重写回调方法
  public void loadData() {

    //注意之所以使用Handlder，主要是想让下面的
    //操作延迟5秒钟，以体现效果。实际开发中不需要
    Handler mHandler = new Handler();
    mHandler.postDelayed(new Runnable() {

      public void run() {

        //获得加载数据
        getLoadData();
        //然后通知MyListView刷新界面
        mAdapter.notifyDataSetChanged();

        //然后通知加载数据已经完成了

        myListView.loadFinish();
      }

    }, 5000);

  }

}