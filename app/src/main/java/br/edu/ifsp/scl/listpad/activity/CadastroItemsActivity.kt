package br.edu.ifsp.scl.listpad.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.databinding.ActivityCadastroItemsBinding
import br.edu.ifsp.scl.listpad.model.ShoppingList
import com.google.firebase.firestore.FirebaseFirestore

abstract class CadastroItemsActivity : AppCompatActivity() {
    private val activityCadastroItemsBinding: ActivityCadastroItemsBinding by lazy {
        ActivityCadastroItemsBinding.inflate(layoutInflater)
    }
    // Data source
    private val itemsList: MutableList<String> = mutableListOf()

    // Adapter
    private val itemsAdapter: ArrayAdapter<String> =
        ArrayAdapter(this, android.R.layout.simple_list_item_1, itemsList.run {
            val itemsStringList = mutableListOf<String>()
            this.forEach { item -> itemsStringList.add(item.toString()) }
            itemsStringList
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityCadastroItemsBinding.root)

        initItemsList()

        activityCadastroItemsBinding.listview.adapter = itemsAdapter
    }

    fun imageButtonOnClick(view: View) {
        val item = findViewById<EditText>(R.id.itemEt)
        val listItem = findViewById<ListView>(R.id.listview)
        itemsList.add(item.text.toString())
        item.text.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.action_salvarContato) {
            val db = FirebaseFirestore.getInstance()

            val nome = findViewById<EditText>(R.id.etNome).text.toString()
            val descricao = findViewById<EditText>(R.id.etDescricao).text.toString()
            val data = findViewById<EditText>(R.id.etData).text.toString()
            val list = ShoppingList(nome, descricao, data, itemsList)
            db.collection("shoppinglist").add(list)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initItemsList() {
        for (indice in 1..100) {
            itemsList.add("$indice")
        }
    }
}