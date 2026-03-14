package com.example.myapplication.data.remote

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

/**
 * 远端版本信息数据类
 */
data class RemoteVersionInfo(
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    val changelog: String
)

/**
 * 更新服务 - 负责检查更新和下载 APK
 */
class UpdateService(
    private val context: Context,
    private val okHttpClient: OkHttpClient
) {

    companion object {
        // 版本信息 JSON 的远端 URL（可配置）
        private const val VERSION_JSON_URL = "https://example.com/version.json"
    }

    /**
     * 检查更新 - 拉取远端 version.json 并解析
     * @return RemoteVersionInfo 或 null（网络错误/解析失败）
     */
    suspend fun checkUpdate(): RemoteVersionInfo? = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = Request.Builder()
                .url(VERSION_JSON_URL)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext null
            }

            val body = response.body?.string() ?: return@withContext null
            val gson = Gson()
            gson.fromJson(body, RemoteVersionInfo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 下载 APK 文件
     * @param apkUrl APK 下载链接
     * @param onProgress 进度回调 (bytesDownloaded, totalBytes)
     * @return 下载成功返回 File，失败返回 null
     */
    suspend fun downloadApk(
        apkUrl: String,
        onProgress: (Long, Long) -> Unit = { _, _ -> }
    ): File? = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = Request.Builder()
                .url(apkUrl)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext null
            }

            val body = response.body ?: return@withContext null
            val totalBytes = body.contentLength()

            // 创建缓存目录
            val apkDir = File(context.cacheDir, "apk")
            if (!apkDir.exists()) {
                apkDir.mkdirs()
            }

            // 生成 APK 文件名
            val apkFile = File(apkDir, "app_update.apk")

            // 写入文件
            var bytesDownloaded = 0L
            apkFile.outputStream().use { fileOut ->
                body.byteStream().use { inputStream ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        fileOut.write(buffer, 0, bytesRead)
                        bytesDownloaded += bytesRead
                        onProgress(bytesDownloaded, totalBytes)
                    }
                }
            }

            apkFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取已下载的 APK 文件（如果存在）
     */
    fun getDownloadedApk(): File? {
        val apkFile = File(context.cacheDir, "apk/app_update.apk")
        return if (apkFile.exists()) apkFile else null
    }

    /**
     * 清理已下载的 APK 文件
     */
    fun clearDownloadedApk() {
        val apkFile = File(context.cacheDir, "apk/app_update.apk")
        if (apkFile.exists()) {
            apkFile.delete()
        }
    }
}
