package com.ahmetefeozenc.chatappre.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmetefeozenc.chatappre.databinding.FragmentAddchatBinding
import com.ahmetefeozenc.chatappre.viewmodel.MainViewModel
import com.ahmetefeozenc.chatappre.viewmodel.AddChatViewModel

class AddChatFragment : Fragment() {

    private var _binding: FragmentAddchatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var addChatViewModel: AddChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddchatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addChatViewModel = ViewModelProvider(this).get(AddChatViewModel::class.java)
        val mainviewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val email = mainviewModel.email


        val addChatButton: Button = binding.addchatekleButton
        val emailEditText: EditText = binding.addchatemailText

        addChatButton.setOnClickListener {
            val otherUserEmail = emailEditText.text.toString()
            addChatViewModel.addChat(email!!, otherUserEmail)
            emailEditText.setText("")
        }

        addChatViewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}