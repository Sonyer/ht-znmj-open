package com.ht.znmj.sdk;


/**
 * Created by HuaChao on 2016/12/29.
 */

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;

public interface libFaceRecognition extends StdCallLibrary {
    /*libFaceRecognition INSTANCE = (libFaceRecognition) Native.loadLibrary(
            new File("").getAbsolutePath()+"\\dlls\\x64\\libFaceRecognitionSDK_x64.dll", libFaceRecognition.class);*/

    public static class FaceRecoInfo extends Structure {
        public int sequence;  //抓拍序号，从1开始，每产生一组抓拍数据增加1。
        public byte[] camId = new byte[32];    //相机编号
        public byte[] posId = new byte[32];     //点位编号
        public byte[] posName = new byte[96];   //点位名称

        public int tvSec;  //抓拍时间秒数，从1970年01月01日00时00分00秒至抓拍时间经过的秒数。
        public int tvUsec;  //抓拍时间微秒数，tvSec的尾数

        public short isRealtimeData;  //实时抓拍标志，0：非实时抓拍数据。非0：实时抓拍数据。

        public short matched;  //比对结果，0：未比对。-1：比对失败。大于0的取值：比对成功时的确信度分数（100分制）。
        public byte[] matchPersonId = new byte[20];    //人员ID
        public byte[] matchPersonName = new byte[16];   //人员姓名
        public int matchRole;  //人员角色，0：普通人员。 1：白名单人员。 2：黑名单人员


        public int existImg;  //全景图，是否包含全景图像。0：不包含全景图像。非0：包含全景图像。
        public byte[] imgFormat = new byte[4];          //全景图像格式
        public int imgLen;                 //全景图像大小
        public short faceXInImg;  //人脸位于全景图像的X坐标。
        public short faceYInImg;  //人脸位于全景图像的y坐标
        public short faceWInImg;  //人脸位于全景图像宽度
        public short faceHInImg;  //人脸位于全景图像高度

        public int existFaceImg;  //人脸图，特写图像标志，是否包含特写图像。0：不包含特写图像。非0：包含特写图像。
        public byte[] faceImgFormat = new byte[4];          //人脸图像封装格式。
        public int faceImgLen;                 //特写图像大小
        public short faceXInFaceImg;  //人脸位于特写图像的X坐标。
        public short faceYInFaceImg;  //人脸位于特写图像的y坐标。
        public short faceWInFaceImg;  //人脸位于特写图像的宽度
        public short faceHInFaceImg;  //人脸位于特写图像的高度

        public int existVideo;  //是否包含视频。0：不包含视频。非0：包含视频。
        public int videoStartSec;   //视频起始时间（秒）
        public int videoStartUsec;  // videoStartSec尾数，微妙
        public int videoEndSec;     //视频结束时间（秒）
        public int videoEndUsec;    // videoEndSec尾数，微妙
        public byte[] videoFormat = new byte[4];       //视频封装格式。
        public int videoLen;                 //视频大小

        public byte sex;         //性别 0: 无此信息 1：男 2：女
        public byte age;         //年龄 0: 无此信息 其它值：年龄
        public byte expression;  //表情 0: 无此信息 其它值：暂未定义
        public byte skinColour;  //肤色 0: 无此信息 其它值：暂未定义
        public byte qValue;  //注册标准分数，分数越高越适合用来注册
        public byte[] resv = new byte[123];          //保留

        public ByteByReference img;           //全景图像数据
        public ByteByReference faceImg;       //特写图像数据
        public ByteByReference video;         //视频数据
        public int feature_size;             //当前抓拍人脸的特征数据大小
        public FloatByReference feature;               //当前抓拍人脸的特征数据
        public int modelFaceImgLen;          //模板人脸图像长度
        public byte[] modelFaceImgFmt = new byte[4];       //模板人脸图像类型
        public ByteByReference modelFaceImg;  //模板人脸图像数据

        public static class ByReference extends FaceRecoInfo implements Structure.ByReference {
        }

