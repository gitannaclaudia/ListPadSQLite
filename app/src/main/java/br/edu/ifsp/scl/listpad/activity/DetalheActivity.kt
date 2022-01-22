package br.edu.ifsp.scl.listpad.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.listpad.Data.ItemListAdapter
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.model.ShoppingList

class DetalheActivity : AppCompatActivity() {
    val db = DatabaseHelper(this)
    private var shoppingList = ShoppingList()
    private var itemsList: MutableList<String> = mutableListOf()
    lateinit var itemListAdapter: ItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        })

        if (this.intent.hasExtra("shoppinglist")) {
            shoppingList = this.intent.getSerializableExtra("shoppinglist") as ShoppingList
            val nome = findViewById<TextView>(R.id.nomeEt)
            val descricao = findViewById<TextView>(R.id.descricaoEt)
            val data = findViewById<TextView>(R.id.dataEt)

            nome.setText(shoppingList.nome)
            descricao.setText(shoppingList.descricao)
            data.setText(shoppingList.data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhe, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        shoppingList = this.intent.getSerializableExtra("shoppinglist") as ShoppingList
        if (item.itemId==R.id.action_editarLista) {

            val nome = findViewById<TextView>(R.id.nomeEt).text.toString()
            val descricao = findViewById<TextView>(R.id.descricaoEt).text.toString()
            val data = findViewById<TextView>(R.id.dataEt).text.toString()

            val list = ShoppingList(null, nome, descricao, data)

            val intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("shoppinglist", list)
            startActivity(intent)

        }

        if (item.itemId==R.id.action_excluirLista) {

            if (db.deletarLista(shoppingList) > 0) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "Lista Excluída", Toast.LENGTH_LONG).show()
            }
            finish()
        }
            return super.onOptionsItemSelected(item)
    }

    private fun updateUI() {
        shoppingList = this.intent.getSerializableExtra("shoppinglist") as ShoppingList
        itemsList = db.listarItems(shoppingList)
        itemListAdapter = ItemListAdapter(itemsList)

        val recyclerview = findViewById<RecyclerView>(R.id.item_list)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = itemListAdapter

        val clickListener = object : ItemListAdapter.ItemListClickListener{
            override fun onItemClick(pos: Int) {
                val intent = Intent(applicationContext, DetalheActivity::class.java)
                intent.putExtra("shoppinglist", itemListAdapter.itemList[pos])
                startActivity(intent)
            }
        }

        itemListAdapter.setClickListener(clickListener)

    }
}