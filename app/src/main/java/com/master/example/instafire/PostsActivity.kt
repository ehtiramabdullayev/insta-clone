package com.master.example.instafire

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.master.example.instafire.models.Post
import kotlinx.android.synthetic.main.activity_posts.*

private const val TAG = "PostsActivity"

class PostsActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostsAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        // create the layout file which represent one post - done
        // create data source
        posts = mutableListOf()
        // create adapter
        adapter = PostsAdapter(this, posts)
        // bind adapter and layout manager to the rv
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(this)

        firestore = FirebaseFirestore.getInstance()
        val postsReference = firestore
            .collection("posts")
            .limit(20)
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)

        postsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.i(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()

            for (post in postList) {
                Log.i(TAG, "Post ${post}")

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}