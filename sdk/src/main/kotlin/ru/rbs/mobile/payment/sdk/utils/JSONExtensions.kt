package ru.rbs.mobile.payment.sdk.utils

import org.json.JSONArray
import org.json.JSONObject

/**
 * Представление массива json объектов в виде списка.
 *
 * @return возвращает список [JSONObject] на основе данных [JSONArray].
 */
fun JSONArray.asList(): List<JSONObject> =
    (0 until length()).asSequence().map { get(it) as JSONObject }.toList()

/**
 * Представление массива json объектов в виде списка срок.
 *
 * @return возвращает список [String] на основе данных [JSONArray].
 */
fun JSONArray.asStringList(): List<String> =
    (0 until length()).asSequence().map { get(it) as String }.toList()
