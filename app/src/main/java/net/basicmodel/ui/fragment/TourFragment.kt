package net.basicmodel.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.layout_fragment_tour.*
import net.basicmodel.R
import net.basicmodel.adapter.ImageAdapter
import net.basicmodel.base.BaseFragment
import net.basicmodel.entity.CustomFiledEntity
import net.basicmodel.entity.ResultEntity
import net.basicmodel.entity.TourEntityNew
import net.basicmodel.event.MessageEvent
import net.basicmodel.popup.MessagePopupWindow
import net.basicmodel.sendmail.EmailUtil
import net.basicmodel.ui.activity.MainActivity
import net.basicmodel.ui.activity.MapActivity
import net.basicmodel.utils.*
import net.basicmodel.widget.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class TourFragment : BaseFragment(), OnOptionClickListener, LocationListener, PhotoCallBack,
    TimeSelectListener, GeocodeSearch.OnGeocodeSearchListener {
    var imgAdapter: ImageAdapter? = null

    var geocoderSearch: GeocodeSearch? = null
    private var flatTime: Long = -1L
    private var flatType: Int = -1

    override fun initView() {

        EventBus.getDefault().register(this)
        activity?.let {
            if (Constant.isOPen(it)) {
                MyLocationManager.get().getLocation(it, this)
            }
        }
        initContainer()
        initClick()

        geocoderSearch = GeocodeSearch(requireActivity())
        geocoderSearch!!.setOnGeocodeSearchListener(this)

        choose_filter.setOnClickListener {
            MessagePopupWindow.MessagePopupWindowBuilder.init(requireActivity())
                .setEnsureListener { type ->
                    when (type) {
                        0 -> {
                            choose_filter.text = "失败"
                            flatType = 0
                        }
                        1 -> {
                            choose_filter.text = "成功"
                            flatType = 1
                        }
                        -1 -> {
                            choose_filter.text = "全部"
                            flatType = -1
                        }
                    }
                }
                .build()
                .show(it)
        }

        choose_time.setOnClickListener {
            TimePickerManager.get().createTimePicker(requireActivity(), "t", this).show()
        }

        reset.setOnClickListener {
            flatTime = -1L
            flatType = -1
            choose_time.text = "选择时间"
            choose_filter.text = "选择类型"
        }
        rg_select_std.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_std_tool -> {

                }
                R.id.rb_other -> {

                }
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.layout_fragment_tour
    }

    private fun initContainer() {
        tourID.getInputView().setHint("桩号")
        tourID.getOptionView().setImageResource(R.mipmap.arrow_down)
        tourID.setTag("tourDrop")
        tourID.setListener(this)
//        tourCode.setHint("桩号")
        tourPoint.setHint("站点")
        tourPort.setHint("通道号")
        tourStartDistance.setHint("开始距离")
        tourEndDistance.setHint("结束距离")

        tourLocation.getInputView().setHint("经纬度")
        tourLocation.getOptionView().setImageResource(R.mipmap.activity_main_refresh_icon)
        tourLocation.setTag("tourRefresh")
        tourLocation.setListener(this)

        //-------------------------------新增开始时间 结束时间------------------------------------------
        tourStartTime.getInputView().setHint("开始时间")
        //val startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
        //tourStartTime.getInputView().setEditTextContent(startTime)
        tourStartTime.getOptionView().setImageResource(R.mipmap.start_btn)
        tourStartTime.setTag("start")
        tourStartTime.setListener(this)

        tourEndTime.getInputView().setHint("结束时间")
        //val endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
        //tourEndTime.getInputView().setEditTextContent(endTime)
        tourEndTime.getOptionView().setImageResource(R.mipmap.end_btn)
        tourEndTime.setTag("end")
        tourEndTime.setListener(this)

        tourStartTime.getOptionView().visibility = View.VISIBLE;
        tourEndTime.getOptionView().visibility = View.INVISIBLE;
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClick() {
        img_add.setOnClickListener {
            SelectDialog(requireActivity(), requireActivity(), this).show()
        }
        tourLocation.getInputView().getEditTextView().setOnTouchListener { p0, p1 ->
            p1?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {
                    val i = Intent(activity, MapActivity::class.java)
                    i.putExtra("index", 2)
                    startActivity(i)
                }
            }
            false
        }
        //-------------------------------新增开始时间 结束时间 点击事件-----------------------------------
        tourStartTime.getInputView().getEditTextView().setOnTouchListener { _, event ->
            event?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {

                    TimePickerManager.get().createTimePicker(requireActivity(), "s", this).show()

                }
            }
            false
        }
        tourEndTime.getInputView().getEditTextView().setOnTouchListener { _, event ->
            event?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {

                    TimePickerManager.get().createTimePicker(requireActivity(), "e", this).show()
                }
            }
            false
        }
    }

    override fun onClick(vararg tag: String) {
        when (tag[0]) {
            "tourDrop" -> {
                val data = MMKVUtils.getKeys("tour")
                if (data == null || data.size == 0) {
                    ToastUtils.s(activity, "暂无数据")
                } else {
                    val d = activity?.let { NameDialog(it, data, 2, flatType, flatTime) }
                    d!!.show()
                }
            }
            "tourRefresh" -> {
                val s = tourLocation.getInputView().getEditTextContent()
                if (TextUtils.isEmpty(s)) return
                val lat = s.split(",")[1].toDouble()
                val lgt = s.split(",")[0].toDouble()
                val query = RegeocodeQuery(
                    LatLonPoint(lat, lgt),
                    200F, GeocodeSearch.AMAP
                )
                geocoderSearch!!.getFromLocationAsyn(query)
                activity?.let { LoadingDialogManager.get().show(it) }
                tourLocation.getInputView().setEditTextContent("")
            }
            "customOk" -> {
                val entity = CustomFiledEntity()
                entity.name = tag[1]
                entity.content = tag[2]
                CustomFiledManager.get().addCustomItem(tourCustomRoot, entity)
            }
            //------------------新增开始时间结束时间 刷新按钮操作---------------------
            "start" -> {
                val startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
                tourStartTime.getInputView().setEditTextContent(startTime)
                tourStartTime.getOptionView().visibility = View.INVISIBLE;
                tourEndTime.getOptionView().visibility = View.VISIBLE;
            }
            "end" -> {
                val endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
                tourEndTime.getInputView().setEditTextContent(endTime)
                tourStartTime.getOptionView().visibility = View.VISIBLE;
                tourEndTime.getOptionView().visibility = View.INVISIBLE;
            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        LoadingDialogManager.get().close()
        val lot = MyLocationManager.get().formatDouble(p0.longitude)
        val lat = MyLocationManager.get().formatDouble(p0.latitude)
        if (tourLocation != null && tourLocation.getInputView().getEditTextContent().isEmpty()) {
            tourLocation.getInputView().setEditTextContent(
                "${lot},${lat}"
            )
        }
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

    fun getData(): TourEntityNew? {
        val idStr = tourID.getInputView().getEditTextContent()
        if (!TextUtils.isEmpty(idStr)) {
            val codeStr = tourCode.getEditTextContent()
            val pointStr = tourPoint.getEditTextContent()
            val portStr = tourPort.getEditTextContent()
            val startDistance = tourStartDistance.getEditTextContent()
            val endDistance = tourEndDistance.getEditTextContent()
            val locationStr = tourLocation.getInputView().getEditTextContent()
            //--------------------------------新增开始时间 结束时间-----------------------------------------
            val start = tourStartTime.getInputView().getEditTextContent()
            val end = tourEndTime.getInputView().getEditTextContent()
            var std_tool = ""
            if (rb_std_tool.isChecked) {
                std_tool = "std_tool"
            } else {
                std_tool = "other"
            }


            val imgData: ArrayList<String>? =
                if (imgAdapter == null) null else imgAdapter!!.data as ArrayList<String>
            val customData: ArrayList<CustomFiledEntity>? =
                if (tourCustomRoot.childCount == 0) null else CustomFiledManager.get()
                    .getCustomData(tourCustomRoot)
            val smsContent = tagView.tag as String?
            val splitStr1 = smsContent?.split(",")
            val splitStr2 = splitStr1?.get(1)?.split(":")
            var id: String? = null
            splitStr2?.get(0)?.let {
                if ((it == "光纤ID") || (it == "fiberID")) {
                    id = splitStr2[1]
                }
            }

            return TourEntityNew(
                idStr,
                codeStr,
                pointStr,
                portStr,
                startDistance,
                endDistance,
                locationStr,
                imgData,
                customData,
                start,
                end,
                smsContent = smsContent,
                event_related_id = id,
                nettour_tool = std_tool
            )
        }
        return null
    }

    fun setData(entityNew: TourEntityNew) {
        tourID.getInputView().setEditTextContent(entityNew.nettour_stake)
        tourCode.setEditTextContent(entityNew.nettour_id)
        tourPoint.setEditTextContent(entityNew.nettour_point)
        tourPort.setEditTextContent(entityNew.nettour_port)
        tourStartDistance.setEditTextContent(entityNew.nettour_start_distance)
        tourEndDistance.setEditTextContent(entityNew.nettour_end_distance)
        //--------------------------------新增开始时间 结束时间-----------------------------------------
        tourStartTime.getInputView().setEditTextContent(entityNew.nettour_start_time)
        tourEndTime.getInputView().setEditTextContent(entityNew.nettour_end_time)
        tourLocation.getInputView().setEditTextContent(entityNew.nettour_lat_lng)
        if (entityNew.nettour_tool.equals("std_tool")) {
            rb_std_tool.isChecked = true
        } else {
            rb_other.isChecked = true
        }
        setImgAdapter(entityNew.imgData)
        CustomFiledManager.get().removeCustomItem(tourCustomRoot)
        entityNew.customData?.let {
            CustomFiledManager.get().removeCustomItem(tourCustomRoot)
            for (item in it) {
                CustomFiledManager.get().addCustomItem(tourCustomRoot, item)
            }
        }
        tagView.tag = entityNew.smsContent
    }

    fun getCurAllData(): ArrayList<TourEntityNew> {
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("tour")
        val tourData = ArrayList<TourEntityNew>()
        if (keySet != null) {
            for (item in keySet) {
                tourData.add(
                    MMKV.defaultMMKV()!!.decodeParcelable(item, TourEntityNew::class.java)!!
                )
            }
        }
        return tourData
    }

    //列表转json
    fun convert2Json(data: ArrayList<TourEntityNew>?): ArrayList<String> {
        val result = ArrayList<String>()
        if (data != null) {
            for (item in data) {
                result.add(Gson().toJson(item))
            }
        }

        return result
    }

    fun clear() {
        tourID.getInputView().setEditTextContent("")
        tourCode.setEditTextContent("")
        tourPoint.setEditTextContent("")
        tourPort.setEditTextContent("")
        tourStartDistance.setEditTextContent("")
        tourEndDistance.setEditTextContent("")

        val startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
        tourStartTime.getInputView().setEditTextContent(startTime)
        tourEndTime.getInputView().setHint("结束时间")
        val endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())

        tourEndTime.getInputView().setEditTextContent(endTime)
        setImgAdapter(null)
        CustomFiledManager.get().removeCustomItem(tourCustomRoot)
    }

    override fun onPause() {
        super.onPause()
        FocusManager.get().clearFocus(tourCustomRoot)
        FocusManager.get().clearFocus(tourCustomRoot)
        val s = tourID.getInputView().getEditTextContent()
        if (!TextUtils.isEmpty(s)) {
            MMKVUtils.saveKeys("tour", s)
            MMKV.defaultMMKV()!!.encode(s, getData())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0] as String) {
            "filed2" -> {
                val d = activity?.let { CustomDialog(it) }
                d!!.listener = this
                d.show()
            }
            "add2" -> {
                val s = tourID.getInputView().getEditTextContent()
                if (TextUtils.isEmpty(s)) {
                    EventBus.getDefault().post(MessageEvent("dontsave", 0))
                } else {
                    val d = activity?.let { SaveDialog(it, 2) }
                    d!!.show()
                }

            }
            "delete2" -> {
                val s = tourID.getInputView().getEditTextContent()
                if (!TextUtils.isEmpty(s)) {
                    MMKV.defaultMMKV()!!.remove(s)
                    MMKVUtils.deleteKey(s, "tour")
                    clear()
                    FileUtils.deleteFile(activity, s)
                }
            }
            "save2" -> {
                activity?.apply {
                    tagView.tag = null
                    getTodayAllSMS { hasSMS, list ->//拿到今天所有的短信，已经筛选过关键字了
                        if (hasSMS) {
                            list.map { sms ->
                                sms.body.dateRule()?.let { //拿到短信中的开始时间和结束时间
                                    val start =
                                        tourStartTime.getInputView().getEditTextContent()
                                    val end = tourEndTime.getInputView().getEditTextContent()
                                    val overtime = getTimeInterval(
                                        start.getTimeToLong(),
                                        end.getTimeToLong(),
                                        it[0].getTimeToLong2(),
                                        it[1].getTimeToLong2()
                                    )
                                    if (overtime >= 6 * 60 * 1000)//重叠时间大于6分钟
                                    {
                                        sms.tag = 1
                                        tagView.tag = Gson().toJson(sms)
                                    }
                                }
                            }
                        }
                    }
                }
                val starttime = tourStartTime.getInputView().getEditTextContent()
                if (TextUtils.isEmpty(starttime)) {
                    ToastUtils.s(activity, "请填写开始时间")
                    return
                }
                val endtime = tourEndTime.getInputView().getEditTextContent()
                if (TextUtils.isEmpty(endtime)) {
                    ToastUtils.s(activity, "请填写结束时间")
                    return
                }

                val overtime = endtime.getTimeToLong() - starttime.getTimeToLong()
                if (overtime < 10 * 1000) {
                    ToastUtils.s(activity, "开始时间和结束时间间隔需要大于10秒")
                    return
                }

                val s = tourID.getInputView().getEditTextContent()
                if (!TextUtils.isEmpty(s)) {
                    MMKVUtils.saveKeys("tour", s)
                    MMKV.defaultMMKV()!!.encode(s, getData())
                    //                    FileUtils.saveFile(activity, getData().toString(), "${s}.txt")
                    ToastUtils.s(activity, "保存成功")
                    tagView.tag = null //重置tag
                    clear()
                } else {
                    ToastUtils.s(activity, "请填写桩号")
                }
            }
            "submit2" -> {
                val allData = getCurAllData()
                if (allData.size == 0) {
                    ToastUtils.s(activity, "没有数据可以提交")
                    return
                }
                val content = convert2Json(allData).toString()

                val userName = MMKV.defaultMMKV()!!.decodeString("user", "")
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(requireActivity(), "请配置用户名", Toast.LENGTH_SHORT).show()
                    return
                }


                TimePickerManager.get().createTimePicker2(requireActivity(), "export", this).show()

//            不再拉起微信
//                ClipUtils.copy(requireActivity(), Constant.FILE_PATH)
//                val intent: Intent? =
//                    requireActivity().packageManager.getLaunchIntentForPackage(Constant.E_MAIL_PACKAGE_NAME)
//                if (intent != null) {
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                }
            }
            "submitAll" -> {
                val allData = getCurAllData()
                if (allData.size == 0) {
                    ToastUtils.s(activity, "没有数据可以提交")
                    return
                }
                val content = convert2Json(allData).toString()

                val userName = MMKV.defaultMMKV()!!.decodeString("user", "")
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(requireActivity(), "请配置用户名", Toast.LENGTH_SHORT).show()
                    return
                }
                val list = DataHandleManager.get()
                    .getCurAllData2(tourCode.getEditTextContent(), getData())
                val resultEntity = Gson().toJson(ResultEntity(userName!!, list))
                FileUtils.saveFile(requireActivity(), resultEntity, "${Constant.tab[0]}.json")
            }
            "send" -> {
                val userName = MMKV.defaultMMKV()!!.decodeString("user", "")
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(requireActivity(), "请配置用户名", Toast.LENGTH_SHORT).show()
                    return
                }
                activity?.let { LoadingDialogManager.get().show(it) }
                val list = DataHandleManager.get()
                    .getCurAllData2(tourCode.getEditTextContent(), getData())
                val data = DataHandleManager.get().handleData2(list)

                val user = MMKV.defaultMMKV()!!.decodeString("mailUser")
                val pwd = MMKV.defaultMMKV()!!.decodeString("mailPwd")

                val resultEntity = Gson().toJson(ResultEntity(userName!!, list))

                val host = MMKV.defaultMMKV()!!.decodeString("host", "")

                if (TextUtils.isEmpty(host)) {
                    ToastUtils.s(activity, "请配置邮箱")
                    return
                }

                var result = false
                val files = ArrayList<String>()
                val zipPath = Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "imgs.zip"
                try {
                    var data = ArrayList<String>()
                    data = getAllImgs(list)
                    if (data.size != 0) {
                        result = ZipUtils.zipFiles(data, zipPath)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i("TAG", "压缩出错");
                }

                if (result) {
                    files.add(zipPath)
                }

                val df = SimpleDateFormat("yyyyMMddHHmmss") //设置日期格式


                val fileName = "Nettour_" + df.format(Date()) + ".json"

                val filePath =
                    Environment.getExternalStorageDirectory().toString() + File.separator + fileName

                try {
                    FileUtils.writeTxt2File(
                        resultEntity,
                        Environment.getExternalStorageDirectory().toString() + "",
                        fileName
                    )
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), "写入文件时出错", Toast.LENGTH_SHORT).show()
                    return
                }
                val file = File(filePath)

                if (file.exists()) {
                    files.add(filePath)
                }

                thread {
                    val result = EmailUtil.autoSendMail(
                        (activity as MainActivity).getThemeText(2),
                        resultEntity.toString(),
                        (msg[1] as String).trim(),
                        host,
                        user!!.trim(),
                        pwd!!.trim(),
                        files.toTypedArray()
                    )
                    EventBus.getDefault().post(MessageEvent("result", result, 2))
                }
            }
            "result" -> {
                LoadingDialogManager.get().close()
                if (msg[1] as Boolean && msg[2] == 2) {
                    DataHandleManager.get().deleteData("tour")
                    clear()
                    ToastUtils.s(activity, "发送成功")
                } else {
                    ToastUtils.s(activity, "发送失败")
                }
            }
            "nameSelect" -> {
                if (msg[1] == 2) {
                    val data = MMKV.defaultMMKV()!!
                        .decodeParcelable(msg[2] as String, TourEntityNew::class.java)
                    data?.let {
                        setData(it)
                    }
                }
            }
            "saveCurrentData" -> {
                val s = tourID.getInputView().getEditTextContent()
                if (!TextUtils.isEmpty(s)) {
                    MMKVUtils.saveKeys("tour", s)
                    MMKV.defaultMMKV()!!.encode(s, getData())
                    clear()
                }
            }
            "dontsave" -> {
                clear()
            }
            "map" -> {
                if (msg[1] == 2) {
                    tourLocation.getInputView().setEditTextContent("${msg[2]},${msg[3]}")
                }
            }
            "sendWithEmail" -> {
                val data = DataHandleManager.get().handleData2(
                    DataHandleManager.get()
                        .getCurAllData2(tourCode.getEditTextContent(), getData())
                )
                if (data.size == 0) {
                    ToastUtils.s(requireActivity(), "没有任何可提交的数据")
                    return
                }
                val user = MMKV.defaultMMKV()!!.decodeString("mailUser")
                val pwd = MMKV.defaultMMKV()!!.decodeString("mailPwd")
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
                    ToastUtils.s(requireActivity(), "请配置邮箱")
                    EventBus.getDefault().post(MessageEvent("configEmail"))
                } else {
                    val d = AddressDialog(requireActivity(), 0)
                    d.show()
                }
            }
            "configEmail" -> {
                EmailConfigDialog(requireActivity()).show()
            }
            "address" -> {
                if (!TextUtils.isEmpty(msg[1].toString())) {
                    tourPoint.setEditTextContent(msg[1].toString())
                }
            }
            "closeInput" -> {
                val im: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(
                    requireActivity().currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            "configUser" -> {
                val userDialog = UserNameConfigDialog(requireActivity())
                userDialog.show()
            }
        }
    }

    override fun timeSelect(flag: String, time: String) {
        when (flag) {
            "s" -> {
                tourStartTime.getInputView().setEditTextContent(time)
            }
            "e" -> {
                tourEndTime.getInputView().setEditTextContent(time)
            }
            "t" -> {
                choose_time.text = time
                flatTime = time.getTimeToLong()
            }
            "export" -> {
                Log.e("timeSelect", time)
                val userName = MMKV.defaultMMKV()!!.decodeString("user", "")
                val list = DataHandleManager.get()
                    .getCurAllData2(tourCode.getEditTextContent(), getData())

                val resultList = ArrayList<TourEntityNew>()
                list.map {
                    val startTime = it.nettour_start_time.split(" ")[0]
                    if (startTime == time) {
                        resultList.add(it)
                    }
                }
                if (resultList.size == 0) {
                    Toast.makeText(activity, "没有符合条件的数据", Toast.LENGTH_SHORT).show()
                } else {
                    val resultEntity = Gson().toJson(ResultEntity(userName!!, resultList))
                    FileUtils.saveFile(requireActivity(), resultEntity, "${Constant.tab[0]}.json")
                }
            }
        }
    }

    override fun dialogDismiss() {

    }

    override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
        p0?.let {
            if (!TextUtils.isEmpty(it.regeocodeAddress.formatAddress)) {
                tourPoint.setEditTextContent(it.regeocodeAddress.formatAddress)
            }
        }

    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

    }

    fun getTimeInterval(stime1: Long, etime1: Long, stime2: Long, etime2: Long): Float {
        var f = 0f
        val lst = stime1
        val let = etime1
        val rst = stime2
        val ret = etime2
        if (lst > let || rst > ret) {
            throw Exception("起始时间不能大于结束时间")
        }
        if (let <= rst || lst >= ret) {
            return f
        }
        val a = longArrayOf(lst, let, rst, ret)
        Arrays.sort(a) //从小到大排序，取第二、第三计算
        f = (a[2] - a[1]).toFloat()

        return f
    }

    private fun getAllImgs(entity: ArrayList<TourEntityNew>): ArrayList<String> {
        val result = ArrayList<String>()
        entity.forEach {
            it.imgData?.let { all ->
                result.addAll(all)
            }
        }
        return result
    }


}