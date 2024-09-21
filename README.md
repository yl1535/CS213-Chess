# README
* This is a code repository created solely for reading purposes, aimed to show some recorded works from part of a project, whose purpose is to create a simulated chess program.
* The program can be normally compiled and executed in any basic java environment, and user can play the chess program by type in command in format "A B", where A is the starting location, and B is the ending location, such as "a2 a4".
* Special Move Formats:
  * Castling: castling move command formats by entering the start and end location of the king, such as "e1 g1".
  * Promotion: promotion move command formats by entering normal move command and follows by the piece type you want to promote, such as "a7 a8 Q".
    * All piece type: Queen -> Q, Rook -> R, Bishop -> B, Knight -> N. If you don't enter in a piece type code, it is assumed that the pawn promotable will automatically promote to a queen.
  * Resign: when one player want to resign, just simply type in "resign" instead of making a legal move.
  * Draw: when one player want to consider draw, type in "draw?" after making a legal move, such as "a2 a4 draw?". In this test program, the system is set to let the other player automatically accept the draw request and end the game.
  * Enpassant: enpassant move command formats same as normal moves, and will be legal as long as the target piece and the opponent's piece targeted by the enpassant move fit the requirements of acting an enpassant move.
  * Illegal move: it will still be one player's turn if they make an illegal move, which will not be executed.
  * Endgame options: type in "quit" to leave the program, type in "reset" to reset the board and start another match.

# Source Assignment
https://www.cs.rutgers.edu/courses/213/classes/spring_2024_venugopal/chess/chess.html