        public static class ByValue extends FaceRecoInfo implements Structure.ByValue {
        }

        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{"sequence", "camId", "posId", "posName", "tvSec", "tvUsec",
                    "isRealtimeData", "matched", "matchPersonId", "matchPersonName", "matchRole", "existImg",
                    "imgFormat", "imgLen", "faceXInImg", "faceYInImg", "faceWInImg", "faceHInImg", "existFaceImg",
                    "faceImgFormat", "faceImgLen", "faceXInFaceImg", "faceYInFaceImg", "faceWInFaceImg", "faceHInFaceImg",
                    "existVideo", "videoStartSec", "videoStartUsec", "videoEndSec", "videoEndUsec", "videoFormat",
                    "videoLen", "sex", "age", "expression", "skinColour", "qValue", "resv", "img", "faceImg", "video",
                    "feature_size", "feature", "modelFaceImgLen", "modelFaceImgFmt", "modelFaceImg"});
        }

    }

    public static class FaceFlags extends Structure {

        public byte[] faceID= new byte[20];;///= new byte[20];   //人员ID
        public byte[] faceName= new byte[16];//= new byte[16];     //人员姓名
        public int role;  //人员角色，0：普通人员。 1：白名单人员。 2：黑名单人员。
        //-1：所有人员。
        public int wgCardNO;  //韦根协议门禁卡号
        public int
                effectTime;  //有效期截止时间，该人员信息在该时间内有效,从1970年1月1日0时0分0秒到截止时间的秒数（0xFFFFFFFF表示永久有效，0表示永久失效）
        public int
                effectStartTime;  //有效期起始时间，该人员信息在该时间之后有效,从1970年1月1日0时0分0秒到起始时间的秒数（0表示未初始化）
        public byte[] resv= new byte[8184];// = new byte[8184];
        public int usr_type;			////权限类型，取值 0 - 5，默认 0
        public static class ByReference extends FaceFlags implements Structure.ByReference {
        }

        public static class ByValue extends FaceFlags implements Structure.ByValue {
        }

        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{"faceID", "faceName", "wgCardNO", "effectTime", "effectStartTime", "resv","usr_type"});
        }

    }

    ;

    public static class FaceImage extends Structure {
        public int img_seq;         //图片编号
        public int img_fmt;         //图片格式 0：jpg 1：bgr
        public ByteByReference img;  //要注册的人脸图片数据，支持主流图像格式
        public int img_len;         // img的长度
        public int width;           //图像宽度，jpg图像不填此项
        public int height;          //图像高度，jpg图像不填此项

        public static class ByReference extends FaceImage implements Structure.ByReference {
        }

        public static class ByValue extends FaceImage implements Structure.ByValue {
        }

        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{"img_seq", "img_fmt", "img", "img_len", "width", "height"});
        }
    }
    public static class SystemNetInfo extends Structure {
    ;//系统网络信息

        public byte[] mac_addr=new byte[18];  //网卡物理地址
        public byte[] ip_addr=new byte[16] ;   //相机ip地址
        public byte[] netmask=new byte[16]   ;   //子网掩码
        public byte[] gateway=new byte[16]  ;   //默认网关
        public byte[] resv=new byte[14] ;        //保留

        public static class ByReference extends SystemNetInfo implements Structure.ByReference {
        }

        public static class ByValue extends SystemNetInfo implements Structure.ByValue {
        }

        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{"mac_addr", "ip_addr", "netmask", "gateway", "resv"});
        }
    };

    //人脸查询回调返回
    public static class QueryFaceInfo extends Structure {
        public int record_count;     //查询的角色记录总量
        public int record_no;        //当前收到的记录序号
        public byte[] personID = new byte[20];//人员ID
        public byte[] personName = new byte[16];  //姓名
        public int role;  //人员角色 0：普通人员。 1：白名单人员。 2：黑名单人员。
        //-1：所有人员。
        public short feature_count;  //当前id的特征数总量
        public short feature_size;   //人脸特征数据长度
        public FloatByReference feature;  //人脸特征数据，按顺序排列所有特征数据 featureSize*feature_count
        public int imgNum;   //模板图像数量
        public int[] imgSize = new int[5];     //图像大小，imgSize[i]为第i张图片的大小
        public byte[] imgFmt = new byte[20];  //图像格式,imgFmt[i]为第i张图片的格式
        public ByteByReference[] imgBuff = new ByteByReference[5];  // imgBuff[i]位第i张图片给数据首地址
        public int wgCardNO;      //韦根门禁卡号
        public int effectTime;  //有效期截止时间，该人员信息在该时间内有效,从1970年1月1日0时0分0秒到截止时间的秒数（0xFFFFFFFF表示永久有效，0表示永久失效）
        public int effectStartTime;  //有效期起始时间，该人员信息在该时间之后有效,从1970年1月1日0时0分0秒到起始时间的秒数（0表示未初始化）

        public static class ByReference extends QueryFaceInfo implements Structure.ByReference {
        }

        ;

        public static class ByValue extends QueryFaceInfo implements Structure.ByValue {
        }

        ;

        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{"record_count", "record_no", "personID", "personName", "role",
                    "feature_count", "feature_size", "feature", "imgNum", "imgSize", "imgFmt", "imgBuff",
                    "wgCardNO", "effectTime", "effectStartTime"});
        }
    }

    ;

    //搜索相机返回
    public static class ipscan_t extends Structure {
        //mac 地址
        public byte[] mac = new byte[20];
        //IP
        public byte[] ip = new byte[20];
        public byte[] netmask = new byte[20];
        //制造商
        public byte[] manufacturer = new byte[16];
        public byte[] platform = new byte[32];
        public byte[] system = new byte[32];
        public byte[] version = new byte[64];

        public static class ByReference extends ipscan_t implements Structure.ByReference {
        }

        ;

        public static class ByValue extends ipscan_t implements Structure.ByValue {
        }

        ;

        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{"mac", "ip", "netmask", "manufacturer", "platform",
                    "system", "version"});
        }
    }

    ;


    // public static class cameraPoint extends Structure {
    //     public static class ByReference extends cameraPoint implements Structure.ByReference {}
    //  public static class ByValue extends cameraPoint implements Structure.ByValue {}
    //    int camera_Point;
    //  }

    void lib_clsClear();

    /**
     * @param ip[in]         相机ip
     * @param port[in]       相机端口，固定为9527
     * @param usrName[in]    用户名，目前版本无效，传空即可
     * @param password[in]   密码，目前版本无效，传空即可
     * @param errorNum[out]  连接失败错误号，0为连接成功，-1为连接失败
     * @param channel[in]    码流通道号，有特殊需求的用户使用，无特殊需求用户直接传0通道即可
     * @param autoReconn[in] 自动重连标志，为1自动重连，0不会自动重连
     * @return 建议用户使用自动重连, autoReconn为1
     * @brief 连接相机
     */

    IntByReference ZBX_ConnectEx(String ip, short port, String usrName, String password,
                                 IntByReference errorNum, int channel, int autoReconn);

