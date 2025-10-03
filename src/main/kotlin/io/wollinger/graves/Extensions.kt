package io.wollinger.graves

import java.util.UUID

fun String.toUUID(): UUID = UUID.fromString(this)