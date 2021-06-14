package com.example.androidwithkotlin.extension

import java.util.*

/**
 * Получить системный формать даты
 * @param dateFormat Формать даты [java.text.DateFormat]
 */
fun Any.getContextDateFormat(dateFormat: Int = java.text.DateFormat.LONG): java.text.DateFormat =
    java.text.DateFormat.getDateInstance(
        dateFormat,
        getApplicationContext().resources.configuration.locale
    )
        ?: java.text.DateFormat.getDateInstance(
            dateFormat,
            Locale.ENGLISH
        )
/**
 * Преобразовать дату в системный формат
 * @param dateFormat Формать даты [java.text.DateFormat]
 */
fun Date.formatDateToContextDateFormat(dateFormat: Int = java.text.DateFormat.LONG): String =
    getContextDateFormat(dateFormat).format(this)

/**
 * Получить системный формать даты времени
 * @param dateFormat Формать даты [java.text.DateFormat]
 */
fun Any.getContextDateTimeFormat(
    dateFormat: Int = java.text.DateFormat.LONG,
    timeFormat: Int = java.text.DateFormat.LONG
): java.text.DateFormat =
    java.text.DateFormat.getDateTimeInstance(
        dateFormat,
        timeFormat,
        getApplicationContext().resources.configuration.locale
    )
        ?: java.text.DateFormat.getDateTimeInstance(
            dateFormat,
            timeFormat,
            Locale.ENGLISH
        )
/**
 * Преобразовать дату время в системный формат
 * @param dateFormat Формать даты [java.text.DateFormat]
 */
fun Date.formatDateTimeToContextDateFormat(
    dateFormat: Int = java.text.DateFormat.LONG,
    timeFormat: Int = java.text.DateFormat.LONG
): String =
    getContextDateTimeFormat(dateFormat, timeFormat).format(this)