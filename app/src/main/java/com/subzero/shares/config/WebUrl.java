package com.subzero.shares.config;

import java.net.URLEncoder;

/**
 * 请求地址的 url
 */
public class WebUrl {

    /**
     * logo地址
     */

    public static final String LOGO = "http://123.56.82.112/gupiao/public/images/logo.png";

    /**
     * 请求条目的长度
     */
    public static final int ROWS = 10;

    public static final String HOST = "http://123.56.82.112/gupiao/index.php/Api/";


    //public static final String SHAREX="http://123.56.82.112/gupiao/index.php?g=Api&m=Common&a=share&id=117&type=2";


    /**
     * 分享获取奖励
     */
    public static final String SHAREPAY = HOST + "Apicommon/sharepay/";

    /**
     * 续费缴费(获取金额)
     */
    public static final String MEMBERMONEY = HOST + "Apiuser/getmembermoney ";

    /**
     * 充值金额
     */
    public static final String PAYMONEY = HOST + "Apiuser/paymember/";
    /**
     * 获取订单
     */
    public static final String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";


    /**
     * 分享（悬赏令）……
     */
    public static final String SHARE2 = "http://123.56.82.112/gupiao/index.php?g=Api&m=Common&a=share2&title=";


    /**
     * 获取趋势课堂下所有分类
     */

    public static final String GETCOURSECATE = HOST + "Apidirect/getcoursecate";


    /**
     * 择机接口
     */


    /**
     * livedetail盘中直播详情
     */
    public static final String GETLIVEDETAIL = HOST + "Apizeji/getlivedetail/";
    /**
     * listen直播收听
     */
    public static final String LISTEN = HOST + "Apizeji/listen/";
    /**
     * unlisten直播收听
     */
    public static final String UNLISTEN = HOST + "Apizeji/unlisten/";

    //    clickGood点赞
    public static final String LIVEGOOD = HOST + "Apizeji/livegood/";
    /**
     * 发布直播
     */
    public static final String RELEASELIVE = HOST + "apizeji/releaselive";

    /**
     * 牛栏山列表
     */
    public static final String RULES = HOST + "apidiscuss/getrules/";

    /**
     * bet下注
     */
    public static final String BET = HOST + "apizeji/bet/";
    /**
     * 顾问或用户回复记录
     */
    public static final String ADVISORUSERREPLY = HOST + "apizeji/advisoruserreply/";
    //获取股指竞猜结果   http://123.56.82.112/gupiao/index.php/Api/Apizeji/getQuizResults/


    /**
     * 服务器返回文件需要添加的主机名
     */
    public static final String FILEHOST = "http://123.56.82.112/gupiao/";


    /**
     * 奇葩的群组头像
     */
    public static final String IMGGROUP = "http://123.56.82.112/";


    //首页获取数据会用到
    public static final String FILEHOST1 = "http://123.56.82.112/gupiao/data/upload/";

    /**
     * 注册
     */
    public static final String REGISTERAPI = HOST + "Apiuser/userRegister/";
    /**
     * 登录
     */
    public static final String LOGIN = HOST + "Apiuser/userLogin/";
    /**
     * 获取融云token
     */
    public static final String GETRCTOKEN = HOST + "Apidiscuss/rytoken/";

    /**
     * 获取群组
     */
    public static final String GROUP = HOST + "apidiscuss/getgroups/";
    /**
     * 忘记密码
     */
    public static final String GETFORPWD = HOST + "Apiuser/forgetpwd/";

    /**
     * 获取 个人信息
     */
    public static final String GETUSERINFO = HOST + "Apiuser/getuserinfo/";

    /**
     * 签到
     */
    public static final String SIGN = HOST + "Apiuser/usersign/";

    /**
     * 关于我们&联系客服
     */
    public static final String ABOUTLTD = HOST + "Apiuser/getpost/";


    /**
     * 用户信息更新
     */
    public static final String INfOUPDATE = HOST + "Apiuser/userInfoUpdate/";
    /**
     * 获取通知
     */
    public static final String GETNOTIFICATION = HOST + "Apiuser/getMessage/";
    /**
     * 获取收支流水
     */
    public static final String GETAMOUNTOFWATER = HOST + "Apiuser/getAmountOfWater/";
    /**
     * 获取支出流水
     */
    public static final String GETAMOUNTOUT = HOST + "Apiuser/getAmountOut/";


    /**
     * 获取微信的分组
     */
    public static final String GETWXGROUPS = HOST + "Apiuser/getwxgroups";

    /**
     * 获取会员等级介绍
     */
    public static final String GETLEVELDESC = HOST + "/Apiuser/getleveldesc/";

    /**
     * 获取收入流水
     */
    public static final String GETAMOUNTIN = HOST + "Apiuser/getAmountIn";
    /**
     * 获取常见问题
     */
    public static final String GETPROBLEMS = HOST + "Apiuser/getProblems/";
    /**
     * 提问
     */
    public static final String PUTQUESTIONSTO = HOST + "apiuser/putQuestionsTo/";
    /**
     * 获取钱包金额
     */
    public static final String GETWEALTH = HOST + "Apiuser/getWealth";
    /**
     * 修改密码
     */
    public static final String MODIFYPASSWORD = HOST + "apiuser/modifyPassword/";

