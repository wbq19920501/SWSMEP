package com.jokeep.swsmep.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.WebViewActivity;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.MyData;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.base.WindowWH;
import com.jokeep.swsmep.model.NewInfo;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-25 17:01.
 * SWSMEP
 */
public class NewsFragment extends Fragment {
    View fragment;
    private PullToRefreshListView mNewsList_refresh;
    private BaseAdapter mBaseAdapter;
    private ViewHolder mViewHolder;
    private String UserID;
    private String TOKENID;
    private SharedPreferences sp;
    private SwsApplication application;
    private ShowDialog dialog;
    private int page = 1;
    private List<NewInfo> mArrayList;
    private ArrayList<NewInfo> mNewArrayList = new ArrayList<>();
    private MyData mMyData = MyData.getInstance();
    private View mFooter;
    //private
    private String[] date = {"今日 9:10", "今日 9:10", "今日 9:10", "2016.9.10", "今日 9:10", "今日 9:10", "今日 9:10"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null) {
            fragment = inflater.inflate(R.layout.news_fragment, container, false);
        } else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        initdata();
        Log.i("NewsFragment", "onCreateView");
        return fragment;
    }

    //dengJ 新闻获取数据；
    public void connectToServerByPost() {
        RequestParams params = new RequestParams(HttpIP.NewsIp);
        final JSONObject object = new JSONObject();
        try {
            object.put("UserID", UserID);
            object.put("PageNo", page);
            object.put("PageSize", 10);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    dialog.dismiss();
                    Log.d("json3", "json------>" + s);
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code == 1) {
                            Toast.makeText(getActivity(), object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        } else if (code == 0) {
                            Log.i("json", object2.getString("Result").toString());
                            JSONArray mArray = new JSONArray(object2.getString("Result").toString());
                            JSONObject mObject = (JSONObject) mArray.get(0);
                            JSONArray mArrayOne = new JSONArray(mObject.getString("Table"));
                            for (int i = 0; i < mArrayOne.length(); i++) {
                                JSONObject object3 = (JSONObject) mArrayOne.get(i);
                                NewInfo mNewInfo = new NewInfo();
                                mNewInfo.setF_CONTENTID(object3.getString("F_CONTENTID"));
                                mNewInfo.setF_CONTENTIMG(object3.getString("F_CONTENTIMG"));
                                mNewInfo.setF_CONTENTTITLE(object3.getString("F_CONTENTTITLE"));
                                mNewInfo.setF_ISIMG(object3.getInt("F_ISIMG"));
                                mNewInfo.setF_VIEWCOUNT(object3.getInt("F_VIEWCOUNT"));
                                mNewInfo.setF_PUBDATE(object3.getString("F_PUBDATE"));
                                mNewInfo.setF_PUBSHOWDATE(object3.getString("F_PUBSHOWDATE"));
                                mNewInfo.setF_STATE(object3.getInt("F_STATE"));
                                mArrayList.add(mNewInfo);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mNewsList_refresh.onRefreshComplete();
                    mBaseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Log.i("json", "onError");
                    mNewsList_refresh.onRefreshComplete();
                    Toast.makeText(getActivity(),"数据加载失败,请稍后再试",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    mNewsList_refresh.onRefreshComplete();
                }

                @Override
                public void onFinished() {
                    mNewsList_refresh.onRefreshComplete();
                }
            });
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }

    }
    private void initdata() {
        dialog.show();
        connectToServerByPost();
        mNewsList_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!isNetworkConnected(getActivity())){
                    connectToServerByPost();
                }else {
                    mArrayList.clear();
                    page=1;
                    connectToServerByPost();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!isNetworkConnected(getActivity())){
                    connectToServerByPost();
                }else {
                    page++;
                    connectToServerByPost();
                    if(mArrayList.size()<(page-1)*10){
                        myToast();
                    }
                }
            }
        });
    }
    private void myToast(){
        Toast mToast = new Toast(getActivity());
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.setView(mFooter);
        mToast.show();
    }
    private void init() {
        //dengJ取TOKENIN,UserID
        mArrayList = new ArrayList<>();
        dialog = new ShowDialog(getActivity(), R.style.MyDialog, getResources().getString(R.string.dialogmsg));
        application = (SwsApplication) getActivity().getApplication();
        UserID = application.getFUSERID();
        Log.i("json", UserID);
        sp = getActivity().getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        TOKENID = sp.getString(SaveMsg.TOKENID, "");
        Log.i("json", TOKENID);
        mFooter = LayoutInflater.from(getActivity()).inflate(R.layout.footer_layout,null);
        mNewsList_refresh = (PullToRefreshListView) fragment.findViewById(R.id.news_refresh);
        mNewsList_refresh.setMode(PullToRefreshBase.Mode.BOTH);
        mBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mArrayList.size();
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (mArrayList.get(position).getF_ISIMG() == 0) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.news_noimages_item, null);
                    mViewHolder = new ViewHolder();
                    mViewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.tv_no_images_title);
                    mViewHolder.mTextViewDate = (TextView) convertView.findViewById(R.id.tv_date);
                    mMyData.setmTextColor(0xFF999999);
                    if (mArrayList.get(position).getF_VIEWCOUNT() == 1) {
                        mViewHolder.mTextViewTitle.setTextColor(0xFF999999);
                    }else {
                        mViewHolder.mTextViewTitle.setTextColor(0xFF333333);
                    }
                    mViewHolder.mTextViewTitle.setText(mArrayList.get(position).getF_CONTENTTITLE());
                    mViewHolder.mTextViewDate.setText(mArrayList.get(position).getF_PUBSHOWDATE());
                } else if (mArrayList.get(position).getF_ISIMG() == 1) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.news_picture_item, null);
                    mViewHolder = new ViewHolder();
                    mViewHolder.mIageView = (ImageView) convertView.findViewById(R.id.yitu_thumbnail);
                    mViewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.yitu_title);
                    mViewHolder.mTextViewDate = (TextView) convertView.findViewById(R.id.yitu_date);
                    mMyData.setmTextColor(0xFF999999);
                    if (mArrayList.get(position).getF_VIEWCOUNT() == 1) {
                        mViewHolder.mTextViewTitle.setTextColor(0xFF999999);
                    }else {
                        mViewHolder.mTextViewTitle.setTextColor(0xFF333333);
                    }
                    x.image().bind(mViewHolder.mIageView, mArrayList.get(position).getF_CONTENTIMG());
                    mViewHolder.mTextViewDate.setText(mArrayList.get(position).getF_PUBSHOWDATE());
                    mViewHolder.mTextViewTitle.setText(mArrayList.get(position).getF_CONTENTTITLE());
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isNetworkConnected(getActivity())){
                            Toast.makeText(getActivity(),"数据加载失败,请稍后再试",Toast.LENGTH_SHORT).show();
                        }else {
                            mMyData.setmViewCount(mArrayList.get(position).getF_VIEWCOUNT() + "");
                            mMyData.setWebID(mArrayList.get(position).getF_CONTENTID());
                            mArrayList.get(position).setF_VIEWCOUNT(1);
                            Intent mIntent = new Intent(getActivity(), WebViewActivity.class);
                            startActivity(mIntent);
                            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    }
                });
                return convertView;
            }
        };
        mNewsList_refresh.setAdapter(mBaseAdapter);
    }

    class ViewHolder {
        ImageView mIageView;
        TextView mTextViewTitle, mTextViewDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("NewsFragment","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("NewsFragment", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        Log.i("NewsFragment","onAttach");
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        Log.i("NewsFragment","onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("NewsFragmen", mArrayList+"");
        mBaseAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("NewsFragment", "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("NewsFragment","onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i("NewsFragment","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i("NewsFragment","onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.i("NewsFragment","onDestroyView");
        super.onDestroyView();
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
