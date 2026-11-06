package com.Lumpexo4.Gotpuc.domain.model

data class Target(
    val id: Long,
    val cell: Int,
    val kind: TargetKind,
    val bornAtMs: Long,
    val lifetimeMs: Long
)
