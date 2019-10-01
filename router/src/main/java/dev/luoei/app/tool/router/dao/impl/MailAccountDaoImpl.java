package dev.luoei.app.tool.router.dao.impl;

import android.content.Context;

import java.util.List;

import dev.luoei.app.tool.db.DBHelper;
import dev.luoei.app.tool.router.entity.MailAccount;
import dev.luoei.app.tool.router.dao.MailAccountDao;

public class MailAccountDaoImpl implements MailAccountDao {

    private Context context;

    public MailAccountDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public void create() {
        String sql = "CREATE TABLE IF NOT EXISTS GW_MAIL_TB (username TEXT PRIMARY KEY ASC NOT NULL,password TEXT,server_url TEXT,sender TEXT,receiver TEXT,port TEXT,timeout TEXT,phone TEXT)";

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.open();
        dbHelper.execute(sql,null);
        dbHelper.close();
    }

    @Override
    public MailAccount getFromCache() {

        String sql = "SELECT * FROM GW_MAIL_TB LIMIT 1";

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.open();
        List<MailAccount> result = dbHelper.query(sql,null, MailAccount.class);
        dbHelper.close();
        if (null != result && result.size() > 0){
            return result.get(0);
        }
        return null;
    }

    @Override
    public void save(MailAccount mailAccount) {
        if (null == mailAccount || null == mailAccount.getUsername() || mailAccount.getUsername().length() == 0)return;
        delete(mailAccount.getUsername());

        String sql = "INSERT INTO GW_MAIL_TB(username,password,server_url,sender,receiver,port,timeout,phone) VALUES(?,?,?,?,?,?,?,?)";

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.open();
        dbHelper.execute(sql,new String[]{mailAccount.getUsername(),mailAccount.getPassword(),mailAccount.getStmpServerUrl(),mailAccount.getSender(),mailAccount.getReceiver(),mailAccount.getPort(),mailAccount.getTimeout(),mailAccount.getPhone()});
        dbHelper.close();
    }

    private void delete(String username) {

        String sql = "DELETE FROM GW_MAIL_TB WHERE username = ?";

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.open();
        dbHelper.execute(sql,new String[]{username});
        dbHelper.close();
    }
}
