package com.example.jay.myappbasic.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smarttechnology.smart.smartg4.model.LightModel;

import java.util.ArrayList;

public class LightInZoneDB {


    private DataBaseHelper myDatabase;
    private SQLiteDatabase sqLiteDatabase;

    public LightInZoneDB(Context context) {
        // TODO Auto-generated constructor stub
        myDatabase = new DataBaseHelper(context);
    }

    public ArrayList<LightModel> getAllLightInZone() {
        ArrayList<LightModel> Arr = null;
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("LightInZone", null, null,
                null, null, null, null);
        if (c.getCount() > 0) {
            Arr = new ArrayList<LightModel>();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                LightModel data = new LightModel();
                data.setZoneId(c.getInt(0));
                data.setLightId(c.getInt(1));
                data.setLightRemark(c.getString(2));
                data.setSubnetId(c.getInt(3));
                data.setDeviceId(c.getInt(4));
                data.setChannelNo(c.getInt(5));
                data.setCanDim(c.getInt(6));
                data.setLightTypeID(c.getInt(7));
                data.setSequenceNo(c.getInt(8));
                data.setIsAllowCentralControl(c.getInt(9));
                data.setStatus(c.getString(10));
                data.setImgOffState(c.getString(11));
                data.setImgOnState(c.getString(12));
                Arr.add(data);
                c.moveToNext();
            }
            c.close();
            myDatabase.close();
            return Arr;

        }

