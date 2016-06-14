package youzi.com.sharelove.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import youzi.com.sharelove.R;
import youzi.com.sharelove.modal.Config;
import youzi.com.sharelove.modal.RoomInfo;
import youzi.com.sharelove.modal.internet.Form;
import youzi.com.sharelove.modal.internet.GetRoomInfo;
import youzi.com.sharelove.modal.internet.HttpMethod;

/**
 * Created by youzi 2016/5/28.
 */
@SuppressLint("ValidFragment")
public class fragment_listen extends Fragment {

    String content;//页面传递的内容
    PullToRefreshListView pullToRefreshListView;

    ArrayList<RoomInfoModal> roomLists;

    RoomInfoAdapter roomInfoAdapter;//房间适配器

    Handler upHandler = null;
    Handler joinHandler = null;//加入房间
    List<RoomInfo> roomInfos = new ArrayList<>();//房间信息
    int nowPoint;
    String token;

    public fragment_listen(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_listen, container, false);
        pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.expand_list);
        nowPoint = 0;//点击的房间下标
        roomLists = new ArrayList<>();
//        roomLists.add(new RoomInfoModal("1", "红色高跟鞋", "来听我的歌吧~", "23人"));
//        roomLists.add(new RoomInfoModal("2", "爱一点", "我的歌更好听~~", "233人"));

        roomInfoAdapter = new RoomInfoAdapter(rootView.getContext(), roomLists);
        pullToRefreshListView.setAdapter(roomInfoAdapter);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        upHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                pullToRefreshListView.invalidate();
                pullToRefreshListView.onRefreshComplete();
            }
        };
        joinHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent = new Intent(rootView.getContext(), listenRoom.class);
                intent.putExtra("token", token);
                intent.putExtra("num", roomInfos.get(nowPoint).num);
                intent.putExtra("signature", roomInfos.get(nowPoint).signature);
                startActivity(intent);
            }
        };
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        upDateRoom(rootView);
                    }
                }).start();
            }
        });
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(rootView.getContext(), "点了:" + ((RoomInfoModal) roomInfoAdapter.getItem(position - 1)).roomid, Toast.LENGTH_SHORT).show();
                getRoomToken(rootView, ((RoomInfoModal) roomInfoAdapter.getItem(position - 1)).roomid);
                nowPoint = position - 1;
            }
        });
        return rootView;
    }

    public void upDateRoom(final View rootView) {
        Form form = new Form(Config.GetRoomUrl);
        new GetRoomInfo(HttpMethod.GET, form, new GetRoomInfo.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                roomInfos = GetRoomInfo.getRooms(result);
                roomLists.clear();
                for (RoomInfo temp : roomInfos) {
                    roomLists.add(new RoomInfoModal(temp.id, temp.creator, temp.signature, temp.num + "人"));
                }
                upHandler.sendEmptyMessage(0);
            }
        }, new GetRoomInfo.FailCallback() {
            @Override
            public void onFail(String result) {
                Toast.makeText(rootView.getContext(), "网络连接失败！-" + result, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getRoomToken(final View rootView, final String id) {
        Form form = new Form(Config.GetRoomTokenUrl);
        form.setKeyValues_post("id", id);
        new GetRoomInfo(HttpMethod.POST, form, new GetRoomInfo.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                token = GetRoomInfo.getRoomToken(result);
                joinHandler.sendEmptyMessage(0);
            }
        }, new GetRoomInfo.FailCallback() {
            @Override
            public void onFail(String result) {
                Toast.makeText(rootView.getContext(), id + "获取Token失败！-" + result, Toast.LENGTH_LONG).show();
            }
        });
    }
}

class RoomInfoModal {
    public String roomid;
    public String musicInfo;
    public String signature;
    public String num;

    public RoomInfoModal(String roomid, String musicInfo, String signature, String num) {
        this.roomid = roomid;
        this.musicInfo = musicInfo;
        this.signature = signature;
        this.num = num;
    }

}

class RoomInfoAdapter extends BaseAdapter {
    private ArrayList<RoomInfoModal> roomLists;
    private Context context;

    RoomInfoAdapter(Context context, ArrayList<RoomInfoModal> roomLists) {
        this.roomLists = roomLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return roomLists.size();
    }

    @Override
    public Object getItem(int position) {
        return roomLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.musicinfo_adapter, null);
        }
        TextView roomid = (TextView) convertView.findViewById(R.id.roomid);
        TextView musicInfo = (TextView) convertView.findViewById(R.id.musicInfo);
        TextView signature = (TextView) convertView.findViewById(R.id.signature);
        TextView num = (TextView) convertView.findViewById(R.id.num);

        roomid.setText(roomLists.get(position).roomid);
        musicInfo.setText(roomLists.get(position).musicInfo);
        signature.setText(roomLists.get(position).signature);
        num.setText(roomLists.get(position).num);

        return convertView;
    }
}
