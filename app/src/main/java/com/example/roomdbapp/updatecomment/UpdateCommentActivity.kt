package com.example.roomdbapp.updatecomment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.roomdbapp.R
import com.example.roomdbapp.data.models.Comment
import com.example.roomdbapp.utils.Constants
import com.example.roomdbapp.data.viewmodel.CommentListViewModel
import com.example.roomdbapp.databinding.ActivityUpdateCommentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateCommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateCommentBinding
    private lateinit var commentModel : Comment
    private val listFragmentViewModel: CommentListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateCommentBinding.inflate(layoutInflater)
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
        commentModel = intent.getSerializableExtra("COMMENT")  as Comment

        binding.edtFirstName.setText(commentModel.body.toString())
        binding.btnSubmit.setOnClickListener {
            callApi()
        }
        observeData()
    }
    private fun callApi() {
        var comment = binding.edtFirstName.text.toString().trim()
        var id = commentModel.id
        listFragmentViewModel.updateComments(this,comment,id)
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            listFragmentViewModel.updateCommentState.collect { users ->
                // Update the RecyclerView with the latest list of users
                when(users.status) {
                    Constants.Status.IDLE -> {
                        binding.progress.visibility = View.GONE
                    }
                    Constants.Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                    }

                    Constants.Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        Log.e("ArrayList", users.toString())
                        if (users != null) {
                            Toast.makeText(
                                this@UpdateCommentActivity,
                                "Comment Updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                    Constants.Status.ERROR -> {
                        binding.progress.visibility = View.GONE
                        Log.e("error", users.message.toString())
                        Toast.makeText(this@UpdateCommentActivity, "${users.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}