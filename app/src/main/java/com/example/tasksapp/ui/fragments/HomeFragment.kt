package com.example.tasksapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasksapp.data.local.TaskLocalStorage
import com.example.tasksapp.data.repository.TaskRepository
import com.example.tasksapp.ui.adapters.TaskAdapter
import com.example.tasksapp.ui.viewmodel.TaskDataViewModel
import com.example.tasksapp.ui.viewmodel.ViewModelFactory
import com.example.tasksapp.utils.AddTaskDialogManager
import com.example.testdatabinding.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskDataViewModel by viewModels {
        val repository = TaskRepository(TaskLocalStorage(requireContext()))
        ViewModelFactory(repository)
    }

    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        observeTasks()
    }


    private fun setupRecyclerView() {
        adapter = TaskAdapter(viewLifecycleOwner.lifecycleScope) { task ->
            Log.d("TaskClick", "Clicked task: ${task.title}")
        }

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }


    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }


    private fun observeTasks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasks.collectLatest { tasks ->
                    adapter.submitList(tasks)
                }
            }
        }
    }

    private fun showAddTaskDialog() {
        AddTaskDialogManager(
            context = requireContext(),
            fragmentManager = parentFragmentManager,
            onTaskCreated = { task ->
                viewModel.addTask(task=task, context = requireContext())
                Log.d("HomeFragment_text", "Task added: ${task.time}")
            }
        ).showDialog()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
