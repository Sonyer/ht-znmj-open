package com.ht.znmj.sdk;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.EquipmentInfoConnStatus;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.view.service.HomeEquipmentUtil;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class MyFunction
{
    public static libFaceRecognition m_FaceRecognition = null;
    public static IntByReference cameraPoint = new IntByReference(0);
    public static boolean BOEStream;
    public static Map<String, String> mapcamreip = new HashMap<String, String>();
    public static Map<String, String> mapcamresn = new HashMap<String, String>();
    byte[] m_rs485_protocal_no = new  byte[1];

    private static ZBX_FaceRecoCb_t_Realize m_RecoCb_t = null;// new ZBX_FaceRecoCb_t_Realize();
    private static ZBX_FaceQueryCb_t_Realize m_FaceQueryCb_t = null;
    private static discover_ipscan_cb_t_Realize m_discover_ipscan = null;
    private static ZBX_ConnectEventCb_t_Realize m_ConnectEventCb = null;
    private static Httpresult_Realize m_HTTPRESULT_PROCESS=null;
    MyFunction() {
        BOEStream = false;
    }

    public static void Init() {
        m_FaceRecognition.ZBX_Init();
        m_FaceRecognition.ZBX_SetNotifyConnected(1);
        m_ConnectEventCb=new  ZBX_ConnectEventCb_t_Realize();
        m_FaceRecognition.ZBX_RegConnectEventCb(m_ConnectEventCb,0);
    }

    public static void Close() {
        m_FaceRecognition.ZBX_DeInit();
    }

    public static void clsClear() {
        m_FaceRecognition.lib_clsClear();
    }

    //????????????
    public static void connectCamera(String CameraIp) {
        Scanner sc = new Scanner(System.in);
        IntByReference err_code = new IntByReference(0);
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) == 1) {
            System.out.println("???????????????????????????????????????????????????????????????");
            return;
        }
        cameraPoint = m_FaceRecognition.ZBX_ConnectEx(CameraIp, (short) 8099, "", "", err_code,
                0, 1);
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("??????????????????");
        } else {
            m_RecoCb_t = new ZBX_FaceRecoCb_t_Realize();
            m_FaceRecognition.ZBX_RegFaceRecoCb(cameraPoint, m_RecoCb_t, Pointer.NULL);
            System.out.println("??????????????????");
        }

    }

    //????????????
    public static void DisConnectCamera() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) == 0) {
            System.out.println("???????????????????????????????????????");
            return;
        }
        m_FaceRecognition.ZBX_DisConnect(cameraPoint);
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) == 0) {
            System.out.println("????????????");
            return;
        } else System.out.println("???????????????????????????");
    }


    private static WinDef.HWND createHWNDByComponent(JLabel jlabel) {
        return new WinDef.HWND(Native.getComponentPointer(jlabel));
    }
    //  public static JFrame jf ;

    /*public static Demo1 d1;

    // ??????/???????????????
    public static void StartStream() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }

        if (!BOEStream) {
            d1 = new Demo1();
            //??????????????????
            m_FaceRecognition.ZBX_StartStream(cameraPoint, createHWNDByComponent(d1.jl1));
            System.out.println("?????????????????????");
            BOEStream=true;
        } else {
            //??????????????????
            m_FaceRecognition.ZBX_StopStreamEx(cameraPoint, createHWNDByComponent(d1.jl1));
            System.out.println("?????????????????????");
            BOEStream=true;
        }
    }*/

    public static void AddFace(VisitorInfo visitorInfo, EquipmentInfo equipmentInfo) {
        IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        libFaceRecognition.FaceFlags.ByValue flag = new libFaceRecognition.FaceFlags.ByValue();
        libFaceRecognition.FaceImage.ByValue faceimg = new libFaceRecognition.FaceImage.ByValue();
        libFaceRecognition.FaceImage.ByValue[] faceimgarray = (libFaceRecognition.FaceImage.ByValue[]) faceimg.toArray(1);
        libFaceRecognition.FaceFlags.ByValue[] flagarray = (libFaceRecognition.FaceFlags.ByValue[]) flag.toArray(1);
        System.out.println("?????????????????????");
        Scanner sc = new Scanner(System.in);
        flagarray[0].faceName = visitorInfo.getName().getBytes();
        System.out.println("?????????"+ flagarray[0].faceName);
        System.out.println("???????????????id");
        flagarray[0].faceID = visitorInfo.getId().getBytes();

        System.out.println("faceIDid"+flagarray[0].faceID);
        flagarray[0].usr_type = 0;
        flagarray[0].wgCardNO =1234;

        //flagarray[0].effectTime = 290000000;
        //flagarray[0].effectStartTime = 250000000;
        //flagarray[0].resv = "";
        System.out.println("?????????????????????+????????????");
        String filepath = visitorInfo.getFaceFilePath();
        if (filepath.isEmpty()) {
            System.out.println("????????????");
        } else {
            byte[] imagedata = imageToByteArray(filepath);
            faceimgarray[0].img_len = imagedata.length;
            Memory a = new Memory(imagedata.length);
            a.write(0, imagedata, 0, imagedata.length);
            faceimgarray[0].img.setPointer(a);
            faceimgarray[0].img_fmt = 0;
            try {
                byte[] imagedata1 = new byte[imagedata.length];
                a.read(0, imagedata1, 0, imagedata.length);
                String filename = "D:\\fgs" + ".jpg";
                // ???????????????
                BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imagedata1));
                //????????????
                writeImageFile(bi, filename);
            } catch (Exception e) {
                System.out.println("????????????");
                return;
            }

            int k = m_FaceRecognition.ZBX_AddJpgFaces(cameraPoint, flagarray,
                    faceimgarray, 1, 0);

            if (k == 0) {
                System.out.println("????????????");
            } else {
                System.out.println("????????????"+k);
            }
        }
    }


    //??????????????????
    public static void degregmng() {
        System.out.println("???????????????:  1????????? 2????????? 3???????????????  ");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
        }
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        if (chooice == 1) {
            m_FaceQueryCb_t = new ZBX_FaceQueryCb_t_Realize();
            m_FaceRecognition.ZBX_RegFaceQueryCb(cameraPoint, m_FaceQueryCb_t, Pointer.NULL);

            int idx = 1;//????????????
            int num = 10;//????????????
            m_FaceRecognition.ZBX_QueryByRole(cameraPoint, -1, idx, num, '0', '0');
        } else if (chooice == 2) {
            System.out.println("?????????????????????id  ");
            String personid = sc.nextLine();
            m_FaceRecognition.ZBX_DeleteFaceDataByPersonID(cameraPoint, personid);
        } else if (chooice == 3) {
            int tag = m_FaceRecognition.ZBX_DeleteFaceDataAll(cameraPoint);
            if (tag == 0) System.out.println("??????????????????  ");
            else System.out.println("??????????????????  ");
        } else System.out.println("????????????");

    }

    //????????????
    public static void searchcerme() {
        m_discover_ipscan = new discover_ipscan_cb_t_Realize();
        m_FaceRecognition.ZBX_RegDiscoverIpscanCb(m_discover_ipscan, 0);
        m_FaceRecognition.ZBX_DiscoverIpscan();
    }

    //????????????
    public static void SettingWG() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        System.out.println("???????????????:  1????????? 2?????????  ");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
            return;
        }
        if (chooice == 1) {
            System.out.println("????????????????????????  ");
            int temp = 0;
            try {
                temp = sc.nextInt();
            } catch (Exception e) {
                System.out.println("????????????");
                return;
            }
            int tag = m_FaceRecognition.ZBX_SetWiegandType(cameraPoint, temp);
            if (tag == 0) System.out.println("????????????");
            else System.out.println("????????????");
        } else if (chooice == 2) {
            IntByReference type = new IntByReference(0);
            if (m_FaceRecognition.ZBX_GetWiegandType(cameraPoint, type) == 0) {
                System.out.println(type.getValue());

            } else {
                System.out.println("????????????");
            }

        } else {
            System.out.println("????????????");
            return;
        }
    }

    //????????????
    void SettingLight() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        System.out.println("???????????????:  1????????? 2?????????  ");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
            return;
        }
        if (chooice == 1) {
            System.out.println("????????????????????? ");
            sc = new Scanner(System.in);
            byte led_mode = (byte) sc.nextInt();
            m_FaceRecognition.ZBX_SetLedMode(cameraPoint, led_mode);
            System.out.println("?????????????????? ");
            sc = new Scanner(System.in);
            byte led_level = (byte) sc.nextInt();
            m_FaceRecognition.ZBX_SetLedLevel(cameraPoint, led_level);
        } else if (chooice == 2) {
            Pointer a = new Memory(2);
            Pointer led_mode = new Memory(10);

            Pointer led_level = new Memory(10);
            m_FaceRecognition.ZBX_GetLedMode(cameraPoint, led_mode);
            for (int i = 0; i < 10; ++i) {
                if ((char) led_mode.getByteArray(0, 10)[i] == '\0') break;
                System.out.print((char) led_mode.getByteArray(0, 10)[i]);
            }
            System.out.println("\n");
            System.out.println(led_mode);
            m_FaceRecognition.ZBX_GetLedLevel(cameraPoint, led_level);
            for (int i = 0; i < 10; ++i) {
                if ((char) led_level.getByteArray(0, 10)[i] == '\0') break;
                System.out.print((char) led_level.getByteArray(0, 10)[i]);
            }
            System.out.println("\n");
        } else {
            System.out.println("????????????");
            return;
        }
    }

    //???????????????
    void Repetition() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        System.out.println("???????????????:  1????????? 2????????? ");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
        }
        if (chooice == 1) {
            IntByReference interval = new IntByReference(-1);
            int succ = m_FaceRecognition.ZBX_GetClusterTimesInterval(cameraPoint, interval);
            if (succ == 0) {
                System.out.println("???????????? ");
                System.out.println(interval.getValue());
            } else {
                System.out.println("???????????? ");
            }
        } else if (chooice == 2) {
            int interval = -1;
            System.out.println("???????????????????????????");
            try {
                interval = sc.nextInt();
            } catch (Exception e) {
                System.out.println("????????????");
            }
            int succ = m_FaceRecognition.ZBX_SetClusterTimesInterval(cameraPoint, interval);
            if (succ == 0) {
                System.out.println("???????????? ");
            } else {
                System.out.println("???????????? ");
            }
        } else {
            System.out.println("????????????");
            return;
        }

    }

    //???????????????
    void SimilaritySetting() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        System.out.println("???????????????:  1????????? 2????????? ");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
        }
        if (chooice == 1) {
            IntByReference score = new IntByReference(-1);
            int succ = m_FaceRecognition.ZBX_GetMatchScore(cameraPoint, score);
            if (succ == 0) {
                System.out.println("???????????? ");
                System.out.println(score.getValue());
            } else {
                System.out.println("???????????? ");
            }
        } else if (chooice == 2) {
            int score = -1;
            System.out.println("??????????????????");
            try {
                score = sc.nextInt();
            } catch (Exception e) {
                System.out.println("????????????");
            }
            if (score < 0 || score > 100) {
                System.out.println("??????????????????????????? ??????0~100");
            }
            int succ = m_FaceRecognition.ZBX_SetMatchScore(cameraPoint, score);
            if (succ == 0) {
                System.out.println("???????????? ");
            } else {
                System.out.println("???????????? ");
            }
        } else {
            System.out.println("????????????");
            return;
        }

    }

    //????????????
    void TagSetting() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        System.out.println("???????????????:  1????????? 2????????? ");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
        }
        if (chooice == 1) {
            byte[] screen_title = new byte[64];

            int succ = m_FaceRecognition.ZBX_GetScreenOsdTitle(cameraPoint, screen_title);
            if (succ == 0) {
                try
                {            String title=new String (getfullbyte(screen_title),"gb2312");
                    System.out.println(title );}
                catch (Exception e) {
                    System.out.println("????????????");}
                System.out.println("???????????? ");

            } else {
                System.out.println("???????????? ");
            }
        } else if (chooice == 2) {
            byte[] title = new byte[64];
            System.out.println("??????????????????");
            try {
                Scanner sc1 = new Scanner(System.in);
                String t = sc1.nextLine();
                title = (t).getBytes();
            } catch (Exception e) {
                System.out.println("????????????");
            }

            int succ = m_FaceRecognition.ZBX_SetScreenOsdTitle(cameraPoint, title);
            if (succ == 0) {
                System.out.println("???????????? ");
            } else {
                System.out.println("???????????? ");
            }
        } else {
            System.out.println("????????????");
            return;
        }

    }

    //????????????
 public static   void TimeSetting() {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        System.out.println("???????????????:  1????????????????????? 2????????? ");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
        }
        if (chooice == 1) {

            IntByReference second = new IntByReference();
            int succ = m_FaceRecognition.ZBX_GetSysTime(cameraPoint, second);
            if (succ == 0) {
                Date date2 = new Date();
                long t = 1000;
                date2.setTime(second.getValue() * t);
                System.out.print(date2);
                System.out.println('\n');
            } else {
                System.out.println("???????????? ");
            }
        } else if (chooice == 2) {

            System.out.println("?????????????????????????????????");

            int succ = m_FaceRecognition.ZBX_SetSysTimeEx(cameraPoint, 2019, 4, 1, 8, 0, 0);
            if (succ == 0) {
                System.out.println("???????????? ");
            } else {
                System.out.println("???????????? ");
            }
        } else {
            System.out.println("????????????");
            return;
        }

    }
