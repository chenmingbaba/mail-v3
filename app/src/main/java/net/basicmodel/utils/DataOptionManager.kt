package net.basicmodel.utils

class DataOptionManager {
    companion object {
        private var i: DataOptionManager? = null
            get() {
                field ?: run {
                    field = DataOptionManager()
                }
                return field
            }

        @Synchronized
        fun get(): DataOptionManager {
            return i!!
        }
    }

    fun add() {

    }

    fun delete() {

    }

    fun save(index: Int) {
        when (index) {
            0 -> {

            }
            1 -> {

            }
            2 -> {

            }
        }
    }

    fun submit() {

    }
}