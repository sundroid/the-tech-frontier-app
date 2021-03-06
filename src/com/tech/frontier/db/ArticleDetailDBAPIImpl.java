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

package com.tech.frontier.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tech.frontier.db.helper.DatabaseHelper;
import com.tech.frontier.listeners.DataListener;

public class ArticleDetailDBAPIImpl implements ArticleDetailDBAPI {

    @Override
    public void saveContent(String postId, String html) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("post_id", postId);
        contentValues.put("content", html);
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        database.insertWithOnConflict(DatabaseHelper.TABLE_ARTICLE_CONTENT, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void loadArticleContent(String postId, DataListener<String> listener) {
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE_CONTENT, new String[] {
                "content"
        }, "post_id=?", new String[] {
                postId
        }, null, null, null);
        if (listener != null) {
            listener.onComplete(queryArticleCotent(cursor));
        }
        cursor.close();
        DatabaseMgr.releaseDatabase();
    }

    private String queryArticleCotent(Cursor cursor) {
        return cursor.moveToNext() ? cursor.getString(0) : "";
    }

    public void deleteAll() {
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        database.execSQL("delete from " + DatabaseHelper.TABLE_ARTICLE_CONTENT);
        DatabaseMgr.releaseDatabase();
    }

}
