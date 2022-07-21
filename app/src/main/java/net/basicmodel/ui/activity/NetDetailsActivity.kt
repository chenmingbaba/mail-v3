package net.basicmodel.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.layout_activity_net.*
import net.basicmodel.R
import net.basicmodel.adapter.NetDetailsAdapter
import net.basicmodel.base.BaseActivity
import net.basicmodel.entity.NetDetailsEntity
import net.basicmodel.event.MessageEvent
import org.greenrobot.eventbus.EventBus

class NetDetailsActivity : BaseActivity() {

    var isNew: Boolean = false
    var index: Int = 0
    var count: Int = 0
    var data: ArrayList<NetDetailsEntity>? = null
    var netDetailsAdapter: NetDetailsAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.layout_activity_net
    }

    override fun initView() {
        val i = intent
        isNew = i.getBooleanExtra("isNew", false)
        count = i.getIntExtra("count", 1)
        index = i.getIntExtra("index", 1)
        data = i.getParcelableArrayListExtra("data")
        if (data == null) {
            data = ArrayList()
            for (ii in 0 until count) {
                val entity = NetDetailsEntity("", "", "", index)
                data!!.add(entity)
                index++
            }
            netDetailsAdapter = NetDetailsAdapter(R.layout.layout_item_net, data!!, true)
        } else {
            netDetailsAdapter = NetDetailsAdapter(R.layout.layout_item_net, data!!, false)
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = netDetailsAdapter
        initClick()
    }

    private fun initClick() {
        cancel.setOnClickListener { finish() }
        confirm.setOnClickListener {
            if (isNew) {
                EventBus.getDefault().post(MessageEvent("netInfo", netDetailsAdapter!!.data))
            } else {
                EventBus.getDefault().post(MessageEvent("dataChanged", netDetailsAdapter!!.data))
            }
            finish()
        }
    }


}