package com.android.plantpal.ui.account

import android.content.Intent
import android.os.Bundle
import com.android.plantpal.ui.utils.Result
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.plantpal.databinding.FragmentAccountBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.account.edit.EditProfileActivity
import com.bumptech.glide.Glide

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupData()
    }

    private fun setupData() {
        viewModel.getUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.username.text = result.data.data.fullname
                    Glide.with(binding.profilePic.context)
                        .load(result.data.data.profilePhoto)
                        .into(binding.profilePic)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Tidak dapat load foto profil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setupAction() {
        binding.buttonEditProfile.setOnClickListener{
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogout.setOnClickListener{
            showAlertDialog(
                title = "Apakah Anda Yakin?",
                message = "Apakah Anda Ingin Keluar?",
                positiveButtonText = "Ya",
                negativeButtonText = "Batal",
                onPositive = { viewModel.logout() }
            )
        }
    }

    private fun showAlertDialog(
        title: String,
        message: String?,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { dialog, _ ->
                onPositive?.invoke()
                dialog.dismiss()
            }
            setNegativeButton(negativeButtonText) { dialog, _ ->
                onNegative?.invoke()
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setupData()
    }

}