package com.jokeep.swsmep.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.WorkReturnIdea2Activity;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.SuggestionFilesInfo;
import com.jokeep.swsmep.model.SuggestionInfo;
import com.jokeep.swsmep.model.SuggestionsInfo;
import com.jokeep.swsmep.model.Work1Info;
import com.jokeep.swsmep.view.RoundImageView;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-3-3 14:50.
 * SWSMEP  意见
 */
public class WorkIdeaFragmet extends Fragment{
    View fragment;
    private ExpandableListView idea_list;
//    WorkIdeaAdapter adapter;
    BaseExpandableListAdapter adapter;
    List<SuggestionInfo> suggestionInfos;
    List<Work1Info> work1Infos;
    List<SuggestionsInfo> suggestionsInfos;
    List<SuggestionFilesInfo> suggestionFilesInfos;
    String F_EXECUTMAINID,TOKENID;
    private ShowDialog dialog;
    Intent intent;
    String Title,BusinessCode,MainID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.work_idea,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        initdata();
        return fragment;
    }

    private void initdata() {
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.ToDo_Opinion_Filter);
        JSONObject object = new JSONObject();
        try {
            object.put("ExcutMainID",F_EXECUTMAINID);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    dialog.dismiss();
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(getActivity(), object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            JSONArray jsonArray = new JSONArray(object2.getString("Result"));
                            JSONObject object3 = (JSONObject) jsonArray.get(0);
                            JSONArray array = new JSONArray(object3.getString("Table"));
                            for (int i=0;i<array.length();i++){
                                JSONObject jsonObject = (JSONObject) array.get(i);
                                SuggestionInfo suggestionInfo = new SuggestionInfo();
                                suggestionInfo.setF_OPINIONID(jsonObject.getString("F_OPINIONID"));
                                suggestionInfo.setF_EXECUTMAINID(jsonObject.getString("F_EXECUTMAINID"));
                                suggestionInfo.setF_NODEID(jsonObject.getString("F_NODEID"));
                                suggestionInfo.setF_OPINION(jsonObject.getString("F_OPINION"));
                                suggestionInfo.setF_CONCLUSION(jsonObject.getString("F_CONCLUSION"));
                                suggestionInfo.setF_HANDLETIME(jsonObject.getString("F_HANDLETIME"));
                                suggestionInfo.setF_HANDLEID(jsonObject.getString("F_HANDLEID"));
                                suggestionInfo.setF_HANDLENAME(jsonObject.getString("F_HANDLENAME"));
                                suggestionInfo.setF_DEPARTMENTNAME(jsonObject.getString("F_DEPARTMENTNAME"));
                                suggestionInfo.setF_POSITIONNAME(jsonObject.getString("F_POSITIONNAME"));
                                suggestionInfo.setF_USERHEADURI(jsonObject.getString("F_USERHEADURI"));
                                suggestionInfo.setSuggestionFilesInfos(files(jsonObject.getString("ATTACHMENT")));
                                suggestionInfo.setSuggestionsInfos(suggestionlist(jsonObject.getString("OPINIONREPLY")));
                                suggestionInfo.setATTACHMENT(jsonObject.getString("ATTACHMENT"));
                                suggestionInfo.setOPINIONREPLY(jsonObject.getString("OPINIONREPLY"));
                                suggestionInfos.add(suggestionInfo);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
//                    Log.d("ex", ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private List<SuggestionsInfo> suggestionlist(String OPINIONREPLY) throws JSONException {
        JSONArray array = new JSONArray(OPINIONREPLY);
        for (int i=0;i<array.length();i++){
            JSONObject object1 = (JSONObject) array.get(i);
            SuggestionsInfo suggestionsInfo = new SuggestionsInfo();
            suggestionsInfo.setF_OPINIONREPLYID(object1.getString("F_OPINIONREPLYID"));
            suggestionsInfo.setF_OPINIONID(object1.getString("F_OPINIONID"));
            suggestionsInfo.setF_OPINION(object1.getString("F_OPINION"));
            suggestionsInfo.setF_USERID(object1.getString("F_USERID"));
            suggestionsInfo.setF_USERNAME(object1.getString("F_USERNAME"));
            suggestionsInfo.setF_REPLYTIME(object1.getString("F_REPLYTIME"));
            suggestionsInfo.setF_REPLYTIMEFULL(object1.getString("F_REPLYTIMEFULL"));
            suggestionsInfo.setF_DEPARTMENTNAME(object1.getString("F_DEPARTMENTNAME"));
            suggestionsInfo.setF_POSITIONNAME(object1.getString("F_POSITIONNAME"));
            suggestionsInfos.add(suggestionsInfo);
        }
        return suggestionsInfos;
    }

    private List<SuggestionFilesInfo> files(String ATTACHMENT) throws JSONException {
        JSONArray array = new JSONArray(ATTACHMENT);
        for (int i=0;i<array.length();i++){
            JSONObject object1 = (JSONObject) array.get(i);
            SuggestionFilesInfo suggestionFilesInfo = new SuggestionFilesInfo();
            suggestionFilesInfo.setF_ATTACHMENTID(object1.getString("F_ATTACHMENTID"));
            suggestionFilesInfo.setF_DATAID(object1.getString("F_DATAID"));
            suggestionFilesInfo.setF_FILENAME(object1.getString("F_FILENAME"));
            suggestionFilesInfo.setF_STORAGEPATH(object1.getString("F_STORAGEPATH"));
            suggestionFilesInfos.add(suggestionFilesInfo);
        }
        return suggestionFilesInfos;
    }
    private void init() {
        dialog = new ShowDialog(getActivity(),R.style.MyDialog,getResources().getString(R.string.dialogmsg));
        suggestionInfos = new ArrayList<SuggestionInfo>();
        suggestionsInfos = new ArrayList<SuggestionsInfo>();
        suggestionFilesInfos = new ArrayList<SuggestionFilesInfo>();
        work1Infos = new ArrayList<Work1Info>();
        work1Infos = (List<Work1Info>) getActivity().getIntent().getSerializableExtra("work1Infos");
        int position = getActivity().getIntent().getIntExtra("intposition", 0);
        F_EXECUTMAINID = work1Infos.get(position).getF_EXECUTMAINID();
        Title = work1Infos.get(position).getF_TITLE();
        BusinessCode = work1Infos.get(position).getF_BUSINESSCODE();
        MainID = work1Infos.get(position).getF_JOINTID();
        TOKENID = getActivity().getIntent().getStringExtra("TOKENID");

//        adapter = new WorkIdeaAdapter(getActivity(),suggestionInfos);

        idea_list = (ExpandableListView) fragment.findViewById(R.id.idea_list);
        idea_list.setGroupIndicator(null);
        adapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return suggestionInfos.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
//                return suggestionInfos.get(groupPosition).getSuggestionsInfos().size();
                return suggestionInfos.size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return getGroup(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return suggestionInfos.get(childPosition);
//                return suggestionInfos.get(groupPosition).getSuggestionsInfos().get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                ViewGroipHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.wrok_idea_item1, null);
                    holder = new ViewGroipHolder();
                    holder.man_name = (TextView) convertView.findViewById(R.id.man_name);
                    holder.man_type = (TextView) convertView.findViewById(R.id.man_type);
                    holder.man_time = (TextView) convertView.findViewById(R.id.man_time);
                    holder.man_img = (RoundImageView) convertView.findViewById(R.id.man_img);
                    holder.man_context = (TextView) convertView.findViewById(R.id.man_context);
                    holder.files = (LinearLayout) convertView.findViewById(R.id.files);
                    holder.man_imgs = (ImageView) convertView.findViewById(R.id.man_imgs);
                    holder.addview = (LinearLayout) convertView.findViewById(R.id.addview);
                    holder.return_msg = (Button) convertView.findViewById(R.id.return_msg);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewGroipHolder) convertView.getTag();
                }
                final SuggestionInfo suggestionInfo = suggestionInfos.get(groupPosition);
                List<SuggestionFilesInfo> suggestionFilesInfos = suggestionInfo.getSuggestionFilesInfos();
                holder.man_name.setText(suggestionInfo.getF_HANDLENAME());
                holder.man_type.setText(suggestionInfo.getF_DEPARTMENTNAME()+"-"+suggestionInfo.getF_POSITIONNAME());
                holder.man_time.setText(suggestionInfo.getF_HANDLETIME());
                holder.man_context.setText(suggestionInfo.getF_OPINION());
                for (int i=0;i<suggestionFilesInfos.size();i++){
                    TextView textView = new TextView(getActivity());
                    textView.setText(suggestionFilesInfos.get(i).getF_FILENAME());
                    textView.setTextColor(Color.parseColor("#21ac69"));
                    holder.addview.addView(textView);
                }
                ImageOptions imageOptions = new ImageOptions.Builder()
                        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                        .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        .setLoadingDrawableId(R.mipmap.ic_launcher)
                        .setFailureDrawableId(R.mipmap.ic_launcher)
                        .build();
                x.image().bind(holder.man_img, suggestionInfo.getF_USERHEADURI(), imageOptions);
                holder.return_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getActivity(), WorkReturnIdea2Activity.class);
                        List<SuggestionInfo> listsuggestionInfo = new ArrayList<SuggestionInfo>();
                        listsuggestionInfo.add(suggestionInfo);
                        intent.putExtra("suggestionInfo", (Serializable) listsuggestionInfo);
                        intent.putExtra("MainID",MainID);
                        intent.putExtra("Title",Title);
                        intent.putExtra("BusinessCode",BusinessCode);
                        startActivityForResult(intent, 2);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                ViewChildHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.wrok_idea_item2,null);
                    holder = new ViewChildHolder();
                    holder.man_name = (TextView) convertView.findViewById(R.id.man_name);
                    holder.man_type = (TextView) convertView.findViewById(R.id.man_type);
                    holder.man_context = (TextView) convertView.findViewById(R.id.man_context);
                    holder.files = (LinearLayout) convertView.findViewById(R.id.files);
                    holder.man_imgs = (ImageView) convertView.findViewById(R.id.man_imgs);
                    holder.man_time = (TextView) convertView.findViewById(R.id.man_time);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewChildHolder) convertView.getTag();
                }
                SuggestionsInfo suggestionsInfo = suggestionInfos.get(groupPosition).getSuggestionsInfos().get(childPosition);
                holder.man_name.setText(suggestionsInfo.getF_USERNAME());
                holder.man_type.setText(suggestionsInfo.getF_DEPARTMENTNAME()+"-"+suggestionsInfo.getF_POSITIONNAME());
                holder.man_context.setText(suggestionsInfo.getF_OPINION());
                holder.man_time.setText(suggestionsInfo.getF_REPLYTIME());
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };

        idea_list.setAdapter(adapter);
        int groupCount = idea_list.getCount();
        for (int i = 0; i < groupCount; i++) {
            idea_list.expandGroup(i);
        }
        idea_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
    }
    class ViewGroipHolder{
        RoundImageView man_img;
        TextView man_name,man_type,man_time;
        TextView man_context;
        ImageView man_imgs;
        LinearLayout files;
        Button return_msg;
        LinearLayout addview;
    }
    class ViewChildHolder{
        TextView man_name,man_type,man_time;
        TextView man_context;
        ImageView man_imgs;
        LinearLayout files;
    }
}
