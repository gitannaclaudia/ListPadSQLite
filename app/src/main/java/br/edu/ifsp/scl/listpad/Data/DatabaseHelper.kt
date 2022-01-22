package br.edu.ifsp.scl.listpad.Data

import android.content.ContentProviderOperation
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.ifsp.scl.listpad.model.ShoppingList

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "tobuy.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "shoppingList"
        private const val ID = "id"
        private const val NOME = "nome"
        private const val DESCRICAO = "descricao"
        private const val DATA = "data"
        private val ITEMS = MutableList<String>(1) { index -> (0).toString() }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NOME TEXT, " +
                "$DESCRICAO TEXT, " +
                "$DATA TEXT, +" +
                "$ITEMS ARRAY)"

        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun inserirLista(list: ShoppingList): Long
    {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(ID, list.id)
        values.put(NOME, list.nome)
        values.put(DESCRICAO, list.descricao)
        values.put(DATA, list.data)
        values.putNull(ITEMS.toString())

        val result = db.insert(TABLE_NAME, null, values)

        db.close()

        return result
    }

    fun atualizarLista( list: ShoppingList): Int
    {
        val operation: MutableList<ContentProviderOperation>
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(ID, list.id)
        values.put(NOME, list.nome)
        values.put(DESCRICAO, list.descricao)
        values.put(DATA, list.data)
        for (i in list.items.indices){
            val ITEM = ""
            ITEMS.add(values.put(ITEM, list.items[i]).toString())
        }

        val result = db.update(TABLE_NAME, values, "id=?", arrayOf(list.id.toString()))

        db.close()

        return result
    }

    fun listarListas(): ArrayList<ShoppingList>
    {
        val listaListas = ArrayList<ShoppingList>()
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $NOME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext())
        {
            val c = ShoppingList (
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3))
            listaListas.add(c)
        }
        cursor.close()
        db.close()

        return listaListas

    }


    fun listarItems(list: ShoppingList): MutableList<String> {


        return list.items

    }

    fun deletarLista(list: ShoppingList): Int
    {
        val db = this.writableDatabase

        val result = db.delete(TABLE_NAME, "id=?", arrayOf(list.id.toString()))

        db.close()

        return result
    }

}