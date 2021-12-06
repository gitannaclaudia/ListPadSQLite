package br.edu.ifsp.scl.listpad.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.adapter.ShoppingListAdapter
import br.edu.ifsp.scl.listpad.model.ShoppingList
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class MainActivity : AppCompatActivity() {

    lateinit var shoppingListAdapter: ShoppingListAdapter

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        updateUI()
        shoppingListAdapter.startListening()
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
        val db = FirebaseFirestore.getInstance()
        val query: Query = db.collection("shoppinglist").orderBy("nome")
        val options: FirestoreRecyclerOptions<ShoppingList> = FirestoreRecyclerOptions.Builder<ShoppingList>()
            .setQuery(query, ShoppingList::class.java).build()

        shoppingListAdapter = ShoppingListAdapter(options)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = shoppingListAdapter

        val clickListener = object :ShoppingListAdapter.ShoppingListClickListener{
            override fun onItemClick(pos: Int) {
                val intent = Intent(applicationContext, DetalheActivity::class.java)
                intent.putExtra("shoppinglistID", shoppingListAdapter.snapshots.getSnapshot(pos).id)
                startActivity(intent)
            }
        }

        shoppingListAdapter.clickListener = clickListener

    }
}