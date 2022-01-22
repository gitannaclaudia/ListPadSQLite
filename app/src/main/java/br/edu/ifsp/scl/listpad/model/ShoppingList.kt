package br.edu.ifsp.scl.listpad.model

import java.io.Serializable

class ShoppingList (
                    var id: Int? = null,
                    var nome: String = "",
                    var descricao: String = "",
                    var data: String = "",
                    var items: MutableList<String> = ArrayList()
): Serializable