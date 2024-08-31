package com.example.myshoppinglist.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment() : Fragment() {

    private lateinit var onEditingFinishListener: OnEditingFinishListener

    private lateinit var tvName: TextInputLayout
    private lateinit var tvCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button
    private lateinit var viewModel: ShopItemViewModel


    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID


    override fun onStart() {
        super.onStart()
        Log.d("ShopItemFragment", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ShopItemFragment", "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ShopItemFragment", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ShopItemFragment", "onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ShopItemFragment", "onDestroyView()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("ShopItemFragment", "onDetach()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ShopItemFragment", "onDestroy()")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishListener){
            onEditingFinishListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishListener interface")
        }
        Log.d("ShopItemFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
        Log.d("ShopItemFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ShopItemFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        setTextChangeListeners()
        observeViewModel()
        Log.d("ShopItemFragment", "onStart()")
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_MODE)) {
            throw RuntimeException("Screen mode param is absent")
        }
        val mode = args.getString(EXTRA_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Shop item id param is absent")
            }
        }
        shopItemId = args.getInt(EXTRA_SHOP_ITEM_ID)
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tvName.error = message
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tvCount.error = message
        }
        viewModel.isComplete.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(
                etName.text?.toString(),
                etCount.text?.toString()
            )
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(
                etName.text.toString(),
                etCount.text.toString()
            )
        }
    }

    private fun initViews(view: View) {
        tvName = view.findViewById(R.id.textInputLayoutName)
        tvCount = view.findViewById(R.id.textInputLayoutCount)
        etName = view.findViewById(R.id.etName)
        etCount = view.findViewById(R.id.etCount)
        buttonSave = view.findViewById(R.id.buttonSave)
    }

    private fun setTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    interface OnEditingFinishListener{
        fun onEditingFinish()
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "shopItemId"
        private const val MODE_ADD = "100"
        private const val MODE_EDIT = "101"
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_MODE = "extra_mode"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MODE, MODE_EDIT)
                    putInt(EXTRA_SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }
}