//????????????
    void upgrade()
{
    if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
        System.out.println("???????????????");
        return;
    }
    System.out.println("????????????????????????+????????? ");

   String filename=null;
    try {
        Scanner sc = new Scanner(System.in);
         filename = sc.nextLine();
    } catch (Exception e) {
        System.out.println("????????????");
    }
    Pointer user_data=null;
        IntByReference second = new IntByReference();
    m_HTTPRESULT_PROCESS=new Httpresult_Realize();
    m_FaceRecognition.ZBX_SetUpdateSystem_CB(cameraPoint,m_HTTPRESULT_PROCESS
            ,user_data);
    int iRet = m_FaceRecognition.ZBX_UpdateSystem(cameraPoint,
            filename.getBytes());

    if (iRet == 0) {
        System.out.println("???????????? ??????????????????");
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) == 1)
            m_FaceRecognition.ZBX_DisConnect(cameraPoint);
        cameraPoint = (null);
    }else
    {
        System.out.println("???????????? ?????????");
    }
}


    //????????????
    void GorgelineSetting()
    {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }
        System.out.println("???????????????:  1??????????????? 2????????? ???3??? ??????????????????");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
        }
        if (chooice == 1) {


            int succ =  m_FaceRecognition.    ZBX_GetRS485ProtocalNo(cameraPoint,m_rs485_protocal_no);
            System.out.print(deCode(new String(getfullbyte(m_rs485_protocal_no))).trim());
            System.out.println(' ');
            IntByReference baudrate = new IntByReference();
            IntByReference parity = new IntByReference();
            IntByReference databit = new IntByReference();
            IntByReference stopbit = new IntByReference();

             succ =  m_FaceRecognition. ZBX_GetTSerial(cameraPoint, 0,  baudrate,
                     parity,  databit,  stopbit);
            if (succ == 0) {
                System.out.print(baudrate.getValue());
                System.out.println(' ');
                System.out.print(parity.getValue());
                System.out.println(' ');
                System.out.print(databit.getValue());
                System.out.println(' ');
                System.out.print(stopbit.getValue());
                System.out.println(' ');

            } else {
                System.out.println("???????????? ");
            }
        } else if (chooice == 2) {

            //????????????
            int index=0,  baudrate=115200,
                    parity=0,  databit=8,  stopbit=1;
            char rs485_protocal_no='0';
          int tag=  m_FaceRecognition.ZBX_OpenTSerial( cameraPoint,  index,  baudrate,
                    parity,  databit,  stopbit);
            if (tag == 0)
            {
                System.out.println("??????????????????");
            }
            else {  System.out.println("??????????????????"); }
            tag=  m_FaceRecognition.ZBX_SetRS485ProtocalNo(cameraPoint,rs485_protocal_no );
            if (tag == 0)
            {
                System.out.println("????????????????????????");
            }
            else {  System.out.println("????????????????????????"); }
        }else if(chooice==3)
        {
            String t="";
            System.out.println("?????????????????????????????????");
            try {
                Scanner sc1 = new Scanner(System.in);
                t = sc1.nextLine();

            } catch (Exception e) {
                System.out.println("????????????");
            }
            int  succ= m_FaceRecognition. ZBX_WriteTSerial(cameraPoint , (int) m_rs485_protocal_no[0],
                    t .getBytes(), t.length());
            if(succ==0)
            {
                System.out.println("????????????");
            }
            else {  System.out.println("????????????"); }
        } else {
            System.out.println("????????????");
            return;
        }

    }
    void webSetting()
    {
        if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
            System.out.println("???????????????");
            return;
        }

        System.out.println("???????????????:  1????????????????????? 2????????????3?????????ip");
        Scanner sc = new Scanner(System.in);
        int chooice = -1;
        try {
            chooice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("????????????");
        }
        if (chooice == 1) {

            libFaceRecognition. SystemNetInfo.ByReference SystemNet=new libFaceRecognition.SystemNetInfo.ByReference();
            int succ =  m_FaceRecognition.ZBX_GetNetConfig(cameraPoint,SystemNet);
            if(succ==0)
            {
                System.out.println("????????????????????????");
                System.out.println(deCode(new String(getfullbyte((SystemNet.ip_addr))).trim()));
                System.out.println(deCode(new String(((SystemNet.mac_addr))).trim()));
                System.out.println(deCode(new String(getfullbyte((SystemNet.netmask))).trim()));
                System.out.println(deCode(new String(((SystemNet.gateway))).trim()));
            }
       else {
                System.out.println("???????????????????????? ");
            }
        } else if (chooice == 2) {

            //????????????
            libFaceRecognition. SystemNetInfo.ByReference netInfo=new libFaceRecognition.SystemNetInfo.ByReference();
            System.out.println("?????????ip");
            Scanner sc1 = new Scanner(System.in);
            netInfo.ip_addr = sc1.nextLine().getBytes();//.getBytes();
            System.out.println("?????????mac");
            netInfo.mac_addr = sc1.nextLine().getBytes();//.getBytes();
            System.out.println("?????????????????????");
            netInfo.netmask = sc1.nextLine().getBytes();//.getBytes();
            System.out.println("?????????????????????");
            netInfo.gateway = sc1.nextLine().getBytes();//.getBytes();
            int  tag= m_FaceRecognition. ZBX_SetNetConfig( cameraPoint, netInfo);

            if (tag == 0)
            {
                System.out.println("????????????");
            }
            else {  System.out.println("????????????"); }

        }else if(chooice==3)
        {
            System.out.println("?????????????????????mac");
            Scanner sc1 = new Scanner(System.in);
            byte[] mac_addr = sc1.nextLine().getBytes();//.getBytes();
            System.out.println("?????????????????????netmask");
            byte[] netmask = sc1.nextLine().getBytes();//.getBytes();
            System.out.println("?????????????????????gateway");
            byte[] gateway = sc1.nextLine().getBytes();//.getBytes();
            System.out.println("????????????ip");
            byte[] ip_addr = sc1.nextLine().getBytes();//.getBytes();
            m_FaceRecognition. ZBX_SetIpBymac(mac_addr,ip_addr,
             netmask,   gateway);

        } else {
            System.out.println("????????????");
            return;
        }

    }

    void DecodeDemo() throws UnsupportedEncodingException {
        try {
            System.out.println("?????????????????????????????????");
            Scanner sc1 = new Scanner(System.in);
            byte[] decoded_data = sc1.nextLine().getBytes();
            System.out.println("??????????????????");
            byte[] key = sc1.nextLine().getBytes();//.getBytes();
            m_FaceRecognition.ZBX_DecodeByKey(decoded_data,decoded_data.length,
                    sc1.nextLine(),sc1.nextLine().length());

                String decoded=new String(decoded_data,"gb2312");
                System.out.println("??????????????????"+decoded);
        } catch (Exception e) {
            System.out.println("????????????" +e.toString());
        }

    }
    //??????????????????
    public static class ZBX_FaceRecoCb_t_Realize implements libFaceRecognition.ZBX_FaceRecoCb_t {
        public void Status(IntByReference cam, libFaceRecognition.FaceRecoInfo.ByReference cb,
                           Pointer usrParam) {
            String savepath = "D:\\face";
            File myPath = new File(savepath);
            if (!myPath.exists()) {
                myPath.mkdirs();
            }
            System.out.println(cb.tvSec);
            long t = 1000;
            SimpleDateFormat lFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            Date date2 = new Date();
            date2.setTime(cb.tvSec * t);
            String datetime=lFormat.format(date2);

            if (cb.matched == -1) {

                System.out.println("???????????????");
                System.out.println(datetime);
                String filepath = savepath + "\\unidentified\\";
                File myfilepath = new File(filepath);
                if (!myfilepath.exists()) {
                    myfilepath.mkdirs();
                }

                String filename = filepath + datetime  + ".jpg";
                byte[] data = cb.faceImg.getPointer().getByteArray(0, cb.faceImgLen);
                try {
                    // ???????????????
                    BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
                    //????????????
                    writeImageFile(bi, filename);
                } catch (Exception e) {
                    System.out.println("????????????");
                    return ;
                }

            } else {
                //??????????????????
                try
                {
                    String personname=new String (getfullbyte(cb.matchPersonName),"gb2312");
                    System.out.println(personname );
                    String perid=new String (getfullbyte(cb.matchPersonId),"gb2312");
                    System.out.println(" Matched person iD:"+ getfullbyte(cb.matchPersonId) );
                    byte[] key="mizue".getBytes();
                    byte[] matedid = new byte[20];
                    matedid = getfullbyte(cb.matchPersonId);
                    String key_s = "mizue";
                    m_FaceRecognition.ZBX_DecodeByKey(matedid,20
                    ,key_s,key_s.length());
                    String perid_d=new String (getfullbyte(matedid),"gb2312");
                    System.out.println("Matched person iD:"+perid_d );
                    //m_FaceRecognition.ZBX_SetAppKey(cam,key,key.length);
                }
                catch (Exception e) {
                    System.out.println("????????????");}


                System.out.println(datetime );
                String filepath = savepath +"\\identified\\";
                File myfilepath = new File(filepath);
                if (!myfilepath.exists()) {
                    myfilepath.mkdirs();
                }
                String filename = filepath + datetime + ".jpg";
                byte[] data = cb.faceImg.getPointer().getByteArray(0, cb.faceImgLen);
                try {
                    // ???????????????
                    BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
                    //????????????
                   writeImageFile(bi, filename);
                } catch (Exception e) {
                    System.out.println("????????????");
                    System.out.println(e.toString());
                    return ;
                }


//????????????????????????
                String modepath = savepath + "\\mode\\";
                File mymodepath = new File(modepath);
                if (!mymodepath.exists()) {
                    mymodepath.mkdirs();
                }
                String path_mode = modepath + datetime + ".jpg";
                byte[] data_mode = cb.modelFaceImg.getPointer().getByteArray(0, cb.modelFaceImgLen);
                try {
                    // ???????????????
                    BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data_mode));
                    //????????????
                    writeImageFile(bi, path_mode);
                } catch (Exception e) {
                    System.out.println("????????????1");
                    System.out.println(e.toString());
                    return ;
                }
            }
            return ;
        }
    }

    //????????????????????????
    public static class ZBX_FaceQueryCb_t_Realize implements libFaceRecognition.ZBX_FaceQueryCb_t {
        public void Status(IntByReference cam, libFaceRecognition.QueryFaceInfo.ByReference QueryFaceIn,
                           Pointer usrParam) {
            try
            {
                String personname=new String (getfullbyte(QueryFaceIn.personName),"gb2312");
                System.out.println(personname );}
            catch (Exception e) {
                System.out.println("????????????");}

            String personId = deCode(new String(QueryFaceIn.personID)).trim();// deCode(matchPersonID).trim();
            System.out.println(personId);
            //???data ????????????byte??????
            byte[] data = QueryFaceIn.imgBuff[0].getPointer().getByteArray(0, QueryFaceIn.imgSize[0]);

            //
            SimpleDateFormat lFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            Date date = new Date();
            date.getTime();
            String path = "D:\\camera";   //??????????????????
            File myPath = new File(path);
            if (!myPath.exists()) {
                myPath.mkdir();
            }
            String filename = path + lFormat.format(date) + ".jpg";

            System.out.println(filename);

            try {
                // ???????????????
                BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
                //????????????
                writeImageFile(bi, filename);
            } catch (Exception e) {
                System.out.println("????????????");
                return;
            }
        }
    }


    //????????????????????????
    public static class discover_ipscan_cb_t_Realize implements libFaceRecognition.discover_ipscan_cb_t {
        public void Status(libFaceRecognition.ipscan_t.ByReference ips, int usr_param) {
            if (mapcamreip.containsKey(deCode(new String(ips.mac)).trim()) == false) {
                //  ???????????????ip
                System.out.println(deCode(new String(ips.ip)).trim());
                System.out.println(deCode(new String(ips.mac)).trim());
                System.out.println(deCode(new String(ips.manufacturer)).trim());
            } else {
                mapcamreip.put(deCode(new String(ips.mac)).trim(), deCode(new String(ips.ip)).trim());
            }
        }
    }


    /* ???????????????????????? */
    // event 1???????????? 2???????????????
    public static class ZBX_ConnectEventCb_t_Realize implements libFaceRecognition.ZBX_ConnectEventCb_t {
        public void Status(IntByReference cam, String ip, short port, int event, int usrParam) {
            if (event == 0) {
                if (ZBX_Global.EQUIPMENT_IP_SN_MAP.get(ip) != null
                        && ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(ZBX_Global.EQUIPMENT_IP_SN_MAP.get(ip)) != null) {
                    String sn = ZBX_Global.EQUIPMENT_IP_SN_MAP.get(ip);
                    m_FaceRecognition.ZBX_DisConnect(ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(sn));

                    HomeEquipmentUtil.updateConnStatus(sn, EquipmentInfoConnStatus.NO_CONNECT.getCode());

                    log.info("????????????:(MAC="+sn+";)");
                }
                return;
            }
            if (event == 1) {
                if (ZBX_Global.EQUIPMENT_IP_SN_MAP.get(ip) != null) {
                    String sn = ZBX_Global.EQUIPMENT_IP_SN_MAP.get(ip);
                    ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.put(sn,cam);

                    HomeEquipmentUtil.updateConnStatus(sn, EquipmentInfoConnStatus.CONNECTED.getCode());

                    Cust_ZBX_FaceRecoCb_t_Realize m_RecoCb_t = new Cust_ZBX_FaceRecoCb_t_Realize(sn);
                    m_FaceRecognition.ZBX_RegFaceRecoCb(cam, m_RecoCb_t, Pointer.NULL);
                    m_FaceRecognition.ZBX_RegFaceOffLineRecoCb(cam, m_RecoCb_t, Pointer.NULL);

                    log.info("???????????????:(MAC="+sn+";)");
                }
            }
        }
    }

    //??????????????????
