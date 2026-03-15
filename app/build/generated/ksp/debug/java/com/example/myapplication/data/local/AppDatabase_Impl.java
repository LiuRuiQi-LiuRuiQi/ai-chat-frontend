package com.example.myapplication.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.myapplication.data.local.dao.AttachmentDao;
import com.example.myapplication.data.local.dao.AttachmentDao_Impl;
import com.example.myapplication.data.local.dao.CharacterDao;
import com.example.myapplication.data.local.dao.CharacterDao_Impl;
import com.example.myapplication.data.local.dao.MessageDao;
import com.example.myapplication.data.local.dao.MessageDao_Impl;
import com.example.myapplication.data.local.dao.ModelDao;
import com.example.myapplication.data.local.dao.ModelDao_Impl;
import com.example.myapplication.data.local.dao.PresetDao;
import com.example.myapplication.data.local.dao.PresetDao_Impl;
import com.example.myapplication.data.local.dao.ProviderDao;
import com.example.myapplication.data.local.dao.ProviderDao_Impl;
import com.example.myapplication.data.local.dao.SessionDao;
import com.example.myapplication.data.local.dao.SessionDao_Impl;
import com.example.myapplication.data.local.dao.WorldBookEntryDao;
import com.example.myapplication.data.local.dao.WorldBookEntryDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SessionDao _sessionDao;

  private volatile MessageDao _messageDao;

  private volatile ProviderDao _providerDao;

  private volatile PresetDao _presetDao;

  private volatile ModelDao _modelDao;

  private volatile CharacterDao _characterDao;

  private volatile WorldBookEntryDao _worldBookEntryDao;

  private volatile AttachmentDao _attachmentDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `providerId` INTEGER, `presetId` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `modelName` TEXT, `characterId` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` INTEGER NOT NULL, `role` TEXT NOT NULL, `content` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `tokenCount` INTEGER, `isError` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_sessionId` ON `messages` (`sessionId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `providers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `baseUrl` TEXT NOT NULL, `apiKey` TEXT NOT NULL, `apiFormat` TEXT NOT NULL, `defaultModel` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `isDefault` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `presets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `systemPrompt` TEXT NOT NULL, `description` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `isDefault` INTEGER NOT NULL, `temperature` REAL, `topP` REAL, `maxTokens` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `models` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `providerId` INTEGER NOT NULL, `modelName` TEXT NOT NULL, `displayName` TEXT NOT NULL, `enabled` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_models_providerId_modelName` ON `models` (`providerId`, `modelName`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `characters` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `greeting` TEXT NOT NULL, `tags` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `world_book_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `keywords` TEXT NOT NULL, `content` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `priority` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attachments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` INTEGER NOT NULL, `messageId` INTEGER, `fileName` TEXT NOT NULL, `mimeType` TEXT NOT NULL, `localUri` TEXT NOT NULL, `extractedText` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`messageId`) REFERENCES `messages`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attachments_sessionId` ON `attachments` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attachments_messageId` ON `attachments` (`messageId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1d07ef5adaa368504794ea2039f76fe9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `sessions`");
        db.execSQL("DROP TABLE IF EXISTS `messages`");
        db.execSQL("DROP TABLE IF EXISTS `providers`");
        db.execSQL("DROP TABLE IF EXISTS `presets`");
        db.execSQL("DROP TABLE IF EXISTS `models`");
        db.execSQL("DROP TABLE IF EXISTS `characters`");
        db.execSQL("DROP TABLE IF EXISTS `world_book_entries`");
        db.execSQL("DROP TABLE IF EXISTS `attachments`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSessions = new HashMap<String, TableInfo.Column>(9);
        _columnsSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("providerId", new TableInfo.Column("providerId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("presetId", new TableInfo.Column("presetId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("isPinned", new TableInfo.Column("isPinned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("modelName", new TableInfo.Column("modelName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("characterId", new TableInfo.Column("characterId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSessions = new TableInfo("sessions", _columnsSessions, _foreignKeysSessions, _indicesSessions);
        final TableInfo _existingSessions = TableInfo.read(db, "sessions");
        if (!_infoSessions.equals(_existingSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "sessions(com.example.myapplication.data.local.entity.SessionEntity).\n"
                  + " Expected:\n" + _infoSessions + "\n"
                  + " Found:\n" + _existingSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(7);
        _columnsMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("sessionId", new TableInfo.Column("sessionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("tokenCount", new TableInfo.Column("tokenCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("isError", new TableInfo.Column("isError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMessages.add(new TableInfo.ForeignKey("sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(1);
        _indicesMessages.add(new TableInfo.Index("index_messages_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(db, "messages");
        if (!_infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(com.example.myapplication.data.local.entity.MessageEntity).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsProviders = new HashMap<String, TableInfo.Column>(9);
        _columnsProviders.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("baseUrl", new TableInfo.Column("baseUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("apiKey", new TableInfo.Column("apiKey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("apiFormat", new TableInfo.Column("apiFormat", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("defaultModel", new TableInfo.Column("defaultModel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProviders.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProviders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProviders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProviders = new TableInfo("providers", _columnsProviders, _foreignKeysProviders, _indicesProviders);
        final TableInfo _existingProviders = TableInfo.read(db, "providers");
        if (!_infoProviders.equals(_existingProviders)) {
          return new RoomOpenHelper.ValidationResult(false, "providers(com.example.myapplication.data.local.entity.ProviderEntity).\n"
                  + " Expected:\n" + _infoProviders + "\n"
                  + " Found:\n" + _existingProviders);
        }
        final HashMap<String, TableInfo.Column> _columnsPresets = new HashMap<String, TableInfo.Column>(10);
        _columnsPresets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("systemPrompt", new TableInfo.Column("systemPrompt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("temperature", new TableInfo.Column("temperature", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("topP", new TableInfo.Column("topP", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPresets.put("maxTokens", new TableInfo.Column("maxTokens", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPresets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPresets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPresets = new TableInfo("presets", _columnsPresets, _foreignKeysPresets, _indicesPresets);
        final TableInfo _existingPresets = TableInfo.read(db, "presets");
        if (!_infoPresets.equals(_existingPresets)) {
          return new RoomOpenHelper.ValidationResult(false, "presets(com.example.myapplication.data.local.entity.PresetEntity).\n"
                  + " Expected:\n" + _infoPresets + "\n"
                  + " Found:\n" + _existingPresets);
        }
        final HashMap<String, TableInfo.Column> _columnsModels = new HashMap<String, TableInfo.Column>(5);
        _columnsModels.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModels.put("providerId", new TableInfo.Column("providerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModels.put("modelName", new TableInfo.Column("modelName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModels.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModels.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysModels = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesModels = new HashSet<TableInfo.Index>(1);
        _indicesModels.add(new TableInfo.Index("index_models_providerId_modelName", true, Arrays.asList("providerId", "modelName"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoModels = new TableInfo("models", _columnsModels, _foreignKeysModels, _indicesModels);
        final TableInfo _existingModels = TableInfo.read(db, "models");
        if (!_infoModels.equals(_existingModels)) {
          return new RoomOpenHelper.ValidationResult(false, "models(com.example.myapplication.data.local.entity.ModelEntity).\n"
                  + " Expected:\n" + _infoModels + "\n"
                  + " Found:\n" + _existingModels);
        }
        final HashMap<String, TableInfo.Column> _columnsCharacters = new HashMap<String, TableInfo.Column>(5);
        _columnsCharacters.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("greeting", new TableInfo.Column("greeting", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCharacters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCharacters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCharacters = new TableInfo("characters", _columnsCharacters, _foreignKeysCharacters, _indicesCharacters);
        final TableInfo _existingCharacters = TableInfo.read(db, "characters");
        if (!_infoCharacters.equals(_existingCharacters)) {
          return new RoomOpenHelper.ValidationResult(false, "characters(com.example.myapplication.data.local.entity.CharacterEntity).\n"
                  + " Expected:\n" + _infoCharacters + "\n"
                  + " Found:\n" + _existingCharacters);
        }
        final HashMap<String, TableInfo.Column> _columnsWorldBookEntries = new HashMap<String, TableInfo.Column>(8);
        _columnsWorldBookEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorldBookEntries.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorldBookEntries.put("keywords", new TableInfo.Column("keywords", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorldBookEntries.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorldBookEntries.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorldBookEntries.put("priority", new TableInfo.Column("priority", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorldBookEntries.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorldBookEntries.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorldBookEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorldBookEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorldBookEntries = new TableInfo("world_book_entries", _columnsWorldBookEntries, _foreignKeysWorldBookEntries, _indicesWorldBookEntries);
        final TableInfo _existingWorldBookEntries = TableInfo.read(db, "world_book_entries");
        if (!_infoWorldBookEntries.equals(_existingWorldBookEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "world_book_entries(com.example.myapplication.data.local.entity.WorldBookEntryEntity).\n"
                  + " Expected:\n" + _infoWorldBookEntries + "\n"
                  + " Found:\n" + _existingWorldBookEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsAttachments = new HashMap<String, TableInfo.Column>(8);
        _columnsAttachments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("sessionId", new TableInfo.Column("sessionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("messageId", new TableInfo.Column("messageId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("fileName", new TableInfo.Column("fileName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("mimeType", new TableInfo.Column("mimeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("localUri", new TableInfo.Column("localUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("extractedText", new TableInfo.Column("extractedText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttachments = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysAttachments.add(new TableInfo.ForeignKey("sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        _foreignKeysAttachments.add(new TableInfo.ForeignKey("messages", "CASCADE", "NO ACTION", Arrays.asList("messageId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAttachments = new HashSet<TableInfo.Index>(2);
        _indicesAttachments.add(new TableInfo.Index("index_attachments_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        _indicesAttachments.add(new TableInfo.Index("index_attachments_messageId", false, Arrays.asList("messageId"), Arrays.asList("ASC")));
        final TableInfo _infoAttachments = new TableInfo("attachments", _columnsAttachments, _foreignKeysAttachments, _indicesAttachments);
        final TableInfo _existingAttachments = TableInfo.read(db, "attachments");
        if (!_infoAttachments.equals(_existingAttachments)) {
          return new RoomOpenHelper.ValidationResult(false, "attachments(com.example.myapplication.data.local.entity.AttachmentEntity).\n"
                  + " Expected:\n" + _infoAttachments + "\n"
                  + " Found:\n" + _existingAttachments);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1d07ef5adaa368504794ea2039f76fe9", "115a680618c6487210ba6363fe833040");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sessions","messages","providers","presets","models","characters","world_book_entries","attachments");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `sessions`");
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `providers`");
      _db.execSQL("DELETE FROM `presets`");
      _db.execSQL("DELETE FROM `models`");
      _db.execSQL("DELETE FROM `characters`");
      _db.execSQL("DELETE FROM `world_book_entries`");
      _db.execSQL("DELETE FROM `attachments`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SessionDao.class, SessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MessageDao.class, MessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProviderDao.class, ProviderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PresetDao.class, PresetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ModelDao.class, ModelDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CharacterDao.class, CharacterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorldBookEntryDao.class, WorldBookEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AttachmentDao.class, AttachmentDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SessionDao sessionDao() {
    if (_sessionDao != null) {
      return _sessionDao;
    } else {
      synchronized(this) {
        if(_sessionDao == null) {
          _sessionDao = new SessionDao_Impl(this);
        }
        return _sessionDao;
      }
    }
  }

  @Override
  public MessageDao messageDao() {
    if (_messageDao != null) {
      return _messageDao;
    } else {
      synchronized(this) {
        if(_messageDao == null) {
          _messageDao = new MessageDao_Impl(this);
        }
        return _messageDao;
      }
    }
  }

  @Override
  public ProviderDao providerDao() {
    if (_providerDao != null) {
      return _providerDao;
    } else {
      synchronized(this) {
        if(_providerDao == null) {
          _providerDao = new ProviderDao_Impl(this);
        }
        return _providerDao;
      }
    }
  }

  @Override
  public PresetDao presetDao() {
    if (_presetDao != null) {
      return _presetDao;
    } else {
      synchronized(this) {
        if(_presetDao == null) {
          _presetDao = new PresetDao_Impl(this);
        }
        return _presetDao;
      }
    }
  }

  @Override
  public ModelDao modelDao() {
    if (_modelDao != null) {
      return _modelDao;
    } else {
      synchronized(this) {
        if(_modelDao == null) {
          _modelDao = new ModelDao_Impl(this);
        }
        return _modelDao;
      }
    }
  }

  @Override
  public CharacterDao characterDao() {
    if (_characterDao != null) {
      return _characterDao;
    } else {
      synchronized(this) {
        if(_characterDao == null) {
          _characterDao = new CharacterDao_Impl(this);
        }
        return _characterDao;
      }
    }
  }

  @Override
  public WorldBookEntryDao worldBookEntryDao() {
    if (_worldBookEntryDao != null) {
      return _worldBookEntryDao;
    } else {
      synchronized(this) {
        if(_worldBookEntryDao == null) {
          _worldBookEntryDao = new WorldBookEntryDao_Impl(this);
        }
        return _worldBookEntryDao;
      }
    }
  }

  @Override
  public AttachmentDao attachmentDao() {
    if (_attachmentDao != null) {
      return _attachmentDao;
    } else {
      synchronized(this) {
        if(_attachmentDao == null) {
          _attachmentDao = new AttachmentDao_Impl(this);
        }
        return _attachmentDao;
      }
    }
  }
}
