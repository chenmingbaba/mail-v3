package net.basicmodel.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.layout_fragment_net.*
import net.basicmodel.R
import net.basicmodel.adapter.ImageAdapter
import net.basicmodel.adapter.NetInfoAdapter
import net.basicmodel.base.BaseFragment
import net.basicmodel.entity.CustomFiledEntity
import net.basicmodel.entity.NetDetailsEntity
import net.basicmodel.entity.NetInfoEntityNew
import net.basicmodel.event.MessageEvent
import net.basicmodel.sendmail.EmailUtil
import net.basicmodel.sendmail.UsefulSTMP
import net.basicmodel.ui.activity.MainActivity
import net.basicmodel.ui.activity.MapActivity
import net.basicmodel.ui.activity.NetDetailsActivity
import net.basicmodel.utils.*
import net.basicmodel.widget.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

class NetFragment : BaseFragment(), OnOptionClickListener, LocationListener, OnDialogClickListener,
    PhotoCallBack {
    var netInfoAdapter: NetInfoAdapter? = null
    var imgAdapter: ImageAdapter? = null
    var locationTv: EditText? = null
    var onItem: Boolean = false
    override fun initView() {
        EventBus.getDefault().register(this)
        activity?.let {
            if (Constant.isOPen(it)) {
                MyLocationManager.get().getLocation(it, this)
            }
        }
        initContainer()
        initClick()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_fragment_net
    }

    private fun initContainer() {
        for (index in 0 until layoutContainer.childCount) {
            val container = layoutContainer.getChildAt(index)
            if (container is EditViewContainer) {
                val input = container.getInputView()
                input.setHint(Constant.hint[index])
                val imageView = container.getOptionView()
                when (index) {
                    0 -> {
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.mipmap.arrow_down)
                        container.setListener(this)
                        container.setTag("name")
                    }
                    1 -> {
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.mipmap.activity_main_refresh_icon)
                        container.setListener(this)
                        container.setTag("refresh")
                        val edit = container.getInputView().getEditTextView()
                        locationTv = edit
                    }
                    else -> {
                        imageView.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onClick(vararg tag: String) {
        val type = tag[0]
        when (type) {
            "customOk" -> {
                val entity = CustomFiledEntity()
                entity.name = tag[1]
                entity.content = tag[2]
                CustomFiledManager.get().addCustomItem(customRoot, entity)
            }
            "name" -> {
                val data = MMKVUtils.getKeys("net")
                if (data == null || data.size == 0) {
                    ToastUtils.s(activity, "暂无数据")
                } else {
                    val d = activity?.let { NameDialog(it, data, 0) }
                    d!!.show()
                }

            }
            "refresh" -> {
                activity?.let { LoadingDialogManager.get().show(it) }
                locationTv!!.setText("")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClick() {
        net_img_add.setOnClickListener {
            val d = activity?.let { it1 -> NetDetailsDialog(it1) }
            d!!.listenter = this
            d.show()
        }
        img_add.setOnClickListener {
            val d = SelectDialog(requireActivity(), requireActivity(), this)
            d.show()
        }
        location.getInputView().getEditTextView().setOnTouchListener { p0, p1 ->
            p1?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {
                    val i = Intent(activity, MapActivity::class.java)
                    i.putExtra("index", 0)
                    startActivity(i)
                }
            }
            false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(p0: Location) {
        Log.i("xxxxxxH", "onLocationChanged1")
        LoadingDialogManager.get().close()
        if (location != null){
            if (TextUtils.isEmpty(location.getInputView().getEditTextContent())) {
                location.getInputView().setEditTextContent(
                    "${
                        MyLocationManager.get().formatDouble(p0.longitude)
                    },${MyLocationManager.get().formatDouble(p0.latitude)}"
                )
            }
        }
    }

    override fun confirmClick(content: String) {
        val i = Intent(activity, NetDetailsActivity::class.java)
        i.putExtra("isNew", true)
        i.putExtra("index", if (netInfoAdapter == null) 1 else setIndex() + 1)
        i.putExtra("count", content.toInt())
        requireActivity().startActivity(i)
    }

    private fun setNetInfoAdapter(data: ArrayList<NetDetailsEntity>?) {
        netInfoAdapter = NetInfoAdapter(R.layout.layout_item_netinfo, data)
        net_recycler.layoutManager = LinearLayoutManager(activity)
        net_recycler.adapter = netInfoAdapter
        netInfoAdapter!!.addChildClickViewIds(R.id.netInfoIv)
        netInfoAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            adapter.removeAt(position)
        }
        netInfoAdapter!!.setOnItemClickListener { adapter, view, position ->
            onItem = true
            val i = Intent(activity, NetDetailsActivity::class.java)
            i.putExtra("isNew", false)
            i.putExtra("data", adapter.data as ArrayList<NetDetailsEntity>)
            requireActivity().startActivity(i)
        }
    }

    private fun setIndex(): Int {
        val s =
            (netInfoAdapter!!.data[netInfoAdapter!!.itemCount - 1].netDetailsBoardName).toString()
        return if (TextUtils.isEmpty(s)) 0 else s.substring(s.length - 1).toInt()
    }


    private fun setImgAdapter(data: ArrayList<String>?) {
        imgAdapter = ImageAdapter(R.layout.layout_item_img, data)
        img_recycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        img_recycler.adapter = imgAdapter
        imgAdapter!!.addChildClickViewIds(R.id.item_del)
        imgAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            adapter.removeAt(position)
        }
    }

    override fun gallery(data: ArrayList<LocalMedia>) {
        if (imgAdapter == null) {
            setImgAdapter(PhotoManager.get().handle(data))
        } else {
            imgAdapter!!.addData(PhotoManager.get().handle(data))
        }
    }

    override fun camera(path: String) {
        val data = ArrayList<String>()
        data.add(path)
        if (imgAdapter == null) {
            setImgAdapter(data)
        } else {
            imgAdapter!!.addData(data)
        }
    }

    fun getData(): NetInfoEntityNew? {
        val nameStr = name.getInputView().getEditTextContent()
        if (TextUtils.isEmpty(nameStr)) return null
        val locationStr = locationTv?.text.toString()
        val roomStr = room.getInputView().getEditTextContent()
        val point = point.getInputView().getEditTextContent()
        val netInfo: ArrayList<NetDetailsEntity>? =
            if (netInfoAdapter == null) null else netInfoAdapter!!.data as ArrayList<NetDetailsEntity>
        val imgData: ArrayList<String>? =
            if (imgAdapter == null) null else imgAdapter!!.data as ArrayList<String>
        val customData: ArrayList<CustomFiledEntity>? =
            if (customRoot.childCount == 0) null else CustomFiledManager.get()
                .getCustomData(customRoot)
        return NetInfoEntityNew(nameStr, locationStr, roomStr, point, netInfo, imgData, customData)
    }

    fun setData(entityNew: NetInfoEntityNew) {
        name.getInputView().setEditTextContent(entityNew.name)
        locationTv?.setText(entityNew.location)
        room.getInputView().setEditTextContent(entityNew.room)
        point.getInputView().setEditTextContent(entityNew.point)
        val netInfo = entityNew.infos
        setNetInfoAdapter(netInfo)
        val imgData = entityNew.imsg
        setImgAdapter(imgData)
        val customData = entityNew.customFiled
        customData?.let {
            CustomFiledManager.get().removeCustomItem(customRoot)
            for (item in it) {
                CustomFiledManager.get().addCustomItem(customRoot, item)
            }
        }
    }

    fun clear() {
        name.getInputView().setEditTextContent("")
        room.getInputView().setEditTextContent("")
        point.getInputView().setEditTextContent("")
        setNetInfoAdapter(null)
        setImgAdapter(null)
        CustomFiledManager.get().removeCustomItem(customRoot)
    }

    fun getCurAllData():ArrayList<NetInfoEntityNew>{
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("net")
        val netData = ArrayList<NetInfoEntityNew>()
        for (item in keySet!!){
            netData.add(MMKV.defaultMMKV()!!.decodeParcelable(item, NetInfoEntityNew::class.java)!!)
        }
        return netData
    }

    //列表转json
    fun convert2Json(data:ArrayList<NetInfoEntityNew>) :ArrayList<String>{
        val result = ArrayList<String>()
        for (item in data){
            result.add(Gson().toJson(item))
        }
        return result
    }

    override fun onPause() {
        super.onPause()
        FocusManager.get().clearFocus(customRoot)
        FocusManager.get().clearFocus(layoutContainer)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0] as String) {
            "netInfo" -> {
                val data = msg[1] as ArrayList<NetDetailsEntity>
                if (netInfoAdapter == null) {
                    setNetInfoAdapter(data)
                } else {
                    netInfoAdapter!!.addData(data)
                }
            }
            "dataChanged" -> {
                netInfoAdapter!!.setNewInstance(msg[1] as ArrayList<NetDetailsEntity>)
            }
            "filed" -> {
                val d = activity?.let { CustomDialog(it) }
                d!!.listener = this
                d.show()
            }
            "save" -> {
                val s = name.getInputView().getEditTextContent()
                if (!TextUtils.isEmpty(s)) {
                    MMKVUtils.saveKeys("net", s)
                    MMKV.defaultMMKV()!!.encode(s, getData())
//                    FileUtils.saveFile(activity, getData().toString(), "${s}.txt")
                } else {
                    ToastUtils.s(activity, "请输入网元名称")
                }
            }
            "nameSelect" -> {
                if (msg[1] == 0) {
                    val data = MMKV.defaultMMKV()!!
                        .decodeParcelable(msg[2] as String, NetInfoEntityNew::class.java)
                    data?.let {
                        setData(it)
                    }
                }
            }
            "delete" -> {
                MMKV.defaultMMKV()!!.remove(name.getInputView().getEditTextContent())
                MMKVUtils.deleteKey(name.getInputView().getEditTextContent(), "net")
                clear()
                FileUtils.deleteFile(activity, name.getInputView().getEditTextContent())
            }
            "add" -> {
                val d = activity?.let { SaveDialog(it, 0) }
                d!!.show()
            }
            "saveCurrentData" -> {
                if (msg[1] == 0) {
                    val s = name.getInputView().getEditTextContent()
                    if (!TextUtils.isEmpty(s)) {
                        MMKVUtils.saveKeys("net", s)
                        MMKV.defaultMMKV()!!.encode(s, getData())
                        clear()
                    } else {
                        ToastUtils.s(activity, "请输入网元名称")
                    }
                }
            }
            "submit" -> {
                FileUtils.saveFile(activity, getCurAllData().toString(), "${Constant.tab[0]}.txt")
                ClipUtils.copy(requireActivity(),Constant.FILE_PATH)
                val intent: Intent? = requireActivity().packageManager.getLaunchIntentForPackage(Constant.E_MAIL_PACKAGE_NAME)
                if (intent != null) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
            "send" -> {
                activity?.let { LoadingDialogManager.get().show(it) }
                val data = DataHandleManager.get().handleData0(
                    DataHandleManager.get()
                        .getCurAllData0(name.getInputView().getEditTextContent(), getData()!!)
                )

                Thread {
                    val result = EmailUtil.autoSendMail(
                        (activity as MainActivity).getThemeText(0),
                        data.toString(),
                        msg[1] as String,
                        UsefulSTMP.QQ,
                        Constant.from,
                        Constant.pwd,
                        null
                    )
                    EventBus.getDefault().post(MessageEvent("result", result, 0))
                }.start()
            }
            "result" -> {
                LoadingDialogManager.get().close()
                if (msg[1] as Boolean && msg[2] == 0) {
                    DataHandleManager.get().deleteData("net")
                    clear()
                    ToastUtils.s(activity, "发送成功")
                } else {
                    ToastUtils.s(activity, "发送失败")
                }
            }
            "map" -> {
                if (msg[1] == 0) {
                    location.getInputView().setEditTextContent("${msg[2]},${msg[3]}")
                }
            }
        }
    }
}