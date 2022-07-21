package net.basicmodel.utils

import android.app.Activity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

class PhotoManager {
    companion object {
        private var i: PhotoManager? = null
            get() {
                field ?: run {
                    field = PhotoManager()
                }
                return field
            }

        @Synchronized
        fun get(): PhotoManager {
            return i!!
        }
    }

    fun openAlbum(activity: Activity, listener: PhotoCallBack) {
        PictureSelector.create(activity).openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    result!!.apply {
                        listener.gallery(this as ArrayList<LocalMedia>)
                    }
                }

                override fun onCancel() {
                }
            })
    }

    fun takePhoto(activity: Activity, listener: PhotoCallBack) {
        PictureSelector.create(activity)
            .openCamera(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    result!!.apply {
                        val path = this[0].path
                        listener.camera(path)
                    }
                }

                override fun onCancel() {
                }
            })
    }

    fun handle(list: ArrayList<LocalMedia>): ArrayList<String> {
        val result = ArrayList<String>()
        for (item in list) {
            result.add(item.realPath)
        }
        return result
    }
}