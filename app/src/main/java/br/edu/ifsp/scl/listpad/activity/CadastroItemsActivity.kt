package br.edu.ifsp.scl.listpad.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import br.edu.ifsp.scl.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.listpad.Data.ItemListAdapter
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.databinding.ActivityCadastroItemsBinding
import br.edu.ifsp.scl.listpad.model.ShoppingList

class CadastroItemsActivity : AppCompatActivity() {
    private lateinit var activityCadastroItemsBinding: ActivityCadastroItemsBinding
    private var itemsList: MutableList<String> = mutableListOf()
    private var shoppingList = ShoppingList()
    private val db = DatabaseHelper(this)
    lateinit var itemListAdapter: ItemListAdapter
    var itemsStringList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCadastroItemsBinding = ActivityCadastroItemsBinding.inflate(layoutInflater)
        setContentView(activityCadastroItemsBinding.root)

        shoppingList = this.intent.getSerializableExtra("shoppinglist") as ShoppingList

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        setSupportActionBar(toolbar)

        if (this.intent.hasExtra("shoppinglist")) {
            val nome = activityCadastroItemsBinding.nomeTv
            val descricao = activityCadastroItemsBinding.descricaoTv
            val data = activityCadastroItemsBinding.dataTv

            nome.text = shoppingList.nome
            descricao.text = shoppingList.descricao
            data.text = shoppingList.data
        }

        val list = ShoppingList(null, shoppingList.nome, shoppingList.descricao, shoppingList.data, shoppingList.items)

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, CadastroActivity::class.java)
            intent.putExtra("shoppinglist", list)
            startActivity(intent)
        })

        itemsStringList = shoppingList.items

        initItemsList(itemsStringList)
    }

    fun imageButtonOnClick(view: View) {
        val item = findViewById<EditText>(R.id.itemEt)
        val listItem = findViewById<ListView>(R.id.listview)
        itemsList.add(item.text.toString())
        item.text.clear()

        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.itemlist_celula, R.id.itemTv, itemsList.run {
                this.forEach { item -> itemsStringList.add(item.toString()) }
                itemsStringList
            })

        activityCadastroItemsBinding.listview.adapter = itemsAdapter

        initItemsList(itemsStringList)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if (item.itemId==R.id.action_salvarLista) {
            val nome = activityCadastroItemsBinding.nomeTv.text.toString()
            val descricao = activityCadastroItemsBinding.descricaoTv.text.toString()
            val data = activityCadastroItemsBinding.dataTv.text.toString()
            val list = ShoppingList(shoppingList.id, nome, descricao, data, itemsStringList)

            db.atualizarLista(list)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "Lista Atualizada", Toast.LENGTH_LONG).show()
        }

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        })

        return super.onOptionsItemSelected(item)
    }

    private fun initItemsList(list: MutableList<String>) {
        for (indice in list) {
            itemsList.add(indice)
        }
    }


}