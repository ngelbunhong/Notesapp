package com.example.notesapp.utils

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
    fun onItemLongClick(item: T)
}