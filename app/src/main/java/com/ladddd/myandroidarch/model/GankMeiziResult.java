package com.ladddd.myandroidarch.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.ladddd.myandroidarch.model.dao.GankMeiziDao;
import com.ladddd.mylib.utils.JsonUtils;

import java.util.List;

public class GankMeiziResult {

  public boolean error;

  @SerializedName("results")
  public List<GankMeiziInfo> gankMeizis;

  public GankMeiziResult(GankMeiziDao gankMeiziDao) {
      error = gankMeiziDao.error;
      gankMeizis = JsonUtils.jsonToObject(gankMeiziDao.gankMeiziListString,
              new TypeToken<List<GankMeiziInfo>>(){}.getType());
  }
}
