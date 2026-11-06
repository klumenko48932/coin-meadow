package com.Lumpexo4.Gotpuc.data.sample

import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.domain.model.Charm
import com.Lumpexo4.Gotpuc.domain.model.DailyChallenge
import com.Lumpexo4.Gotpuc.domain.model.MeadowLevel
import com.Lumpexo4.Gotpuc.domain.model.Reward

object SampleData {

    val charms: List<Charm> = listOf(
        Charm(1, R.string.charm_name_1, R.string.charm_flavor_1, 100),
        Charm(2, R.string.charm_name_2, R.string.charm_flavor_2, 250),
        Charm(3, R.string.charm_name_3, R.string.charm_flavor_3, 450),
        Charm(4, R.string.charm_name_4, R.string.charm_flavor_4, 700),
        Charm(5, R.string.charm_name_5, R.string.charm_flavor_5, 1000),
        Charm(6, R.string.charm_name_6, R.string.charm_flavor_6, 1400),
        Charm(7, R.string.charm_name_7, R.string.charm_flavor_7, 1900),
        Charm(8, R.string.charm_name_8, R.string.charm_flavor_8, 2500),
        Charm(9, R.string.charm_name_9, R.string.charm_flavor_9, 3200),
        Charm(10, R.string.charm_name_10, R.string.charm_flavor_10, 4000),
        Charm(11, R.string.charm_name_11, R.string.charm_flavor_11, 5000),
        Charm(12, R.string.charm_name_12, R.string.charm_flavor_12, 6200)
    )

    val levels: List<MeadowLevel> = listOf(
        MeadowLevel(1, R.string.level_name_1, 0),
        MeadowLevel(2, R.string.level_name_2, 500),
        MeadowLevel(3, R.string.level_name_3, 1000),
        MeadowLevel(4, R.string.level_name_4, 2000),
        MeadowLevel(5, R.string.level_name_5, 3500),
        MeadowLevel(6, R.string.level_name_6, 5500),
        MeadowLevel(7, R.string.level_name_7, 8000),
        MeadowLevel(8, R.string.level_name_8, 11000),
        MeadowLevel(9, R.string.level_name_9, 15000),
        MeadowLevel(10, R.string.level_name_10, 20000),
        MeadowLevel(11, R.string.level_name_11, 26000),
        MeadowLevel(12, R.string.level_name_12, 33000)
    )

    val rewards: List<Reward> = listOf(
        Reward(1, R.string.reward_name_1, 250, 1),
        Reward(2, R.string.reward_name_2, 750, 1),
        Reward(3, R.string.reward_name_3, 1500, 2),
        Reward(4, R.string.reward_name_4, 3000, 2),
        Reward(5, R.string.reward_name_5, 6000, 3),
        Reward(6, R.string.reward_name_6, 12000, 3)
    )

    val dailyChallenges: List<DailyChallenge> = listOf(
        DailyChallenge(
            id = 0,
            nameRes = R.string.daily_name_1,
            ruleRes = R.string.daily_rule_1,
            targetRes = R.string.daily_target_1,
            durationMs = GameConfig.DAILY_SHORT_ROUND_MS,
            lives = GameConfig.MAX_LIVES,
            coinTarget = GameConfig.DAILY_GOLD_RUSH_TARGET,
            strikeTarget = 0,
            chestWeightMultiplier = 1,
            coinsOnly = true
        ),
        DailyChallenge(
            id = 1,
            nameRes = R.string.daily_name_2,
            ruleRes = R.string.daily_rule_2,
            targetRes = R.string.daily_target_2,
            durationMs = GameConfig.ROUND_DURATION_MS,
            lives = 1,
            coinTarget = GameConfig.DAILY_STEADY_HAND_TARGET,
            strikeTarget = 0,
            chestWeightMultiplier = 1,
            coinsOnly = false
        ),
        DailyChallenge(
            id = 2,
            nameRes = R.string.daily_name_3,
            ruleRes = R.string.daily_rule_3,
            targetRes = R.string.daily_target_3,
            durationMs = GameConfig.ROUND_DURATION_MS,
            lives = GameConfig.MAX_LIVES,
            coinTarget = 0,
            strikeTarget = GameConfig.DAILY_CHEST_HUNT_TARGET,
            chestWeightMultiplier = 3,
            coinsOnly = false
        )
    )
}
