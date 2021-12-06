package br.edu.ifsp.scl.listpad.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.model.ShoppingList
import com.google.firebase.firestore.FirebaseFirestore

class DetalheActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    lateinit var shoppingListID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)

        shoppingListID = this.intent.getStringExtra("shoppinglistID") as String

        db.collection("shoppinglist").document(shoppingListID)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val list = value.toObject(ShoppingList::class.java)

                    val nome = findViewById<TextView>(R.id.nomeEt)
                    val descricao = findViewById<TextView>(R.id.descricaoEt)
                    val data = findViewById<TextView>(R.id.dataEt)

                    nome.setText(list?.nome.toString())
                    descricao.setText(list?.descricao.toString())
                    data.setText(list?.data.toString())
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}