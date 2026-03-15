package com.example.myapplication.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.myapplication.data.local.entity.ProviderEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ProviderDao_Impl implements ProviderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProviderEntity> __insertionAdapterOfProviderEntity;

  private final EntityDeletionOrUpdateAdapter<ProviderEntity> __deletionAdapterOfProviderEntity;

  private final EntityDeletionOrUpdateAdapter<ProviderEntity> __updateAdapterOfProviderEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearDefault;

  private final SharedSQLiteStatement __preparedStmtOfSetDefault;

  public ProviderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProviderEntity = new EntityInsertionAdapter<ProviderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `providers` (`id`,`name`,`baseUrl`,`apiKey`,`apiFormat`,`defaultModel`,`enabled`,`createdAt`,`isDefault`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProviderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getBaseUrl());
        statement.bindString(4, entity.getApiKey());
        statement.bindString(5, entity.getApiFormat());
        statement.bindString(6, entity.getDefaultModel());
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCreatedAt());
        final int _tmp_1 = entity.isDefault() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
      }
    };
    this.__deletionAdapterOfProviderEntity = new EntityDeletionOrUpdateAdapter<ProviderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `providers` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProviderEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProviderEntity = new EntityDeletionOrUpdateAdapter<ProviderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `providers` SET `id` = ?,`name` = ?,`baseUrl` = ?,`apiKey` = ?,`apiFormat` = ?,`defaultModel` = ?,`enabled` = ?,`createdAt` = ?,`isDefault` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProviderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getBaseUrl());
        statement.bindString(4, entity.getApiKey());
        statement.bindString(5, entity.getApiFormat());
        statement.bindString(6, entity.getDefaultModel());
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCreatedAt());
        final int _tmp_1 = entity.isDefault() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfClearDefault = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE providers SET isDefault = 0";
        return _query;
      }
    };
    this.__preparedStmtOfSetDefault = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE providers SET isDefault = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ProviderEntity provider,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProviderEntity.insertAndReturnId(provider);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ProviderEntity provider,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProviderEntity.handle(provider);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ProviderEntity provider,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProviderEntity.handle(provider);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearDefault(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearDefault.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearDefault.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setDefault(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetDefault.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetDefault.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProviderEntity>> getAllProviders() {
    final String _sql = "SELECT * FROM providers ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"providers"}, new Callable<List<ProviderEntity>>() {
      @Override
      @NonNull
      public List<ProviderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfApiKey = CursorUtil.getColumnIndexOrThrow(_cursor, "apiKey");
          final int _cursorIndexOfApiFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "apiFormat");
          final int _cursorIndexOfDefaultModel = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultModel");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final List<ProviderEntity> _result = new ArrayList<ProviderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProviderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBaseUrl;
            _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            final String _tmpApiKey;
            _tmpApiKey = _cursor.getString(_cursorIndexOfApiKey);
            final String _tmpApiFormat;
            _tmpApiFormat = _cursor.getString(_cursorIndexOfApiFormat);
            final String _tmpDefaultModel;
            _tmpDefaultModel = _cursor.getString(_cursorIndexOfDefaultModel);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDefault;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp_1 != 0;
            _item = new ProviderEntity(_tmpId,_tmpName,_tmpBaseUrl,_tmpApiKey,_tmpApiFormat,_tmpDefaultModel,_tmpEnabled,_tmpCreatedAt,_tmpIsDefault);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getProviderById(final long id,
      final Continuation<? super ProviderEntity> $completion) {
    final String _sql = "SELECT * FROM providers WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ProviderEntity>() {
      @Override
      @Nullable
      public ProviderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfApiKey = CursorUtil.getColumnIndexOrThrow(_cursor, "apiKey");
          final int _cursorIndexOfApiFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "apiFormat");
          final int _cursorIndexOfDefaultModel = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultModel");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final ProviderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBaseUrl;
            _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            final String _tmpApiKey;
            _tmpApiKey = _cursor.getString(_cursorIndexOfApiKey);
            final String _tmpApiFormat;
            _tmpApiFormat = _cursor.getString(_cursorIndexOfApiFormat);
            final String _tmpDefaultModel;
            _tmpDefaultModel = _cursor.getString(_cursorIndexOfDefaultModel);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDefault;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp_1 != 0;
            _result = new ProviderEntity(_tmpId,_tmpName,_tmpBaseUrl,_tmpApiKey,_tmpApiFormat,_tmpDefaultModel,_tmpEnabled,_tmpCreatedAt,_tmpIsDefault);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDefaultProvider(final Continuation<? super ProviderEntity> $completion) {
    final String _sql = "SELECT * FROM providers WHERE isDefault = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ProviderEntity>() {
      @Override
      @Nullable
      public ProviderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfApiKey = CursorUtil.getColumnIndexOrThrow(_cursor, "apiKey");
          final int _cursorIndexOfApiFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "apiFormat");
          final int _cursorIndexOfDefaultModel = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultModel");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final ProviderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBaseUrl;
            _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            final String _tmpApiKey;
            _tmpApiKey = _cursor.getString(_cursorIndexOfApiKey);
            final String _tmpApiFormat;
            _tmpApiFormat = _cursor.getString(_cursorIndexOfApiFormat);
            final String _tmpDefaultModel;
            _tmpDefaultModel = _cursor.getString(_cursorIndexOfDefaultModel);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDefault;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp_1 != 0;
            _result = new ProviderEntity(_tmpId,_tmpName,_tmpBaseUrl,_tmpApiKey,_tmpApiFormat,_tmpDefaultModel,_tmpEnabled,_tmpCreatedAt,_tmpIsDefault);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
