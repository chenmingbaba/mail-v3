package net.basicmodel.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tencent.mmkv.MMKV
import com.xxxxxxh.mailv2.utils.Constant
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    var views: ArrayList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        MMKV.initialize(this)
        initView()
        Constant.setLocale(this)
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

}