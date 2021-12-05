package com.notes.ui.details

import android.os.Bundle
import androidx.core.text.trimmedLength
import androidx.core.widget.doOnTextChanged
import com.notes.data.NoteDbo
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.di.DependencyManager
import com.notes.ui.RootActivity
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.list.NoteListFragment

class NoteDetailsFragment(
    var note: NoteDbo? = null
) : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {
    private val viewModel by lazy { DependencyManager.noteDetailsViewModel() }



    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)



        with(viewBinding) {

            note?.let { noteData ->
                titleEdit.setText(noteData.title)
                descriptionEdit.setText(noteData.content)
            }

            toolbar.apply {
                (activity as RootActivity).setSupportActionBar(this)
                setNavigationOnClickListener {
                   activity?.onBackPressed()
                }
            }

            buttonOk.setOnClickListener {
                val titleText = titleEdit.text?.trim().toString()
                val descriptionText = descriptionEdit.text?.trim().toString()
                if (descriptionText.isEmpty() || titleText.isEmpty()) {
                    if (descriptionText.isEmpty()) {
                        descriptionEdit.requestFocus()
                        description.error = "Обязательное поле"
                    }
                    if (titleText.isEmpty()) {
                        titleEdit.requestFocus()
                        title.error = "Обязательное поле"
                    }
                } else {
                    if(note == null) {
                        viewModel.addNote(titleText, descriptionText)
                    } else {
                        note?.let {
                            viewModel.changeNote(it.id, titleText, descriptionText)
                        }
                    }
                }
            }

            titleEdit.doOnTextChanged { text, _, _, _ ->
                if(text?.trimmedLength() ?: 0 > 0) {
                    title.error = null
                }
            }

            descriptionEdit.doOnTextChanged { text, _, _, _ ->
                if(text?.trimmedLength() ?: 0 > 0) {
                    description.error = null
                }
            }
        }

        viewModel.navigateToNoteList.observe(
            viewLifecycleOwner,
            {
                findImplementationOrThrow<FragmentNavigator>()
                    .navigateTo(
                        NoteListFragment()
                    )

            }
        )
    }


}