//  是否连接相机

    int ZBX_Connected(IntByReference cam);


    /**
     * @param 无
     * @return 无ZBX_SetNotifyConnected
     * @brief sdk初始化，
     * @brief 1)最多连接9个相机。
     * @brief 2)与ZBX_InitEx这两个函数，二者只能调其中一个。
     * @brief 3)如果想连接更多相机，请调用ZBX_InitEx
     */
    void ZBX_Init();


    int ZBX_DeInit();


    //设置是否需要相机连接上的事件的回调通知
    void ZBX_SetNotifyConnected(int notify);

    //断开相机
    void ZBX_DisConnect(IntByReference cam);

    /**
     * @param cam[in]  相机句柄
     * @param hWnd[in] 窗口句柄
     * @return 无
     * @brief 开始显示视频流
     */
    void ZBX_StartStream(IntByReference cam, WinDef.HWND hWnd);

    void ZBX_StartSecondStream(IntByReference cam, WinDef.HWND hWnd);

    void ZBX_StopStream(IntByReference cam);

    /**
     * @param cam[in] 相机句柄
     * @return 无
     * @brief 终止显示第二路视频流的某一个窗口
     */
    void ZBX_StopStreamEx(IntByReference cam, WinDef.HWND hWnd);

    /**
     * @param cb[in]       回调函数指针
     * @param usrParam[in] 用户参数
     * @return 无
     * @brief 注册人脸抓拍数据接收回调函数
     */
    void ZBX_RegFaceRecoCb(IntByReference cam, ZBX_FaceRecoCb_t cb,
                           Pointer usrParam);


    void ZBX_RegFaceOffLineRecoCb(IntByReference cam, ZBX_FaceRecoCb_t cb,
                             Pointer usrParam);


    /**
     * @param [IN] cam 要注册的相机句柄
     * @param [IN] faceID 人员标记，用于唯一标记注册的人脸
     * @param [IN] imgs 人脸图像，图像格式须为JPG
     * @param [IN] img_count 图像数量
     * @param [IN] picture_flags 下发图像到相机的标识，0表示不存图片到相机, >0
     *             表示存到相机的图片数量
     * @return 返回值为0表示成功，返回负数表示失败，具体参考错误码
     * @brief 注册人脸到相机，支持单个id多张人脸 最大5张人脸图像
     */
    int ZBX_AddJpgFaces(IntByReference cam, FaceFlags[] faceID,
                        FaceImage[] imgs, int img_count,
                        int picture_flags);

    /**
     * @param [IN] cam 要注册的相机句柄
     * @param [IN] faceID 人员标记，用于唯一标记注册的人脸
     * @param [IN] imgs 人脸图像，图像格式须为JPG
     * @return 返回值为0表示成功，返回负数表示失败，具体参考错误码
     * @brief 注册人脸到相机，支持单个id多张人脸 最大5张人脸图像
     */
    int ZBX_ModifyFaces(IntByReference cam, FaceFlags[] faceID,
                        FaceImage[] imgs);

    /**
     * @param [IN] cam 要查看的相机句柄
     * @param [IN] role 要查询的人员角色 0：普通人员。 1：白名单人员。
     *             2：黑名单人员。 -1：所有人员。
     * @param [IN] page_no 要查询的页码
     * @param [IN] page_size 每页的数据条数，用于数据分页 最大值100
     * @param [IN] featureFlags
     *             特征查询标记，是否查询特征信息，0表示需要，非0表示不需要
     * @param [IN] imgFlags 是否查询人脸图像
     * @return 返回值为0表示成功，返回负数表示失败，具体参考错误码
     * @brief 通过人员角色查询
     */
    int ZBX_QueryByRole(IntByReference cam, int role, int page_no,
                        int page_size, char featureFlags,
                        char imgFlags);

    /**
     * @param cb[in]       回调函数指针
     * @param usrParam[in] 用户参数
     * @return 无
     * @brief 注册人脸查询数据回调函数
     */
    void ZBX_RegFaceQueryCb(IntByReference cam, ZBX_FaceQueryCb_t cb,
                            Pointer usrParam);

    /**
     * @param [IN] cam 要注销的相机句柄
     * @param [IN] personID  需要删除的人脸ID
     * @return 返回值为0表示成功，返回负数表示失败，具体参考错误码
     * @brief 删除一个人员信息
     */
    int ZBX_DeleteFaceDataByPersonID(IntByReference cam,
                                     String personID);
    /**解密字段
     * @param   data[in][out]   解密的字串(解密后将作为输出)
     * @param   size[in][out]   解密的字串长度
     * @param   key[in]   密码字串
     * @param   key_size[in]   密码长度
     * @return  无
     */
     void ZBX_DecodeByKey(
             byte[] decode_data
             , int size
             , String data
             , int key_size);

    /**
     * @param [IN] cam 要删除的相机句柄
     * @return 返回值为0表示成功，返回负数表示失败，具体参考错误码
     * @brief 删除所有人脸
     */
    int ZBX_DeleteFaceDataAll(IntByReference cam);


    /**
     * @param cb[in]       回调函数指针
     * @param usrParam[in] 用户参数
     * @return 无
     * @brief 注册搜索相机回调函数
     */
    void ZBX_RegDiscoverIpscanCb(discover_ipscan_cb_t cb,
                                 int usrParam);

    /**
     * @param 无
     * @return 无
     * @brief 搜索相机
     */
    void ZBX_DiscoverIpscan();

    /**
     * @param cam[in]  相机句柄
     * @param type[in] 韦根类型 枚举类型为WG26,WG34, WG36, WG44，目前只支持WG26,
     *                 WG34
     * @return <0 设置失败
     * @brief 设置闸机韦根类型
     */
    int ZBX_SetWiegandType(IntByReference cam, int type);

    /**
     * @param cam[in]   相机句柄
     * @param type[out] 韦根类型 枚举类型为WG26,WG34, WG36, WG44，目前只支持WG26,
     *                  WG34
     * @return <0 获取失败
     * @brief 获取闸机韦根类型
     */
    int ZBX_GetWiegandType(IntByReference cam, IntByReference type);

    void lib_getimage(ByteByReference[] img5, byte[] img);

    /**
     * @param cam[in]        相机句柄
     * @param led_level[out] 亮度 1~100
     * @return <0 获取失败  参考错误码
     * @brief 获取led亮度
     */
    int ZBX_GetLedLevel(IntByReference cam, Pointer led_level);

    /**
     * @param cam[in]       相机句柄
     * @param led_mode[out] led灯模式 1：常亮 2：自动控制 3：常闭
     * @return <0 获取失败  参考错误码
     * @brief 获取led灯模式
     */
    int ZBX_GetLedMode(IntByReference cam, Pointer led_mode);

    /**
     * @param cam[in]      相机句柄
     * @param led_mode[in] led灯模式 1：常亮 2：自动控制 3：常闭
     * @return <0 设置失败  参考错误码
     * @brief 设置led灯模式
     */
    int ZBX_SetLedMode(IntByReference cam, byte led_mode);

    /**
     * @param cam[in]       相机句柄
     * @param led_level[in] 亮度 1~100
     * @return <0 设置失败  参考错误码
     * @brief 设置led灯亮度
     */
    int ZBX_SetLedLevel(IntByReference cam, byte led_level);


    /**
     * @param cam[in]       相机句柄
     * @param interval[out] 计算人员通过次数的间隔时间，单位：秒
     * @return <0 获取失败
     * @brief 获取计算人员通过次数的间隔时间，在该时间内人员只算通过一次
     */
    int ZBX_GetClusterTimesInterval(IntByReference cam, IntByReference interval);

    /**
     * @param cam[in]       相机句柄
     * @param interval[out] 计算人员通过次数的间隔时间，单位：秒
     * @return <0 获取失败
     * @brief 设置计算人员通过次数的间隔时间，在该时间内人员只算通过一次
     */
    int ZBX_SetClusterTimesInterval(IntByReference cam, int interval);


    /**
     * @param cam[in]    相机句柄
     * @param score[out] 确信分数（0-100分）
     * @return <0 获取失败  配置.h并没有更新
     * @brief 获取人脸比对确性分数
     */
    int ZBX_GetMatchScore(IntByReference cam, IntByReference score);

    /**
     * @param cam[in]   相机句柄
     * @param score[in] 确信分数（0-100分）
     * @return <0 设置失败  配置.h并没有更新
     * @brief 设置人脸比对确性分数
     */
    int ZBX_SetMatchScore(IntByReference cam, int score);


    /**
     * @param cam[in]           相机句柄
     * @param screen_title[out] 外接显示屏标题 UTF8格式 最大64个字节
     * @return <0 获取失败  参考错误码
     * @brief 获取外接显示屏标题
     */
    int ZBX_GetScreenOsdTitle(IntByReference cam, byte[] screen_title);

    /**
     * @param cam[in]          相机句柄
     * @param screen_title[in] 外接显示屏标题 UTF8格式 最大64个字节
     * @return <0 设置失败  参考错误码
     * @brief 设置外接显示屏标题
     */
    int ZBX_SetScreenOsdTitle(IntByReference cam, byte[] screen_title);

    /**
     * @brief   验证密钥
     * @return  无
     */
     void  ZBX_SetAppKey(IntByReference cam, byte[] key, int nsize);
    /**
     * @param cam[in]    相机句柄
     * @param year[in]   年份（如：2016）
     * @param month[in]  月份（如：5）
     * @param day[in]    日期（如：6）
     * @param hour[in]   小时（如：14）
     * @param minute[in] 分钟（如：35）
     * @param second[in] 秒（如：20）
     * @return <0 失败
     * @brief 设置相机时间
     */
    int ZBX_SetSysTimeEx(IntByReference cam, int year, int month, int day,
                         int hour, int minute, int second);


    /**
     * @param cam[in]  相机句柄
     * @param time[in] Unix时间
     * @return <0 失败
     * @brief 获取相机时间
     */
    int ZBX_GetSysTime(IntByReference cam, IntByReference time);

    /**
     * @param cb[in]       回调函数指针
     * @param usrParam[in] 用户参数
     * @return 无
     * @brief 注册连接事件回调函数, 须先调用ZBX_SetNotifyConnected(1)才生效
     */
    void ZBX_RegConnectEventCb(ZBX_ConnectEventCb_t cb,
                               int usrParam);


    // 判断是否是服务端的conn
    int ZBX_IsServer(IntByReference cam);


    /*
     * @brief   升级接口
     * @param   系统文件路径
     *

     * @return 0 成功
     */
    int ZBX_UpdateSystem(IntByReference cam, byte[] filePth);

    void ZBX_SetUpdateSystem_CB(IntByReference cam, ZBX_HTTPRESULT_PROCESS cb, Pointer user_data);



    /**
     * @brief   打开透明串口
     * @param   cam[in]   相机句柄
     * @param   index[in] 第N路串口,目前只支持 ZBX_SERIA_RS485  ZBX_SERIA_RS232
     * @param   baudrate[in] 波特率
     * @param   baudrate:只能为以下值：1200, 2400, 4800, 9600, 14400, 19200, 38400,
     * 56000, 57600, 115200, 128000, 256000
     * @param   parity[in] 校验位。0:none, 1:odd, 2:even, 3:mark, 4:space
     * @param   databit[in] 数据位，只能为5，6，7，8
     * @param   stopbit[in] 停止位，只能为1，2
     * @return  0 成功
     * @return  <0 失败
     */
     int  ZBX_OpenTSerial(IntByReference cam, int index, int baudrate,
                          int parity, int databit, int stopbit);

    /**
     * @brief   设置485输出协议编号
     * @param   cam[in]   相机句柄
     * @param   rs485_protocal_no[in] rs485输出协议编号 0：表示关
     * @return  0 设置成功
     * @return  <0 设置失败  参考错误码
     */
     int    ZBX_SetRS485ProtocalNo(IntByReference cam, char rs485_protocal_no);

    /**
     * @brief   获取透明串口参数
     * @param   cam[in]   相机句柄
     * @param   index[in] 第N路串口,目前只支持 ZBX_SERIA_RS485  ZBX_SERIA_RS232
     * @param   baudrate[out] 波特率
     * @param   baudrate:只能为以下值：1200, 2400, 4800, 9600, 14400, 19200, 38400,
     * 56000, 57600, 115200, 128000, 256000
     * @param   parity[out] 校验位。只能为0，无校验位
     * @param   databit[out] 数据位，只能为5，6，7，8
     * @param   stopbit[out] 停止位，只能为1，2
     * @return  0 成功
     * @return  <0 失败
     */
     int    ZBX_GetTSerial(IntByReference cam, int index, IntByReference baudrate,
                           IntByReference parity, IntByReference databit, IntByReference stopbit);

    /**
     * @brief   获取485输出协议编号
     * @param   cam[in]   相机句柄
     * @param   rs485_protocal_no[out] rs485输出协议编号 0：表示关
     * @return  0 获取成功
     * @return  <0 获取失败  参考错误码
     */
     int    ZBX_GetRS485ProtocalNo(IntByReference cam,
                                   byte[] rs485_protocal_no);


    /**
     * @brief   写透明串口
     * @param   cam[in]   相机句柄
     * @param   index[in] 第N路串口,目前只支持 ZBX_SERIA_RS485  ZBX_SERIA_RS232
     * @param   data[in] 数据
     * @param   size[in] 数据长度
     * @return  0 成功
     * @return  <0 失败
     */
     int    ZBX_WriteTSerial(IntByReference cam, int index,
                             byte[] data, int size);

    /**
     * @brief   查看网络参数配置
     * @param   cam[in]   相机句柄
     * @param   netInfo[out] 网络参数
     * @return  0 获取成功
     * @return  <0 获取失败  参考错误码
     */
     int    ZBX_GetNetConfig(IntByReference cam, SystemNetInfo.ByReference netInfo);


    /**
     * @brief   设置网络参数配置
     * @param   cam[in]   相机句柄
     * @param   netInfo[in] 网络参数
     * @return  0 设置成功
     * @return  <0 设置失败  参考错误码
     */
     int    ZBX_SetNetConfig(IntByReference cam, SystemNetInfo.ByReference netInfo);


    /**
     * @brief   通过mac地址，跨网段设置相机IP
     * @param   mac[in] mac地址
     * @param   ip[in] 相机IP
     * @param   netmask[in] 子网掩码
     * @param   gateway[in] 默认网关
     * @return  无
     */
     void    ZBX_SetIpBymac(byte[] mac, byte[] ip,
                            byte[] netmask, byte[] gateway);

    public interface ZBX_FaceRecoCb_t extends StdCallCallback {
        public void Status(IntByReference cam, FaceRecoInfo.ByReference cb,
                           Pointer usrParam);

    }

    //脸查询数据回调函数
    public interface ZBX_FaceQueryCb_t extends StdCallCallback {
        public void Status(IntByReference cam, QueryFaceInfo.ByReference FaceQueryCb,
                           Pointer usrParam);
    }

    //搜索相机回调函数
    public interface discover_ipscan_cb_t extends StdCallCallback {
        public void Status(ipscan_t.ByReference ips, int usr_param);
    }


    /* 连接事件回调函数 */
