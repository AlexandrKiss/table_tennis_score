package com.kiss.tabletennisscore.common

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private var pref: SharedPreferences? = null

    fun init(context : Context){
        pref = context.getSharedPreferences(context.packageName, 0)
    }

    fun init(context : Context, name : String){
        pref = context.getSharedPreferences(name, 0)
    }

    fun save(key: String, value: Boolean) {
        if(pref != null)
            pref!!.edit().putBoolean(key, value).apply()
    }

    fun load(key: String, defaultValue: Boolean): Boolean {
        return if(pref != null) pref!!.getBoolean(key, defaultValue)
        else defaultValue
    }

    fun save(key: String, value: Int) {
        if(pref != null)
            pref!!.edit().putInt(key, value).apply()
    }

    fun load(key: String, defaultValue: Int): Int {
        return if(pref != null) pref!!.getInt(key, defaultValue)
        else defaultValue
    }

    fun save(key: String, value: Long) {
        if(pref != null)
            pref!!.edit().putLong(key, value).apply()
    }

    fun load(key: String, defaultValue: Long): Long {
        return if(pref != null) pref!!.getLong(key, defaultValue)
        else defaultValue
    }

    fun save(key: String, value: Float) {
        if(pref != null)
            pref!!.edit().putFloat(key, value).apply()
    }

    fun load(key: String, defaultValue: Float): Float {
        return if(pref != null) pref!!.getFloat(key, defaultValue)
        else defaultValue
    }

    fun save(key: String, value: Double) {
        if(pref != null)
            pref!!.edit().putString(key, "$value").apply()
    }

    fun load(key: String, defaultValue: Double): Double {
        return if(pref != null) (pref!!.getString(key, "$defaultValue") ?: "$defaultValue").toDouble()
        else defaultValue
    }

    fun save(key: String, value: String) {
        if(pref != null)
            pref!!.edit().putString(key, value).apply()
    }

    fun load(key: String, defaultValue: String): String {
        return (if(pref != null) pref?.getString(key, defaultValue)
        else defaultValue) as String
    }

    fun save(key: String, value: Set<String>) {
        if(pref != null)
            pref!!.edit().putStringSet(key, value).apply()
    }

    fun load(key: String, defaultValue: Set<String>): Set<String> {
        return (if(pref != null) pref?.getStringSet(key, defaultValue)
        else defaultValue) as Set<String>
    }

    fun cancel(){
        pref?.edit()?.clear()
    }
}