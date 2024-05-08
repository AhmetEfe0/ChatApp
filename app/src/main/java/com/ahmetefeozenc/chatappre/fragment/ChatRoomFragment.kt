package com.ahmetefeozenc.chatappre.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.chatappre.adapter.ChatRoomControlAdapter
import com.ahmetefeozenc.chatappre.databinding.FragmentChatroomBinding
import com.ahmetefeozenc.chatappre.viewmodel.AddChatViewModel
import com.ahmetefeozenc.chatappre.viewmodel.ChatRoomViewModel
import com.ahmetefeozenc.chatappre.viewmodel.MainViewModel


class ChatRoomFragment : Fragment() {

    private var _binding: FragmentChatroomBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatRoomViewModel: ChatRoomViewModel
    private lateinit var addChatViewModel: AddChatViewModel
    private lateinit var chatRoomAdapter: ChatRoomControlAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatroomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addChatViewModel = ViewModelProvider(this).get(AddChatViewModel::class.java)
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val email = mainViewModel.email ?: ""


        // Initialize ViewModel
        chatRoomViewModel = ViewModelProvider(this).get(ChatRoomViewModel::class.java)

        // Initialize RecyclerView and Adapter
        val chatRoomRecyclerView: RecyclerView = binding.chatroomrecylerview
        chatRoomAdapter = ChatRoomControlAdapter(email)
        chatRoomRecyclerView.layoutManager = LinearLayoutManager(context)
        chatRoomRecyclerView.adapter = chatRoomAdapter

        // Observe chat rooms from ViewModel
        chatRoomViewModel.chatRooms.observe(viewLifecycleOwner, Observer { chatRooms ->
            chatRoomAdapter.submitList(chatRooms)
        })

        // Fetch chat rooms
        chatRoomViewModel.fetchChatRooms(email)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}