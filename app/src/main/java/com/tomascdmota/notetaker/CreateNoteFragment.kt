package com.tomascdmota.notetaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tomascdmota.notetaker.database.NotesDatabase
import com.tomascdmota.notetaker.entities.Notes
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateNoteFragment : BaseFragment() {

    var currentDate:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Defines the date when a note was create (and its format) /*I have removed the seconds part*/
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        currentDate = sdf.format(Date())

        //Sets the text view to the current date when the note is beind created
        tv_date.text = currentDate

        img_done.setOnClickListener {
            //Save Note
            saveNote()
        }

        img_back.setOnClickListener{
            //Replaces createNoteFragment with home fragment
            //istransition : false, tells the app no to use the transition effect inside the replace fragment function
            replaceFragment(HomeFragment.newInstance(), false)
        }
    }


    private fun saveNote() {

        if(notes_title.text.isNullOrEmpty()) {
            Toast.makeText(context, "A Title is Required", Toast.LENGTH_SHORT).show()
        }

        if(notes_subtitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "A Subtitle is Required", Toast.LENGTH_SHORT).show()
        }
        if(note_desc.text.isNullOrEmpty()) {
            Toast.makeText(context, "A Description is Required", Toast.LENGTH_SHORT).show()
        }
        //launch a coroutine scope
        //Sets all the strings to their defined values
        launch {
            val notes = Notes()
            notes.title = notes_title.text.toString()
            notes.subTitle = notes_subtitle.text.toString()
            notes.noteText = note_desc.text.toString()
            notes.dateTime = currentDate

            //Resets all the views values
            context?.let {
                NotesDatabase.getDatabase(it).notedao().insertNotes(notes)
                note_desc.setText("")
                notes_subtitle.setText("")
                notes_title.setText("")
            }

        }
    }


     fun replaceFragment(fragment: Fragment, istransition:Boolean) {
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if(istransition) {
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }

        fragmentTransition.add(R.id.frame_layout, fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }
}