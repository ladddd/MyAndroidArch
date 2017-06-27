package com.ladddd.mylib.imageloader;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 陈伟达 on 2017/6/27.
 */

public class CacheOnlyStreamLoader implements StreamModelLoader<String> {

    @Override
    public DataFetcher getResourceFetcher(final String model, int width, int height) {
        return new DataFetcher<InputStream>() {
            @Override
            public InputStream loadData(Priority priority) throws Exception {
                throw new IOException();
            }

            @Override
            public void cleanup() {

            }

            @Override
            public String getId() {
                return model;
            }

            @Override
            public void cancel() {

            }
        };
    }
}
