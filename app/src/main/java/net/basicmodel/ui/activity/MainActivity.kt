package net.basicmodel.ui.activity

import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.activity_main.*
import net.basicmodel.R
import net.basicmodel.base.BaseActivity
import net.basicmodel.event.MessageEvent
import net.basicmodel.ui.fragment.NetFragment
import net.basicmodel.ui.fragment.ScramFragment
import net.basicmodel.ui.fragment.TourFragment
import net.basicmodel.utils.*
import net.basicmodel.widget.LoadingDialog
import net.basicmodel.widget.OptionDialog
import org.greenrobot.eventbus.EventBus
import java.util.*

class MainActivity : BaseActivity(), OnPermissionCallback, ViewPager.OnPageChangeListener,
    OptionClickListener {

    var netFragment: NetFragment? = null
    var scramFragment: ScramFragment? = null
    var tourFragment: TourFragment? = null
    var dialog: LoadingDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        requestPermissions()
        "有开始时间，比如 2021-3-20 18:30:23到2021-3-20 18:50:50".dateRule()
    }

    private fun requestPermissions() {
        XXPermissions.with(this).permission(Constant.permission).request(this)
    }

    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
        initViewpager()
    }

    private fun initViewpager() {
        netFragment = NetFragment()
        scramFragment = ScramFragment()
        tourFragment = TourFragment()
//        views.add(netFragment!!)
//        views.add(scramFragment!!)
        views.add(tourFragment!!)
        tab.setViewPager(viewpager, Constant.tab, this, views)
        viewpager.setOnPageChangeListener(this)
        option.setOnClickListener {
            val p = OptionDialog(this)
            p.listener = this
            p.show()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        KeyboardManager.get().hideKeyboard(this)
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun OptionClick(index: Int) {
        when (index) {
            0 -> {
                when (tab.currentTab) {
                    0 -> {
//                        EventBus.getDefault().post(MessageEvent("filed"))
                        EventBus.getDefault().post(MessageEvent("filed2"))
                    }
//                    1 -> {
//                        EventBus.getDefault().post(MessageEvent("filed2"))
//                    }
//                    2 -> {
//                        EventBus.getDefault().post(MessageEvent("filed2"))
//                    }
                }

            }
            1 -> {
                when (tab.currentTab) {
                    0 -> {
//                        EventBus.getDefault().post(MessageEvent("add"))
                        EventBus.getDefault().post(MessageEvent("add2"))
                    }
//                    1 -> {
//                        EventBus.getDefault().post(MessageEvent("add2"))
//                    }
//                    2 -> {
//                        EventBus.getDefault().post(MessageEvent("add2"))
//                    }
                }
            }
            2 -> {
                when (tab.currentTab) {
                    0 -> {
//                        EventBus.getDefault().post(MessageEvent("delete"))
                        EventBus.getDefault().post(MessageEvent("delete2"))
                    }
//                    1 -> {
//                        EventBus.getDefault().post(MessageEvent("delete2"))
//                    }
//                    2 -> {
//                        EventBus.getDefault().post(MessageEvent("delete2"))
//                    }
                }
            }
            3 -> {
                when (tab.currentTab) {
                    0 -> {
//                        EventBus.getDefault().post(MessageEvent("save"))
                        EventBus.getDefault().post(MessageEvent("save2"))
                    }
//                    1 -> {
//                        EventBus.getDefault().post(MessageEvent("save2"))
//                    }
//                    2 -> {
//                        EventBus.getDefault().post(MessageEvent("save2"))
//                    }
                }
            }
            4 -> {
                when (tab.currentTab) {
                    0 -> {
//                        EventBus.getDefault().post(MessageEvent("submit"))
                        EventBus.getDefault().post(MessageEvent("submit2"))
                    }
//                    1 -> {
//                        EventBus.getDefault().post(MessageEvent("submit2"))
//                    }
//                    2 -> {
//                        EventBus.getDefault().post(MessageEvent("submit2"))
//                    }
                }
            }
            5 -> {
                when (tab.currentTab) {
                    0 -> {
                        EventBus.getDefault().post(MessageEvent("sendWithEmail"))
                    }
                }
            }
            6 -> {
                when (tab.currentTab) {
                    0 -> {
                        EventBus.getDefault().post(MessageEvent("configEmail"))
                    }
                }
            }
            7 -> {
                when (tab.currentTab) {
                    0 -> {
                        EventBus.getDefault().post(MessageEvent("configUser"))
                    }
                }
            }
            8-> {
                when (tab.currentTab) {
                    0 -> {
                        EventBus.getDefault().post(MessageEvent("submitAll"))
                    }
                }
            }
        }
    }

    fun getThemeText(index: Int): String {
        when (index) {
            0 -> return "光纤巡缆${MyLocationManager.get().formatDate(Date())}"
            1 -> return "光纤巡缆${MyLocationManager.get().formatDate(Date())}"
            2 -> return "光纤巡缆${MyLocationManager.get().formatDate(Date())}"
        }
        return ""
    }

}