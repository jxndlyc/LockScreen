package com.zx.lockscreen.popmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zx.lockscreen.R;

import java.util.ArrayList;

/**
 * Created by liuyuchuan on 2018/8/20.
 */

public class PopMenuActivity extends Activity {

    private static final int USER_SEARCH = 0;
    private static final int USER_ADD = 1;
    private PopMenuMore mMenu;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_menu);
        initMenu();
        mTextView = (TextView) findViewById(R.id.hello_tv);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenu.showAsDropDown(mTextView);
            }
        });
    }

    private void initMenu() {
        mMenu = new PopMenuMore(this);
        // mMenu.setCorner(R.mipmap.demand_icon_location);
        // mMenu.setBackgroundColor(Color.parseColor("#ff8800"));
        ArrayList<PopMenuMoreItem> items = new ArrayList<>();
        items.add(new PopMenuMoreItem(USER_SEARCH, "搜索"));
        items.add(new PopMenuMoreItem(USER_ADD, "添加"));
        items.add(new PopMenuMoreItem(USER_SEARCH, "搜索"));
        items.add(new PopMenuMoreItem(USER_ADD, "添加"));
        items.add(new PopMenuMoreItem(USER_SEARCH, "搜索"));
        items.add(new PopMenuMoreItem(USER_ADD, "添加"));
        /*items.add(new PopMenuMoreItem(USER_SEARCH, R.mipmap.demand_icon_number, "搜索"));
        items.add(new PopMenuMoreItem(USER_ADD, R.mipmap.demand_icon_location, "添加"));
        items.add(new PopMenuMoreItem(USER_SEARCH, R.mipmap.demand_icon_number, "搜索"));
        items.add(new PopMenuMoreItem(USER_ADD, R.mipmap.demand_icon_location, "添加"));
        items.add(new PopMenuMoreItem(USER_SEARCH, R.mipmap.demand_icon_number, "搜索"));
        items.add(new PopMenuMoreItem(USER_ADD, R.mipmap.demand_icon_location, "添加"));*/

        mMenu.addItems(items);
        mMenu.setOnItemSelectedListener(new PopMenuMore.OnItemSelectedListener() {
            @Override
            public void selected(View view, PopMenuMoreItem item, int position) {
                switch (item.id) {
                    case USER_SEARCH:
//                        startActivity(new Intent(this, UserSearchActivity.class));
                        break;
                    case USER_ADD:
//                        startActivity(new Intent(getActivity(), UserAddActivity.class));
                        break;
                }
            }
        });
    }
}
