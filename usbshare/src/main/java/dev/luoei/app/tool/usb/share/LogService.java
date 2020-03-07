package dev.luoei.app.tool.usb.share;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;

import java.io.File;

public class LogService {

    private static final int MAX_BYTES = 500 * 1024; // 500K averages to a 4000 lines per file

    public static void initLogger(){

        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separatorChar + "logger/usbshare";

        HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
        ht.start();
        Handler handler = new DiskLogStrategy.WriteHandler(ht.getLooper(), folder, MAX_BYTES);
        DiskLogStrategy logStrategy = new DiskLogStrategy(handler);

        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .tag("USB")
                .logStrategy(logStrategy)
                .build();

        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

}
