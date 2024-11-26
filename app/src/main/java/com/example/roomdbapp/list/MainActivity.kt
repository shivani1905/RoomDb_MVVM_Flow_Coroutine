package com.example.roomdbapp.list

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdbapp.updatecomment.UpdateCommentActivity
import com.example.roomdbapp.R
import com.example.roomdbapp.data.models.Comment
import com.example.roomdbapp.databinding.ActivityMainBinding
import com.example.roomdbapp.utils.Constants
import com.example.roomdbapp.utils.SwipeHelper
import com.example.roomdbapp.data.viewmodel.CommentListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mArrayList: ArrayList<Comment>
    lateinit var mAdapter : CommentListAdapter
    val listFragmentViewModel: CommentListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindView()
        setContentView(binding.root)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bindView() {
        binding.apply {
            recycler.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
        observeData()
        observeDeleteComment()
        callApi()
        mArrayList = ArrayList()
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }
        }
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recycler) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val deleteButton = deleteButton(position)
                return listOf(deleteButton)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    if(position!= -1){
                        callDeleteComment(mArrayList[position].id)
                        Log.e("Position",mArrayList[position].user.username)
                        Log.e("item",position.toString())
                    mArrayList.removeAt(position)
                    mAdapter.notifyItemRemoved(position)
                    }
                }
            })
    }

    private fun callApi() {
        listFragmentViewModel.fetchComments(this)
    }

    private fun callDeleteComment(id: Int) {
        listFragmentViewModel.deleteUserById(id)
    }

    private fun observeData() {
        lifecycleScope.launch {
            listFragmentViewModel.commentState.collect { users ->
                when (users.status) {
                    Constants.Status.IDLE -> {
                        binding.progress.visibility = View.GONE
                    }
                    Constants.Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                    }

                    Constants.Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        // Update the RecyclerView with the latest list of users
                        Log.e("ArrayList", users.toString())
                        users.data?.let { mArrayList.addAll(it) }
                        mAdapter = CommentListAdapter(mArrayList,
                            onItemClickListener = { it ->
                                var intent =  Intent(
                                    this@MainActivity,
                                    UpdateCommentActivity::class.java
                                )
                                intent.putExtra("COMMENT",it)
                                startActivity(
                                    intent
                                )
                                finish()
                            },
                            onItemLongClickListener = {
                                lifecycleScope.launch {
                                }
                            }
                        )
                        binding.recycler.adapter = mAdapter
                        mAdapter.notifyDataSetChanged()
                    }
                    Constants.Status.ERROR -> {
                        binding.progress.visibility = View.GONE
                        Log.e("error",users.message.toString())
                        Toast.makeText(this@MainActivity,"${users.message}",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeDeleteComment() {
        lifecycleScope.launch {
            listFragmentViewModel.deleteCommentState.collect { users ->
                when (users.status) {
                    Constants.Status.IDLE -> {
                        binding.progress.visibility = View.GONE
                    }
                    Constants.Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                    }

                    Constants.Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        Toast.makeText(this@MainActivity,"User deleted successfully",Toast.LENGTH_SHORT).show()
                    }
                    Constants.Status.ERROR -> {
                        binding.progress.visibility = View.GONE
                        Log.e("error",users.message.toString())
                        Toast.makeText(this@MainActivity,"${users.message}",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    fun openMap(latitude: Double, longitude: Double) {
        // Create a URI for the latitude and longitude
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        // Create an Intent to open Google Maps
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps") // Optional: Ensures it opens in Google Maps app

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Handle case where Google Maps app is not available
            Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlertDialog(lat: Double, lng: Double) {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.apply {
            //setIcon(R.drawable.ic_hello)
            setTitle(getString(R.string.app_name))
            setMessage("User want to see that user on map")
            setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
               openMap(lat,lng)
            }
            setNegativeButton("No") { _, _ ->
                Toast.makeText(context, "Negative", Toast.LENGTH_SHORT).show()
            }

        }.create().show()
    }
}