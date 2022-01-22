package br.edu.ifsp.scl.listpad.activity

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.Data.ShoppingListAdapter
import br.edu.ifsp.scl.listpad.model.ShoppingList
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    val db = DatabaseHelper(this)
    var listaLista = ArrayList<ShoppingList>()
    lateinit var shoppingListAdapter: ShoppingListAdapter
    private lateinit var permissaoListaArl: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent(applicationContext, CadastroActivity::class.java)
            startActivity(intent)
        }

        permissaoListaArl = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissoes ->
            if (permissoes.containsValue(false)) {
                requisitarPermissaoListas()
            } else {
                updateUI()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.search -> {
            Toast.makeText(this, "Buscar item", Toast.LENGTH_LONG).show()
            true
        }
        R.id.add -> {
            val intent = Intent(applicationContext, CadastroActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI() {

        listaLista = db.listarListas()
        shoppingListAdapter = ShoppingListAdapter(listaLista)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = shoppingListAdapter

        val clickListener = object :ShoppingListAdapter.ShoppingListClickListener{
            override fun onItemClick(pos: Int) {
                val intent = Intent(applicationContext, DetalheActivity::class.java)
                intent.putExtra("shoppinglist", shoppingListAdapter.shoppingList[pos])
                startActivity(intent)
            }
        }

        shoppingListAdapter.setClickListener(clickListener)

    }

    private fun requisitarPermissaoListas() = permissaoListaArl.launch(arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    ))
}