    /**
     * 修改头像
     */
    public static final String MODIFYAVATAR = HOST + "Apiuser/modifyavatar/";


    /**
     * 城市列表
     */
    public static final String GETAREA = HOST + "Apiuser/getarea/";
    /**
     * 获取首页的咨询内容
     */
    public static final String GETCONSULTATION = HOST + "Apidirect/getConsultation/";
    /**
     * 获取课程
     */

    public static final String GETCOURSE = HOST + "Apidirect/getCourse/";
    /**
     * 搜索
     */

    public static final String SEARCH = HOST + "Apidirect/search/";
    /**
     * 开户
     */

    public static final String BANK = HOST + "apidirect/bank";


    /**
     * 获取群员
     */
    public static final String USERGROUP = HOST + "apidiscuss/getmembers/";

    /**
     * 购买量化股票池单条信息权限
     */
    public static final String VALIREAD = HOST + "Apizeji/valiread";


    public static String Sharepay = HOST + "Apicommon/sharepay/";

    /**
     * 获取顾问个人诊股记录（顾问角色登录）
     */
    public static final String GETADVISORRECORD = HOST + "apizeji/getadvisorrecord/";
    /**
     * 获取诊股记录详情回复
     */
    public static final String GETRECORDREPLYS = HOST + "apizeji/getrecordreplys/";
    /**
     * 提问顾问
     */
    public static final String ADVISORASK = HOST + "Apizeji/advisorAsk/";

    /**
     * 参赛
     */
    public static final String ENTERPOST = HOST + "apidiscuss/enterpost/";

    /**
     * 获取悬赏令信息
     */
    public static final String GETOFFERREWARD = HOST + "Apidiscuss/getofferlist";

    /**
     * 我发布的悬赏令
     */
    public static final String MYCOMMIT = HOST + "Apidiscuss/myrelease ";


    /**
     * 获取悬赏令回复
     */
    public static final String GETREWARDREPLYS = HOST + "Apidiscuss/getrewardreplys";
    /**
     * 举报
     */
    public static final String REPORT = HOST+"apidiscuss/report";
    /**
     * 我来回答
     */
    public static final String ANSWER = HOST + "apidiscuss/answer";

    /**
     * 点赞
     */
    public static final String CLICKGOOD = HOST + "Apidiscuss/clickgood";
    /**
     * ReleaseReward发布悬赏令
     */
    public static final String RELEASEREWARD = HOST + "Apidiscuss/ReleaseReward/";

    /**
     * ReleaseReward发布悬赏令
     */
    public static final String GETREWARDCATE = HOST + "Apidiscuss/getrewardcate";

    /**
     * 虚拟币支付
     */
    public static final String VMONEYPAY = HOST + "Apicommon/vimoneypay";
    /**
     * 赏金给回答的用户
     */
    public static final String SELECTREPLY = HOST + "Apidiscuss/selectreply/";


    /**
     * 择机接口
     */
    /**
     获取股指竞猜结果
     *
     */
    public static final String GETQUIZRESULTS1 = HOST + "Apizeji/getQuizResults/";

    /**
     comment评论
     *
     */
    public static final String COMMENT1 = HOST + "Apizeji/livecomment/";

    /**
     advisordetail获取顾问信息
     *
     */

    public static final String ADVISORDETAIL1 = HOST + "apizeji/advisordetail/";

    /**
     evaluate评论顾问
     *
     */
    public static final String EVALUATE1 = HOST + "apizeji/evaluate/";

    /**
     getstockpool量化股票池
     *
     */
    public static final String GETSTOCKPOOL = HOST + "Apizeji/getStockPool/";


    /**
     * 盘中直播信息列表
     */
    public static final String GETDIRECTSEEDING = HOST + "Apizeji/getDirectSeeding/";
    /**
     * 顾问自己发布直播信息列表
     */
    public static final String GETMYRELEASE = HOST + "Apizeji/getmyrelease/";
    /**
     * 获取直播发布标签
     */
    public static final String GETTAGS = HOST + "Apizeji/gettags/";
    /**
     * 判断用户或顾问是否能看直播或开直
     */
    public static final String VALIRUN = HOST + "Apizeji/valirun/";


    /**
     *
     ADVISORDETAILLIST获取顾问列表
     *
     */
    public static final String ADVISORDETAILLIST = HOST + "Apizeji/advisorlist";

    /**
     *
     getPutQuestionsToRecord获取提问记录
     */
    public static final String GETPUTQUESTIONSTORECORD1 = HOST + "Apizeji/getPutQuestionsToRecord/";

    /**
     *
     获取诊股记录详情
     */
    public static final String GETRECORDDETAIL = HOST + "Apizeji/getrecordreplys/";


    /**
     * 编码
     */
    public static String encode(String strUnCode) {
        try {
            return URLEncoder.encode(strUnCode, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUnCode;
    }
}
