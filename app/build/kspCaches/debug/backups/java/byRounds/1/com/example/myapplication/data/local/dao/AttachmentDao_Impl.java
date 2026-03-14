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
import com.example.myapplication.data.local.entity.AttachmentEntity;
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
public final class AttachmentDao_Impl implements AttachmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AttachmentEntity> __insertionAdapterOfAttachmentEntity;

  private final EntityDeletionOrUpdateAdapter<AttachmentEntity> __deletionAdapterOfAttachmentEntity;

  private final EntityDeletionOrUpdateAdapter<AttachmentEntity> __updateAdapterOfAttachmentEntity;

  private final SharedSQLiteStatement __preparedStmtOfBindToMessage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBySessionId;

  public AttachmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAttachmentEntity = new EntityInsertionAdapter<AttachmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `attachments` (`id`,`sessionId`,`messageId`,`fileName`,`mimeType`,`localUri`,`extractedText`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttachmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSessionId());
        if (entity.getMessageId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getMessageId());
        }
        statement.bindString(4, entity.getFileName());
        statement.bindString(5, entity.getMimeType());
        statement.bindString(6, entity.getLocalUri());
        statement.bindString(7, entity.getExtractedText());
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfAttachmentEntity = new EntityDeletionOrUpdateAdapter<AttachmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `attachments` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttachmentEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAttachmentEntity = new EntityDeletionOrUpdateAdapter<AttachmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `attachments` SET `id` = ?,`sessionId` = ?,`messageId` = ?,`fileName` = ?,`mimeType` = ?,`localUri` = ?,`extractedText` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttachmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSessionId());
        if (entity.getMessageId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getMessageId());
        }
        statement.bindString(4, entity.getFileName());
        statement.bindString(5, entity.getMimeType());
        statement.bindString(6, entity.getLocalUri());
        statement.bindString(7, entity.getExtractedText());
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfBindToMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE attachments SET messageId = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteBySessionId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attachments WHERE sessionId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AttachmentEntity attachment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAttachmentEntity.insertAndReturnId(attachment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final AttachmentEntity attachment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAttachmentEntity.handle(attachment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final AttachmentEntity attachment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAttachmentEntity.handle(attachment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object bindToMessage(final long attachmentId, final long messageId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfBindToMessage.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, messageId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, attachmentId);
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
          __preparedStmtOfBindToMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBySessionId(final long sessionId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBySessionId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sessionId);
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
          __preparedStmtOfDeleteBySessionId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super AttachmentEntity> $completion) {
    final String _sql = "SELECT * FROM attachments WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AttachmentEntity>() {
      @Override
      @Nullable
      public AttachmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final AttachmentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final Long _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            }
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalUri;
            _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            final String _tmpExtractedText;
            _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new AttachmentEntity(_tmpId,_tmpSessionId,_tmpMessageId,_tmpFileName,_tmpMimeType,_tmpLocalUri,_tmpExtractedText,_tmpCreatedAt);
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
  public Object getBySessionId(final long sessionId,
      final Continuation<? super List<AttachmentEntity>> $completion) {
    final String _sql = "SELECT * FROM attachments WHERE sessionId = ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttachmentEntity>>() {
      @Override
      @NonNull
      public List<AttachmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AttachmentEntity> _result = new ArrayList<AttachmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttachmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final Long _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            }
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalUri;
            _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            final String _tmpExtractedText;
            _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AttachmentEntity(_tmpId,_tmpSessionId,_tmpMessageId,_tmpFileName,_tmpMimeType,_tmpLocalUri,_tmpExtractedText,_tmpCreatedAt);
            _result.add(_item);
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
  public Flow<List<AttachmentEntity>> observeBySessionId(final long sessionId) {
    final String _sql = "SELECT * FROM attachments WHERE sessionId = ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attachments"}, new Callable<List<AttachmentEntity>>() {
      @Override
      @NonNull
      public List<AttachmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AttachmentEntity> _result = new ArrayList<AttachmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttachmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final Long _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            }
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalUri;
            _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            final String _tmpExtractedText;
            _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AttachmentEntity(_tmpId,_tmpSessionId,_tmpMessageId,_tmpFileName,_tmpMimeType,_tmpLocalUri,_tmpExtractedText,_tmpCreatedAt);
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
  public Object getPendingBySessionId(final long sessionId,
      final Continuation<? super List<AttachmentEntity>> $completion) {
    final String _sql = "SELECT * FROM attachments WHERE sessionId = ? AND messageId IS NULL ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttachmentEntity>>() {
      @Override
      @NonNull
      public List<AttachmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AttachmentEntity> _result = new ArrayList<AttachmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttachmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final Long _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            }
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalUri;
            _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            final String _tmpExtractedText;
            _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AttachmentEntity(_tmpId,_tmpSessionId,_tmpMessageId,_tmpFileName,_tmpMimeType,_tmpLocalUri,_tmpExtractedText,_tmpCreatedAt);
            _result.add(_item);
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
  public Flow<List<AttachmentEntity>> observePendingBySessionId(final long sessionId) {
    final String _sql = "SELECT * FROM attachments WHERE sessionId = ? AND messageId IS NULL ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attachments"}, new Callable<List<AttachmentEntity>>() {
      @Override
      @NonNull
      public List<AttachmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AttachmentEntity> _result = new ArrayList<AttachmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttachmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final Long _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            }
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalUri;
            _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            final String _tmpExtractedText;
            _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AttachmentEntity(_tmpId,_tmpSessionId,_tmpMessageId,_tmpFileName,_tmpMimeType,_tmpLocalUri,_tmpExtractedText,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
