package com.android.plantpal.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.plantpal.R
import com.android.plantpal.databinding.FragmentAccountBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.account.edit.EditProfileActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.showAlertDialog
import com.bumptech.glide.Glide

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
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
                        .error(R.drawable.person_pc)
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
                requireContext(),
                title = "Apakah Anda Yakin?",
                message = "Apakah Anda Ingin Keluar?",
                positiveButtonText = "Ya",
                negativeButtonText = "Batal",
                onPositive = { viewModel.logout() }
            )
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