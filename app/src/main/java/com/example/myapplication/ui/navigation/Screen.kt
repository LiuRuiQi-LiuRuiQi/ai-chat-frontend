package com.example.myapplication.ui.navigation

/**
 * 导航路由定义
 */
sealed class Screen(val route: String) {
    /** 会话列表页 */
    object SessionList : Screen("session_list")

    /** Provider 列表页 */
    object ProviderList : Screen("provider_list")

    /** Provider 编辑页 */
    object ProviderEdit : Screen("provider_edit?providerId={providerId}") {
        fun createRoute(providerId: Long? = null): String {
            return if (providerId == null) {
                "provider_edit"
            } else {
                "provider_edit?providerId=$providerId"
            }
        }
    }

    /** Preset 列表页 */
    object PresetList : Screen("preset_list")

    /** Preset 编辑页 */
    object PresetEdit : Screen("preset_edit?presetId={presetId}") {
    /** 新建 Preset 的伪 ID（与导航默认值保持一致） */
    const val NEW_PRESET_ID = -1L

    fun createRoute(presetId: Long): String = "preset_edit?presetId=$presetId"
}

    /** 聊天页 - 携带 sessionId 参数 */
    object Chat : Screen("chat/{sessionId}") {
        fun createRoute(sessionId: Long) = "chat/$sessionId"
    }

    /** 角色列表页 */
    object CharacterList : Screen("character_list")
}
