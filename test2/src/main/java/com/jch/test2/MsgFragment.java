package com.jch.test2;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author changhua.jiang
 * @since 2018/7/10 下午5:41
 */

public class MsgFragment extends Fragment {
    String titles[] = new String[]{"永生说的对","按永生说的办","永生说历史","永生讲政治"};
    String msgs[] = new String[]{"好！","👌","牛逼！","精辟！","啪啪啪，鼓掌！"};
    String[] lastTime = new String[]{"今天 上午8:00","公元前 221年","明天 下午3:12","三叠纪 321年"};
    private List<Conversation> c_list = new ArrayList<>();

    @BindView(R.id.rv_conversation_list)
    RecyclerView mRecyclerView;

    private ConversationAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        long b = SystemClock.currentThreadTimeMillis();
        View root = inflater.inflate(R.layout.fragment_msg,null);
        ButterKnife.bind(this,root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(adapter == null)
            adapter = new ConversationAdapter();
        mRecyclerView.setAdapter(adapter);
        long e = SystemClock.currentThreadTimeMillis();
        Log.e("xxxooo","time is " + (e -b));
        return root;
    }



    public int getRandom(int max,int min){
        if(max < min){
            int tmp = min;
            max = tmp;
            min = max;
        }
        Random random = new Random();
        int ret = random.nextInt(max - min) + min;
        return ret;
    }

    class ConversationAdapter extends RecyclerView.Adapter<CV>{
        int size = getRandom(11,6);
        @Override
        public CV onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item,null);
            //itemView.setBackgroundResource(R.drawable.general_click_bg);
            return new CV(itemView);
        }

        @Override
        public void onBindViewHolder(CV holder, int position) {
            //Conversation conversation = c_list.get(position);
//            Bitmap bmp = BitmapFactory.decodeResource(holder.imgPersonHead.getResources(),R.drawable.default_persion);
//            holder.imgPersonHead.setImageBitmap(bmp);

            holder.tvTitle.setText(titles[getRandom(4,0)]);
            holder.tvLastMsg.setText(msgs[getRandom(5,0)]);
            holder.tvLastTime.setText(lastTime[getRandom(4,0)]);
            //Bitmap bmp = BitmapFactory.decodeResource(holder.itemView.getResources(),R.drawable.default_persion);
            //holder.imgPersonHead.setImageBitmap(bmp);
            //holder.imgPersonHead.setImageResource(R.drawable.default_persion);
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }
    class Conversation{
        int personImage;
        String title;
        String lastMsg;
        String lastTime;
    }

    class CV extends RecyclerView.ViewHolder{
        @BindView(R.id.img_person)
        ImageView imgPersonHead;
        @BindView(R.id.tv_conversation_title)
        TextView tvTitle;
        @BindView(R.id.tv_conversation_last_msg)
        TextView tvLastMsg;
        @BindView(R.id.tv_conversation_last_time)
        TextView tvLastTime;

        public CV(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}




