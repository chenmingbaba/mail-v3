package net.basicmodel.utils

import com.luck.picture.lib.entity.LocalMedia

interface PhotoCallBack {
    fun gallery(data: ArrayList<LocalMedia>)
    fun camera(path: String)
}