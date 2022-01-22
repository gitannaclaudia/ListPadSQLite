package br.edu.ifsp.scl.listpad.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import br.edu.ifsp.scl.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.databinding.ActivityCadastroBinding
import br.edu.ifsp.scl.listpad.model.ShoppingList
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class CadastroActivity : AppCompatActivity() {
    private lateinit var activityCadastroBinding: ActivityCadastroBinding
    var cal = Calendar.getInstance()
    private val db = DatabaseHelper(this)
    private var shoppingList = ShoppingList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCadastroBinding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(activityCadastroBinding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        setSupportActionBar(toolbar)

        if (this.intent.hasExtra("shoppinglist")) {
            shoppingList = this.intent.getSerializableExtra("shoppinglist") as ShoppingList
            val nome = activityCadastroBinding.etNome
            val descricao = activityCadastroBinding.etDescricao
            val data = activityCadastroBinding.etData

            nome.setText(shoppingList.nome)
            descricao.setText(shoppingList.descricao)
            data.setText(shoppingList.data)

            val list =  ShoppingList(null, shoppingList.nome, shoppingList.descricao, shoppingList.data, shoppingList.items)

            toolbar.setNavigationOnClickListener(View.OnClickListener {
                val intent = Intent(applicationContext, DetalheActivity::class.java)
                intent.putExtra("shoppinglist", list)
                startActivity(intent)
            })
        }

        activityCadastroBinding.etData.setOnClickListener{
            dismissKeyboardShortcutsHelper()
        }

        val btnNext = findViewById<ExtendedFloatingActionButton>(R.id.btNext)

        findViewById<ExtendedFloatingActionButton>(R.id.btNext).setOnClickListener {
            observerFields(findViewById(R.id.etData), btnNext)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if (item.itemId==R.id.action_salvarLista) {

            val nome = activityCadastroBinding.etNome.text.toString()
            val descricao = activityCadastroBinding.etDescricao.text.toString()
            val data = activityCadastroBinding.etData.text.toString()

            val list = ShoppingList(null, nome, descricao, data)
            db.inserirLista(list)

        }

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        })
        return super.onOptionsItemSelected(item)
    }

    fun imageButtonOnClick(view: View) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        val datePicker = DatePickerDialog(
            this@CadastroActivity,
            android.R.style.Theme_Material_Dialog_MinWidth,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()

        print(dateSetListener)
    }

    private fun observerFields(field: EditText, button: ExtendedFloatingActionButton){
        if (field.text.isNotEmpty()) {
            val partiaList = partialSave()
            val intent = Intent(applicationContext, CadastroItemsActivity::class.java)
            intent.putExtra("shoppinglist", partiaList)
            startActivity(intent)
        } else {
            Toast.makeText(this@CadastroActivity, "Por favor! Para prosseguir preencha todos os campos", Toast.LENGTH_LONG).show()
        }
    }

    private fun partialSave(): ShoppingList {
        val nome = activityCadastroBinding.etNome.text.toString()
        val descricao = activityCadastroBinding.etDescricao.text.toString()
        val data = activityCadastroBinding.etData.text.toString()

        val list = ShoppingList( null, nome, descricao, data, ArrayList())

        if (this.intent.hasExtra("shoppinglist")) {
            db.atualizarLista(list)
        } else {
            db.inserirLista(list)
        }

        return list
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val dateString = sdf.format(cal.time)
        activityCadastroBinding.etData.setText(dateString)
    }
}
