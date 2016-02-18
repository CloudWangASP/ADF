package com.cloud.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utility {

    /**
     * 私有构造函数.
     */
    public Utility() {

    }

    /**
     * Hides the input method.
     *
     * @param context context
     * @param view    The currently focused view
     */
    public void hideInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Show the input method.
     *
     * @param context context
     * @param view    The currently focused view, which would like to receive soft
     *                keyboard input
     */
    public void showInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 检查当前是否有可用网络
     *
     * @param context Context
     * @return true 表示有可用网络，false 表示无可用网络
     */
    public boolean isNetWorkEnabled(Context context) {

        try {
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (manager == null) {
                return false;
            }

            for (NetworkInfo info : manager.getAllNetworkInfo()) {
                if (info != null && info.isConnected()) {
                    return true;
                }
            }
        } catch (Exception ex) {

        }

        return false;
    }

    /**
     * 判断当前网络类型是否是wifi
     *
     * @param context Context
     * @return true 是wifi网络，false 非wifi网络
     */
    public boolean isWifiNetWork(Context context) {
        String networktype = "NotAvaliable";
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isAvailable()) {
                networktype = networkinfo.getTypeName().toLowerCase();
                if (networktype.equalsIgnoreCase("wifi")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 取得网络类型，wifi 2G 3G
     *
     * @param context context
     * @return WF 2G 3G 4G，或空 如果没网
     */
    public String getWifiOr2gOr3G(Context context) {
        String networkType = "";
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting()) { // 有网
                networkType = activeNetInfo.getTypeName().toLowerCase();
                if (networkType.equalsIgnoreCase("wifi")) {
                    networkType = "wifi";
                } else { // 移动网络
                    // //如果使用移动网络，则取得apn
                    // apn = activeNetInfo.getExtraInfo();
                    // 将移动网络具体类型归一化到2G 3G 4G
                    networkType = "2G"; // 默认是2G
                    int subType = activeNetInfo.getSubtype();
                    switch (subType) {
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                            networkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_CDMA: // IS95
                            break;
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 2.75
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            networkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            networkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 2.5
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSDPA: // 3.5
                            networkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSPA: // 3.5
                            networkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                            networkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                            networkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                            networkType = "3G";
                            break; // ~ 1-2 Mbps
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            networkType = "3G";
                            break; // ~ 5 Mbps
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            networkType = "3G";
                            break; // ~ 10-20 Mbps
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            break; // ~25 kbps
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            networkType = "4G";
                            break; // ~ 10+ Mbps
                        default:
                            break;
                    }
                } // end 移动网络if
            } // end 有网的if
        }
        return networkType;
    }

    /**
     * 获取设备的imei号
     *
     * @param context Context
     * @return imei号
     */
    public String getDeviceId(Context context) {
        String deviceId = null;
        if (deviceId != null && deviceId.length() > 0) {
            return deviceId;
        }

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                deviceId = tm.getDeviceId();
                // 模拟器会返回 000000000000000
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(deviceId)) {
            // 如果imei好为空，使用随机数产生一个 15 位 deviceid，
            final int imeiLength = 15; // gsm imei 号长度 15
            final int ten = 10; // 产生10以内的随机数
            Random random = new Random();
            StringBuffer sb = new StringBuffer(imeiLength);
            for (int i = 0; i < imeiLength; i++) {
                int r = random.nextInt(ten);
                sb.append(r);
            }
            deviceId = sb.toString();
        } else {
            // 如果获取到 imei，对系统imei号进行反转
            StringBuffer sb = new StringBuffer(deviceId);
            deviceId = sb.reverse().toString();
        }
        return deviceId;
    }

    /**
     * 是否安装了sdcard。
     *
     * @return true表示有，false表示没有
     */
    public static boolean haveSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = haveSDCard();
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();

    }

    /**
     * 判断路径是否存在 不存在就创建 方法描述 : 创建者：colin 项目名称： colin.base 类名： Utility.java 版本：
     * v1.0 创建时间： 2014-3-11 上午10:16:34
     *
     * @param path void
     */
    public static void isExistPath(String path) {
        File file = new File(path);
        // 判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);

        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 判断文件因为SD未挂载而不可用
     *
     * @param filePath 文件路径
     * @return true表示被unmount
     */
    public static boolean isFileUnmountBySdcard(String filePath) {
        if (filePath == null) {
            return false;
        }

        File extDir = Environment.getExternalStorageDirectory();
        return (filePath.startsWith(extDir.getPath()) && !Utility.haveSDCard());
    }

    /**
     * 去除字符串前后的空格，包括全角与半角
     *
     * @param str 要去掉的空格的内容
     * @return 去掉空格后的内容
     */
    public static String trimAllSpace(String str) {
        if (str == null) {
            return str;
        }
        return str.replaceAll("^[\\s　]*|[\\s　]*$", "");
    }

    /**
     * 得到当前网络的dns服务地址
     *
     * @param ctx Context
     * @return dns
     */
    public String getDNS(Context ctx) {
        if (isWifiNetWork(ctx)) {
            WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo info = wifi.getDhcpInfo();
            return intToInetAddress(info.dns1).getHostAddress();
        } else {
            return "";
        }
    }

    /**
     * Convert a IPv4 address from an integer to an InetAddress.
     *
     * @param hostAddress an int corresponding to the IPv4 address in network byte order
     * @return {@link InetAddress}
     */
    public static InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = {(byte) (0xff & hostAddress), // SUPPRESS
                // CHECKSTYLE
                (byte) (0xff & (hostAddress >> 8)), // SUPPRESS CHECKSTYLE
                (byte) (0xff & (hostAddress >> 16)), // SUPPRESS CHECKSTYLE
                (byte) (0xff & (hostAddress >> 24))}; // SUPPRESS CHECKSTYLE

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    /**
     * 获取手机IP信息
     *
     * @return info ip地址
     */
    public static String getIpInfo() {
        String ipInfo = null;

        try {
            Enumeration<NetworkInterface> faces = NetworkInterface.getNetworkInterfaces();

            LOOP:
            while (faces.hasMoreElements()) {
                Enumeration<InetAddress> addresses = faces.nextElement().getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        ipInfo = inetAddress.getHostAddress().toString();

                        break LOOP;
                    }
                }
            }

        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(ipInfo)) {
            ipInfo = "";
        }

        return ipInfo;
    }

    /**
     * 获取基站信息， gsm网络是cell id，cdma是base station id
     *
     * @param ctx Context
     * @return info
     */
    public String getCellInfo(Context ctx) {
        String cellInfo = null;

        try {
            TelephonyManager teleMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            CellLocation cellLocation = teleMgr.getCellLocation();

            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                cellInfo = Integer.toString(gsmCellLocation.getCid());

            } else if (cellLocation instanceof CdmaCellLocation) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                cellInfo = Integer.toString(cdmaCellLocation.getBaseStationId());
            }

        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(cellInfo)) {
            cellInfo = "";
        }

        return cellInfo;
    }

    /**
     * 获取Wifi信息，mac地址
     *
     * @param ctx Context
     * @return info
     */
    public String getWifiInfo(Context ctx) {
        String wifiInfo = null;

        try {
            WifiManager wifiMgr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            wifiInfo = wifiMgr.getConnectionInfo().getMacAddress();

        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(wifiInfo)) {
            wifiInfo = "";
        }

        return wifiInfo;
    }

    /**
     * 获取IMSI信息
     *
     * @param ctx Context
     * @return info
     */
    public String getImsiInfo(Context ctx) {
        String imsiInfo = null;

        try {
            TelephonyManager teleMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imsiInfo = teleMgr.getSubscriberId();

        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(imsiInfo)) {
            imsiInfo = "";
        }

        return imsiInfo;
    }

    /**
     * 针对当前安卓设备计算，获取android设备唯一标识（uuid） 32位的16进制数据 如果返回值为空，则获取失败，否则获取成功
     **/
    public String getAndroidDeviceUUID(Context context) {
        String androidUUID = "";

        String sIMEI = "";
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            sIMEI = TelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sPseudo_Unique_ID = "";
        try {
            sPseudo_Unique_ID = "35" + // we make this look like a valid IMEI

                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; // 13
            // //
            // digits
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sAndroidID = "";
        try {
            sAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

            // Logger.e("sAndroidID:" + sAndroidID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if (!TextUtils.isEmpty(sIMEI))
        // {
        // androidUUID = sIMEI;
        // }
        // else
        // {
        // androidUUID = sAndroidID;
        // }

        String sWALNMAC = "";
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            sWALNMAC = wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String m_szBTMAC = "";
        try {
            BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            m_szBTMAC = m_BluetoothAdapter.getAddress();
        } catch (Exception ex) {

        }

        androidUUID = sIMEI + sPseudo_Unique_ID + sAndroidID + sWALNMAC + m_szBTMAC;

        try {
            String tempLongID = sIMEI + sPseudo_Unique_ID + sAndroidID + sWALNMAC + m_szBTMAC;
            // compute md5
            MessageDigest m = null;
            try {
                m = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                e.printStackTrace();
            }
            m.update(tempLongID.getBytes(), 0, tempLongID.length());
            // get md5 bytes
            byte p_md5Data[] = m.digest();
            // create a hex string
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < p_md5Data.length; i++) {
                int b = (0xFF & p_md5Data[i]);
                // if it is a single digit, make sure it have 0 in front (proper
                // padding)
                if (b <= 0xF) {
                    md5StrBuff.append("0");
                }

                // add number to string
                md5StrBuff.append(Integer.toHexString(b));
            } // hex string to uppercase
            androidUUID = md5StrBuff.toString();
            // androidUUID = androidUUID.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return androidUUID;
    }

    /**
     * 判断输入的字符串是否是邮箱格式
     */
    public static Boolean isEmailOK(String input) {
        Boolean isOk = false;
        Pattern mailPattern = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
        Matcher matcher = mailPattern.matcher(input);
        isOk = matcher.matches();
        return isOk;
    }

    /**
     * 判断是否完善资料了,判断状态位置(ismob 0未完善过，1为已经完善过资料)
     */
    public static Boolean isMob(String mobType) {
        Boolean isMob = false;
        if (mobType.equals("1"))
            isMob = true;

        return isMob;
    }

    /**
     * 获取用户是否完善资料的标志位
     */
    public static String getUserMobType(Boolean isMob) {
        String mobType = "0";
        if (isMob) {
            mobType = "1";
        }

        return mobType;
    }

    public String getAuthorityFromPermission(Context context, String permission) {
        if (permission == null) {
            return null;
        }

        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)) {
                            return provider.authority;
                        }

                        if (permission.equals(provider.writePermission)) {
                            return provider.authority;
                        }
                    }
                }
            }
        }
        return null;
    }


    public static File getFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 注册时 汉字、字母或者数字 检测用户名是否合法
     *
     * @param username
     * @return
     */
    public static boolean checkUserName(String username) {

        Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z][\\u4e00-\\u9fa5a-zA-Z0-9\\-]+$");
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }

    /**
     * 检测只包含英文字母数字
     *
     * @param username
     * @return
     */
    public static boolean checkString(String username) {

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }

    public static boolean checkNumber(String username) {

        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    /**
     * 是否包含汉字 方法描述 : 创建者：colin 项目名称： colin.base 类名： Utility.java 版本： v1.0 创建时间：
     * 2014-2-22 下午5:23:24
     *
     * @param username
     * @return boolean
     */
    public static boolean checkChinese(String username) {
        String pstr = "[^x00-xff]";
        Pattern p = Pattern.compile(pstr);
        Matcher m = p.matcher(username);
        return m.find();
    }

    /**
     * 检测邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {

        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 检测手机号是否合法 如果手机号合法，则返回true
     **/
    public static Boolean checkCellPhone(String cellPhone) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(cellPhone);
        return matcher.matches();
    }

    /**
     * 将内容写到SD卡
     *
     * @param fileName 文件名
     * @param content  需要保存的内容
     */
    public static void WriteStringToSD(String fileName, String content) {
        Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(Color.WHITE);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx - 6, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, src, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * dp转 px.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int dp2px(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    /**
     * px转dp.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int px2dp(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) ((value * 160) / scale + 0.5f);
    }

    /**
     * sp转px.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public int sp2px(float value, Context context) {
        Resources r;
        if (context == null) {
            r = Resources.getSystem();
        } else {
            r = context.getResources();
        }
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        return (int) (spvalue + 0.5f);
    }

    /**
     * px转sp.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public int px2sp(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        // Logger.e("-------width:" + width + "   height:" + height);
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // 获取这个图片的宽和高，注意此处的bitmap为null
            bitmap = BitmapFactory.decodeFile(imagePath, options);

            options.inJustDecodeBounds = false; // 设为 false
            // 计算缩放比
            int h = options.outHeight;
            int w = options.outWidth;
            int beWidth = w / width;
            int beHeight = h / height;
            int be = 1;
            if (beWidth < beHeight) {
                be = beWidth;
            } else {
                be = beHeight;
            }
            if (be <= 0) {
                be = 1;
            }
            options.inSampleSize = be;
            // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false

            bitmap = BitmapFactory.decodeFile(imagePath, options);
            // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象

            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

            int degree = readPictureDegree(imagePath);
            // Logger.e(degree + "------------");
            bitmap = rotaingImageView(degree, bitmap);
        } catch (Exception ex) {
            bitmap = null;
        }

        return bitmap;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {

        // Matrix matrix = new Matrix();
        // matrix.reset();
        // matrix.setRotate(angle);
        // Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
        // bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片地址格式化成位置字符 TODO
     *
     * @param url
     * @return
     * @throws
     * @author colin
     */
    public static String getUrlFormatValue(String url) {
        String str = null;
        try {
            int hashKey = url.hashCode();
            str = String.format("%1$-16d", hashKey);
            str = str + url.substring(url.lastIndexOf("."), url.length());
        } catch (Exception ex) {
            return null;
        }
        return str;
    }

    /**
     * 根据图片地址获得图片名称 TODO
     *
     * @param paramStr
     * @return
     * @throws
     * @author colin
     */
    public static String getPicNameFromUrl(String paramStr) {
        String picName = null;
        picName = paramStr.substring(paramStr.lastIndexOf("/") + 1, paramStr.length());
        return picName;
    }

    /**
     * 清除目录
     *
     * @param file
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

}
