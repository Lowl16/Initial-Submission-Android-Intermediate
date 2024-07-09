package com.dicoding.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.preferences.UserPreferences
import com.dicoding.storyapp.data.preferences.dataStore
import com.dicoding.storyapp.data.response.ListStory
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.ui.adapter.StoryAdapter
import com.dicoding.storyapp.ui.viewmodel.StoryViewModel
import com.dicoding.storyapp.ui.viewmodel.StoryViewModelFactory
import com.dicoding.storyapp.ui.viewmodel.UserPreferencesViewModel
import com.dicoding.storyapp.ui.viewmodel.UserPreferencesViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyViewModelFactory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this@MainActivity)
        val storyViewModel: StoryViewModel by viewModels { storyViewModelFactory }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        storyViewModel.getStories()
        storyViewModel.stories.observe(this) { story ->
            setStoryData(story)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {

                val userPreferences: UserPreferences = UserPreferences.getInstance(application.dataStore)
                val userPreferencesViewModelFactory: UserPreferencesViewModelFactory = UserPreferencesViewModelFactory.getInstance(userPreferences)
                val userPreferencesViewModel: UserPreferencesViewModel by viewModels { userPreferencesViewModelFactory }

                val builder = AlertDialog.Builder(this)

                builder.setTitle(getString(R.string.logout))
                builder.setMessage(getString(R.string.logout_confirmation))

                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    userPreferencesViewModel.removeSession()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }

                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStoryData(story: List<ListStory>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        val adapter = StoryAdapter()
        adapter.submitList(story)
        binding.rvStory.adapter = adapter
    }
}