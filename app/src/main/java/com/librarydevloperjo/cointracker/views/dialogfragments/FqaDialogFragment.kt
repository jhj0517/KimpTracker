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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FqaDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding =  FragmentFqaDialogBinding.inflate(LayoutInflater.from(context))

            binding.ivBack.setOnClickListener {
                dismiss()
            }

            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}