//?????????????????????????????? ?????????????????????0 ?????????????????????
    public static class Httpresult_Realize implements libFaceRecognition.ZBX_HTTPRESULT_PROCESS {
        public int Status(Pointer user_data, double rDlTotal, double rDlNow, double rUlTotal, double rUlNow)
        {
            if (!(rUlTotal==0) && !(rUlNow==0))
            {
                return 0;
            }
            int process =(int)  (rDlNow * 100 / rUlTotal);
            System.out.println("???????????????????????? ");
            System.out.println(process);
            return 0;
        }
    }


    //byte???????????????
    public static  void byte2image(byte[] data,String path){
        if(data.length<3||path.equals("")) return;
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }



    public static String deCode(String str) {
        try {
            return java.net.URLDecoder.decode(str, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String deCodeUtf_8(String str) {
        try {
            return java.net.URLDecoder.decode(str,   "Unicode");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

  //  ?????????????????????byte[]
    public static byte[] imageToByteArray(String imgPath) {
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(imgPath));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int size = 0;
            byte[] temp = new byte[1024];
            while((size = in.read(temp))!=-1) {
                out.write(temp, 0, size);
            }
            in.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
//????????????
    public  static BufferedImage readImageFile(String filename) {
        File file = new File(filename);
        try {
            BufferedImage image = ImageIO.read(file);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //????????????
    public static  void writeImageFile(BufferedImage bi,String filename) throws IOException {
        File outputfile = new File(filename);
        ImageIO.write(bi, "jpg", outputfile);




    }
    public static byte[] bufferedImageToByteArray(BufferedImage img)
       throws ImageFormatException, IOException
    {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
  JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
   encoder.encode(img);
    return os.toByteArray();
}

public static byte[] getfullbyte (byte[] orgbyte)
{
    int length = 0;
    for (int i = 0; i < orgbyte.length; ++i) {
        if (orgbyte[i] != '\0') ++length;
        else break;
    }
    byte[] temp = new byte[length + 1];
    for (int i = 0; i < length; ++i) {
        temp[i] = orgbyte[i];
    }
    temp[length] = '\0';
    return temp;
}


}

