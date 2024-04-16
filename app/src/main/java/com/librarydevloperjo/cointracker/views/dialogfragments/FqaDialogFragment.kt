package com.librarydevloperjo.cointracker.views.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.databinding.FragmentFqaDialogBinding
import com.librarydevloperjo.cointracker.databinding.FragmentWidgetMakerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FqaDialogFragment : DialogFragment() {
    private var _binding: FragmentFqaDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            _binding = FragmentFqaDialogBinding.inflate(layoutInflater)

            binding.apply {
                ivBack.setOnClickListener {
                    dismiss()
                }
            }

            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}