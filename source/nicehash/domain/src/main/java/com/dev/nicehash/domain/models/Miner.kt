package com.dev.nicehash.domain.models

import java.io.Serializable

/**
 * Created by Alex Gladkov on 23.06.18.
 * Data model for Miner entity in domain layer
 */
data class Miner(val id: Int, val name: String, var hash: String, var isSelected: Boolean): Serializable {

    companion object {
        fun getDefaultInstance(): Miner {
            return Miner(id = -10, name = "Demo miner", hash = "Demo miner", isSelected = true)
        }
    }
}