package com.librarydevloperjo.cointracker.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.databinding.FragmentInfoBinding
import com.librarydevloperjo.cointracker.views.dialogfragments.FqaDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater,container,false)
        binding.apply {
            rootBack.setOnClickListener {
                findNavController().popBackStack()
            }

            rootReview.setOnClickListener {
                // If your app is on the PlayStore, this will take you to the PlayStore.
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=" + requireActivity().packageName)
                startActivity(intent)
            }

            rootFqa.setOnClickListener {
                val dialog = FqaDialogFragment()
                dialog.show(requireActivity().supportFragmentManager,"dialog")
            }

            rootPrivacy.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(requireActivity().getString(R.string.privacyurl))
                startActivity(intent)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }

}