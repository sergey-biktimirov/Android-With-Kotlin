package com.example.androidwithkotlin.extension

import java.util.*

/**
 * Преобразовать милисекунды в системный формат даты
 * @param dateFormat Формать даты [java.text.DateFormat]
 */
fun Long.formatDateToContextDateFormat(): String =
    Date(this).formatDateToContextDateFormat()

/**
 * Преобразовать милисекунды в системный формат даты времени
 * @param dateFormat Формать даты [java.text.DateFormat]
 */
fun Long.formatDateTimeToContextDateFormat(): String =
    Date(this).formatDateTimeToContextDateFormat()