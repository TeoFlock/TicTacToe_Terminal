package tictactoe.main

import java.lang.IllegalArgumentException

class Game(val player1: Player, val player2: Player) {

    val fields = mutableListOf<Field>()
    var runs = 0
    var currentPlayer = player1
    val dummyvar = 0

    fun printPlayground(): Unit {

        fields.asSequence().forEach {
            val row = it.row
            val col = it.column

            if(it.player != null) {
                if(it.player === player1) {
                    print(" x ")
                } else { print(" o ") }
            } else { print("   ")}

            if(col < 2) { print("|") }
            if(row < 2 && col == 2) {
                print("\n")
                print("-----------")
                print("\n")
            }
        }
        print("\n")
    }

    fun setField(field: Field): Boolean {
        fields.find { it == Field(field.row, field.column) }.apply {
            if (this?.player == null) {
                this?.player = currentPlayer
                runs++
                return true
            }
            return false
        }
    }

    fun checkForWinner(): Player? {
        if(runs < 5) {
            return null
        }

        var fieldsLine = mutableListOf<Field?>()

        for(col in 0..2) {
            fieldsLine.clear()
            for(row in 0..2) {
                fieldsLine.add(fields.find { it == Field(row, col) })
            }
            if(fieldsLine.threeInRow()) {
                return fieldsLine[0]?.player
            }
        }

        for(row in 0..2) {
            fieldsLine.clear()
            for(col in 0..2) {
                fieldsLine.add(fields.find { it == Field(row, col) })
            }
            if(fieldsLine.threeInRow()) {
                return fieldsLine[0]?.player
            }
        }

        fieldsLine.clear()
        for(diagonalIdx in 0..2) {
            fieldsLine.add(fields.find { it == Field(diagonalIdx, diagonalIdx) })
        }
        if(fieldsLine.threeInRow()) {
            return fieldsLine[0]?.player
        }

        fieldsLine.clear()
        var row = 0
        var col = 2
        while(row < 3) {
            fieldsLine.add(fields.find { it == Field(row, col) })
            row++
            col--
        }
        if(fieldsLine.threeInRow()) {
            return fieldsLine[0]?.player
        }
        return null
    }

    fun clearPlayground() {
        fields.forEach { it.player = null }
        runs = 0
    }

    fun switchCurPlayer() {
        currentPlayer = if (currentPlayer === player1) {
            player2
        } else {
            player1
        }
    }

    private fun MutableList<Field?>.threeInRow(): Boolean {
        if(this.size != 3) {
            throw IllegalArgumentException("List must have 3 entries!")
        }
        val firstFieldPlayer: Player? = this[0]?.player
        return this.filter { it?.player === firstFieldPlayer }.size == 3
    }

    init {
        for(row in 0..2) {
            for(col in 0..2) {
                fields.add(Field(row, col, null))
            }
        }
    }

}

data class Field(val row: Int, val column: Int, var player: Player? = null) {

    override fun equals(other: Any?): Boolean {
        if (other !is Field) {return super.equals(other)}

        return this.row == other.row && this.column == other.column
    }
}

data class Player(val name: String) {
    var score = 0
}

