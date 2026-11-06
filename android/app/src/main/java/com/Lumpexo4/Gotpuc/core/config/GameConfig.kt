package com.Lumpexo4.Gotpuc.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val ROUND_DURATION_MS = 45_000L
    const val DAILY_SHORT_ROUND_MS = 30_000L
    const val TICK_MS = 50L

    const val BOARD_COLS = 4
    const val BOARD_ROWS = 5
    const val BOARD_CELLS = BOARD_COLS * BOARD_ROWS
    const val MAX_CONCURRENT_TARGETS = 4

    const val SPAWN_INTERVAL_BASE_MS = 820L
    const val SPAWN_INTERVAL_STEP_MS = 35L
    const val SPAWN_INTERVAL_FLOOR_MS = 420L

    const val TARGET_LIFETIME_BASE_MS = 1500L
    const val TARGET_LIFETIME_STEP_MS = 60L
    const val TARGET_LIFETIME_FLOOR_MS = 780L

    const val COIN_VALUE = 10
    const val CHARM_COIN_BONUS = 25
    const val CLOVER_TIME_BONUS_MS = 1500L

    const val MAX_LIVES = 3
    const val MAX_LEVEL = 12
    const val COINS_PER_LEVEL = 150

    const val STRIKE_SWEEP_MS = 900
    const val STRIKE_CORE_FRACTION = 0.06f
    const val STRIKE_BAND_WIDE = 0.22f
    const val STRIKE_BAND_MEDIUM = 0.18f
    const val STRIKE_BAND_TIGHT = 0.14f
    const val STRIKE_VERDICT_HOLD_MS = 900L

    const val BOOST_DURATION_MS = 5000L
    const val BOOST_SLOWDOWN = 0.35f
    const val COINS_PER_BOOST_CHARGE = 500
    const val MAX_BOOST_CHARGES = 5

    const val STREAK_RIBBON_MIN = 3
    const val SPARKLE_STREAK_MIN = 7
    const val LEVEL_BANNER_MS = 900L

    const val DAILY_WEEK_DAYS = 7
    const val DAILY_GOLD_RUSH_TARGET = 240
    const val DAILY_STEADY_HAND_TARGET = 150
    const val DAILY_CHEST_HUNT_TARGET = 4
}
