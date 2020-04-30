package com.food.android.app.ui.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.food.android.app.App
import com.food.android.app.R
import com.food.android.app.adapters.HomeRecyclerAdapter
import com.food.android.app.models.Item
import com.food.android.app.ui.activities.AddPostActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_find.*

import android.app.SearchManager
import android.R.menu
import android.content.Context
import android.content.SharedPreferences
import android.view.*
import android.widget.Toast

/**
 * Created by Aleesha Kanwal on 2/2/2020.
 */

class CommunityFragment : Fragment() {


    private var searchView: SearchView? = null

    private var queryTextListener: SearchView.OnQueryTextListener? = null

    private  lateinit var recyclerAdapter: HomeRecyclerAdapter

    private lateinit var gridLayoutManager: RecyclerView.LayoutManager

    internal lateinit var databaseReference: DatabaseReference

    internal lateinit var mDatabase: FirebaseDatabase

    internal lateinit var progressDialog: ProgressDialog

    internal var list: ArrayList<Item> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_find, container, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupSpinner()
    }

    private fun setup() {

        progressDialog = ProgressDialog(context)

        mDatabase = FirebaseDatabase.getInstance()

        databaseReference = mDatabase.getReference(AddPostActivity.Database_Path)

        if(!com.food.android.app.utils.SharedPreferences.isFirstTimeList()) {
            progressDialog.setCancelable(false)
            progressDialog.setTitle(context?.getString(R.string.please_wait_title))
            progressDialog.setMessage(context?.getString(R.string.loader_message))
            progressDialog.show()
            com.food.android.app.utils.SharedPreferences.setFirstTimeList(true)
        }

        getItems("ALL")
    }


    private fun getItems(comunity: String) {

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list = ArrayList()

                if (snapshot.hasChildren()) {
                    for (dataSnapshot in snapshot.children) {

                        val studentDetails = dataSnapshot.getValue(Item::class.java)

                        if (studentDetails?.community.equals(comunity) || comunity.equals("ALL")) {
                            list.add(studentDetails!!)
                        }

                    }

                    if(list.isNotEmpty()) {
                        recyclerAdapter = HomeRecyclerAdapter(list, context)
                        gridLayoutManager = GridLayoutManager(context, 2)
                        recyclerview?.layoutManager = gridLayoutManager
                        recyclerview?.adapter = recyclerAdapter
                    }
                }

                progressDialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                progressDialog.dismiss()

            }
        })
    }





    private fun setupSpinner() {

        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item,  resources.getStringArray(R.array.all_community))
        dataAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item)
        spinner.adapter = dataAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                System.out.println("POSITION" + p2)
                val text = spinner.selectedItem.toString()
                getItems(text);
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)

        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
            searchView?.setMaxWidth(Integer.MAX_VALUE);
        }
        if (searchView != null) {
            searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                   // Log.i("onQueryTextChange", newText)

                    recyclerAdapter.getFilter().filter(newText)

                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                  //  Log.i("onQueryTextSubmit", query)
                    if (!searchView!!.isIconified()) {
                        searchView?.onActionViewCollapsed()
                    }
                    return true
                }

            }

            searchView?.setQueryHint("Searh by name");
            searchView?.setIconified(true)

            searchView?.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_search ->
                // Not implemented here
                return false
            else -> {
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

}