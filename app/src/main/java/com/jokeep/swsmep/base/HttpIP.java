package com.jokeep.swsmep.base;

/**
 * Created by wbq501 on 2016-1-7 11:31.
 * SWSMEP
 */
public class HttpIP {
    public static String Base = "http://171.221.173.66:20083/SWSMEP/";
//    public static String Base = "http://192.168.2.103/SWSMEP/";
//    public static String Base = "http://192.168.1.212/SWSMEP/";
//    public static String Base = "http://192.168.2.114/SWSMEP/";
    public static final String FlowPreview = "http://192.168.1.212/SWSMEP/WorkFlow/WorkFlow/Preview/FlowPreview.aspx";//查看流程
    public static String IP = Base+"SSO/Service.asmx";
    public static String MainService = Base+"AndroidService/MainService.svc";
    public static String Web = Base+"Web";
    // 获取加密key,同步tokenId
    public static final String ACTION_KEY = "getEncryptKey";
    public static final String Login = "AndroidLoginCheck";
    public static final String ResetPassword = "/ResetPassword";//改密码
    public static final String UpImage = Base+"Web/AndroidUpLoadFileService.svc/UpdatePhoto";//更新头像
    public static final String PhoneMan = "/AddressBook_Filter";//通讯录
    public static final String JointToDo_Filter = "/JointToDo_Filter";//协同待办、已办
    public static final String Joint_Filter = "/Joint_Filter";//协同待发、已发
    public static final String CommonGroupPersonnel_Filter = "/CommonGroupPersonnel_Filter";//常用联系人
    public static final String User_Filter = "/User_Filter";//人员选择
    public static final String UploadFile = Base+"Web/AndroidUpLoadFileService.svc/UploadFile";//上传文件
    public static final String Joint_Save = "/Joint_Save";//新建，修改，保存协同
    public static final String UploadFiles = Base+"Web/Ashx/AndroidUploadFileHandler.ashx";//上传多文件
    public static final String JointAttByID = "/JointAttByID";//编辑协同查询
    public static final String JointToDoInfo = "/JointToDoInfo";//办理协同查询
    public static final String Joint_Deal = "/Joint_Deal";//通过数据保存
    public static final String Joint_RollBack = "/Joint_RollBack";//协同退回
    public static final String UserPhrasebook_Filter = "/UserPhrasebook_Filter";//常用语
    public static final String UserPhrasebook_Insert = "/UserPhrasebook_Insert";//常用语添加
    public static final String UserPhrasebook_Delete = "/UserPhrasebook_Delete";//常用语删除
    public static final String workIp = Base+"AndroidService/MainService.svc/RefreshToDo";
    public static final String mainNumber = Base+"AndroidService/MainService.svc/RefreshToDoCount";
    public static final String ToDo_Opinion_Filter = "/ToDo_Opinion_Filter";//办理意见

}
