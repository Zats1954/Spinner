package ru.zatsoft.spinner

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

import ru.zatsoft.spinner.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), Removable {

    private val role = mutableListOf(
        "Должность",
        "Бухгалтер",
        "Кладовщик",
        "Менеджер",
        "Администратор",
        "Сортировщик"
    )
    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf<Person>()
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        title = " "
        val inputKeyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        listAdapter =ListAdapter(this, list)
        binding.listView.adapter = listAdapter
        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedPerson = listAdapter.getItem(position)
                remove(selectedPerson)
                listAdapter.notifyDataSetChanged()
            }


        val spinnerAdapter = ArrayAdapter(
            this,
                   android.R.layout.simple_spinner_item,
                   role
                  )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spinner.adapter = spinnerAdapter
        var selectedItem  = " "
        val itemSelected : OnItemSelectedListener =
           object: OnItemSelectedListener {

               override fun onItemSelected(
                   parent: AdapterView<*>?,
                   view: View?,
                   position: Int,
                   id: Long
               ) { if(position > 0)
                      selectedItem = parent?.getItemAtPosition(position) as String
                   else
                       selectedItem = " "
               }

               override fun onNothingSelected(parent: AdapterView<*>?)
               { selectedItem = " " }
           }
        binding.spinner.onItemSelectedListener = itemSelected

        binding.btnSave.setOnClickListener {
            val person = Person(binding.edName.text.toString(),
                binding.edLastName.text.toString(),
                binding.edAge.text.toString().toInt(),
                selectedItem
            )
            list.add(person)
            listAdapter.notifyDataSetChanged()
            inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
//   Очистка полей
            binding.edName.text.clear()
            binding.edLastName.text.clear()
            binding.edAge.text.clear()
            selectedItem = " "
            binding.spinner.setSelection(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.menu_main, menu)
       return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.exit)
        finish()
    return super.onOptionsItemSelected(item)
}

    override fun remove(person: Person) {
        listAdapter.remove(person)
    }
}