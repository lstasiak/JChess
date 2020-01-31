package com.chess.engine.board;

import static com.chess.engine.board.Move.*;

// to wrap the board when the move is done
public class MoveTransition {
    private final ChessBoard transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final ChessBoard transitionBoard,
                          final Move move,
                          final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
