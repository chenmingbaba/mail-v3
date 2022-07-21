package net.basicmodel.utils

import android.view.ViewGroup
import net.basicmodel.entity.CustomFiledEntity
import net.basicmodel.widget.InputEditText

class CustomFiledManager {
    companion object {
        private var i: CustomFiledManager? = null
            get() {
                field ?: run {
                    field = CustomFiledManager()
                }
                return field
            }

        @Synchronized
        fun get(): CustomFiledManager {
            return i!!
        }
    }


    fun addCustomItem(parent: ViewGroup, entity: CustomFiledEntity) {
        val customItem = InputEditText(parent.context)
        customItem.setMargin(ScreenUtils.get().dip2px(parent.context, 20f), 0, 0, 0)
        customItem.setHint(entity.name)
        customItem.setEditTextContent(entity.content)
        parent.addView(customItem)
        parent.invalidate()
    }

    fun getCustomData(parent: ViewGroup): ArrayList<CustomFiledEntity> {
        val result = ArrayList<CustomFiledEntity>()
        for (index in 0 until parent.childCount) {
            val item = parent.getChildAt(index) as InputEditText
            val entity = CustomFiledEntity(item.getHint(), item.getEditTextContent())
            result.add(entity)
        }
        return result
    }

    fun removeCustomItem(parent: ViewGroup) {
        parent.removeAllViews()
    }
}