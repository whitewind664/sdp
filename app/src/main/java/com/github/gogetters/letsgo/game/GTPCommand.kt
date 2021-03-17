package com.github.gogetters.letsgo.game

sealed class GTPCommand {

    //TODO: add ID values

    //TODO: turn de/serialize into tostring and tocommand, provide actual serialization

    // SETUP COMMANDS -------------------------
    data class BOARD_SIZE(val size: Board.Size) : GTPCommand(){
        override fun serialize(): String {
            return String.format("boardsize %d", size)
        }
    }

    object CLEAR_BOARD : GTPCommand(){
        override fun serialize(): String {
            return "clear_board"
        }
    }

    data class KOMI(val new_komi: Float): GTPCommand(){
        override fun serialize(): String{
            return String.format("komi %f", new_komi)
        }
    }

    data class FIXED_HANDICAP(val numberOfStones: Int) : GTPCommand(){
        override fun serialize(): String {
            return String.format("fixed_handicap %d", numberOfStones)
        }
    }

    data class PLACE_FREE_HANDICAP(val numberOfStones: Int) : GTPCommand(){
        override fun serialize(): String{
            return String.format("place_free_handicap %d", numberOfStones)
        }
    }

    data class SET_FREE_HANDICAP(val vertices: List<Point>) : GTPCommand(){

        //TODO: adapt toString for vertices
        override fun serialize(): String {
            return String.format("place_free_handicap %s", vertices.toString())
        }
    }
    // CORE PLAY COMMANDS -------------------------

    data class PLAY(val move: Move) : GTPCommand() {

        //TODO: proper toString for moves
        override fun serialize(): String {
            return String.format("play %s", move.toString())
        }
    }


    data class GENMOVE(val color: Stone) : GTPCommand(){
        //TODO: need to properly convert colors to strings, also handle empty case
        override fun serialize(): String {
            return String.format("move %s", color.toString())
        }
    }

    object UNDO : GTPCommand(){
        override fun serialize(): String {
            return "undo"
        }
    }


    /**
     * Serializes command with given id
     */
    fun serialize(id: Int): String{
        //TODO: verify ID
        return String.format("%d %s", id, serialize())
    }

    abstract fun serialize(): String

    fun deserialize(s: String): GTPCommand {
        val decomposed = s.split(" ")

        //TODO: create new exception for this case
        if(decomposed.isEmpty())
            throw Error("no valid command detected")

        val command = decomposed[0]
        val args = decomposed.drop(1)

        return when (command) {
            "boardsize" -> if(args.isEmpty()) throw Error() else return BOARD_SIZE(Board.Size.withSize(args[1].toInt()))
            "clear_board" -> CLEAR_BOARD
            "komi" -> if(args.isEmpty()) throw Error() else return KOMI(args[1].toFloat())
            "fixed_handicap" -> if(args.isEmpty()) throw Error() else return FIXED_HANDICAP(args[1].toInt())
            "place_free_handicap" -> if(args.isEmpty()) throw Error() else return PLACE_FREE_HANDICAP(args[1].toInt())
            "set_free_handicap" -> if(args.isEmpty()) throw Error() else TODO("need to convert strings to points")

            "play" -> TODO("need to create moves from Strings")
            "genmove" -> TODO("need to create moves from Strings")
            "undo" -> UNDO


            else -> throw Error("no valid command detected")
        }
    }
}