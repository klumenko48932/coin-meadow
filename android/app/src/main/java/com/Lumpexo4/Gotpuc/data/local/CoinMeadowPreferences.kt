package com.Lumpexo4.Gotpuc.data.local

import android.content.Context
import android.content.SharedPreferences

class CoinMeadowPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun text(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun ids(key: String): Set<Int> = text(key, "")
        .split(SEPARATOR)
        .mapNotNull { it.trim().toIntOrNull() }
        .toSet()

    fun write(block: SharedPreferences.Editor.() -> Unit) {
        val editor = prefs.edit()
        editor.block()
        editor.apply()
    }

    fun writeIds(key: String, values: Set<Int>) {
        write { putString(key, values.sorted().joinToString(SEPARATOR.toString())) }
    }

    fun wipe(keys: List<String>) {
        write { keys.forEach { remove(it) } }
    }

    companion object {
        private const val STORE = "coin_meadow_store"
        private const val SEPARATOR = ','

        const val KEY_TOTAL_COINS = "total_coins"
        const val KEY_BEST_SCORE = "best_score"
        const val KEY_LONGEST_STREAK = "longest_streak"
        const val KEY_ROUNDS_PLAYED = "rounds_played"
        const val KEY_PERFECT_STRIKES = "perfect_strikes"
        const val KEY_BOOST_CHARGES = "boost_charges"
        const val KEY_CLAIMED_REWARDS = "claimed_reward_ids"
        const val KEY_UNLOCKED_CHARMS = "unlocked_charm_ids"
        const val KEY_LAST_DAILY_DAY = "last_daily_day"
        const val KEY_DAILY_STREAK = "daily_streak"
        const val KEY_DAILY_CLEARED_DAYS = "daily_cleared_days"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_REDUCED_MOTION = "reduced_motion"
        const val KEY_DIFFICULTY = "difficulty"
    }
}