// event 1为已连接 2为连接中断
    public interface ZBX_ConnectEventCb_t extends StdCallCallback {
      public  void Status(IntByReference cam, String ip,
                          short port, int event,
                          int usrParam);
    }

    //系统升级进度
//注意：如果注册此回调 该回调必须返回0 否则会终止传输
    public interface ZBX_HTTPRESULT_PROCESS extends StdCallCallback {
       public int Status(Pointer user_data, double rDlTotal, double rDlNow, double rUlTotal, double rUlNow);
    }

    /**
     * 触发白名单报警，强制模式开闸
     * @param cam 相机句柄
     * @param min 输入、输出 1输入 2输出
     * @param onoff  开关 1连通 0断开
     * @return
     */
    int ZBX_WhiteListAlarm(IntByReference cam, int inout, int onoff);


    /**
     *  获取离线数据
     * @param cam
     * @return
     */
    int ZBX_QueryFaceOffLineReco(IntByReference cam,int squece,FaceRecoInfo face_info);

    /**
     *  获取当前识别数据
     * @param cam
     * @return
     */
    int ZBX_GetCurSquece(IntByReference cam,int squece);


    /**
     * 通过时间段获取识别记录
     * @param cam
     * @param s_time
     * @param e_time
     * @param s_seq
     * @param e_seq
     * @return
     */
    int ZBX_GetResByTime(IntByReference cam, String s_time, String e_time,int s_seq,int e_seq);
}
