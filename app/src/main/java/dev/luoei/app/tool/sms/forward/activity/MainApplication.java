//package dev.luoei.app.tool.sms.forward.activity;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.marswin89.marsdaemon.DaemonApplication;
//import com.marswin89.marsdaemon.DaemonClient;
//import com.marswin89.marsdaemon.DaemonConfigurations;
//
//import dev.luoei.app.tool.sms.service.SmsObserverReceiver;
//import dev.luoei.app.tool.sms.service.SmsObserverReceiver2;
//import dev.luoei.app.tool.sms.service.SmsObserverService;
//import dev.luoei.app.tool.sms.service.SmsObserverService2;
//
//public class MainApplication extends DaemonApplication {
//
////    private DaemonClient mDaemonClient;
//
//
//    @Override
//    public void attachBaseContextByDaemon(Context base) {
//        super.attachBaseContextByDaemon(base);
//    }
//
////    @Override
////    protected void attachBaseContext(Context base) {
////        super.attachBaseContext(base);
////
////        mDaemonClient = new DaemonClient(createDaemonConfigurations());
////        mDaemonClient.onAttachBaseContext(base);
////    }
//
//
//    @Override
//    protected DaemonConfigurations getDaemonConfigurations() {
//        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
//                "dev.luoei.app.tool.sms.forward:process1",
//                SmsObserverService.class.getCanonicalName(),
//                SmsObserverReceiver.class.getCanonicalName());
//        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
//                "dev.luoei.app.tool.sms.forward:process2",
//                SmsObserverService2.class.getCanonicalName(),
//                SmsObserverReceiver2.class.getCanonicalName());
//        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
//        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
//        return new DaemonConfigurations(configuration1, configuration2, listener);
//    }
//
////    private DaemonConfigurations createDaemonConfigurations(){
////        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
////                "dev.luoei.app.tool.sms.forward:process1",
////                SmsObserverService.class.getCanonicalName(),
////                SmsObserverReceiver.class.getCanonicalName());
////        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
////                "dev.luoei.app.tool.sms.forward:process2",
////                SmsObserverService2.class.getCanonicalName(),
////                SmsObserverReceiver2.class.getCanonicalName());
////        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
////        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
////        return new DaemonConfigurations(configuration1, configuration2, listener);
////    }
//
//    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
//        @Override
//        public void onPersistentStart(Context context) {
//        }
//
//        @Override
//        public void onDaemonAssistantStart(Context context) {
//        }
//
//        @Override
//        public void onWatchDaemonDaed() {
//        }
//    }
//
//}
