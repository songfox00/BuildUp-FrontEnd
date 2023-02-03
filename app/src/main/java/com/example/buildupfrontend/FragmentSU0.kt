package com.example.buildupfrontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class FragmentSU0: FragmentSharedUser() {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return super.getView()
    }

    override fun nextStep() {
        viewModel.userName = etName.text.toString()
        viewModel.userBday = etBday.text.toString()
        viewModel.userNumber = etNumber.text.toString()
        viewModel.userMobile = etMobile.text.toString()

        (activity as SignupActivity?)!!.nextFragment(1, FragmentSU1())
    }

}