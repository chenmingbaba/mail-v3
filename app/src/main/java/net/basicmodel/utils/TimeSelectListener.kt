package net.basicmodel.utils


interface TimeSelectListener {
    fun timeSelect(flag: String, time: String)
    fun dialogDismiss()
}