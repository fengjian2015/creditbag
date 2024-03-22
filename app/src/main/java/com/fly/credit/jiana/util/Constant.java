package com.fly.credit.jiana.util;


import com.fly.credit.jiana.bean.RoomBean;
import com.fly.credit.jiana.bean.RoomHandleBean;

/**
 * lucksoft
 * Created by AndroidDevelop on 2019年10月15日,0015.
 * 158045632@qq.com
 */
public class Constant {
    public static final String CODED_BITMAP = "codedBitmap";
    public static final String CODED_CONTENT = "codedContent";
    public static final int DECODE = 1;
    public static final int DECODE_FAILED = 2;
    public static final int DECODE_SUCCEEDED = 3;
    public static final int FLASH_CLOSE = 9;
    public static final int FLASH_OPEN = 8;
    public static final String INTENT_ZXING_CONFIG = "zxingConfig";
    public static final int LAUNCH_PRODUCT_QUERY = 4;
    public static final int QUIT = 5;
    public static final int REQUEST_IMAGE = 10;
    public static final int RESTART_PREVIEW = 6;
    public static final int RETURN_SCAN_RESULT = 7;

    /**
     * 定义子线程到主线程的消息种类
     **/
    public static final int CMD_TEST = 0x001;
    public static final int CMD_EXIT = 0x1000;
    public static final int CMD_BACKSTAGE = 0x1001;
    public static final int CMD_CLOSE_APP = 0x1002;
    public static final int CMD_FRONT_DESK = 0x1003;
    public static final int CMD_UPDATE_WEIGHT = 0x1004;
    public static final int CMD_USB_PRINT = 0x1005;

    public static final int BACKSTAGE_FRAGMENT = 1000;//后台管理界面
    public static final int CASH_SETTLEMENT_FRAGMENT = 1001;//支付界面
    public static final int PAY_SUCCESS_FRAGMENT = 1002;//支付成功界面
    public static final int RECHARGE_FRAGMENT = 1003;//会员充值界面
    public static final int ADD_MEMBER_FRAGMENT = 1004;//新增会员界面
    public static final int SELECT_ACTIVITY_FRAGMENT = 1005;//选择优惠活动界面
    public static final int SELECT_STAFF_FRAGMENT = 1006;//选择提成员工界面
    public static final int MODIFY_GOODS_FRAGMENT = 1007;//修改购物车商品信息界面
    public static final int ORDER_SINGLE_FRAGMENT = 1008;//整单优惠界面
    public static final int SHOW_MESSAGE_FRAGMENT = 1009;//系统提示/确认界面
    public static final int INPUT_FRAGMENT = 1010;//输入界面
    public static final int SELECT_COUPON_FRAGMENT = 1011;//选择优惠券界面
    public static final int GETORDER_FRAGMENT = 1012;//取单界面
    public static final int KEYBOARD_FRAGMENT = 1013;//自定义键盘界面
    public static final int QUICKCONSUME_FRAGMENT = 1014;//快速消费界面
    public static final int SEARCHMEMBER_FRAGMENT = 1015;//会员搜索界面
    public static final int SELECTIMAGE_FRAGMENT = 1016;//选择图片界面
    public static final int CUTIMAGE_FRAGMENT = 1017;//裁剪图片界面
    public static final int FINDPASSWORD_FRAGMENT = 1018;//忘记密码界面
    public static final int ORDERDETAIL_FRAGMENT = 1019;//单据详情界面
    public static final int REVOKEORDER_FRAGMENT = 1020;//撤单界面
    public static final int RETURNGOODS_FRAGMENT = 1021;//退货界面
    public static final int RETURNGOODS_HAND_DUTY = 1022;//交班界面
    public static final int AUTO_CUSTOMER_SERVICE = 1023;//客服界面
    public static final int SELECT_RETURN_ON = 1023;//查询物流信息
    public static final int RETURN_ON_LINE_ORDER = 1024;//同城发货
    public static final int WRITE_OFF_MALL_ORDER = 1025;//核销订单
    public static final int RETURN_OPEN_IMAGE = 1026;//打开图片
    public static final int RECHARGECOUNT_GOODSDETAIL_FRAGMENT = 1027;//计次商品详情界面
    public static final int SELECT_GOODSSPECS_FRAGMENT = 1028;//选择多规格商品界面
    public static final int CODE_SEARCH_FORRESULT = 1029;//选择多规格商品界面
    public static final int PAY_CANCLE = 1030;
    public static final int TASTE_SELECT_DOWN = 1031;
    public static final int TASTE_SELECT_DOWNS = 1032;
    public static final int STOP_TIME = 1033;//暂停计时

    public static final String APIVersionNumber = "100";

    /***
     * 消费类型：1 快速消费  2 商品消费 3 会员充值 4 会员充次 100 油品消费
     */
    public static final int QUICK_CONCUME = 1;
    public static final int GOODS_CONSUME = 2;
    public static final int MEMBER_RECHARGE = 3;
    public static final int MEMBER_RECHARGE_TIMES = 4;
    public static final int OIL_CONSUME = 100;
    public static final int VENUE_CONSUME = 105;
    public static final int FIXED_AMOUNT_CONSUME = 110;
    public static final int QUICK_TIME_CONSUM = 115;
    public static final int VIPCARD_SALE_DELAY = 200;
    public static final int MEMBER_RESTING = 5;
    public static final int MEMBER_GIFT_EXCHANGE = 6;
    public static final int MEMBER_POINT_CHANGE = 7;
    public static final int OPENDILL_CONSUME = 150;
    public static final int REPRINT_MARK = 666;

    /*当前是不是属于开单消费*/
    public static boolean isOpenDillConsume = false;

    /***是否进行蓝牙80打印*/
    public static boolean isFiveEightPrint = false;

    /***是否允许负库存销售*/
    public static boolean isAallowNegativeInventory = false;

    /**
     * 缓存 Key 定义
     */
    public static final String LoginInfo = "v8frontcashclientlogin";
    public static final String AutoWeight = "v8frontcash_autoweight";


    /**** 工程版本名称,区分主版本 商户收银宝， 收银POS， 智油星加油站 **/
    public static final int Project_Shsyb = 1;
    public static final int Project_SyPos = 2;
    public static final int Project_ZyxOil = 3;
    public static final int Project_Luck = 4;

    public static final int Project_Other = 10000;

    public static RoomBean cacheRoom = null;
    public static RoomHandleBean cacheRoomHandle = null;

    /*打印设置缓存需要的KEY*/
    public static String PRINT_PARAMS_DATA;
    /*标签打印设置缓存需要的KEY*/
    public static String PRINT_PARAMS_MARK_DATA;
    /*商品标签打印设置缓存需要的KEY*/
    public static String PRINT_PARAMS_MARK_DATA_GOOD;
    public static boolean NEED_ADJUST_PAPER = true;

    //设置中的语音播报
    public static String SETTING_SOUND = "SETTING_SOUND";

    //当前开单模式是房台开单还是手牌开单 默认手牌
    public static boolean isRoomDillOpen = false;
    public static final String SUBSCRIBE_MONEY_PAYLIST = "subscribe_money_paylist";
    public static final int MAX_GOODS_SIZE = 48;

    /*当前企业是否具备预付款版本权限*/
    public static boolean hasAdvancepaymentSoft = false;

    public static String currentFragShow = "";
    public static String CASHIER_MEMBERSEARCH_MARK = "";
}
