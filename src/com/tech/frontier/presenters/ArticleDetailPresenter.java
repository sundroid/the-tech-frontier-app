/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.tech.frontier.presenters;

import android.text.TextUtils;
import android.util.Log;

import com.tech.frontier.db.ArticleDetailDBAPI;
import com.tech.frontier.db.DatabaseFactory;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.net.ArticleAPI;
import com.tech.frontier.net.ArticleAPIImpl;
import com.tech.frontier.ui.interfaces.ArticleDetailView;

public class ArticleDetailPresenter {
    ArticleDetailView mArticleView;
    // 从网络上获取文章的Api
    ArticleAPI mArticleApi = new ArticleAPIImpl();

    ArticleDetailDBAPI mArticleDBAPI = DatabaseFactory.createArticleDetailDBAPI();

    public ArticleDetailPresenter(ArticleDetailView view) {
        mArticleView = view;
    }

    public void fetchArticleContent(final String post_id) {
        mArticleDBAPI.loadArticleContent(post_id, new DataListener<String>() {

            @Override
            public void onComplete(String result) {
                // 数据库中没有则通过网络获取
                if (TextUtils.isEmpty(result)) {
                    Log.e("", "### 没有文章缓存");
                    mArticleApi.fetchArticleContent(post_id, new DataListener<String>() {

                        @Override
                        public void onComplete(String result) {
                            mArticleView.showArticleContent(result);
                            // 存储文章到数据库中
                            mArticleDBAPI.saveContent(post_id, result);
                        }
                    });
                } else {
                    Log.e("", "### 含有文章缓存");
                    mArticleView.showArticleContent(result);
                }
            }
        });

    }
}