        return Arr;

    }

    public ArrayList<LightModel> getLightInZoneWithZoneID(int ZoneID) {
        ArrayList<LightModel> Arr = null;
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("LightInZone", null, "ZoneID=" + ZoneID,
                null, null, null, null);
        if (c.getCount() > 0) {
            Arr = new ArrayList<LightModel>();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                LightModel data = new LightModel();
                data.setZoneId(c.getInt(0));
                data.setLightId(c.getInt(1));
                data.setLightRemark(c.getString(2));
                data.setSubnetId(c.getInt(3));
                data.setDeviceId(c.getInt(4));
                data.setChannelNo(c.getInt(5));
                data.setCanDim(c.getInt(6));
                data.setLightTypeID(c.getInt(7));
                data.setSequenceNo(c.getInt(8));
                data.setIsAllowCentralControl(c.getInt(9));
                data.setStatus(c.getString(10));
                data.setImgOffState(c.getString(11));
                data.setImgOnState(c.getString(12));
                Arr.add(data);
                c.moveToNext();
            }
            c.close();
            myDatabase.close();
            return Arr;

        }

        return Arr;

    }


    public int getLastSizeId(int ZoneID) {
        int lastId = 0;
        String slastId;
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("LightInZone", null, "ZoneID=" + ZoneID,
                null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                LightModel data = new LightModel();
                data.setLightId(c.getInt(1));
                lastId = data.getLightId();
                c.moveToNext();
            }
            c.close();
            myDatabase.close();
            return lastId;

        }

        return lastId;

    }


    public ArrayList<LightModel> getLightStatus(int ZoneID) {
        ArrayList<LightModel> Arr = null;
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("LightInZone", null, "ZoneID=" + ZoneID + " and  Status >=" + 0,
                null, null, null, null);
        if (c.getCount() > 0) {
            Arr = new ArrayList<LightModel>();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                LightModel data = new LightModel();
                data.setZoneId(c.getInt(0));
                data.setLightId(c.getInt(1));
                data.setLightRemark(c.getString(2));
                data.setSubnetId(c.getInt(3));
                data.setDeviceId(c.getInt(4));
                data.setChannelNo(c.getInt(5));
                data.setCanDim(c.getInt(6));
                data.setLightTypeID(c.getInt(7));
                data.setSequenceNo(c.getInt(8));
                data.setIsAllowCentralControl(c.getInt(9));
                data.setStatus(c.getString(10));
                data.setImgOffState(c.getString(11));
                data.setImgOnState(c.getString(12));
                Arr.add(data);
                c.moveToNext();
            }
            c.close();
            myDatabase.close();
            return Arr;

        }

        return Arr;

    }


    public ArrayList<LightModel> getcentralLightInZoneWithZoneID(int ZoneID) {
        ArrayList<LightModel> Arr = null;
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("LightInZone", null, "ZoneID=" + ZoneID + " and  IsAllowCentralControl =" + 1,
                null, null, null, null);
        if (c.getCount() > 0) {
            Arr = new ArrayList<LightModel>();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                LightModel data = new LightModel();
                data.setZoneId(c.getInt(0));
                data.setLightId(c.getInt(1));
                data.setLightRemark(c.getString(2));
                data.setSubnetId(c.getInt(3));
                data.setDeviceId(c.getInt(4));
                data.setChannelNo(c.getInt(5));
                data.setCanDim(c.getInt(6));
                data.setLightTypeID(c.getInt(7));
                data.setSequenceNo(c.getInt(8));
                data.setIsAllowCentralControl(c.getInt(9));
                data.setStatus(c.getString(10));
                data.setImgOffState(c.getString(11));
                data.setImgOnState(c.getString(12));
                Arr.add(data);
                c.moveToNext();
            }
            c.close();
            myDatabase.close();
            return Arr;

        }

        return Arr;

    }


    public void Add_Light(LightModel model) {
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DButils.Zone_Id, model.getZoneId());
        value.put(DButils.Light_Id, model.getLightId());
        value.put(DButils.Light_Remark, model.getLightRemark());
        value.put(DButils.Subnet_Id, model.getSubnetId());
        value.put(DButils.Device_Id, model.getDeviceId());
        value.put(DButils.Channel_No, model.getChannelNo());
        value.put(DButils.Can_Dim, model.getCanDim());
        value.put(DButils.Light_TypeId, model.getLightTypeID());
        value.put(DButils.Sequence_No, model.getSequenceNo());
        value.put(DButils.IsAllowCentralcontrol, model.getIsAllowCentralControl());
        value.put(DButils.OFF_STATE, model.getImgOffState());
        value.put(DButils.ON_STATE, model.getImgOnState());
        sqLiteDatabase.insert(DButils.LightInZone, null, value);
        sqLiteDatabase.close();
    }

    public void Update_Light(LightModel model, int zonid, int lightid) {
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DButils.Light_Remark, model.getLightRemark());
        value.put(DButils.Light_TypeId, model.getLightTypeID());
        value.put(DButils.Device_Id, model.getDeviceId());
        value.put(DButils.Subnet_Id, model.getSubnetId());
        value.put(DButils.Channel_No, model.getChannelNo());
        value.put(DButils.Can_Dim, model.getCanDim());
        value.put(DButils.IsAllowCentralcontrol, model.getIsAllowCentralControl());
        value.put(DButils.OFF_STATE, model.getImgOffState());
        value.put(DButils.ON_STATE, model.getImgOnState());
        sqLiteDatabase.update(DButils.LightInZone, value, "ZoneID = ? and LightID = ?", new String[]{String.valueOf(zonid), String.valueOf(lightid)});
        sqLiteDatabase.close();
    }

    public void deleteLightInZoneWithZoneID(int zoneId, int lightId) {
        sqLiteDatabase = myDatabase.getWritableDatabase();
        sqLiteDatabase.delete(DButils.LightInZone, "ZoneID = ? and LightID = ?", new String[]{String.valueOf(zoneId), String.valueOf(lightId)});
        sqLiteDatabase.close();
    }

    public void deletLightwithZoneID(int zoneId) {
        sqLiteDatabase = myDatabase.getWritableDatabase();
        sqLiteDatabase.delete(DButils.LightInZone, "ZoneID = ? ", new String[]{String.valueOf(zoneId)});
        sqLiteDatabase.close();
    }


    public ArrayList<LightModel> getLightInZoneWithLightID(int LightID) {
        ArrayList<LightModel> Arr = null;
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("LightInZone", null, "LightID=" + LightID,
                null, null, null, "SequenceNo");
        if (c.getCount() > 0) {
            Arr = new ArrayList<LightModel>();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                LightModel data = new LightModel();
                data.setZoneId(c.getInt(0));
                data.setLightId(c.getInt(1));
                data.setLightRemark(c.getString(2));
                data.setSubnetId(c.getInt(3));
                data.setDeviceId(c.getInt(4));
                data.setChannelNo(c.getInt(5));
                data.setCanDim(c.getInt(6));
                data.setLightTypeID(c.getInt(7));
                data.setSequenceNo(c.getInt(8));
                data.setIsAllowCentralControl(c.getInt(9));
                data.setStatus(c.getString(10));
                data.setImgOffState(c.getString(11));
                data.setImgOnState(c.getString(12));
                Arr.add(data);
                c.moveToNext();
            }
            c.close();
            myDatabase.close();
            return Arr;

        }

        return Arr;

    }


    public void updateStatus(int zoneId, int subnetId, int deviceId, int channelNo, String s) {
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DButils.Status, s);
        sqLiteDatabase.update(DButils.LightInZone, value, "ZoneID = ? and SubnetID = ? and DeviceID = ? and ChannelNo = ?", new String[]{String.valueOf(zoneId), String.valueOf(subnetId), String.valueOf(deviceId), String.valueOf(channelNo)});
        sqLiteDatabase.close();
    }


}
