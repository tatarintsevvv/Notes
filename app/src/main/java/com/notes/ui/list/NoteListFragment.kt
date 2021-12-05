package com.notes.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.data.NoteDbo
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.details.NoteDetailsFragment

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteListViewModel() }

    private val recyclerViewAdapter = RecyclerViewAdapter(
        onDelete = { noteId -> viewModel.deleteNote(noteId) },
        onEdit = { note -> viewModel.editNote(note) }
    )

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            viewModel.onCreateNoteClick()
        }

        viewModel.notes.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    recyclerViewAdapter.setItems(it)
                }
            }
        )
        viewModel.navigateToNoteCreation.observe(
            viewLifecycleOwner,
            { note ->
                findImplementationOrThrow<FragmentNavigator>()
                    .navigateTo(
                        NoteDetailsFragment(note)
                    )

            }
        )
    }

    private class RecyclerViewAdapter(
        var onDelete: (Long) -> Unit,
        var onEdit: (NoteDbo) -> Unit
    ) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private var items = mutableListOf<NoteListItem>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position], onDelete, onEdit)
        }

        override fun getItemCount() = items.size

        fun setItems(
            items: List<NoteListItem>
        ) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        private class ViewHolder(
            private val binding: ListItemNoteBinding
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {

            fun bind(
                note: NoteListItem,
                onDelete: (Long) -> Unit,
                onEdit: (NoteDbo) -> Unit
            ) {
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content
                binding.deleteBlock.setOnClickListener {
                    onDelete.invoke(note.id)
                }
                binding.rightBlock.setOnClickListener {
                                                                                                                                                                                                                                     onEdit.invoke(NoteDbo(
                        note.id,
                        note.title,
                        note.content,
                        note.createdAt,
                        note.modifiedAt
                    ))
                }
            }

        }

    }

}