package com.example.supervisorar.domain.mapper

interface Mapper<in T, out R> {
    fun map(t: T): R
}