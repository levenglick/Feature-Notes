package bez.dev.featurenotes.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bez.dev.featurenotes.R
import kotlinx.android.synthetic.main.detail_activity_list_item.view.*

class DetailListAdapter internal constructor(myListener: OnDetailItemClickListener, myItemTouchHelper: ItemTouchHelper, editMode: Boolean) : ListAdapter<String, DetailListAdapter.DetailItemHolder>(DIFF_CALLBACK) {

    private var mIsEditMode: Boolean = editMode
    private var touchHelper: ItemTouchHelper = myItemTouchHelper
    private var listener: OnDetailItemClickListener = myListener


    internal fun getDetailItemAt(position: Int): String {
        return getItem(position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DetailItemHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.detail_activity_list_item, viewGroup, false)
        return DetailItemHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(detailItemHolder: DetailItemHolder, position: Int) {
        val currentNote = getDetailItemAt(position)
        detailItemHolder.itemText.text = currentNote.trim()

        //do regardless of mode
        detailItemHolder.deleteItem.showOnEditMode(mIsEditMode)
        detailItemHolder.dragItem.showOnEditMode(mIsEditMode)

        detailItemHolder.itemText.setOnLongClickListener {
            val text = getDetailItemAt(position)
            listener.onDetailItemLongClick(text, position)
        }

        if (mIsEditMode) { //ONLY edit mode
            detailItemHolder.itemText.setTextColor(ContextCompat.getColor(listener as Context, R.color.black))

            detailItemHolder.itemText.setOnClickListener {
                val text = getDetailItemAt(position)
                listener.onDetailItemClick(text, position)
            }
            detailItemHolder.deleteItem.setOnClickListener {
                listener.onDeleteItemClick(position)
            }
            detailItemHolder.dragItem.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(detailItemHolder)
                }
                false
            }
        } else {  //NOT edit mode
            detailItemHolder.itemText.setTextColor(ContextCompat.getColor(listener as Context, R.color.gray))

            detailItemHolder.itemText.setOnClickListener { }

        }
    }

    inner class DetailItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val deleteItem: ImageView = itemView.detail_image_delete_item
        internal val itemText: TextView = itemView.detail_item_text
        internal val dragItem: ImageView = itemView.detail_image_drag_item
    }


    interface OnDetailItemClickListener {
        fun onDetailItemClick(text: String, position: Int)
        fun onDetailItemLongClick(text: String, position: Int): Boolean
        fun onDeleteItemClick(position: Int)
    }


    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return false
            }

            override fun areContentsTheSame(oldNote: String, newNote: String): Boolean {
                return false
            }
        }
    }


}

fun View.showOnEditMode(show: Boolean) {
    visibility = if (show) {
        View.VISIBLE
    } else {
        View.GONE
    }
}