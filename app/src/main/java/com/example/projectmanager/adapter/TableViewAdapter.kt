package com.example.projectmanager.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.example.projectmanager.R
import com.example.projectmanager.model.Project
import kotlinx.android.synthetic.main.table_list_item.view.*

class TableViewAdapter(private val projectList: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<TableViewAdapter.RowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.table_list_item, parent, false)
        return RowViewHolder(itemView)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val rowPos = holder.adapterPosition

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            holder.itemView.apply {
                setHeaderBg(ProjectName)
                setHeaderBg(Status)

                ProjectName.text = "ProjectName"
                Status.text = "Status"
            }
        } else {
            val modal = projectList[rowPos - 1]

            holder.itemView.apply {
                setContentBg(ProjectName)
                setContentBg(Status)

                ProjectName.text = modal.get("name")
                Status.text = modal.get("status")
            }
        }
    }

    override fun getItemCount(): Int {
        return projectList.size + 1 // one more to add header row
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}