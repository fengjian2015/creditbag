package com.fly.credit.jiana.util;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.text.TextUtils;

import com.fly.credit.jiana.MyApplication;
import com.fly.credit.jiana.bean.ContactInfo;
import com.fly.credit.jiana.bean.GroupEntity;
import com.fly.credit.jiana.bean.SmsInfo;

import java.util.ArrayList;
import java.util.List;

public class AuthDataUtil {

    public static ArrayList<SmsInfo> getSmsInfo() {
        long smsTime = DateTool.INSTANCE.getServerTimestamp() - 365L * 24 * 60 * 60 * 1000;
        ArrayList<SmsInfo> arrayList = new ArrayList<>();
        try {
            LogUtil.d("---" + DateTool.INSTANCE.getTimeFromLongYMDHMS(DateTool.INSTANCE.getServerTimestamp()));
            Cursor cur = MyApplication.getApplication().getContentResolver().query(Uri.parse("content://sms"), null, Telephony.Sms.DATE + " > " + smsTime, null, "date desc");
            while (cur.moveToNext()) {
                SmsInfo smsBean = new SmsInfo();
                smsBean.setAddress(cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)));
                smsBean.setType(cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE)));
                smsBean.setContent(cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY)));
                smsBean.setTime(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE)));
                smsBean.setPhone(smsBean.getAddress());
                smsBean.set_read(String.valueOf(cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.READ))));
                smsBean.setSms_id(String.valueOf(cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.THREAD_ID))));
                smsBean.setSms_status(String.valueOf(cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.STATUS))));
                smsBean.setUpdate_time(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE)));
                arrayList.add(smsBean);
            }
            cur.close();
            getContactName(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    private static void getContactName(ArrayList<SmsInfo> beans) {
        try {
            ArrayList<ContactInfo> allContacts = getContactInfoModelsName();
            for (SmsInfo bean : beans) {
                for (ContactInfo contactBean : allContacts) {
                    if (bean.getAddress().equals(contactBean.getPhone())) {
                        bean.setContactor_name(contactBean.getName());
                        continue;
                    }
                }
                if (TextUtils.isEmpty(bean.getContactor_name())){
                    bean.setContactor_name(bean.getAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ContactInfo> getContactInfoModelsName() {
        ArrayList<ContactInfo> contactInfoModels = new ArrayList<>();
        try {
            contactInfoModels.clear();
            Cursor cursor = MyApplication.getApplication().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                ContactInfo contactInfoModel = new ContactInfo();
                contactInfoModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                contactInfoModel.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contactInfoModels.add(contactInfoModel);
            }
            cursor.close();
            return contactInfoModels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactInfoModels;
    }


    public static ArrayList<ContactInfo> getContactInfoModels() {
        ArrayList<ContactInfo> contactInfoModels = new ArrayList<>();
        try {
            contactInfoModels.clear();
            Cursor cursor = MyApplication.getApplication().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                ContactInfo contactInfoModel = new ContactInfo();
                contactInfoModel.setContact_id(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                contactInfoModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                contactInfoModel.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contactInfoModel.setLast_update_times(CommonUtil.INSTANCE.stringToLong(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP))));
                contactInfoModel.setLast_contact_time(CommonUtil.INSTANCE.stringToLong(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED))));
                contactInfoModel.setSource(getContactAccount(contactInfoModel.getContact_id()));
                contactInfoModels.add(contactInfoModel);
            }
            cursor.close();
            getAllGroupInfo(contactInfoModels);
//            getContentCallLog(contactInfoModels);
            return contactInfoModels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactInfoModels;
    }

    private static String getContactAccount(String id) {
        Cursor cursor = null;
        String accountName = null;
        try {
            cursor = MyApplication.getApplication().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,
                    new String[]{ContactsContract.RawContacts.ACCOUNT_NAME, ContactsContract.RawContacts.ACCOUNT_TYPE},
                    ContactsContract.RawContacts.CONTACT_ID +"=?",
                    new String[]{String.valueOf(id)},
                    null);
            if (cursor != null && cursor.getCount() >0) {
                cursor.moveToFirst();
                accountName= cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.RawContacts.ACCOUNT_NAME));
                cursor.close();
            }
        } catch (Exception e) {
           e.printStackTrace();
        } finally{
            cursor.close();
        }
        return accountName;
    }

    //获取通话记录
    private static void getContentCallLog(ArrayList<ContactInfo> contactInfoModels) {
        try {
            Cursor cursor = MyApplication.getApplication().getContentResolver().query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                    null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
            );
            if(cursor == null) return;
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));  //姓名
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));  //号码
                long dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE)); //获取通话日期
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
                for (ContactInfo contactInfoModel : contactInfoModels) {
                    if (contactInfoModel.getPhone().equals(number)) {
                        contactInfoModel.setContact_times(contactInfoModel.getContact_times() + 1);
                        contactInfoModel.setLast_used_times(duration);
                        if (contactInfoModel.getLast_contact_time() == 0)
                        contactInfoModel.setLast_contact_time(dateLong);
                        continue;
                    }
                }
                LogUtil.d("Call log: " + "\n"
                        + "name: " + name + "\n"
                        + "phone number: " + number + "\n"
                        + "phone dateLong: " + dateLong + "\n"
                );
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取所有的 联系人分组信息
     *
     * @return
     */
    public static List<GroupEntity> getAllGroupInfo() {
        List<GroupEntity> groupList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = MyApplication.getApplication().getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
                    null, null, null, null);
            if(cursor == null) return groupList;
            while (cursor.moveToNext()) {
                GroupEntity ge = new GroupEntity();
                int groupId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Groups._ID)); // 组id
                String groupName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Groups.TITLE)); // 组名
                ge.setGroupId(groupId);
                ge.setGroupName(groupName);
                LogUtil.d("group id:" + groupId + ">>groupName:" + groupName);
                groupList.add(ge);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return groupList;
    }

    /**
     * 获取所有的 联系人分组信息
     *
     * @return
     * @param contactInfoModels
     */
    public static List<GroupEntity> getAllGroupInfo(ArrayList<ContactInfo> contactInfoModels) {
        List<GroupEntity> groupList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = MyApplication.getApplication().getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
                    null, null, null, null);
            if(cursor == null) return groupList;
            while (cursor.moveToNext()) {
                GroupEntity ge = new GroupEntity();
                int groupId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Groups._ID)); // 组id
                String groupName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Groups.TITLE)); // 组名
                ge.setGroupId(groupId);
                ge.setGroupName(groupName);
                getAllGroupMembers(groupId, groupName,contactInfoModels);
                LogUtil.d("group id:" + groupId + ">>groupName:" + groupName);
                groupList.add(ge);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return groupList;
    }

    private static void getAllGroupMembers(int raw_group_id, String groupName, ArrayList<ContactInfo> contactInfoModels) {
        try {
            Cursor groupContactCursor = MyApplication.getApplication().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                    ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = " + raw_group_id,
                    null,
                    null);
            if(groupContactCursor == null) return ;
            while (groupContactCursor.moveToNext()) {
                Cursor contactCursor = MyApplication.getApplication().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        new String[]{ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME},
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.Data.RAW_CONTACT_ID + "=" + groupContactCursor.getInt(0),
                        null,
                        null);
                contactCursor.moveToNext();
                String contactId = contactCursor.getString(0);
                for (ContactInfo contactInfoModel : contactInfoModels) {
                    if (contactId != null && contactId.equals(contactInfoModel.getContact_id())) {
                        contactInfoModel.setGroups(groupName);
                        continue;
                    }
                }
                LogUtil.d("Member name is: " + contactCursor.getString(0) + "  " + contactCursor.getString(1) + " " + contactCursor.getString(2));
                contactCursor.close();
            }
            groupContactCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }


}
