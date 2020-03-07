package dev.luoei.app.tool.usb.share;

import java.io.DataOutputStream;

public class UsbShare {

    /// 获取ROOT权限
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes( "service call connectivity 33 i32 1\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     * USB 共享开关
     * adb shell su -c service call connectivity 30 i32 1 to turn USB Tethering ON
     *
     * adb shell su -c service call connectivity 30 i32 0 to turn USB Tethering OFF
     *
     * So, for 8.0 / 8.1 Oreo:
     *
     * service call connectivity 34 i32 1 s16 text - turn USB tethering ON
     *
     * service call connectivity 34 i32 0 s16 text - turn USB tethering OFF
     *
     * It works for me Android Pie with
     *
     * service call connectivity 33 i32 1 s16 text - turn USB tethering ON
     *
     * service call connectivity 33 i32 0 s16 text - turn USB tethering OFF
     *
     * service call connectivity 32 i32 1 on Ice Cream Sandwich (4.0)
     * service call connectivity 33 i32 1 on Jelly Bean (4.1 to 4.3)
     * service call connectivity 34 i32 1 on KitKat (4.4)
     * service call connectivity 30 i32 1 on Lollipop (5.0)
     * service call connectivity 31 i32 1 on Lollipop (5.1) according to an answer by Firelord
     * service call connectivity 30 i32 1 on Marshmallow (6.0), untested
     * service call connectivity 41 i32 1 on Samsung Marshmallow (6.0)
     * service call connectivity 33 i32 1 on Nougat (7.0)
     * service call connectivity 39 i32 1 on Samsung Nougat (7.0)
     *
     * @param open
     * @return
     */
    public static boolean setOpen(Boolean open) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes( "service call connectivity 33 i32 "+ (open ? "1" : "0") + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

}
