package com.notes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.notes.databinding.ActivityRootBinding
import com.notes.ui._base.FragmentNavigator
import com.notes.ui.details.NoteDetailsFragment
import com.notes.ui.list.NoteListFragment

const val TAG_NOTES_LIST = "notes_list"
const val TAG_NOTE_DETAIL = "note_detail"

class RootActivity : AppCompatActivity(), FragmentNavigator {

    private var viewBinding: ActivityRootBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityRootBinding.inflate(layoutInflater)
        this.viewBinding = viewBinding
        setContentView(viewBinding.root)
        supportFragmentManager
            .beginTransaction()
            .add(
                viewBinding.container.id,
                NoteListFragment()
            )
            .commit()
    }

    override fun navigateTo(
        fragment: Fragment
    ) {
        val viewBinding = this.viewBinding ?: return
        supportFragmentManager
            .beginTransaction()
            .replace(
                viewBinding.container.id,
                fragment
            )
            .commit()
    }

    override fun onBackPressed() {
        var fragments = supportFragmentManager.fragments
        if(fragments.size == 1 && fragments[0] is NoteDetailsFragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    viewBinding!!.container.id,
                    NoteListFragment()
                )
                .commit()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}