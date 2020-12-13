package com.supernova.bkashmanager.model

import androidx.room.Embedded
import androidx.room.Relation

class UserHistory {

    @Embedded
    var history: History? = null

    @Relation(
            parentColumn = "user_id",
            entityColumn = "id")
    var user: User? = null
}