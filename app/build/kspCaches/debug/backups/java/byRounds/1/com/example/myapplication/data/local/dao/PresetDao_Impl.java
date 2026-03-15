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
import com.example.myapplication.data.local.entity.PresetEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
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
public final class PresetDao_Impl implements PresetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PresetEntity> __insertionAdapterOfPresetEntity;

  private final EntityDeletionOrUpdateAdapter<PresetEntity> __deletionAdapterOfPresetEntity;

  private final EntityDeletionOrUpdateAdapter<PresetEntity> __updateAdapterOfPresetEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearDefault;

  private final SharedSQLiteStatement __preparedStmtOfSetDefault;

  public PresetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPresetEntity = new EntityInsertionAdapter<PresetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `presets` (`id`,`name`,`systemPrompt`,`description`,`createdAt`,`updatedAt`,`isDefault`,`temperature`,`topP`,`maxTokens`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PresetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSystemPrompt());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getUpdatedAt());
        final int _tmp = entity.isDefault() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getTemperature() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getTemperature());
        }
        if (entity.getTopP() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getTopP());
        }
        if (entity.getMaxTokens() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getMaxTokens());
        }
      }
    };
    this.__deletionAdapterOfPresetEntity = new EntityDeletionOrUpdateAdapter<PresetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `presets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PresetEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPresetEntity = new EntityDeletionOrUpdateAdapter<PresetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `presets` SET `id` = ?,`name` = ?,`systemPrompt` = ?,`description` = ?,`createdAt` = ?,`updatedAt` = ?,`isDefault` = ?,`temperature` = ?,`topP` = ?,`maxTokens` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PresetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSystemPrompt());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getUpdatedAt());
        final int _tmp = entity.isDefault() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getTemperature() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getTemperature());
        }
        if (entity.getTopP() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getTopP());
        }
        if (entity.getMaxTokens() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getMaxTokens());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfClearDefault = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE presets SET isDefault = 0";
        return _query;
      }
    };
    this.__preparedStmtOfSetDefault = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE presets SET isDefault = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PresetEntity preset, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPresetEntity.insertAndReturnId(preset);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PresetEntity preset, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPresetEntity.handle(preset);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PresetEntity preset, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPresetEntity.handle(preset);
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
  public Flow<List<PresetEntity>> getAllPresets() {
    final String _sql = "SELECT * FROM presets ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"presets"}, new Callable<List<PresetEntity>>() {
      @Override
      @NonNull
      public List<PresetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSystemPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "systemPrompt");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfTopP = CursorUtil.getColumnIndexOrThrow(_cursor, "topP");
          final int _cursorIndexOfMaxTokens = CursorUtil.getColumnIndexOrThrow(_cursor, "maxTokens");
          final List<PresetEntity> _result = new ArrayList<PresetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PresetEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSystemPrompt;
            _tmpSystemPrompt = _cursor.getString(_cursorIndexOfSystemPrompt);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final Float _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getFloat(_cursorIndexOfTemperature);
            }
            final Float _tmpTopP;
            if (_cursor.isNull(_cursorIndexOfTopP)) {
              _tmpTopP = null;
            } else {
              _tmpTopP = _cursor.getFloat(_cursorIndexOfTopP);
            }
            final Integer _tmpMaxTokens;
            if (_cursor.isNull(_cursorIndexOfMaxTokens)) {
              _tmpMaxTokens = null;
            } else {
              _tmpMaxTokens = _cursor.getInt(_cursorIndexOfMaxTokens);
            }
            _item = new PresetEntity(_tmpId,_tmpName,_tmpSystemPrompt,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDefault,_tmpTemperature,_tmpTopP,_tmpMaxTokens);
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
  public Object getPresetById(final long id, final Continuation<? super PresetEntity> $completion) {
    final String _sql = "SELECT * FROM presets WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PresetEntity>() {
      @Override
      @Nullable
      public PresetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSystemPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "systemPrompt");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfTopP = CursorUtil.getColumnIndexOrThrow(_cursor, "topP");
          final int _cursorIndexOfMaxTokens = CursorUtil.getColumnIndexOrThrow(_cursor, "maxTokens");
          final PresetEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSystemPrompt;
            _tmpSystemPrompt = _cursor.getString(_cursorIndexOfSystemPrompt);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final Float _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getFloat(_cursorIndexOfTemperature);
            }
            final Float _tmpTopP;
            if (_cursor.isNull(_cursorIndexOfTopP)) {
              _tmpTopP = null;
            } else {
              _tmpTopP = _cursor.getFloat(_cursorIndexOfTopP);
            }
            final Integer _tmpMaxTokens;
            if (_cursor.isNull(_cursorIndexOfMaxTokens)) {
              _tmpMaxTokens = null;
            } else {
              _tmpMaxTokens = _cursor.getInt(_cursorIndexOfMaxTokens);
            }
            _result = new PresetEntity(_tmpId,_tmpName,_tmpSystemPrompt,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDefault,_tmpTemperature,_tmpTopP,_tmpMaxTokens);
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
  public Object getDefaultPreset(final Continuation<? super PresetEntity> $completion) {
    final String _sql = "SELECT * FROM presets WHERE isDefault = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PresetEntity>() {
      @Override
      @Nullable
      public PresetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSystemPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "systemPrompt");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfTopP = CursorUtil.getColumnIndexOrThrow(_cursor, "topP");
          final int _cursorIndexOfMaxTokens = CursorUtil.getColumnIndexOrThrow(_cursor, "maxTokens");
          final PresetEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSystemPrompt;
            _tmpSystemPrompt = _cursor.getString(_cursorIndexOfSystemPrompt);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final Float _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getFloat(_cursorIndexOfTemperature);
            }
            final Float _tmpTopP;
            if (_cursor.isNull(_cursorIndexOfTopP)) {
              _tmpTopP = null;
            } else {
              _tmpTopP = _cursor.getFloat(_cursorIndexOfTopP);
            }
            final Integer _tmpMaxTokens;
            if (_cursor.isNull(_cursorIndexOfMaxTokens)) {
              _tmpMaxTokens = null;
            } else {
              _tmpMaxTokens = _cursor.getInt(_cursorIndexOfMaxTokens);
            }
            _result = new PresetEntity(_tmpId,_tmpName,_tmpSystemPrompt,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDefault,_tmpTemperature,_tmpTopP,_tmpMaxTokens);
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
