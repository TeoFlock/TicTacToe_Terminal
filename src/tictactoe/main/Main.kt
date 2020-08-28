import tictactoe.main.Game
import tictactoe.main.Player
import tictactoe.main.Field

enum class State {
    INITGAME, PLAY, MENU, SHOW_SCORE, ENDGAME, VERSION_INFO
}


fun getUserInput(player: Player, state: State, fieldWasFree: Boolean): Pair<Field, State> {
    if(!fieldWasFree) {
        println("Feld ist schon belegt!")
    } else {
        println("${player.name} ist am Zug!")
    }

    var row: Int = -1
    var col: Int = -1
    var success = false

    do {
        println("Feld eingeben: ")
        val readField = readLine()

        if(readField == "m") {
            return Pair(Field(row, col, player), State.MENU)
        }

        if (readField == null || readField.length != 2) {
            println("ungültige Eingabe!")
        } else {
            row = when (readField[0]) {
                'a' -> 0
                'b' -> 1
                'c' -> 2
                else -> -1
            }

            col = readField[1].toString().toInt().dec()

            if (row < 0 || col < 0 || col > 2) {
                println("ungültige Eingabe!")
            } else {
                success = true
            }
        }
    } while(!success)

    return Pair(Field(row, col, player), state)
}

fun showMenu(): Unit {
    print("1. Zurück\n" +
            "2. Spielstand\n" +
            "3. Neues Spiel\n" +
            "4. Beende Spiel\n" +
            "5. Über\n" +
            "Wähle einen Eintrag:")
}

fun getMenuEntry(): State {
    var inputString: String?

    do {
        inputString = readLine()
    } while(inputString == null)

    return when(inputString) {
        "1" -> State.PLAY
        "2" -> State.SHOW_SCORE
        "3" -> State.INITGAME
        "4" -> State.ENDGAME
        "5" -> State.VERSION_INFO
        else -> State.MENU
    }
}

fun getUsers(): Pair<Player, Player> {
    println("hallör\n")

    println("Name Spieler 1 eingeben: ")
    var userNameFirst = readLine()
    while(userNameFirst == null) {
        println("Bitte mindestens ein Zeichen eingeben: ")
        userNameFirst = readLine()
    }

    println("Name Spieler 2 eingeben: ")
    var userNameSecond = readLine()
    while(userNameSecond == null) {
        println("Bitte mindestens ein Zeichen eingeben: ")
        userNameSecond = readLine()
    }

    return Pair(Player(userNameFirst), Player(userNameSecond))
}

fun main() {
    var players = getUsers()
    var game = Game(players.first, players.second)
    var machineState: State = State.PLAY

    while(true) {
        when (machineState) {
            State.INITGAME -> {
                players = getUsers()
                game = Game(players.first, players.second)
                machineState = State.PLAY
            }

            State.PLAY -> {
                game.printPlayground()
                var fieldIsFree = true

                do {
                    val userInput = getUserInput(game.currentPlayer, machineState, fieldIsFree)
                    machineState = userInput.second
                    fieldIsFree = game.setField(userInput.first)
                } while(!fieldIsFree && machineState != State.MENU)

                if(machineState != State.MENU) {
                    val winner = game.checkForWinner()

                    if (winner != null) {
                        println("${winner.name} scored!")
                        winner.score++
                        game.clearPlayground()
                    } else if(game.runs == 9) {
                        game.clearPlayground()
                    }

                    game.switchCurPlayer()
                }
            }

            State.MENU -> {
                showMenu()
                machineState = getMenuEntry()
            }

            State.SHOW_SCORE -> {
                println("\n${players.first.name} ${players.first.score} - ${players.second.score} ${players.second.name}\n")
                machineState = State.PLAY
                readLine()
            }

            State.VERSION_INFO -> {
                println("\nVersion: 0.0.0")
                println("Developer: mafl@esolutions\n")
                machineState = State.PLAY
                readLine()
            }

            State.ENDGAME -> {
                return
            }
        }
    }


}